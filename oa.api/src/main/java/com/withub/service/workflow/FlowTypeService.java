package com.withub.service.workflow;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.User;
import com.withub.model.workflow.po.FlowNode;
import com.withub.model.workflow.po.FlowType;
import com.withub.service.EntityService;

import java.util.Map;

public interface FlowTypeService extends EntityService {

    public void saveWorkflowConfig(final String flowTypeId, Map data, User user) throws Exception;

    public FlowType getFlowTypeByEntity(AbstractBaseEntity entity) throws Exception;

    public FlowNode getBeginFlowNode(FlowType flowType);

    public FlowNode getEndFlowNode(FlowType flowType);

    public FlowNode getFirstFlowNode(FlowType flowType);

}
