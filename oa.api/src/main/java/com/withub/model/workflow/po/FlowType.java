package com.withub.model.workflow.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.std.po.FileTemplate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@javax.persistence.Entity
@Table(name = "WF_FLOWTYPE")
public class FlowType extends AbstractBaseEntity {

    //=================== 属性声明 ============================================

    private String code;

    private String name;

    private String flowTypeTag;

    private String instanceName;

    private String statusProperty;

    @OneToOne(targetEntity = com.withub.model.system.po.Entity.class)
    @JoinColumn(name = "EntityId")
    private com.withub.model.system.po.Entity entity;

    private String subFlowTypeExpression;

    private Integer skipHandler;

    private String entranceMethod;

    private String url;

    private Integer enable;

    @OneToMany(targetEntity = FlowNode.class, mappedBy = "flowType", fetch = FetchType.LAZY)
    private List<FlowNode> flowNodeList = new ArrayList<FlowNode>();

    @OneToOne(targetEntity = FlowChart.class, mappedBy = "flowType", fetch = FetchType.LAZY)
    private FlowChart flowChart;

    private String description;

    @OneToOne(targetEntity = FileTemplate.class)
    @JoinColumn(name = "FileTemplateId")
    private FileTemplate fileTemplate;

    private Integer orderNo;

    //=================== 属性方法 ============================================

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getFlowTypeTag() {

        return flowTypeTag;
    }

    public void setFlowTypeTag(String flowTypeTag) {

        this.flowTypeTag = flowTypeTag;
    }

    public String getInstanceName() {

        return instanceName;
    }

    public void setInstanceName(String instanceName) {

        this.instanceName = instanceName;
    }

    public String getStatusProperty() {

        return statusProperty;
    }

    public void setStatusProperty(String statusProperty) {

        this.statusProperty = statusProperty;
    }

    public com.withub.model.system.po.Entity getEntity() {

        return entity;
    }

    public void setEntity(com.withub.model.system.po.Entity entity) {

        this.entity = entity;
    }

    public String getSubFlowTypeExpression() {

        return subFlowTypeExpression;
    }

    public void setSubFlowTypeExpression(String subFlowTypeExpression) {

        this.subFlowTypeExpression = subFlowTypeExpression;
    }

    public Integer getSkipHandler() {

        return skipHandler;
    }

    public void setSkipHandler(Integer skipHandler) {

        this.skipHandler = skipHandler;
    }

    public String getEntranceMethod() {

        return entranceMethod;
    }

    public void setEntranceMethod(String entranceMethod) {

        this.entranceMethod = entranceMethod;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public Integer getEnable() {

        return enable;
    }

    public void setEnable(Integer enable) {

        this.enable = enable;
    }

    public List<FlowNode> getFlowNodeList() {

        return flowNodeList;
    }

    public void setFlowNodeList(List<FlowNode> flowNodeList) {

        this.flowNodeList = flowNodeList;
    }

    public FlowChart getFlowChart() {

        return flowChart;
    }

    public void setFlowChart(FlowChart flowChart) {

        this.flowChart = flowChart;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public FileTemplate getFileTemplate() {

        return fileTemplate;
    }

    public void setFileTemplate(FileTemplate fileTemplate) {

        this.fileTemplate = fileTemplate;
    }

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }
}
