package com.withub.service.workflow;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.std.event.agencyevent.AgencyCreatedEvent;
import com.withub.model.std.event.agencyevent.AgencyFinishedEvent;
import com.withub.model.std.event.entityevent.EntityTimeoutEvent;
import com.withub.model.system.po.User;
import com.withub.model.workflow.enumeration.TaskHandleResult;
import com.withub.model.workflow.event.instanceevent.InstanceAbortEvent;
import com.withub.model.workflow.event.instanceevent.InstanceCreateEvent;
import com.withub.model.workflow.event.instanceevent.InstanceDeleteEvent;
import com.withub.model.workflow.po.*;
import com.withub.service.EntityService;

import java.util.List;

public interface TaskService extends EntityService {

    public Task getTaskById(String taskId) throws Exception;

    public void commit(Task task, TaskHandleResult taskHandleResult, String opinion, List<User> nextHandlerList, List<User> firstNodeHandlerList) throws Exception;

    public void commit(User currentUser, Task task, TaskHandleResult taskHandleResult, String opinion, List<User> nextHandlerList, List<User> firstNodeHandlerList) throws Exception;

    public void saveInstanceOrganization(Instance instance, FlowNode flowNode, Task task) throws Exception;

    public void rollback(Task task) throws Exception;

    public EntityModifyTask getEntityModifyTask(String relatedObjectId) throws Exception;

    public void onInstanceCreateEvent(InstanceCreateEvent event) throws Exception;

    public void onInstanceDeleteEvent(InstanceDeleteEvent event) throws Exception;

    public void onInstanceAbortEvent(InstanceAbortEvent event) throws Exception;

    public void createTask(SubInstance subInstance, MasterTask previousMasterTask, FlowNode flowNode, List<User> handlerList, List<User> firstNodeHandlerList) throws Exception;

    public void createTask(User currentUser, SubInstance subInstance, MasterTask previousMasterTask, FlowNode flowNode, List<User> handlerList, List<User> firstNodeHandlerList) throws Exception;

    public void finishMasterTask(MasterTask masterTask) throws Exception;

    public void finishTask(Task task, FlowNode flowNode, TaskHandleResult result, String opinion) throws Exception;

    public List<Task> listRunningTasks(MasterTask masterTask);

    public void abortInstanceRunningTasks(Instance instance) throws Exception;

    public void finishInstance(Instance instance) throws Exception;

    public void setEntityStatusByRamus(AbstractBaseEntity entityInstance, Ramus ramus) throws Exception;

    public void onAgencyCreatedEvent(AgencyCreatedEvent event) throws Exception;

    public void onAgencyFinishedEvent(AgencyFinishedEvent event) throws Exception;

    public void onEntityTimeoutEvent(EntityTimeoutEvent event) throws Exception;

    public void onEntityWillTimeoutEvent(EntityTimeoutEvent event) throws Exception;
}
