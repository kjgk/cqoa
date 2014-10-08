package com.withub.service.workflow;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.User;
import com.withub.model.workflow.po.Instance;
import com.withub.model.workflow.po.Task;
import com.withub.service.EntityService;

import java.util.List;

public interface InstanceService extends EntityService {

    public Instance start(AbstractBaseEntity entity) throws Exception;

    public Instance start(AbstractBaseEntity entity, List<User> nextHandlerList) throws Exception;

    public void delete(Instance instance) throws Exception;

    public void abort(Instance instance) throws Exception;

    public AbstractBaseEntity getRelatedEntityInstance(Instance instance) throws Exception;

    public List<Task> listRunningTasks(Instance instance);

    public String parseInstanceName(AbstractBaseEntity entity, String template) throws Exception;
}
