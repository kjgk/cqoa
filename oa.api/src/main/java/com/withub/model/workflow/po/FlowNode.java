package com.withub.model.workflow.po;


import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Code;
import com.withub.model.workflow.enumeration.FlowNodeTaskMode;
import com.withub.model.workflow.enumeration.FlowNodeType;
import com.withub.model.workflow.enumeration.HandlerFetchType;
import com.withub.model.workflow.enumeration.TaskExecuteMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "WF_FLOWNODE")
public class FlowNode extends AbstractBaseEntity {

    //=============================== 属性声明 =======================================================

    private String code;

    private String name;

    @ManyToOne(targetEntity = FlowType.class)
    @JoinColumn(name = "flowTypeId")
    private FlowType flowType;

    private String flowNodeTag;

    private Integer passAction;

    private Integer returnAction;

    private Integer rejectAction;

    private Integer discardAction;

    private Integer completeAction;

    @OneToMany(targetEntity = FlowNodeRoute.class, mappedBy = "fromFlowNode", fetch = FetchType.LAZY)
    private List<FlowNodeRoute> flowNodeRouteList = new ArrayList<FlowNodeRoute>();

    @Enumerated(EnumType.STRING)
    private FlowNodeType flowNodeType;

    private Integer manualSelectHandler;

    private String handlerServiceMethod;

    private String handlerOnFlowNode;

    private String userPropertyOnEntity;

    private String organizationId;

    private String roleId;

    private Integer useRootNode;

    private String organizationProperty;

    private String roleProperty;

    private Integer handlerFetchCount = 0;

    @Enumerated(EnumType.STRING)
    private HandlerFetchType handlerFetchType;

    @Enumerated(EnumType.STRING)
    private TaskExecuteMode taskExecuteMode;

    @Enumerated(EnumType.STRING)
    private FlowNodeTaskMode flowNodeTaskMode;

    private String entityStatusTag;

    private Integer instanceReturnMode;

    private Integer skipHandler;

    private Integer allowAgent;

    private Integer allowTransmit;

    private Integer suspendInstance;

    private String suspendDescription;

    private Integer expiration;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "ExpirationTimeUnitId")
    private Code expirationTimeUnit;

    private String timeoutRegulationId;

    private String voteRegulationId;

    private String activity;

    private Integer email;

    private Integer sms;

    private Integer notifyInstanceCreator;

    private String notifyContent;

    //=============================== 属性方法 =======================================================

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

    public FlowType getFlowType() {

        return flowType;
    }

    public void setFlowType(FlowType flowType) {

        this.flowType = flowType;
    }

    public String getFlowNodeTag() {

        return flowNodeTag;
    }

    public void setFlowNodeTag(String flowNodeTag) {

        this.flowNodeTag = flowNodeTag;
    }

    public Integer getPassAction() {

        return passAction;
    }

    public void setPassAction(Integer passAction) {

        this.passAction = passAction;
    }

    public Integer getReturnAction() {

        return returnAction;
    }

    public void setReturnAction(Integer returnAction) {

        this.returnAction = returnAction;
    }

    public Integer getRejectAction() {

        return rejectAction;
    }

    public void setRejectAction(Integer rejectAction) {

        this.rejectAction = rejectAction;
    }

    public Integer getDiscardAction() {

        return discardAction;
    }

    public void setDiscardAction(Integer discardAction) {

        this.discardAction = discardAction;
    }

    public Integer getCompleteAction() {

        return completeAction;
    }

    public void setCompleteAction(Integer completeAction) {

        this.completeAction = completeAction;
    }

    public List<FlowNodeRoute> getFlowNodeRouteList() {

        return flowNodeRouteList;
    }

    public void setFlowNodeRouteList(List<FlowNodeRoute> flowNodeRouteList) {

        this.flowNodeRouteList = flowNodeRouteList;
    }

    public FlowNodeType getFlowNodeType() {

        return flowNodeType;
    }

    public void setFlowNodeType(FlowNodeType flowNodeType) {

        this.flowNodeType = flowNodeType;
    }

    public Integer getManualSelectHandler() {

        return manualSelectHandler;
    }

    public void setManualSelectHandler(Integer manualSelectHandler) {

        this.manualSelectHandler = manualSelectHandler;
    }

    public String getHandlerServiceMethod() {

        return handlerServiceMethod;
    }

    public void setHandlerServiceMethod(String handlerServiceMethod) {

        this.handlerServiceMethod = handlerServiceMethod;
    }

    public String getHandlerOnFlowNode() {

        return handlerOnFlowNode;
    }

    public void setHandlerOnFlowNode(String handlerOnFlowNode) {

        this.handlerOnFlowNode = handlerOnFlowNode;
    }

    public String getUserPropertyOnEntity() {

        return userPropertyOnEntity;
    }

    public void setUserPropertyOnEntity(String userPropertyOnEntity) {

        this.userPropertyOnEntity = userPropertyOnEntity;
    }

    public String getOrganizationId() {

        return organizationId;
    }

    public void setOrganizationId(String organizationId) {

        this.organizationId = organizationId;
    }

    public String getRoleId() {

        return roleId;
    }

    public void setRoleId(String roleId) {

        this.roleId = roleId;
    }

    public Integer getUseRootNode() {

        return useRootNode;
    }

    public void setUseRootNode(Integer useRootNode) {

        this.useRootNode = useRootNode;
    }

    public String getOrganizationProperty() {

        return organizationProperty;
    }

    public void setOrganizationProperty(String organizationProperty) {

        this.organizationProperty = organizationProperty;
    }

    public String getRoleProperty() {

        return roleProperty;
    }

    public void setRoleProperty(String roleProperty) {

        this.roleProperty = roleProperty;
    }

    public Integer getHandlerFetchCount() {

        return handlerFetchCount;
    }

    public void setHandlerFetchCount(Integer handlerFetchCount) {

        this.handlerFetchCount = handlerFetchCount;
    }

    public HandlerFetchType getHandlerFetchType() {

        return handlerFetchType;
    }

    public void setHandlerFetchType(HandlerFetchType handlerFetchType) {

        this.handlerFetchType = handlerFetchType;
    }

    public TaskExecuteMode getTaskExecuteMode() {

        return taskExecuteMode;
    }

    public void setTaskExecuteMode(TaskExecuteMode taskExecuteMode) {

        this.taskExecuteMode = taskExecuteMode;
    }

    public FlowNodeTaskMode getFlowNodeTaskMode() {

        return flowNodeTaskMode;
    }

    public void setFlowNodeTaskMode(FlowNodeTaskMode flowNodeTaskMode) {

        this.flowNodeTaskMode = flowNodeTaskMode;
    }

    public String getEntityStatusTag() {

        return entityStatusTag;
    }

    public void setEntityStatusTag(String entityStatusTag) {

        this.entityStatusTag = entityStatusTag;
    }

    public Integer getInstanceReturnMode() {

        return instanceReturnMode;
    }

    public void setInstanceReturnMode(Integer instanceReturnMode) {

        this.instanceReturnMode = instanceReturnMode;
    }

    public Integer getSkipHandler() {

        return skipHandler;
    }

    public void setSkipHandler(Integer skipHandler) {

        this.skipHandler = skipHandler;
    }

    public Integer getAllowAgent() {

        return allowAgent;
    }

    public void setAllowAgent(Integer allowAgent) {

        this.allowAgent = allowAgent;
    }

    public Integer getAllowTransmit() {

        return allowTransmit;
    }

    public void setAllowTransmit(Integer allowTransmit) {

        this.allowTransmit = allowTransmit;
    }

    public Integer getSuspendInstance() {

        return suspendInstance;
    }

    public void setSuspendInstance(Integer suspendInstance) {

        this.suspendInstance = suspendInstance;
    }

    public String getSuspendDescription() {

        return suspendDescription;
    }

    public void setSuspendDescription(String suspendDescription) {

        this.suspendDescription = suspendDescription;
    }

    public Integer getExpiration() {

        return expiration;
    }

    public void setExpiration(Integer expiration) {

        this.expiration = expiration;
    }

    public Code getExpirationTimeUnit() {

        return expirationTimeUnit;
    }

    public void setExpirationTimeUnit(Code expirationTimeUnit) {

        this.expirationTimeUnit = expirationTimeUnit;
    }

    public String getTimeoutRegulationId() {

        return timeoutRegulationId;
    }

    public void setTimeoutRegulationId(String timeoutRegulationId) {

        this.timeoutRegulationId = timeoutRegulationId;
    }

    public String getVoteRegulationId() {

        return voteRegulationId;
    }

    public void setVoteRegulationId(String voteRegulationId) {

        this.voteRegulationId = voteRegulationId;
    }

    public String getActivity() {

        return activity;
    }

    public void setActivity(String activity) {

        this.activity = activity;
    }

    public Integer getEmail() {

        return email;
    }

    public void setEmail(Integer email) {

        this.email = email;
    }

    public Integer getSms() {

        return sms;
    }

    public void setSms(Integer sms) {

        this.sms = sms;
    }

    public Integer getNotifyInstanceCreator() {

        return notifyInstanceCreator;
    }

    public void setNotifyInstanceCreator(Integer notifyInstanceCreator) {

        this.notifyInstanceCreator = notifyInstanceCreator;
    }

    public String getNotifyContent() {

        return notifyContent;
    }

    public void setNotifyContent(String notifyContent) {

        this.notifyContent = notifyContent;
    }
}