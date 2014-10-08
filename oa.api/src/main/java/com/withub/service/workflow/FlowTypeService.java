package com.withub.service.workflow;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.workflow.po.FlowNode;
import com.withub.model.workflow.po.FlowType;
import com.withub.service.EntityService;

public interface FlowTypeService extends EntityService {

    public void saveWorkflowConfig(String flowTypeId, String xml) throws Exception;

    public FlowType getFlowTypeByEntity(AbstractBaseEntity entity) throws Exception;

    public FlowNode getBeginFlowNode(FlowType flowType);

    public FlowNode getEndFlowNode(FlowType flowType);

    public FlowNode getFirstFlowNode(FlowType flowType);

}
