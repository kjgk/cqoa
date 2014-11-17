package com.withub.service.impl.workflow;

import com.alibaba.fastjson.JSON;
import com.withub.common.enumeration.TimeUnit;
import com.withub.common.util.CollectionUtil;
import com.withub.common.util.EnumUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.exception.BaseBusinessException;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.User;
import com.withub.model.workflow.enumeration.FlowNodeTaskMode;
import com.withub.model.workflow.enumeration.FlowNodeType;
import com.withub.model.workflow.enumeration.HandlerFetchType;
import com.withub.model.workflow.enumeration.TaskExecuteMode;
import com.withub.model.workflow.po.*;
import com.withub.service.EntityServiceImpl;
import com.withub.service.system.CodeService;
import com.withub.service.workflow.FlowTypeService;
import com.withub.service.workflow.WFRegulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("flowTypeService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class FlowTypeServiceImpl extends EntityServiceImpl implements FlowTypeService {

    //================================ 属性声明 ===========================================================

    @Autowired
    private WFRegulationService wfRegulationService;

    @Autowired
    private CodeService codeService;

    //=============================== 接口实现 ============================================================

    public void saveWorkflowConfig(final String flowTypeId, Map data, User user) throws Exception {

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
        FlowType flowType = get(FlowType.class, flowTypeId);
        flowChart.setFlowType(flowType);
        String jsonChart = JSON.toJSON(data.get("content")).toString();
        flowChart.setConfigInfo(jsonChart);
        save(flowChart);

        ArrayList<LinkedHashMap> linkedHashMapList = (ArrayList<LinkedHashMap>) ((LinkedHashMap) data.get("content")).get("cells");

        Code hourTimeUnit = codeService.getCodeByEnum(TimeUnit.Hour);

        for (LinkedHashMap linkedHashMap : linkedHashMapList) {

            if (StringUtil.compareValue(linkedHashMap.get("type").toString(), "basic.Circle") || StringUtil.compareValue(linkedHashMap.get("type").toString(), "basic.Rect")) {
                FlowNode flowNode = new FlowNode();

                flowNode.setObjectId(linkedHashMap.get("id").toString());
                flowNode.setCurrentUser(user);
                flowNode.setFlowType(flowType);
                String flowNodeName = ((LinkedHashMap) ((LinkedHashMap) linkedHashMap.get("attrs")).get("text")).get("text").toString();
                flowNode.setName(flowNodeName);

                FlowNodeType flowNodeType;
                if (StringUtil.compareValue(linkedHashMap.get("nodeType").toString(), "normal")) {
                    flowNodeType = (FlowNodeType) EnumUtil.getEnumByFieldName(FlowNodeType.class, linkedHashMap.get("FlowNodeType").toString());
                } else {
                    flowNodeType = (FlowNodeType) EnumUtil.getEnumByFieldName(FlowNodeType.class, linkedHashMap.get("nodeType").toString());
                }
                flowNode.setFlowNodeType(flowNodeType);
                flowNode.setFlowNodeTag(linkedHashMap.get("FlowNodeTag") == null ? "" : linkedHashMap.get("FlowNodeTag").toString());
                if (flowNodeType == FlowNodeType.Begin || flowNodeType == FlowNodeType.End) {
                    flowNode.setTaskExecuteMode(TaskExecuteMode.Auto);
                } else {
                    flowNode.setTaskExecuteMode(TaskExecuteMode.Manual);
                }
                flowNode.setExpiration(linkedHashMap.get("TimeLimit") == null ? 0 : Integer.parseInt(linkedHashMap.get("TimeLimit").toString()));
                flowNode.setExpirationTimeUnit(hourTimeUnit);

                flowNode.setFlowNodeTaskMode(FlowNodeTaskMode.Parallelism);
                flowNode.setActivity(linkedHashMap.get("Activity") == null ? "" : linkedHashMap.get("Activity").toString());

                LinkedHashMap flowNodeActionMap = (LinkedHashMap) linkedHashMap.get("FlowNodeAction");
                if (flowNodeActionMap != null) {
                    flowNode.setPassAction((Boolean) flowNodeActionMap.get("passAction") ? 1 : 0);
                    flowNode.setReturnAction((Boolean) flowNodeActionMap.get("returnAction") ? 1 : 0);
                    flowNode.setRejectAction((Boolean) flowNodeActionMap.get("rejectAction") ? 1 : 0);
                    flowNode.setDiscardAction((Boolean) flowNodeActionMap.get("discardAction") ? 1 : 0);
                    flowNode.setCompleteAction((Boolean) flowNodeActionMap.get("completeAction") ? 1 : 0);
                }

                // TODO 完善流程节点上实现取人规则
                flowNode.setHandlerServiceMethod(linkedHashMap.get("Executer") == null ? "" : linkedHashMap.get("Executer").toString());
                flowNode.setHandlerOnFlowNode(linkedHashMap.get("HandlerOnFlowNode") == null ? "" : linkedHashMap.get("HandlerOnFlowNode").toString());

                flowNode.setUserPropertyOnEntity(linkedHashMap.get("UserPropertyOnEntity") == null ? "" : linkedHashMap.get("UserPropertyOnEntity").toString());

                flowNode.setOrganizationId(linkedHashMap.get("OrganizationId") == null ? "" : linkedHashMap.get("OrganizationId").toString());

                flowNode.setRoleId(linkedHashMap.get("RoleId") == null ? "" : linkedHashMap.get("RoleId").toString());

                flowNode.setUseRootNode(0);

                flowNode.setOrganizationProperty(linkedHashMap.get("OrganizationProperty") == null ? "" : linkedHashMap.get("OrganizationProperty").toString());

                flowNode.setRoleProperty(linkedHashMap.get("RoleProperty") == null ? "" : linkedHashMap.get("RoleProperty").toString());

                flowNode.setHandlerFetchCount(linkedHashMap.get("HandlerFetchCount") == null ? 1 : Integer.parseInt(linkedHashMap.get("HandlerFetchCount").toString()));



                // 当有多个任务处理人时的取人方式
                flowNode.setHandlerFetchType(null);
//                flowNode.setHandlerFetchType(linkedHashMap.get("HandlerFetchType") == null ? null
//                        : HandlerFetchType.valueOf(linkedHashMap.get("HandlerFetchType").toString()));


                flowNode.setManualSelectHandler(0);
                flowNode.setAllowAgent(0);
                flowNode.setAllowTransmit(0);
                flowNode.setSkipHandler(0);
                flowNode.setNotifyInstanceCreator(0);
                flowNode.setInstanceReturnMode(0);
                flowNode.setSuspendInstance(0);
                LinkedHashMap configMap = (LinkedHashMap) linkedHashMap.get("Config");
                if (configMap != null) {
                    if ((Boolean) configMap.get("ManualSelectHandler")) {
                        flowNode.setManualSelectHandler(1);
                    } else if ((Boolean) configMap.get("AllowAgent")) {
                        flowNode.setAllowAgent(1);
                        flowNode.setAllowTransmit(1);
                    } else if ((Boolean) configMap.get("SkipHandler")) {
                        flowNode.setSkipHandler(1);
                    } else if ((Boolean) configMap.get("NotifyInstanceCreator")) {
                        flowNode.setNotifyInstanceCreator(1);
                    } else if ((Boolean) configMap.get("InstanceReturnMode")) {
                        flowNode.setInstanceReturnMode(1);
                    } else if ((Boolean) configMap.get("SuspendInstance")) {
                        flowNode.setSuspendInstance(1);
                    }
                }

                flowNode.setEntityStatusTag(linkedHashMap.get("EntityStatusTag") == null ? "" : linkedHashMap.get("EntityStatusTag").toString());

                // TODO 提醒方式
                /*String notify = Dom4jUtil.getAttributeStringValue(elementObject, "WarnType");
                flowNode.setEmail(notify.contains("email") ? 1 : 0);
                flowNode.setSms(notify.contains("sms") ? 1 : 0);
                flowNode.setNotifyInstanceCreator(Dom4jUtil.getAttributeIntegerValue(elementObject, "NotifyInstanceCreator", 0));
                flowNode.setNotifyContent("");*/

                save(flowNode);
            }

            if (StringUtil.compareValue(linkedHashMap.get("type").toString(), "link")) {

                Ramus ramus = new Ramus();
                String ramusId = linkedHashMap.get("id").toString();
                ramus.setObjectId(ramusId);
                int ramusType = 1;
                if (linkedHashMap.get("RamusType") != null) {
                    ramusType = Integer.parseInt(linkedHashMap.get("RamusType").toString());
                }
                ramus.setRamusType(ramusType);
                ramus.setCurrentUser(user);
                ramus.setFlowTypeId(flowTypeId);
                ramus.setRamusTag(linkedHashMap.get("RamusTag") == null ? "" : linkedHashMap.get("RamusTag").toString());
                String ramusName = "";
                try {
                    ramusName = ((LinkedHashMap) ((LinkedHashMap) linkedHashMap.get("attrs")).get("text")).get("text").toString();
                } catch (Exception e) {
                    // do nothing
                }
                ramus.setName(ramusName);

                ramus.setEvent(linkedHashMap.get("Event") == null ? "" : linkedHashMap.get("Event").toString());
                ramus.setStatusTag(linkedHashMap.get("EntityStatusTag") == null ? "" : linkedHashMap.get("EntityStatusTag").toString());
                save(ramus);

                String expression = linkedHashMap.get("Cond") == null ? "" : linkedHashMap.get("Cond").toString();
                if (StringUtil.isNotEmpty(expression)) {
                    RamusRegulation ramusRegulation = new RamusRegulation();
                    ramusRegulation.setRamus(ramus);
                    ramusRegulation.setExpression(expression);
                    save(ramusRegulation);
                }

                FlowNodeRoute flowNodeRoute = new FlowNodeRoute();
                flowNodeRoute.setObjectId(StringUtil.getUuid());
                flowNodeRoute.setFlowType(flowType);

                flowNodeRoute.setFromFlowNodeId(((LinkedHashMap) linkedHashMap.get("source")).get("id").toString());
                flowNodeRoute.setToFlowNodeId(((LinkedHashMap) linkedHashMap.get("target")).get("id").toString());
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
