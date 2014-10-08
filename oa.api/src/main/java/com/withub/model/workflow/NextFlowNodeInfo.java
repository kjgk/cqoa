package com.withub.model.workflow;

import com.withub.model.system.po.User;
import com.withub.model.workflow.po.FlowNode;

public final class NextFlowNodeInfo {

    //============================= 属性声明 ===========================================

    private FlowNode currentFlowNode;

    private FlowNode nextFlowNode;

    private User handler;

    //============================= 属性方法 ===========================================

    public FlowNode getCurrentFlowNode() {

        return currentFlowNode;
    }

    public void setCurrentFlowNode(FlowNode currentFlowNode) {

        this.currentFlowNode = currentFlowNode;
    }

    public FlowNode getNextFlowNode() {

        return nextFlowNode;
    }

    public void setNextFlowNode(FlowNode nextFlowNode) {

        this.nextFlowNode = nextFlowNode;
    }

    public User getHandler() {

        return handler;
    }

    public void setHandler(User handler) {

        this.handler = handler;
    }
}
