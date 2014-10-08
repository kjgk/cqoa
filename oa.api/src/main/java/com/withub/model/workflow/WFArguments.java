package com.withub.model.workflow;


import com.withub.model.workflow.po.FlowNode;
import com.withub.model.workflow.po.Instance;

public final class WFArguments {

    private Instance instance;

    private FlowNode flowNode;

    public Instance getInstance() {

        return instance;
    }

    public void setInstance(Instance instance) {

        this.instance = instance;
    }

    public FlowNode getFlowNode() {

        return flowNode;
    }

    public void setFlowNode(FlowNode flowNode) {

        this.flowNode = flowNode;
    }
}
