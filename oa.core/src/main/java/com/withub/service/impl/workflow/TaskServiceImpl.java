package com.withub.service.impl.workflow;


import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.common.util.ReflectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.dao.EntityDao;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.exception.BaseBusinessException;
import com.withub.model.std.event.agencyevent.AgencyCreatedEvent;
import com.withub.model.std.event.agencyevent.AgencyFinishedEvent;
import com.withub.model.std.event.entityevent.EntityTimeoutEvent;
import com.withub.model.std.po.Agency;
import com.withub.model.system.enumeration.SystemConstant;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.User;
import com.withub.model.workflow.enumeration.*;
import com.withub.model.workflow.event.WorkflowEventPublisher;
import com.withub.model.workflow.event.instanceevent.*;
import com.withub.model.workflow.event.taskevent.ModifyTaskCreateEvent;
import com.withub.model.workflow.event.taskevent.TaskCreateEvent;
import com.withub.model.workflow.event.taskevent.TaskEventArgs;
import com.withub.model.workflow.event.taskevent.TaskEventPublisher;
import com.withub.model.workflow.po.*;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.SmsQueueService;
import com.withub.service.std.AgencyService;
import com.withub.service.std.WorkCalendarService;
import com.withub.service.system.CodeService;
import com.withub.service.system.UserService;
import com.withub.service.workflow.FlowTypeService;
import com.withub.service.workflow.InstanceService;
import com.withub.service.workflow.TaskService;
import com.withub.service.workflow.WFRegulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("taskService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class TaskServiceImpl extends EntityServiceImpl implements TaskService {

    //================================ 属性声明 ===========================================================

    @Autowired
    private FlowTypeService flowTypeService;

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private WFRegulationService wfRegulationService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private UserService userService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private WorkCalendarService workCalendarService;

    @Autowired
    private EntityDao entityDao;

    @Autowired
    private SmsQueueService smsQueueService;

    //=============================== 接口实现 ============================================================

    public void onInstanceCreateEvent(InstanceCreateEvent event) throws Exception {

        Instance instance = event.getInstanceEventArgs().getInstance();
        FlowType flowType = instance.getFlowType();
        FlowNode beginFlowNode = flowTypeService.getBeginFlowNode(flowType);
        createTask(event.getInstanceEventArgs().getInstance().getCurrentUser(), event.getInstanceEventArgs().getSubInstance(), null, beginFlowNode, null, event.getInstanceEventArgs().getFirstNodeHandlerList());
    }

    public EntityModifyTask getEntityModifyTask(String relatedObjectId) throws Exception {

        EntityModifyTask entityModifyTask = (EntityModifyTask) getByPropertyValue(EntityModifyTask.class, "entityInstanceId", relatedObjectId);
        return entityModifyTask;
    }

    public Task getTaskById(String taskId) throws Exception {

        Task task = (Task) get(Task.class, taskId);
        return task;
    }

//    public void commit(Task task, TaskHandleResult taskHandleResult, String opinion, List<User> nextHandlerList, List<User> firstNodeHandlerList) throws Exception {
//
//        User currentUser = SpringSecurityUtil.getCurrentUser();
//        commit(currentUser, task, taskHandleResult, opinion, nextHandlerList, firstNodeHandlerList);
//    }

    public void commit(User currentUser, Task task, List<TaskContext> taskContextList, TaskHandleResult taskHandleResult, String opinion
            , List<User> nextHandlerList, List<User> firstNodeHandlerList) throws Exception {

        if (!task.getStatus().getCodeTag().equals(TaskStatus.Running.toString())) {
            return;
        }

        if (CollectionUtil.isNotEmpty(taskContextList)) {
            for (TaskContext taskContext : taskContextList) {
                addTaskContext(task.getObjectId(), taskContext.getContextKey(), taskContext.getContextValue());
            }
        }

        MasterTask masterTask = task.getMasterTask();
        SubInstance subInstance = masterTask.getSubInstance();
        Instance instance = subInstance.getInstance();
        FlowNode currentFlowNode = masterTask.getFlowNode();
        AbstractBaseEntity relatedEntityInstance = instanceService.getRelatedEntityInstance(instance);

        if (currentFlowNode.getFlowNodeType() != FlowNodeType.Begin && currentFlowNode.getFlowNodeType() != FlowNodeType.First
                && currentFlowNode.getFlowNodeType() != FlowNodeType.End) {
            MasterTask previousMasterTask;
            if (currentFlowNode.getFlowNodeType() == FlowNodeType.Modify) {
                SubInstance previousSubInstance = instance.getSubInstanceList().get(1);
                previousMasterTask = previousSubInstance.getMasterTaskList().get(previousSubInstance.getMasterTaskList().size() - 1);
            } else {
                previousMasterTask = masterTask.getPreviousMasterTask();
            }
            for (Task t : previousMasterTask.getTaskList()) {
                t.setLocked(1);
                update(t);
            }
        }

        InstanceEventArgs instanceEventArgs = new InstanceEventArgs();
        instanceEventArgs.setInstance(instance);

        if (currentFlowNode.getSuspendInstance() == 1) {
            opinion = currentFlowNode.getSuspendDescription();
        }
        finishTask(task, currentFlowNode, taskHandleResult, opinion);

        // 流程被挂起,等待用户的后续决策事件
        if (currentFlowNode.getSuspendInstance() == 1) {
            return;
        }

        if (currentFlowNode.getFlowNodeType() == FlowNodeType.End) {
            finishMasterTask(masterTask);
            finishInstance(instance);
            return;
        }

        List<Task> runningTaskList = listRunningTasks(masterTask);
        if (runningTaskList.size() == 0) {
            finishMasterTask(masterTask);
        }

        if (currentFlowNode.getFlowNodeType() == FlowNodeType.Modify) {
            EntityModifyTask objectModifyTask = getEntityModifyTask(instance.getRelatedObjectId());
            delete(objectModifyTask);
        }

        if (taskHandleResult == TaskHandleResult.Reject) {
            if (currentFlowNode.getFlowNodeType() == FlowNodeType.AndSign) {
                Code taskAbortStatus = codeService.getCodeByEnum(TaskStatus.Abort);
                for (Task t : runningTaskList) {
                    t.setStatus(taskAbortStatus);
                    update(t);
                    TaskEventArgs taskEventArgs = new TaskEventArgs();
                    taskEventArgs.setTask(t);
                    TaskEventPublisher.publishTaskAbortEvent(this, taskEventArgs);
                }

                Code instanceResult = codeService.getCodeByEnum(InstanceResult.Reject);
                subInstance.setResult(instanceResult);
                entityDao.update(subInstance);
                instance.setResult(instanceResult);
                update(instance);

                instanceEventArgs.setInstance(instance);
                InstanceEventPublisher.publishInstanceApproveRejectEvent(this, instanceEventArgs);
            }

            if (currentFlowNode.getFlowNodeType() == FlowNodeType.Vote) {
                if (runningTaskList.size() > 0) {
                    return;
                }
                // TODO : 根据投票规则决定后续分支
            }
        }

        boolean instanceReturn = false;
        if (taskHandleResult == TaskHandleResult.Pass) {
            if (runningTaskList.size() > 0) {
                return;
            }
            if (currentFlowNode.getFlowNodeType() == FlowNodeType.AndSign) {
                if (currentFlowNode.getInstanceReturnMode() == 1) {
                    for (Task t : masterTask.getTaskList()) {
                        if (t.getResult().getCodeTag().equals(TaskHandleResult.Return.name())) {
                            instanceReturn = true;
                            break;
                        }
                    }
                }
            } else if (currentFlowNode.getFlowNodeType() == FlowNodeType.Vote) {
                // TODO : 根据投票规则决定后续分支
            }
        }

        if (taskHandleResult == TaskHandleResult.Return) {
            instanceReturn = !(runningTaskList.size() > 0 && currentFlowNode.getInstanceReturnMode() == 1);
        }

        task.setContextList(taskContextList);

        FlowNodeRoute flowNodeRoute;
        FlowNode nextFlowNode;
        if (instanceReturn) {
            flowNodeRoute = wfRegulationService.parseFlowNodeRoute(instance, currentFlowNode, task, TaskHandleResult.Return);
            nextFlowNode = flowNodeRoute.getToFlowNode();
            instance.setCurrentFlowNode(nextFlowNode);
            update(instance);
            setEntityStatusByRamus(relatedEntityInstance, flowNodeRoute.getRamus());
            if (runningTaskList.size() > 0) {
                Code taskAbortStatus = codeService.getCodeByEnum(TaskStatus.Abort);
                for (Task t : runningTaskList) {
                    t.setStatus(taskAbortStatus);
                    update(t);
                    TaskEventArgs taskEventArgs = new TaskEventArgs();
                    taskEventArgs.setTask(t);
                    TaskEventPublisher.publishTaskAbortEvent(this, taskEventArgs);
                }
            }
            Code status = codeService.getCodeByEnum(InstanceStatus.Finish);
            Code instanceResult = codeService.getCodeByEnum(InstanceResult.Return);
            subInstance.setStatus(status);
            subInstance.setFinishTime(DateUtil.getCurrentTime());
            subInstance.setResult(instanceResult);
            entityDao.update(subInstance);

            SubInstance nextSubInstance = new SubInstance();
            nextSubInstance.setInstance(instance);
            nextSubInstance.setStatus(instance.getStatus());
            nextSubInstance.setRank(subInstance.getRank() + 1);
            nextSubInstance.setCreateTime(DateUtil.getCurrentTime());
            nextSubInstance.setCreator(instance.getCreator());
            save(nextSubInstance);
            createTask(currentUser, nextSubInstance, masterTask, nextFlowNode, null, null);
            return;
        }

        // TODO 为投票选择一个合适的任务处理结果  Pass OR Reject
        flowNodeRoute = wfRegulationService.parseFlowNodeRoute(instance, currentFlowNode, task, taskHandleResult);
        nextFlowNode = flowNodeRoute.getToFlowNode();
        instance.setCurrentFlowNode(nextFlowNode);
        update(instance);

        if ((taskHandleResult == TaskHandleResult.Return || taskHandleResult == TaskHandleResult.Submit) && StringUtil.isEmpty(flowNodeRoute.getRamus().getStatusTag())) {
            throw new BaseBusinessException("", "分支[" + flowNodeRoute.getRamus().getName() + "]上没有设定业务实体的状态标识.");
        }
        if (StringUtil.isNotEmpty(flowNodeRoute.getRamus().getStatusTag())) {
            setEntityStatusByRamus(relatedEntityInstance, flowNodeRoute.getRamus());
        }

        if (nextFlowNode.getFlowNodeType() == FlowNodeType.End || nextFlowNode.getFlowNodeType() == FlowNodeType.First) {
            createTask(currentUser, subInstance, masterTask, nextFlowNode, nextHandlerList, firstNodeHandlerList);
            return;
        }

        if (CollectionUtil.isNotEmpty(firstNodeHandlerList)) {
            createTask(currentUser, subInstance, masterTask, nextFlowNode, firstNodeHandlerList, null);
        } else {
            if (CollectionUtil.isEmpty(nextHandlerList)) {
                nextHandlerList = new ArrayList<User>();
                if (nextFlowNode.getSuspendInstance() == 1) {
                    nextHandlerList.add(userService.getSystemUser());
                } else {
                    nextHandlerList = wfRegulationService.parseTaskHandler(instance, nextFlowNode);
                    // todo 过滤审批人列表
                    nextHandlerList = nextHandlerList.subList(0, 1);
                }
            }
            createTask(currentUser, subInstance, masterTask, nextFlowNode, nextHandlerList, firstNodeHandlerList);
        }
    }

    public void saveInstanceOrganization(Instance instance, FlowNode flowNode, Task task) throws Exception {

        if (flowNode.getFlowNodeType() != FlowNodeType.Begin && flowNode.getFlowNodeType() != FlowNodeType.First
                && flowNode.getFlowNodeType() != FlowNodeType.End && flowNode.getFlowNodeType() != FlowNodeType.Modify) {
            InstanceOrganization instanceOrganization = new InstanceOrganization();
            instanceOrganization.setObjectId(StringUtil.getUuid());
            instanceOrganization.setInstance(instance);
            instanceOrganization.setFlowNode(flowNode);
            instanceOrganization.setOrganization(task.getOwner().getOrganization());
            instanceOrganization.setUser(task.getOwner());
            instanceOrganization.setTask(task);
            instanceOrganization.setCreateTime(DateUtil.getCurrentTime());
            save(instanceOrganization);
        }
    }

    public void rollback(Task task) throws Exception {

        MasterTask masterTask = task.getMasterTask();
        if (masterTask.getTaskList().size() == 1) {
            for (Task t : masterTask.getPreviousMasterTask().getTaskList()) {
                t.setLocked(0);
                update(t);
            }
        } else {
            boolean findFinishTask = false;
            for (Task t : masterTask.getTaskList()) {
                if (t.getObjectId().equals(task.getObjectId())) {
                    continue;
                }
                if (t.getFinishTime() != null) {
                    findFinishTask = true;
                    break;
                }
            }
            if (!findFinishTask) {
                for (Task t : masterTask.getPreviousMasterTask().getTaskList()) {
                    t.setLocked(0);
                    update(t);
                }
            }
        }

        // 重新激活任务
        Code taskRunningStatus = codeService.getCodeByEnum(TaskStatus.Running);
        task.setStatus(taskRunningStatus);
        task.setFinishTime(null);
        task.setResult(null);
        update(task);

        // 抛出任务回滚事件,让业务对象先处理
        TaskEventArgs taskArgs = new TaskEventArgs();
        taskArgs.setTask(task);
        TaskEventPublisher.publishTaskRollbackEvent(this, taskArgs);

        // 清除任务处理的上下文
        delete(task.getTaskOpinion());
        delete(task.getContextList());

        // 设置业务实体状态
        Instance instance = task.getMasterTask().getSubInstance().getInstance();
        AbstractBaseEntity entityInstance = instanceService.getRelatedEntityInstance(instance);
        FlowNode flowNode = task.getMasterTask().getFlowNode();
        if (StringUtil.isNotEmpty(flowNode.getEntityStatusTag())) {
            try {
                String codeColumnTag = flowNode.getEntityStatusTag().split("[.]")[0];
                String codeTag = flowNode.getEntityStatusTag().split("[.]")[1];
                Code status = codeService.getCodeByTag(codeColumnTag, codeTag);
                if (status != null) {
                    ReflectionUtil.setFieldValue(entityInstance, "status", status);
                    update(entityInstance);
                }
            } catch (Exception e) {
                // do nothing
            }
        }

        // 重新激活被中止的任务
        for (Task t : masterTask.getTaskList()) {
            boolean b = t.getStatus().getCodeTag().equalsIgnoreCase(TaskStatus.Abort.toString());
            if (b) {
                t.setStatus(taskRunningStatus);
                update(t);
            }
        }

        // 删除后续任务
        if (masterTask.getStatus().getCodeTag().equalsIgnoreCase(TaskStatus.Finish.toString())) {

            if (masterTask.getPreviousMasterTask().getFlowNode().getFlowNodeType() == FlowNodeType.First
                    || masterTask.getPreviousMasterTask().getFlowNode().getFlowNodeType() == FlowNodeType.Modify) {
                Ramus ramus = masterTask.getPreviousMasterTask().getFlowNode().getFlowNodeRouteList().get(0).getRamus();
                setEntityStatusByRamus(entityInstance, ramus);
            }

            // 主任务完成后,必定产生下一个主任务
            String hql = "select o from " + MasterTask.class.getName() + " o"
                    + " where o.previousMasterTask.objectId='" + masterTask.getObjectId() + "'";
            List list = listByHql(hql);

            List<MasterTask> nextMasterTaskList = (List<MasterTask>) list;
            for (int i = 0; i < nextMasterTaskList.size(); i++) {
                MasterTask nextMasterTask = nextMasterTaskList.get(i);
                for (Task t : nextMasterTask.getTaskList()) {
                    if (nextMasterTask.getFlowNode().getFlowNodeType() != FlowNodeType.End) {
                        taskArgs = new TaskEventArgs();
                        taskArgs.setTask(t);
                        TaskEventPublisher.publishTaskDeleteEvent(this, taskArgs);
                    }
                    hql = "delete from " + EntityModifyTask.class.getName() + " o where o.task.objectId='" + t.getObjectId() + "'";
                    executeHql(hql);
                    delete(t);

                    hql = "delete " + InstanceOrganization.class.getName() + " o" + " where o.task.objectId='" + t.getObjectId() + "'";
                    executeHql(hql);
                }
                delete(nextMasterTask);

                // 判断是否删除子流程
                if (nextMasterTask.getFlowNode().getFlowNodeType() == FlowNodeType.Modify) {
                    delete(nextMasterTask.getSubInstance());
                }
            }
        }

        // 重置主任务
        masterTask.setStatus(taskRunningStatus);
        masterTask.setFinishTime(null);
        update(masterTask);

        // 重置子流程
        Code instanceRunningStatus = codeService.getCodeByEnum(InstanceStatus.Running);
        SubInstance subInstance = masterTask.getSubInstance();
        subInstance.setStatus(instanceRunningStatus);
        subInstance.setFinishTime(null);
        subInstance.setResult(null);
        update(subInstance);

        // 重置流程
        instance.setStatus(instanceRunningStatus);
        instance.setCurrentFlowNode(task.getMasterTask().getFlowNode());
        instance.setFinishTime(null);
        instance.setResult(null);
        update(instance);

    }

    public void onInstanceDeleteEvent(InstanceDeleteEvent event) throws Exception {

        Instance instance = event.getInstanceEventArgs().getInstance();
        abortInstanceRunningTasks(instance);
    }

    public void onInstanceAbortEvent(InstanceAbortEvent event) throws Exception {

        Instance instance = event.getInstanceEventArgs().getInstance();
        abortInstanceRunningTasks(instance);
    }

//    public void createTask(SubInstance subInstance, MasterTask previousMasterTask, FlowNode flowNode, List<User> handlerList, List<User> firstNodeHandlerList) throws Exception {
//
//        User currentUser = SpringSecurityUtil.getCurrentUser();
//        createTask(currentUser, subInstance, previousMasterTask, flowNode, handlerList, firstNodeHandlerList);
//    }

    public void createTask(User currentUser, SubInstance subInstance, MasterTask previousMasterTask, FlowNode flowNode, List<User> handlerList, List<User> firstNodeHandlerList) throws Exception {

        FlowNodeType flowNodeType = flowNode.getFlowNodeType();

        Date taskCreateTime = DateUtil.getCurrentTime();
        Date taskExpiredTime = taskCreateTime;
        if (flowNode.getExpiration() > 0) {
            taskExpiredTime = workCalendarService.getWorkTimeExpiration(taskCreateTime, flowNode.getExpiration());
        }
        Code taskRunningStatus = codeService.getCodeByEnum(TaskStatus.Running);
        if (flowNode.getFlowNodeTaskMode() == FlowNodeTaskMode.Alone && CollectionUtil.isNotEmpty(handlerList)) {
            for (User handler : handlerList) {
                // 主任务
                MasterTask masterTask = new MasterTask();
                masterTask.setObjectId(StringUtil.getUuid());
                masterTask.setSubInstance(subInstance);
                masterTask.setPreviousMasterTask(previousMasterTask);
                masterTask.setFlowNode(flowNode);
                masterTask.setStarter(currentUser);
                masterTask.setStatus(taskRunningStatus);
                save(masterTask);

                // 任务
                Task task = new Task();
                task.setObjectId(StringUtil.getUuid());
                task.setMasterTask(masterTask);
                task.setStatus(taskRunningStatus);
                task.setOwner(handler);
                task.setHandler(handler);
                // 代理实现
                if (flowNode.getAllowAgent() == 1) {
                    if (handler.getObjectId().equals(currentUser.getObjectId()) || handler.getObjectId().equals(SystemConstant.USER_SYSTEM)) {
                        task.setSourceType(TaskSourceType.SYSTEM);
                    } else {
                        User agent = agencyService.getAgent(handler);
                        if (agent != null) {
                            task.setHandler(agent);
                            task.setSourceType(TaskSourceType.AGENCY);
                        }
                    }
                }
                task.setCreateTime(taskCreateTime);
                task.setTimeMillis(taskCreateTime.getTime());
                task.setArriveTime(taskCreateTime);
                task.setDeliver(userService.getSystemUser());
                task.setExpiration(flowNode.getExpiration());
                task.setExpiredTime(taskExpiredTime);
                save(task);

                saveInstanceOrganization(subInstance.getInstance(), flowNode, task);

                // 抛出任务创建事件
                TaskEventArgs taskEventArgs = new TaskEventArgs();
                taskEventArgs.setTask(task);
                if (flowNode.getFlowNodeType() == FlowNodeType.Modify) {
                    TaskEventPublisher.publishModifyTaskCreateEvent(this, taskEventArgs);
                } else {
                    TaskEventPublisher.publishTaskCreateEvent(this, taskEventArgs);
                }
            }
        } else {
            // 主任务
            MasterTask masterTask = new MasterTask();
            masterTask.setObjectId(StringUtil.getUuid());
            masterTask.setSubInstance(subInstance);
            masterTask.setPreviousMasterTask(previousMasterTask);
            masterTask.setFlowNode(flowNode);
            masterTask.setStarter(currentUser);
            Code status = codeService.getCodeByEnum(TaskStatus.Running);
            masterTask.setStatus(status);
            masterTask.setCreateTime(DateUtil.getCurrentTime());
            save(masterTask);

            if (flowNodeType == FlowNodeType.Begin || flowNodeType == FlowNodeType.End) {
                User systemUser = userService.getSystemUser();
                handlerList = new ArrayList();
                handlerList.add(systemUser);
            } else if (flowNodeType == FlowNodeType.First) {
                handlerList = new ArrayList();
                handlerList.add(currentUser);
            } else if (flowNodeType == FlowNodeType.Modify) {
                handlerList = new ArrayList();
                handlerList.add(subInstance.getInstance().getCreator());
            }

            // 任务
            for (User handler : handlerList) {
                Task task = new Task();
                task.setObjectId(StringUtil.getUuid());
                task.setMasterTask(masterTask);
                task.setStatus(taskRunningStatus);
                task.setOwner(handler);
                task.setHandler(handler);
                // 代理实现
                if (flowNode.getAllowAgent() == 1) {
                    if (handler.getObjectId().equals(currentUser.getObjectId()) || handler.getObjectId().equals(SystemConstant.USER_SYSTEM)) {
                        task.setSourceType(TaskSourceType.SYSTEM);
                    } else {
                        User agent = agencyService.getAgent(handler);
                        if (agent != null) {
                            task.setHandler(agent);
                            task.setSourceType(TaskSourceType.AGENCY);
                        }
                    }
                }
                task.setCreateTime(taskCreateTime);
                task.setTimeMillis(taskCreateTime.getTime());
                task.setArriveTime(taskCreateTime);
                task.setDeliver(userService.getSystemUser());
                task.setExpiration(flowNode.getExpiration());
                task.setExpiredTime(taskExpiredTime);
                save(task);

                saveInstanceOrganization(subInstance.getInstance(), flowNode, task);

                // 抛出任务创建事件
                if (flowNodeType != FlowNodeType.Begin && flowNodeType != FlowNodeType.End
                        && flowNodeType != FlowNodeType.First) {
                    TaskEventArgs taskEventArgs = new TaskEventArgs();
                    taskEventArgs.setTask(task);
                    if (flowNodeType == FlowNodeType.Modify) {
                        TaskEventPublisher.publishModifyTaskCreateEvent(this, taskEventArgs);
                    } else {
                        TaskEventPublisher.publishTaskCreateEvent(this, taskEventArgs);
                    }
                }

                // 记录对象的修改任务，以便在对象修改后再次提交时关联到相关任务
                if (flowNodeType == FlowNodeType.Modify) {
                    EntityModifyTask objectModifyTask = new EntityModifyTask();
                    objectModifyTask.setEntityInstanceId(subInstance.getInstance().getRelatedObjectId());
                    objectModifyTask.setTask(task);
                    objectModifyTask.setFlowNode(previousMasterTask.getFlowNode());
                    save(objectModifyTask);
                }

                // 判断是否自动提交任务
                // TODO : 应该根据TaskExecuteMode来判断,自动提交前应该实行一个服务调用接口
                if (flowNodeType == FlowNodeType.Begin) {
                    commit(currentUser, task, null, TaskHandleResult.Start, "流程启动", null, firstNodeHandlerList);
                } else if (flowNodeType == FlowNodeType.First) {
                    commit(currentUser, task, null, TaskHandleResult.Submit, "提交", null, firstNodeHandlerList);
                } else if (flowNodeType == FlowNodeType.End) {
                    commit(currentUser, task, null, TaskHandleResult.Complete, "流程结束", null, null);
                } else if (flowNode.getSuspendInstance() == 1) {
                    commit(currentUser, task, null, TaskHandleResult.Suspend, flowNode.getSuspendDescription(), null, null);
                }
            }
        }
    }


    public void finishInstance(Instance instance) throws Exception {

        Code status = codeService.getCodeByEnum(InstanceStatus.Finish);
        Code result = codeService.getCodeByEnum(InstanceResult.Pass);

        instance.setStatus(status);
        FlowNode endFlowNode = flowTypeService.getEndFlowNode(instance.getFlowType());
        instance.setCurrentFlowNode(endFlowNode);
        instance.setFinishTime(DateUtil.getCurrentTime());
        if (instance.getResult() == null) {
            instance.setResult(result);
        }
        update(instance);

        SubInstance subInstance = instance.getSubInstanceList().get(0);
        subInstance.setStatus(status);
        subInstance.setFinishTime(DateUtil.getCurrentTime());
        if (subInstance.getResult() == null) {
            subInstance.setResult(result);
        }
        update(subInstance);

        InstanceEventArgs instanceEventArgs = new InstanceEventArgs();
        instanceEventArgs.setInstance(instance);
        InstanceEventPublisher.publishInstanceFinishEvent(this, instanceEventArgs);
    }

    public void setEntityStatusByRamus(AbstractBaseEntity entityInstance, Ramus ramus) throws Exception {

        if (StringUtil.isEmpty(ramus.getStatusTag())) {
            return;
        }

        try {
            String codeColumnTag = ramus.getStatusTag().split("[.]")[0];
            String codeTag = ramus.getStatusTag().split("[.]")[1];
            Code status = codeService.getCodeByTag(codeColumnTag, codeTag);
            if (status == null) {
                throw new BaseBusinessException("", "分支[" + ramus.getName() + "]无法更新实体的状态值.");
            }
            ReflectionUtil.setFieldValue(entityInstance, "status", status);
            update(entityInstance);
        } catch (Exception e) {
            throw new BaseBusinessException("", "分支[" + ramus.getName() + "]无法更新实体的状态值.");
        }

        WorkflowEventPublisher.publishEntityStatusChangeEvent(this, entityInstance);
    }

    public void addTaskContext(String taskId, String contextKey, String contextValue) throws Exception {

        Task task = get(Task.class, taskId);
        TaskContext taskContext = new TaskContext();
        taskContext.setTask(task);
        taskContext.setContextKey(contextKey);
        taskContext.setContextValue(contextValue);
        save(taskContext);
    }

    public void finishMasterTask(MasterTask masterTask) throws Exception {

        Code taskFinishStatus = codeService.getCodeByEnum(TaskStatus.Finish);
        masterTask.setStatus(taskFinishStatus);
        masterTask.setFinishTime(DateUtil.getCurrentTime());
        entityDao.update(masterTask);
    }

    public void finishTask(Task task, FlowNode flowNode, TaskHandleResult result, String opinion) throws Exception {

        Code taskFinishStatus = codeService.getCodeByEnum(TaskStatus.Finish);
        task.setStatus(taskFinishStatus);
        task.setFinishTime(DateUtil.getCurrentTime());
        Code taskHandleResult = codeService.getCodeByEnum(result);
        task.setResult(taskHandleResult);
        update(task);

        if (StringUtil.isNotEmpty(opinion)) {
            TaskOpinion taskOpinion = new TaskOpinion();
            taskOpinion.setTask(task);
            taskOpinion.setOpinion(opinion);
            save(taskOpinion);
        }

        if (flowNode.getFlowNodeType() != FlowNodeType.Begin && flowNode.getFlowNodeType() != FlowNodeType.End) {
            TaskEventArgs args = new TaskEventArgs();
            args.setTask(task);
            TaskEventPublisher.publishTaskFinishEvent(this, args);
        }
    }

    public List<Task> listRunningTasks(MasterTask masterTask) {

        List<Task> runningTaskList = new ArrayList<Task>();
        for (Task task : masterTask.getTaskList()) {
            if (task.getStatus().getCodeTag().equalsIgnoreCase(TaskStatus.Running.toString())) {
                runningTaskList.add(task);
            }
        }

        return runningTaskList;
    }

    public void abortInstanceRunningTasks(Instance instance) throws Exception {

        List<Task> runningTaskList = instanceService.listRunningTasks(instance);

        if (CollectionUtil.isEmpty(runningTaskList)) {
            return;
        }
        Code taskAbortStatus = codeService.getCodeByEnum(TaskStatus.Abort);
        for (Task task : runningTaskList) {
            task.setStatus(taskAbortStatus);
            save(task);
            TaskEventArgs taskEventArgs = new TaskEventArgs();
            taskEventArgs.setTask(task);
            TaskEventPublisher.publishTaskAbortEvent(this, taskEventArgs);
        }
    }

    public void onAgencyCreatedEvent(AgencyCreatedEvent event) throws Exception {

        Agency agency = event.getAgencyEventArgs().getAgency();
        String hql = "select o from " + Task.class.getName() + " o"
                + " where handler.objectId=? and o.status.codeTag=?";
        List list = listByHql(hql, agency.getOwner().getObjectId(), TaskStatus.Running.name());
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        for (Task task : (List<Task>) list) {
            if (task.getMasterTask().getFlowNode().getAllowAgent() == 0) {
                continue;
            }
            task.setHandler(agency.getAgent());
            update(task);
            TaskEventArgs args = new TaskEventArgs();
            args.setTask(task);
            TaskEventPublisher.publishTaskSetAgentEvent(this, args);
        }
    }

    public void onAgencyFinishedEvent(AgencyFinishedEvent event) throws Exception {

        Agency agency = event.getAgencyEventArgs().getAgency();
        String hql = "select o from " + Task.class.getName() + " o"
                + " where handler.objectIdowner.objectId and handler.objectId=? and o.status.codeTag=?";
        List list = listByHql(hql, agency.getAgent().getObjectId(), TaskStatus.Running.name());
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        for (Task task : (List<Task>) list) {
            task.setHandler(agency.getAgent());
            save(task);
            TaskEventArgs args = new TaskEventArgs();
            args.setTask(task);
            TaskEventPublisher.publishTaskTakebackEvent(this, args);
        }
    }

    public void onEntityTimeoutEvent(EntityTimeoutEvent event) throws Exception {

        if (!(event.getEntityInstance() instanceof Task)) {
            return;
        }

        Task task = (Task) event.getEntityInstance();
        task.setTimeout(1);
        update(task);

        TaskEventArgs args = new TaskEventArgs();
        args.setTask(task);
        TaskEventPublisher.publishTaskTimeoutEvent(this, args);
    }

    public void onEntityWillTimeoutEvent(EntityTimeoutEvent event) throws Exception {

        if (!(event.getEntityInstance() instanceof Task)) {
            return;
        }

        Task task = (Task) event.getEntityInstance();

        TaskEventArgs args = new TaskEventArgs();
        args.setTask(task);
        TaskEventPublisher.publishTaskWillTimeoutEvent(this, args);
    }

    public void onModifyTaskCreateEvent(ModifyTaskCreateEvent event) throws Exception {

        Task task = event.getTaskEventArgs().getTask();
        if (StringUtil.isNotEmpty(task.getHandler().getMobile())) {
            smsQueueService.messageToQueue
                    (task.getHandler().getMobile(), "您提交的" + task.getMasterTask().getFlowNode().getFlowType().getName() + "被退回。");
        }
    }

    public void onTaskCreateEvent(TaskCreateEvent event) throws Exception {

        Task task = event.getTaskEventArgs().getTask();
        if (StringUtil.isNotEmpty(task.getHandler().getMobile())) {
            smsQueueService.messageToQueue
                    (task.getHandler().getMobile(), "您有新的待办事项，请及时处理。");
        }
    }

}
