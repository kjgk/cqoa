package com.withub.model.workflow.event.instanceevent;

import com.withub.model.system.po.User;
import com.withub.model.workflow.po.FlowNode;
import com.withub.model.workflow.po.Instance;
import com.withub.model.workflow.po.SubInstance;

import java.util.List;

public class InstanceEventArgs {

    private Instance instance;

    private SubInstance subInstance;

    private FlowNode flowNode;

    private List<User> firstNodeHandlerList;

    public Instance getInstance() {

        return instance;
    }

    public void setInstance(Instance instance) {

        this.instance = instance;
    }

    public SubInstance getSubInstance() {

        return subInstance;
    }

    public void setSubInstance(SubInstance subInstance) {

        this.subInstance = subInstance;
    }

    public FlowNode getFlowNode() {

        return flowNode;
    }

    public void setFlowNode(FlowNode flowNode) {

        this.flowNode = flowNode;
    }

    public List<User> getFirstNodeHandlerList() {

        return firstNodeHandlerList;
    }

    public void setFirstNodeHandlerList(List<User> firstNodeHandlerList) {

        this.firstNodeHandlerList = firstNodeHandlerList;
    }
}
