package com.withub.model.workflow.po;

import com.withub.model.entity.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name = "WF_FLOWNODEROUTE")
public class FlowNodeRoute extends AbstractEntity {

    @ManyToOne(targetEntity = FlowType.class)
    @JoinColumn(name = "flowTypeId")
    private FlowType flowType;

    private String fromFlowNodeId;

    @ManyToOne(targetEntity = FlowNode.class)
    @JoinColumn(name = "fromFlowNodeId", insertable = false, updatable = false)
    private FlowNode fromFlowNode;

    private String ramusId;

    @OneToOne(targetEntity = Ramus.class)
    @JoinColumn(name = "ramusId", insertable = false, updatable = false)
    private Ramus ramus;

    private String toFlowNodeId;

    @OneToOne(targetEntity = FlowNode.class)
    @JoinColumn(name = "toFlowNodeId", insertable = false, updatable = false)
    private FlowNode toFlowNode;

    //=================== 属性方法 ============================================

    public String getFromFlowNodeId() {

        return fromFlowNodeId;
    }

    public void setFromFlowNodeId(String fromFlowNodeId) {

        this.fromFlowNodeId = fromFlowNodeId;
    }

    public FlowNode getFromFlowNode() {

        return fromFlowNode;
    }

    public void setFromFlowNode(FlowNode fromFlowNode) {

        this.fromFlowNode = fromFlowNode;
    }

    public String getRamusId() {

        return ramusId;
    }

    public void setRamusId(String ramusId) {

        this.ramusId = ramusId;
    }

    public Ramus getRamus() {

        return ramus;
    }

    public void setRamus(Ramus ramus) {

        this.ramus = ramus;
    }

    public String getToFlowNodeId() {

        return toFlowNodeId;
    }

    public void setToFlowNodeId(String toFlowNodeId) {

        this.toFlowNodeId = toFlowNodeId;
    }

    public FlowNode getToFlowNode() {

        return toFlowNode;
    }

    public void setToFlowNode(FlowNode toFlowNode) {

        this.toFlowNode = toFlowNode;
    }

    public FlowType getFlowType() {

        return flowType;
    }

    public void setFlowType(FlowType flowType) {

        this.flowType = flowType;
    }
}