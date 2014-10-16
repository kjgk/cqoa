package com.withub.service.impl.workflow;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.User;
import com.withub.model.workflow.enumeration.InstanceStatus;
import com.withub.model.workflow.enumeration.TaskStatus;
import com.withub.model.workflow.event.instanceevent.InstanceEventArgs;
import com.withub.model.workflow.event.instanceevent.InstanceEventPublisher;
import com.withub.model.workflow.po.*;
import com.withub.service.EntityServiceImpl;
import com.withub.service.system.CodeService;
import com.withub.service.workflow.FlowTypeService;
import com.withub.service.workflow.InstanceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("instanceService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class InstanceServiceImpl extends EntityServiceImpl implements InstanceService {

    //================================ 属性声明 ===========================================================

    @Autowired
    private FlowTypeService flowTypeService;

    @Autowired
    private CodeService codeService;

    //================================ 接口实现 ===========================================================

    public Instance start(AbstractBaseEntity entity) throws Exception {

        return start(entity, null);
    }

    public Instance start(AbstractBaseEntity entity, List<User> nextHandlerList) throws Exception {

        FlowType flowType = flowTypeService.getFlowTypeByEntity(entity);

        // 主流程
        Instance instance = new Instance();
        String instanceId = StringUtil.getUuid();
        instance.setObjectId(instanceId);
        instance.setFlowType(flowType);
        String relatedObjectId = entity.getObjectId();
        instance.setRelatedObjectId(relatedObjectId);
        String instanceName = parseInstanceName(entity, flowType.getInstanceName());
        instance.setName(instanceName);
        Code status = codeService.getCodeByEnum(InstanceStatus.Running);
        instance.setStatus(status);

        FlowNode beginFlowNode = flowTypeService.getBeginFlowNode(flowType);
        instance.setCurrentFlowNode(beginFlowNode);
        instance.setOrganization(entity.getCurrentUser().getOrganization());
        instance.setCurrentUser(entity.getCurrentUser());
        save(instance);

        // 子流程
        SubInstance subInstance = new SubInstance();

        String subInstanceId = StringUtil.getUuid();
        subInstance.setObjectId(subInstanceId);
        subInstance.setInstance(instance);
        subInstance.setRank(1);
        subInstance.setCreator(instance.getCreator());
        subInstance.setStatus(status);
        subInstance.setCreateTime(DateUtil.getCurrentTime());
        save(subInstance);

        // 发布流程创建事件
        InstanceEventArgs instanceEventArgs = new InstanceEventArgs();
        instanceEventArgs.setInstance(instance);
        instanceEventArgs.setSubInstance(subInstance);
        instanceEventArgs.setFirstNodeHandlerList(nextHandlerList);
        InstanceEventPublisher.publishInstanceCreateEvent(this, instanceEventArgs);

        return instance;
    }

    public void delete(Instance instance) throws Exception {

        logicDelete(instance);

        InstanceEventArgs instanceEventArgs = new InstanceEventArgs();
        instanceEventArgs.setInstance(instance);
        InstanceEventPublisher.publishInstanceDeleteEvent(this, instanceEventArgs);
    }

    public void abort(Instance instance) throws Exception {

        instance = (Instance) get(Instance.class, instance.getObjectId());
        Code instanceAbortStatus = codeService.getCodeByEnum(InstanceStatus.Abort);
        instance.setStatus(instanceAbortStatus);
        save(instance);

        InstanceEventArgs instanceEventArgs = new InstanceEventArgs();
        instanceEventArgs.setInstance(instance);
        InstanceEventPublisher.publishInstanceAbortEvent(this, instanceEventArgs);
    }

    public AbstractBaseEntity getRelatedEntityInstance(Instance instance) throws Exception {

        String className = instance.getFlowType().getEntity().getClassName();
        AbstractBaseEntity entity = (AbstractBaseEntity) getEntityByClassName(className, instance.getRelatedObjectId());
        return entity;
    }

    public List<Task> listRunningTasks(Instance instance) {

        List<Task> runningTaskList = new ArrayList<Task>();

        String instanceStatusTag = instance.getStatus().getCodeTag();
        if (!instanceStatusTag.equalsIgnoreCase(InstanceStatus.Running.toString())) {
            return null;
        }

        SubInstance subInstance = instance.getSubInstanceList().get(0);
        List<MasterTask> masterTaskList = subInstance.getMasterTaskList();
        for (MasterTask masterTask : masterTaskList) {
            if (masterTask.getStatus().getCodeTag().equalsIgnoreCase(TaskStatus.Running.toString())) {
                List<Task> taskList = masterTask.getTaskList();
                for (Task task : taskList) {
                    if (task.getStatus().getCodeTag().equalsIgnoreCase(TaskStatus.Running.toString())) {
                        runningTaskList.add(task);
                    }
                }
            }
        }
        return runningTaskList;
    }

    public String parseInstanceName(AbstractBaseEntity entity, String template) throws Exception {

        String text = template;

        String[] phraseArray = StringUtils.substringsBetween(template, "{#", "#}");

        if (CollectionUtil.isNotEmpty(phraseArray)) {
            for (String pharse : phraseArray) {
                String value = getPropertyValue(entity, pharse).toString();
                text = text.replace("{#" + pharse + "#}", value);
            }
        }

        return text;
    }

    //================================== 属性方法 =========================================================

    public FlowTypeService getFlowTypeService() {

        return flowTypeService;
    }

    public void setFlowTypeService(FlowTypeService flowTypeService) {

        this.flowTypeService = flowTypeService;
    }

    public CodeService getCodeService() {

        return codeService;
    }

    public void setCodeService(CodeService codeService) {

        this.codeService = codeService;
    }
}
