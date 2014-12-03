package com.withub.service.impl.workflow;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.entity.event.EntityLogicDeleteEvent;
import com.withub.model.entity.query.*;
import com.withub.model.exception.BaseBusinessException;
import com.withub.model.system.po.User;
import com.withub.model.workflow.NextFlowNodeInfo;
import com.withub.model.workflow.enumeration.FlowNodeType;
import com.withub.model.workflow.enumeration.InstanceStatus;
import com.withub.model.workflow.enumeration.TaskHandleResult;
import com.withub.model.workflow.enumeration.TaskSourceType;
import com.withub.model.workflow.event.DecisionCreateEvent;
import com.withub.model.workflow.event.instanceevent.InstanceDeleteEvent;
import com.withub.model.workflow.event.taskevent.TaskEventArgs;
import com.withub.model.workflow.event.taskevent.TaskEventPublisher;
import com.withub.model.workflow.po.*;
import com.withub.model.workflow.vo.TaskFlowNodeInfo;
import com.withub.model.workflow.vo.TaskInfo;
import com.withub.service.EntityServiceImpl;
import com.withub.service.system.CodeService;
import com.withub.service.workflow.*;
import com.withub.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("workflowService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class WorkflowServiceImpl extends EntityServiceImpl implements WorkflowService {

    //============================== 属性声明 =============================================================

    @Autowired
    private FlowTypeService flowTypeService;

    @Autowired
    private WFRegulationService wfRegulationService;

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CodeService codeService;

    //=============================== 接口实现 ============================================================

    public RecordsetInfo queryCurrentUserTask(QueryInfo queryInfo) throws Exception {

        User user = SpringSecurityUtil.getCurrentUser();
        SqlExpressionConfig sqlExpressionConfig = new SqlExpressionConfig();
        sqlExpressionConfig.setPropertyName("handler");
        sqlExpressionConfig.setPropertyValue(user.getObjectId());
        sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.Equals);
        QueryConditionNode queryConditionNode = new QueryConditionNode();
        queryConditionNode.setSqlExpressionConfig(sqlExpressionConfig);
        queryInfo.getQueryConditionTree().getUserConditionNode().appendNode(queryConditionNode);

        RecordsetInfo recordsetInfo = query(queryInfo);
        return recordsetInfo;
    }

    public RecordsetInfo queryCurrentUserInstance(QueryInfo queryInfo) throws Exception {

        SqlExpressionConfig sqlExpressionConfig = new SqlExpressionConfig();
        sqlExpressionConfig.setPropertyName("creator.objectId");
        sqlExpressionConfig.setPropertyValue(SpringSecurityUtil.getCurrentUser().getObjectId());
        sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.Equals);
        QueryConditionNode queryConditionNode = new QueryConditionNode();
        queryConditionNode.setSqlExpressionConfig(sqlExpressionConfig);
        queryInfo.getQueryConditionTree().getUserConditionNode().appendNode(queryConditionNode);

        RecordsetInfo recordsetInfo = query(queryInfo);
        return recordsetInfo;
    }

    public RecordsetInfo queryInstance(QueryInfo queryInfo) throws Exception {

        RecordsetInfo recordsetInfo = query(queryInfo);

        return recordsetInfo;
    }

    public FlowNode getNextApproveFlowNode(String taskId) throws Exception {

        Task task = get(Task.class, taskId);
        Instance instance = getInstanceByTaskId(taskId);
        FlowNode currentFlowNode = task.getMasterTask().getFlowNode();

        List<FlowNodeRoute> flowNodeRouteList = currentFlowNode.getFlowNodeRouteList();

        if (CollectionUtil.isEmpty(flowNodeRouteList)) {
            return null;
        }

        FlowNodeRoute flowNodeRoute = wfRegulationService.parseFlowNodeRoute(instance, currentFlowNode, task, TaskHandleResult.Pass);
        FlowNode nextFlowNode = flowNodeRoute.getToFlowNode();
        return nextFlowNode;
    }

    public FlowNode getFlowNodeByTaskId(String taskId) throws Exception {

        Task task = (Task) get(Task.class, taskId);
        FlowNode flowNode = task.getMasterTask().getFlowNode();
        return flowNode;
    }

    public NextFlowNodeInfo getNextFlowNodeInfo(String taskId) throws Exception {

        NextFlowNodeInfo nextFlowNodeInfo = new NextFlowNodeInfo();

        Task task = get(Task.class, taskId);
        Instance instance = getInstanceByTaskId(taskId);
        FlowNode currentFlowNode = task.getMasterTask().getFlowNode();
        nextFlowNodeInfo.setCurrentFlowNode(currentFlowNode);

        List<FlowNodeRoute> flowNodeRouteList = currentFlowNode.getFlowNodeRouteList();

        if (CollectionUtil.isEmpty(flowNodeRouteList)) {
            return null;
        }

        FlowNodeRoute flowNodeRoute = wfRegulationService.parseFlowNodeRoute(instance, currentFlowNode, task, TaskHandleResult.Pass);
        FlowNode nextFlowNode = flowNodeRoute.getToFlowNode();

        nextFlowNodeInfo.setNextFlowNode(nextFlowNode);
        if (nextFlowNode == null) {
            return nextFlowNodeInfo;
        }

        String hql = "select o from " + InstanceOrganization.class.getName() + " o"
                + " where o.instance.objectId=? and o.flowNode.objectId=?"
                + " order by o.createTime desc";
        InstanceOrganization instanceOrganization = (InstanceOrganization) getByHql(hql, instance.getObjectId(), nextFlowNode.getObjectId());
        if (instanceOrganization != null) {
            nextFlowNodeInfo.setHandler(instanceOrganization.getUser());
        }

        return nextFlowNodeInfo;
    }

    public NextFlowNodeInfo getNextFlowNodeInfo(Class<? extends AbstractBaseEntity> clazz, String flowTypeTag) throws Exception {

        NextFlowNodeInfo nextFlowNodeInfo = new NextFlowNodeInfo();
        String hql = "select o from " + FlowType.class.getName() + " o where o.objectStatus=1"
                + " and o.entity.entityName='" + clazz.getSimpleName() + "'";
        if (StringUtil.isNotEmpty(flowTypeTag)) {
            hql += " and o.flowTypeTag='" + flowTypeTag + "'";
        }

        FlowType flowType = (FlowType) getByHql(hql);
        FlowNode firstFlowNode = flowTypeService.getFirstFlowNode(flowType);
        nextFlowNodeInfo.setCurrentFlowNode(firstFlowNode);
        FlowNode nextFlowNode = firstFlowNode.getFlowNodeRouteList().get(0).getToFlowNode();
        nextFlowNodeInfo.setNextFlowNode(nextFlowNode);

        return nextFlowNodeInfo;
    }

    public AbstractBaseEntity getInstanceRelatedEntity(Instance instance) throws Exception {

        return instanceService.getRelatedEntityInstance(instance);
    }

    public void deleteInstance(String instanceId) throws Exception {

        Instance instance = (Instance) get(Instance.class, instanceId);
        instanceService.delete(instance);

        // 删除流程对应的实体
        AbstractBaseEntity entity = instanceService.getRelatedEntityInstance(instance);
        logicDelete(entity);
    }

    public void abortInstance(String instanceId) {

    }

    public List<User> listInstanceCurrentHandler(String instanceId) throws Exception {

        List<User> userList = new ArrayList<User>();
        Instance instance = (Instance) get(Instance.class, instanceId);
        if (!instance.getStatus().getCodeTag().equalsIgnoreCase(InstanceStatus.Running.toString())) {
            return null;
        }

        List<Task> taskList = instanceService.listRunningTasks(instance);
        for (Task t : taskList) {
            userList.add(t.getHandler());
        }

        return userList;
    }

    public List<User> listEntityInstanceCurrentHandler(String entityInstanceId) throws Exception {

        String hql = "select o from " + Instance.class.getName() + " o where o.objectStatus=1"
                + " and o.relatedObjectId=? and o.status.codeTag=?";
        Instance instance = (Instance) getByHql(hql, entityInstanceId, InstanceStatus.Running.name());
        return listInstanceCurrentHandler(instance.getObjectId());
    }

    public Instance getInstanceByTaskId(String taskId) throws Exception {

        Task task = (Task) get(Task.class, taskId);
        Instance instance = task.getMasterTask().getSubInstance().getInstance();
        return instance;
    }


    public AbstractBaseEntity getRelatedEntityInstanceByTaskId(String taskId) throws Exception {

        Instance instance = getInstanceByTaskId(taskId);
        AbstractBaseEntity entityInstance = instanceService.getRelatedEntityInstance(instance);
        return entityInstance;
    }

    public Instance getInstanceByRelatedObjectId(String relatedObjectId) throws Exception {

        Instance instance = (Instance) getByPropertyValue(Instance.class, "relatedObjectId", relatedObjectId);

        return instance;
    }

    public FlowNode getFlowNodeByTag(String flowNodeTag) throws Exception {

        String hql = "select o from " + FlowNode.class.getName() + " o"
                + " where o.objectStatus=1 and lower(o.flowNodeTag)='" + flowNodeTag.toLowerCase() + "'";
        FlowNode flowNode = (FlowNode) getByHql(hql);
        return flowNode;
    }

    public Task getTaskById(String taskId) throws Exception {

        Task task = taskService.getTaskById(taskId);
        return task;
    }

    public TaskInfo getCurrentUserApproveTaskByRelatedObjectId(String relatedObjectId) throws Exception {

        TaskInfo taskInfo = null;
        String hql = "select o from " + TaskInfo.class.getName() + " o"
                + " where o.relatedObjectId='" + relatedObjectId + "'"
                + " and o.handler='" + SpringSecurityUtil.getCurrentUser().getObjectId() + "'"
                + " and o.flowNodeType='AndSign'"
                + " and lower(o.taskStatusTag)='running'";
        List list = listByHql(hql);
        if (CollectionUtil.isNotEmpty(list)) {
            taskInfo = (TaskInfo) list.get(0);
        }

        return taskInfo;
    }

    /**
     * 获取任务节点的审批信息
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    public TaskFlowNodeInfo getTaskFlowNode(String taskId) throws Exception {

        FlowNode flowNode = getFlowNodeByTaskId(taskId);
        TaskFlowNodeInfo taskFlowNodeInfo = new TaskFlowNodeInfo();
        taskFlowNodeInfo.setFlowNodeName(flowNode.getName());
        taskFlowNodeInfo.setFlowNodeTag(flowNode.getFlowNodeTag());
        taskFlowNodeInfo.setPassAction(flowNode.getPassAction());
        taskFlowNodeInfo.setReturnAction(flowNode.getReturnAction());
        taskFlowNodeInfo.setRejectAction(flowNode.getRejectAction());
        taskFlowNodeInfo.setCompleteAction(flowNode.getCompleteAction());
        taskFlowNodeInfo.setDiscardAction(flowNode.getDiscardAction());
        taskFlowNodeInfo.setManualSelectHandler(0);
        if (flowNode.getManualSelectHandler() != null && flowNode.getManualSelectHandler() == 1) {
            NextFlowNodeInfo nextFlowNodeInfo = getNextFlowNodeInfo(taskId);
            if (nextFlowNodeInfo != null) {
                taskFlowNodeInfo.setManualSelectHandler(nextFlowNodeInfo.getCurrentFlowNode().getManualSelectHandler());
                if (nextFlowNodeInfo.getNextFlowNode() != null) {
                    taskFlowNodeInfo.setHandlerFetchCount(nextFlowNodeInfo.getNextFlowNode().getHandlerFetchCount());
                    taskFlowNodeInfo.setNextFlowNodeName(nextFlowNodeInfo.getNextFlowNode().getName());
                    taskFlowNodeInfo.setNextFlowNodeType(nextFlowNodeInfo.getNextFlowNode().getFlowNodeType().name());
                    if (nextFlowNodeInfo.getNextFlowNode().getFlowNodeType() != FlowNodeType.End) {
                        Instance instance = getInstanceByTaskId(taskId);
                        List<User> handlerList = wfRegulationService.parseTaskHandler(instance, nextFlowNodeInfo.getNextFlowNode());
                        taskFlowNodeInfo.setHandlerList(handlerList);
                    }
                }
            }
        }
        return taskFlowNodeInfo;
    }

    public void commitTask(User currentUser, String taskId, List<TaskContext> taskContextList, TaskHandleResult result, String opinion) throws Exception {

        Task task = get(Task.class, taskId);
        taskService.commit(currentUser, task, taskContextList, result, opinion, null, null);
    }

    public void passTask(User currentUser, String taskId, List<TaskContext> taskContextList, String opinion) throws Exception {

        Task task = get(Task.class, taskId);
        task.setContextList(taskContextList);
        taskService.commit(currentUser, task, taskContextList, TaskHandleResult.Pass, opinion, null, null);
    }

    public void passTask(User currentUser, String taskId, List<TaskContext> taskContextList, String opinion, User nextHandler) throws Exception {

        Task task = get(Task.class, taskId);
        List<User> nextHandlerList = new ArrayList();
        nextHandlerList.add(nextHandler);
        taskService.commit(currentUser, task, taskContextList, TaskHandleResult.Pass, opinion, nextHandlerList, null);
    }

    public void passTask(User currentUser, String taskId, List<TaskContext> taskContextList, String opinion, List<User> nextHandlerList) throws Exception {

        Task task = get(Task.class, taskId);
        taskService.commit(currentUser, task, taskContextList, TaskHandleResult.Pass, opinion, nextHandlerList, null);
    }

    public void returnTask(User currentUser, String taskId, List<TaskContext> taskContextList, String opinion) throws Exception {

        commitTask(currentUser, taskId, taskContextList, TaskHandleResult.Return, opinion);
    }

    public void rejectTask(User currentUser, String taskId, List<TaskContext> taskContextList, String opinion) throws Exception {

        commitTask(currentUser, taskId, taskContextList, TaskHandleResult.Reject, opinion);
    }

    public void completeTask(User currentUser, String taskId, List<TaskContext> taskContextList, String opinion) throws Exception {

        commitTask(currentUser, taskId, taskContextList, TaskHandleResult.Complete, opinion);
    }

    public void rollbackTask(User currentUser, String taskId) throws Exception {

        Task task = get(Task.class, taskId);
        taskService.rollback(task);
    }

    public void transmitTask(User currentUser, String taskId, String handler) throws Exception {

        Task task = get(Task.class, taskId);
        User user = get(User.class, handler);
        task.setHandler(user);
        task.setSourceType(TaskSourceType.TRANSMIT);
        task.setArriveTime(DateUtil.getCurrentTime());
        task.setDeliver(SpringSecurityUtil.getCurrentUser());
        save(task);

        TaskEventArgs args = new TaskEventArgs();
        args.setTask(task);
        TaskEventPublisher.publishTaskTransmitEvent(this, args);
    }

    public void startWorkflow(AbstractBaseEntity entity) throws Exception {

        List<User> nextHandlerList = null;
        startWorkflow(entity, nextHandlerList);

    }

    public void startWorkflow(AbstractBaseEntity entity, User nextHandler) throws Exception {

        List<User> nextHandlerList = new ArrayList<User>();
        nextHandlerList.add(nextHandler);
        startWorkflow(entity, nextHandlerList);
    }

    public void startWorkflow(AbstractBaseEntity entity, List<User> nextHandlerList) throws Exception {

        String relateObjectId = entity.getObjectId();
        Instance instance = getInstanceByRelatedObjectId(relateObjectId);

        FlowType flowType = flowTypeService.getFlowTypeByEntity(entity);

        // TODO 从第一个节点的后续分支中预判是否要启动工作流
        // TODO 增加不启动工作流时的实体状态字段

        // 判断是否启动新流程
        if (instance == null) {
            instance = instanceService.start(entity, nextHandlerList);
        }

        // 重新解析流程名
        String instanceName = instanceService.parseInstanceName(entity, flowType.getInstanceName());
        instance.setName(instanceName);
        update(instance);

        // 如果是退回修改后的提交,则结束当前任务
        EntityModifyTask objectModifyTask = taskService.getEntityModifyTask(relateObjectId);
        if (objectModifyTask != null) {
            taskService.commit(entity.getCurrentUser(), objectModifyTask.getTask(), null, TaskHandleResult.Submit, "提交", null, nextHandlerList);
        }
    }

    public void onInstanceDeleteEvent(InstanceDeleteEvent event) throws Exception {

        Instance instance = event.getInstanceEventArgs().getInstance();
        AbstractBaseEntity entity = instanceService.getRelatedEntityInstance(instance);
        if (entity != null) {
            logicDelete(entity);
        }
    }

    public void onDecisionCreateEvent(DecisionCreateEvent event) throws Exception {

        String relatedObjectId = event.getDecisionEventArgs().getEntityInstance().getObjectId();
        Instance instance = getInstanceByRelatedObjectId(relatedObjectId);
        if (instance.getCurrentFlowNode() == null || instance.getCurrentFlowNode().getSuspendInstance() == 0) {
            return;
        }

        String simpleEventName = event.getDecisionEventArgs().getApplicationEvent().getSimpleName();
        String hql = "select o from " + FlowNodeRoute.class.getName() + " o"
                + " where o.fromFlowNode.objectId=? and o.ramus.event=?";

        FlowNodeRoute flowNodeRoute = (FlowNodeRoute) getByHql(hql, instance.getCurrentFlowNode().getObjectId(), simpleEventName);
        if (flowNodeRoute == null) {
            throw new BaseBusinessException("", "决策事件[" + simpleEventName + "]没有对应的流程分支!");
        }

        SubInstance subInstance = instance.getSubInstanceList().get(0);
        MasterTask masterTask = subInstance.getMasterTaskList().get(0);

        List<User> nextHandlerList = new ArrayList<User>();
        User user = event.getDecisionEventArgs().getUser();
        nextHandlerList.add(user);
        // todo test event.getDecisionEventArgs().getEntityInstance().getCurrentUser()
        taskService.createTask(event.getDecisionEventArgs().getEntityInstance().getCurrentUser(), subInstance, masterTask, flowNodeRoute.getToFlowNode(), nextHandlerList, null);

        hql = "select o from " + Task.class.getName() + " o where o.masterTask.subInstance.objectId=?"
                + " and o.handler.objectId=? order by o.createTime desc";
        List list = listByHql(hql, subInstance.getObjectId(), user.getObjectId());
        Task task = (Task) list.get(0);

        TaskHandleResult taskHandleResult;
        try {
            taskHandleResult = TaskHandleResult.valueOf(flowNodeRoute.getRamus().getRamusTag());
        } catch (Exception e) {
            throw new BaseBusinessException("", "分支[" + flowNodeRoute.getRamus().getName() + "]没有定义任务处理结果类型.");
        }
        commitTask(event.getDecisionEventArgs().getEntityInstance().getCurrentUser(), task.getObjectId(), null, taskHandleResult, event.getDecisionEventArgs().getOpinion());
    }

    public void onEntityLogicDeleteEvent(EntityLogicDeleteEvent event) throws Exception {

        String hql = "select o from " + Instance.class.getName() + " o where o.objectStatus=1"
                + " and o.relatedObjectId=?";
        Instance instance = (Instance) getByHql(hql, event.getEntityInstance().getObjectId());

        if (instance == null) {
            return;
        }

        instanceService.delete(instance);
    }

    public List<Task> getFlowNodeTask(Instance instance, String flowNodeId) throws Exception {

        SubInstance subInstance = instance.getSubInstanceList().get(0);
        List<Task> taskList = null;
        for (MasterTask masterTask : subInstance.getMasterTaskList()) {
            if (masterTask.getFlowNode().getObjectId().equals(flowNodeId)) {
                taskList = masterTask.getTaskList();
                break;
            }
        }
        return taskList;
    }

    public List<Task> getLastRankTaskListByFlowNode(Instance instance, String flowNodeTag) throws Exception {

        List<Task> taskList = null;
        SubInstance subInstance = instance.getSubInstanceList().get(0);
        for (MasterTask masterTask : subInstance.getMasterTaskList()) {
            if (flowNodeTag.equalsIgnoreCase(masterTask.getFlowNode().getFlowNodeTag())) {
                taskList = masterTask.getTaskList();
                break;
            }
        }

        return taskList;
    }

    public Task getLastRankTaskByFlowNode(String relatedObjectId, String flowNodeTag) throws Exception {

        if (StringUtil.isEmpty(flowNodeTag)) {
            return null;
        }

        Task task = null;
        Instance instance = getInstanceByRelatedObjectId(relatedObjectId);
        List<Task> taskList = getLastRankTaskListByFlowNode(instance, flowNodeTag);
        if (CollectionUtil.isNotEmpty(taskList)) {
            task = taskList.get(0);
        } else {
            String hql = "select o from " + Task.class.getName() + " o"
                    + " where o.masterTask.subInstance.instance.relatedObjectId=?"
                    + " and o.masterTask.flowNode.flowNodeTag=? order by o.createTime desc";
            List list = listByHql(hql, relatedObjectId, flowNodeTag);
            if (CollectionUtil.isNotEmpty(list)) {
                task = (Task) list.get(0);
            }
        }
        return task;
    }

    //================================== 属性方法 =========================================================

    public FlowTypeService getFlowTypeService() {

        return flowTypeService;
    }

    public void setFlowTypeService(FlowTypeService flowTypeService) {

        this.flowTypeService = flowTypeService;
    }

    public InstanceService getInstanceService() {

        return instanceService;
    }

    public void setInstanceService(InstanceService instanceService) {

        this.instanceService = instanceService;
    }

    public TaskService getTaskService() {

        return taskService;
    }

    public void setTaskService(TaskService taskService) {

        this.taskService = taskService;
    }

    public CodeService getCodeService() {

        return codeService;
    }

    public void setCodeService(CodeService codeService) {

        this.codeService = codeService;
    }
}
