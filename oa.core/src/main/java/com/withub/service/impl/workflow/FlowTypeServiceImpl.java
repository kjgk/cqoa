package com.withub.service.impl.workflow;

import com.withub.common.enumeration.TimeUnit;
import com.withub.common.util.CollectionUtil;
import com.withub.common.util.Dom4jUtil;
import com.withub.common.util.EnumUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.exception.BaseBusinessException;
import com.withub.model.system.po.Code;
import com.withub.model.workflow.enumeration.FlowNodeTaskMode;
import com.withub.model.workflow.enumeration.FlowNodeType;
import com.withub.model.workflow.enumeration.HandlerFetchType;
import com.withub.model.workflow.enumeration.TaskExecuteMode;
import com.withub.model.workflow.po.*;
import com.withub.service.EntityServiceImpl;
import com.withub.service.system.CodeService;
import com.withub.service.workflow.FlowTypeService;
import com.withub.service.workflow.WFRegulationService;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("flowTypeService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class FlowTypeServiceImpl extends EntityServiceImpl implements FlowTypeService {

    //================================ 属性声明 ===========================================================

    @Autowired
    private WFRegulationService wfRegulationService;

    @Autowired
    private CodeService codeService;

    //=============================== 接口实现 ============================================================

    public void saveWorkflowConfig(String flowTypeId, String xml) throws Exception {

        // 删除流程相关配置信息
        String sql = "delete from WF_FlowChart where flowTypeId=?";
        executeSql(sql, flowTypeId);
        sql = "delete from WF_FlowNodeRoute where flowTypeId=?";
        executeSql(sql, flowTypeId);
        sql = "delete from WF_RamusRegulation where RamusId in (select ObjectId from WF_Ramus where flowTypeId=?)";
        executeSql(sql, flowTypeId);
        sql = "delete from WF_Ramus where flowTypeId=?";
        executeSql(sql, flowTypeId);

        // 保存流程图
        FlowChart flowChart = new FlowChart();
        flowChart.setObjectId(StringUtil.getUuid());
        FlowType flowType = (FlowType) get(FlowType.class, flowTypeId);
        flowChart.setFlowType(flowType);
        flowChart.setConfigInfo(xml);
        save(flowChart);

        // 解析 XML 配置信息
        Document xmlDoc = DocumentHelper.parseText(xml);
        Element rootElement = xmlDoc.getRootElement();

        /*
        * 保存流程节点
        * 为了正常显示历史任务,流程图上删除的流节点必须保存下来.
         */

        Code hourTimeUnit = codeService.getCodeByEnum(TimeUnit.Hour);

        List<Element> procList = rootElement.selectNodes("//Flow/Procs//Proc");
        for (Element element : procList) {
            List<Element> nodeList = element.selectNodes("BaseProperties");
            for (Element elementObject : nodeList) {
                FlowNode flowNode = new FlowNode();
                String flowNodeId = Dom4jUtil.getAttributeStringValue(elementObject, "id");
                flowNode.setObjectId(flowNodeId);
                flowNode.setFlowType(flowType);
                flowNode.setCode(Dom4jUtil.getAttributeStringValue(elementObject, "code"));
                flowNode.setName(Dom4jUtil.getAttributeStringValue(elementObject, "text"));
                FlowNodeType flowNodeType = (FlowNodeType) EnumUtil.getEnumByFieldName(FlowNodeType.class, Dom4jUtil.getAttributeStringValue(elementObject, "ProcType"));
                flowNode.setFlowNodeType(flowNodeType);
                flowNode.setFlowNodeTag(Dom4jUtil.getAttributeStringValue(elementObject, "FlowNodeTag"));
                if (flowNodeType == FlowNodeType.Begin || flowNodeType == FlowNodeType.End) {
                    flowNode.setTaskExecuteMode(TaskExecuteMode.Auto);
                } else {
                    flowNode.setTaskExecuteMode(TaskExecuteMode.Manual);
                }
                flowNode.setInstanceReturnMode(Dom4jUtil.getAttributeIntegerValue(elementObject, "InstanceReturnMode", 0));

                flowNode.setExpiration(Dom4jUtil.getAttributeIntegerValue(elementObject, "TimeLimit", 0));
                flowNode.setExpirationTimeUnit(hourTimeUnit);

                flowNode.setFlowNodeTaskMode(FlowNodeTaskMode.Parallelism);
                flowNode.setActivity(Dom4jUtil.getAttributeStringValue(elementObject, "Activity"));

                String flowNodeAction = Dom4jUtil.getAttributeStringValue(elementObject, "FlowNodeAction");
                flowNode.setPassAction(flowNodeAction.contains(",passAction,") ? 1 : 0);
                flowNode.setReturnAction(flowNodeAction.contains(",returnAction,") ? 1 : 0);
                flowNode.setRejectAction(flowNodeAction.contains(",rejectAction,") ? 1 : 0);
                flowNode.setDiscardAction(flowNodeAction.contains(",discardAction,") ? 1 : 0);
                flowNode.setCompleteAction(flowNodeAction.contains(",completeAction,") ? 1 : 0);

                // TODO 完善流程节点上实现取人规则
                flowNode.setHandlerServiceMethod(Dom4jUtil.getAttributeStringValue(elementObject, "Executer"));
                flowNode.setHandlerOnFlowNode(Dom4jUtil.getAttributeStringValue(elementObject, "HandlerOnFlowNode"));
                flowNode.setUserPropertyOnEntity(Dom4jUtil.getAttributeStringValue(elementObject, "UserPropertyOnEntity"));
                flowNode.setOrganizationId(Dom4jUtil.getAttributeStringValue(elementObject, "OrganizationId"));
                flowNode.setRoleId(Dom4jUtil.getAttributeStringValue(elementObject, "RoleId"));
                flowNode.setUseRootNode(Dom4jUtil.getAttributeIntegerValue(elementObject, "UseRootNode", 0));
                flowNode.setOrganizationProperty(Dom4jUtil.getAttributeStringValue(elementObject, "OrganizationProperty"));
                flowNode.setRoleProperty(Dom4jUtil.getAttributeStringValue(elementObject, "RoleProperty"));
                flowNode.setHandlerFetchCount(Dom4jUtil.getAttributeIntegerValue(elementObject, "HandlerFetchCount", 0));

                // 当有多个任务处理人时的取人方式
                String handlerFetchType = Dom4jUtil.getAttributeStringValue(elementObject, "HandlerFetchType");
                if (handlerFetchType.contains("Random")) {
                    flowNode.setHandlerFetchType(HandlerFetchType.Random);
                } else if (handlerFetchType.contains("IdleMost")) {
                    flowNode.setHandlerFetchType(HandlerFetchType.IdleMost);
                } else if (handlerFetchType.contains("TaskLeast")) {
                    flowNode.setHandlerFetchType(HandlerFetchType.TaskLeast);
                } else {
                    flowNode.setHandlerFetchType(null);
                }


                flowNode.setManualSelectHandler(Dom4jUtil.getAttributeIntegerValue(elementObject, "SelectExit", 0));
                flowNode.setSkipHandler(Dom4jUtil.getAttributeIntegerValue(elementObject, "SkipHandler", 0));
                int allowAgent = Dom4jUtil.getAttributeIntegerValue(elementObject, "AllowAgent", 0);
                flowNode.setAllowAgent(allowAgent);
                flowNode.setAllowTransmit(allowAgent);
                flowNode.setSuspendInstance(Dom4jUtil.getAttributeIntegerValue(elementObject, "SuspendInstance", 0));
                flowNode.setSuspendDescription(Dom4jUtil.getAttributeStringValue(elementObject, "SuspendDescription"));
                flowNode.setEntityStatusTag(Dom4jUtil.getAttributeStringValue(elementObject, "EntityStatusTag"));

                // 提醒方式
                String notify = Dom4jUtil.getAttributeStringValue(elementObject, "WarnType");
                flowNode.setEmail(notify.contains("email") ? 1 : 0);
                flowNode.setSms(notify.contains("sms") ? 1 : 0);
                flowNode.setNotifyInstanceCreator(Dom4jUtil.getAttributeIntegerValue(elementObject, "NotifyInstanceCreator", 0));
                flowNode.setNotifyContent(Dom4jUtil.getAttributeStringValue(elementObject, "NotifyContent"));

                save(flowNode);
            }
        }

        // 保存分支及路径图
        List<Element> stepList = rootElement.selectNodes("//Flow/Steps//Step");
        for (Element element : stepList) {
            List<Element> ramusList = element.selectNodes("BaseProperties");
            for (Element elementObject : ramusList) {
                Ramus ramus = new Ramus();
                String ramusId = Dom4jUtil.getAttributeStringValue(elementObject, "id");
                ramus.setObjectId(ramusId);
                ramus.setFlowTypeId(flowTypeId);
                ramus.setRamusTag(Dom4jUtil.getAttributeStringValue(elementObject, "RamusTag"));
                ramus.setName(Dom4jUtil.getAttributeStringValue(elementObject, "text"));
                ramus.setEvent(Dom4jUtil.getAttributeStringValue(elementObject, "Event"));
                ramus.setStatusTag(Dom4jUtil.getAttributeStringValue(elementObject, "StatusTag"));
                save(ramus);

                // TODO: 由于界面的原因,现在只实行表达式
                String expression = Dom4jUtil.getAttributeStringValue(elementObject, "Cond");
                if (StringUtil.isNotEmpty(expression)) {
                    RamusRegulation ramusRegulation = new RamusRegulation();
                    ramusRegulation.setRamus(ramus);
                    ramusRegulation.setExpression(expression);
                    save(ramusRegulation);
                }

                FlowNodeRoute flowNodeRoute = new FlowNodeRoute();
                flowNodeRoute.setObjectId(StringUtil.getUuid());
                flowNodeRoute.setFlowType(flowType);
                flowNodeRoute.setFromFlowNodeId(Dom4jUtil.getAttributeStringValue(elementObject, "from"));
                flowNodeRoute.setToFlowNodeId(Dom4jUtil.getAttributeStringValue(elementObject, "to"));
                flowNodeRoute.setRamusId(ramusId);
                save(flowNodeRoute);
            }
        }
    }

    public FlowType getFlowTypeByEntity(AbstractBaseEntity entity) throws Exception {

        String hql = "select o from " + FlowType.class.getName() + " o where o.objectStatus=1"
                + " and o.entity.entityName=?";
        List list = listByHql(hql, entity.getClass().getSimpleName());
        if (CollectionUtil.isEmpty(list)) {
            throw new BaseBusinessException("", "实体[" + entity.getClass().getName() + "]未配置工作流程!");
        }

        if (list.size() == 1) {
            return (FlowType) list.get(0);
        }

        // 子流程
        for (FlowType flowType : (List<FlowType>) list) {
            // TODO 检查子流程表达式规则
        }

        throw new BaseBusinessException("", "实体[" + entity.getClass().getName() + "]未配置相应的工作子流程!");
    }

    public FlowNode getBeginFlowNode(FlowType flowType) {

        FlowNode beginFlowNode = null;
        List<FlowNode> flowNodeList = flowType.getFlowNodeList();
        for (FlowNode flowNode : flowNodeList) {
            if (flowNode.getFlowNodeType() == FlowNodeType.Begin) {
                beginFlowNode = flowNode;
                break;
            }
        }

        return beginFlowNode;
    }

    public FlowNode getEndFlowNode(FlowType flowType) {

        FlowNode endFlowNode = null;
        List<FlowNode> flowNodeList = flowType.getFlowNodeList();
        for (FlowNode flowNode : flowNodeList) {
            if (flowNode.getFlowNodeType() == FlowNodeType.End) {
                endFlowNode = flowNode;
                break;
            }
        }

        return endFlowNode;
    }

    public FlowNode getFirstFlowNode(FlowType flowType) {

        FlowNode firstFlowNode = null;
        List<FlowNode> flowNodeList = flowType.getFlowNodeList();
        for (FlowNode flowNode : flowNodeList) {
            if (flowNode.getFlowNodeType() == FlowNodeType.First) {
                firstFlowNode = flowNode;
                break;
            }
        }

        return firstFlowNode;
    }

    //=============================== 属性方法 ============================================================

    public WFRegulationService getWfRegulationService() {

        return wfRegulationService;
    }

    public void setWfRegulationService(WFRegulationService wfRegulationService) {

        this.wfRegulationService = wfRegulationService;
    }

    public CodeService getCodeService() {

        return codeService;
    }

    public void setCodeService(CodeService codeService) {

        this.codeService = codeService;
    }
}
