package com.withub.model.workflow.po;

import com.withub.model.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "WF_FLOWCHART")
public class FlowChart extends AbstractEntity {


    @OneToOne(targetEntity = FlowType.class)
    @JoinColumn(name = "FlowTypeId")
    private FlowType flowType;

    private String configInfo;

    //=================== 属性方法 ============================================

    public FlowType getFlowType() {

        return flowType;
    }

    public void setFlowType(FlowType flowType) {

        this.flowType = flowType;
    }

    public String getConfigInfo() {

        return configInfo;
    }

    public void setConfigInfo(String configInfo) {

        this.configInfo = configInfo;
    }
}