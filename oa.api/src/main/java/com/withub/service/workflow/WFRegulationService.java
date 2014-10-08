package com.withub.service.workflow;

import com.withub.model.system.po.User;
import com.withub.model.workflow.enumeration.TaskHandleResult;
import com.withub.model.workflow.po.FlowNode;
import com.withub.model.workflow.po.FlowNodeRoute;
import com.withub.model.workflow.po.Instance;
import com.withub.service.EntityService;

import java.util.List;

public interface WFRegulationService extends EntityService {

    public List<User> parseTaskHandler(Instance instance, FlowNode flowNode) throws Exception;

    public FlowNodeRoute parseFlowNodeRoute(Instance instance, FlowNode flowNode, TaskHandleResult taskHandleResult) throws Exception;
}
