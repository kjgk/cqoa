package com.withub.web.controller.workflow;

import com.withub.common.util.CollectionUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.system.po.Entity;
import com.withub.model.workflow.po.FlowType;
import com.withub.service.workflow.FlowTypeService;
import com.withub.util.SpringSecurityUtil;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/workflow")
public class FlowTypeController extends BaseController {

    //======================= 属性声明 ===================================================

    @Autowired
    private FlowTypeService flowTypeService;

    //======================= Controller 方法 =============================================

    @RequestMapping(value = "/flowType/flowChartEdit", method = RequestMethod.GET)
    public String flowChartEdit(HttpServletRequest request, @RequestParam("flowTypeId") String flowTypeId) throws Exception {

        FlowType flowType = flowTypeService.get(FlowType.class, flowTypeId);
        String configInfo = "";
        if (flowType.getFlowChart() != null) {
            configInfo = flowType.getFlowChart().getConfigInfo();
            configInfo = configInfo.replace("\r\n", "");
        }
        request.setAttribute("configInfo", configInfo);
        request.setAttribute("flowType", flowType);
        return "workflow/flowType/flowChartEdit";
    }

    @RequestMapping(value = "/flowType/create", method = RequestMethod.POST)
    public void createFlowType(ModelMap modelMap, FlowType flowType) throws Exception {

        flowType.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        flowTypeService.save(flowType);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/flowType/update", method = RequestMethod.POST)
    public void updateFlowType(ModelMap modelMap, FlowType flowType) throws Exception {

        flowType.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        flowTypeService.save(flowType);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/flowType/load/{objectId}", method = RequestMethod.GET)
    public void loadFlowType(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        FlowType flowType = flowTypeService.get(FlowType.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", flowType.getObjectId());
        model.put("name", flowType.getName());
        model.put("flowTypeTag", flowType.getFlowTypeTag());
        model.put("code", flowType.getCode());
        model.put("entity.objectId", Entity.class.getSimpleName() + "_" + flowType.getEntity().getObjectId());
        model.put("instanceName", flowType.getInstanceName());
        model.put("subFlowTypeExpression", flowType.getSubFlowTypeExpression());
        model.put("entranceMethod", flowType.getEntranceMethod());
        model.put("url", flowType.getUrl());
        model.put("enable", flowType.getEnable());
        model.put("skipHandler", flowType.getSkipHandler());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/flowType/delete/{objectId}", method = RequestMethod.GET)
    public void deleteFlowType(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        flowTypeService.logicDelete(FlowType.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/flowType/query", method = RequestMethod.GET)
    public void queryFlowType(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(FlowType.class);

        this.setPageInfoQueryCondition(request, queryInfo);

        this.setInputFieldQueryCondition(request, queryInfo, "name");
        this.setAscOrderBy(queryInfo, "orderNo");

        RecordsetInfo recordsetInfo = flowTypeService.query(queryInfo);

        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (FlowType flowType : (List<FlowType>) list) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", flowType.getObjectId());
            item.put("name", flowType.getName());
            item.put("flowTypeTag", flowType.getFlowTypeTag());
            item.put("entityName", flowType.getEntity().getName());
            item.put("enable", flowType.getEnable());
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/flowType/list", method = RequestMethod.GET)
    public void listFlowType(ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(FlowType.class);
        this.setAscOrderBy(queryInfo, "orderNo");
        List list = flowTypeService.list(queryInfo);
        modelMap.put("items", list);
    }

    @RequestMapping(value = "/flowType/{objectId}/loadWorkflowConfig", method = RequestMethod.GET)
    public void loadWorkflowConfig(@PathVariable String objectId, ModelMap modelMap) throws Exception {

        FlowType flowType = flowTypeService.get(FlowType.class, objectId);
        if (flowType.getFlowChart() != null) {
            putData(modelMap, flowType.getFlowChart().getConfigInfo());
        }
    }

    @RequestMapping(value = "/flowType/{objectId}/saveWorkflowConfig", method = RequestMethod.POST)
    public void saveWorkflowConfig(@PathVariable String objectId, @RequestBody Map data) throws Exception {

        /*String content = JSON.toJSON(data.get("content")).toString();
        flowTypeService.saveWorkflowConfig(objectId, content);*/
        flowTypeService.saveWorkflowConfig(objectId, data, SpringSecurityUtil.getCurrentUser());
    }
}
