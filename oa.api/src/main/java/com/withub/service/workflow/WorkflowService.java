package com.withub.service.workflow;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.entity.event.EntityLogicDeleteEvent;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.system.po.User;
import com.withub.model.workflow.NextFlowNodeInfo;
import com.withub.model.workflow.enumeration.TaskHandleResult;
import com.withub.model.workflow.event.DecisionCreateEvent;
import com.withub.model.workflow.event.instanceevent.InstanceDeleteEvent;
import com.withub.model.workflow.po.FlowNode;
import com.withub.model.workflow.po.Instance;
import com.withub.model.workflow.po.Task;
import com.withub.model.workflow.vo.TaskFlowNodeInfo;
import com.withub.model.workflow.vo.TaskInfo;
import com.withub.service.EntityService;

import java.util.List;

public interface WorkflowService extends EntityService {

    public RecordsetInfo queryCurrentUserTask(QueryInfo queryInfo) throws Exception;

    public RecordsetInfo queryCurrentUserInstance(QueryInfo queryInfo) throws Exception;

    public RecordsetInfo queryInstance(QueryInfo queryInfo) throws Exception;

    public void startWorkflow(AbstractBaseEntity entity) throws Exception;

    public void startWorkflow(AbstractBaseEntity entity, User nextHandler) throws Exception;

    public void startWorkflow(AbstractBaseEntity entity, List<User> nextHandlerList) throws Exception;

    public void deleteInstance(String instanceId) throws Exception;

    public void abortInstance(String instanceId);

    public Instance getInstanceByTaskId(String taskId) throws Exception;

    public AbstractBaseEntity getRelatedEntityInstanceByTaskId(String taskId) throws Exception;

    public Instance getInstanceByRelatedObjectId(String relatedObjectId) throws Exception;

    public void onInstanceDeleteEvent(InstanceDeleteEvent event) throws Exception;

    public FlowNode getFlowNodeByTag(String flowNodeTag) throws Exception;

    public FlowNode getFlowNodeByTaskId(String taskId) throws Exception;

    public FlowNode getNextApproveFlowNode(String taskId) throws Exception;

    public NextFlowNodeInfo getNextFlowNodeInfo(String taskId) throws Exception;

    public NextFlowNodeInfo getNextFlowNodeInfo(Class<? extends AbstractBaseEntity> clazz, String flowTypeTag) throws Exception;

    public AbstractBaseEntity getInstanceRelatedEntity(Instance instance) throws Exception;

    public List<User> listInstanceCurrentHandler(String instanceId) throws Exception;

    public List<User> listEntityInstanceCurrentHandler(String entityInstanceId) throws Exception;

    public Task getTaskById(String taskId) throws Exception;

    public TaskInfo getCurrentUserApproveTaskByRelatedObjectId(String relatedObjectId) throws Exception;

    public TaskFlowNodeInfo getTaskFlowNode(String taskId) throws Exception;

    public void commitTask(User currentUser, String taskId, TaskHandleResult result, String opinion) throws Exception;

    public void passTask(User currentUser, String taskId, String opinion) throws Exception;

    public void passTask(User currentUser, String taskId, String opinion, User nextHandler) throws Exception;

    public void passTask(User currentUser, String taskId, String opinion, List<User> nextHandlerList) throws Exception;

    public void addTaskContext(String taskId, String contextKey, String contextValue) throws Exception;

    public void returnTask(User currentUser, String taskId, String opinion) throws Exception;

    public void rejectTask(User currentUser, String taskId, String opinion) throws Exception;

    public void completeTask(User currentUser, String taskId, String opinion) throws Exception;

    public void rollbackTask(User currentUser, String taskId) throws Exception;

    public void transmitTask(User currentUser, String taskId, String handler) throws Exception;

    public List<Task> getFlowNodeTask(Instance instance, String flowNodeId) throws Exception;

    public void onDecisionCreateEvent(DecisionCreateEvent event) throws Exception;

    public void onEntityLogicDeleteEvent(EntityLogicDeleteEvent event) throws Exception;

    public List<Task> getLastRankTaskListByFlowNode(Instance instance, String flowNodeTag) throws Exception;

    public Task getLastRankTaskByFlowNode(String relatedObjectId, String flowNodeTag) throws Exception;
}
