package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.po.EventNotifyServiceType;
import com.withub.model.std.po.NotifyServiceType;
import com.withub.model.std.po.SystemEvent;
import com.withub.model.system.po.Code;
import com.withub.service.std.SystemEventService;
import com.withub.util.SpringSecurityUtil;
import com.withub.web.common.BaseController;
import com.withub.web.common.ext.TreeNode;
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
@RequestMapping(value = "/std")
public class SystemEventController extends BaseController {

    //======================= 属性声明 ===================================================

    @Autowired
    private SystemEventService systemEventService;

    //======================= Controller 方法 =============================================

    @RequestMapping(value = "/systemEvent/query", method = RequestMethod.GET)
    public void querySystemEvent(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(SystemEvent.class);
        this.setPageInfoQueryCondition(request, queryInfo);
        this.setAscOrderBy(queryInfo, "orderNo");

        RecordsetInfo recordsetInfo = systemEventService.query(queryInfo);
        List list = recordsetInfo.getEntityList();
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (SystemEvent systemEvent : (List<SystemEvent>) list) {
            HashMap item = new HashMap();

            item.put("objectId", systemEvent.getObjectId());
            item.put("name", systemEvent.getName());
            item.put("className", systemEvent.getClassName());
            item.put("shouldSerializable", systemEvent.getShouldSerializable());
            item.put("entityProperty", systemEvent.getEntityProperty());
            item.put("enableNotify", systemEvent.getEnableNotify());
            item.put("priority", systemEvent.getPriority());
            item.put("delay", systemEvent.getDelay());
            item.put("intervalValue", systemEvent.getIntervalValue());
            item.put("intervalTimeUnit", systemEvent.getIntervalTimeUnit().getName());
            item.put("retrySendCount", systemEvent.getRetrySendCount());
            item.put("accepterServiceMethod", systemEvent.getAccepterServiceMethod());
            item.put("userProperty", systemEvent.getUserProperty());
            item.put("description", systemEvent.getDescription());
            item.put("orderNo", systemEvent.getOrderNo());
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/systemEvent/create", method = RequestMethod.POST)
    public void createSystemEvent(ModelMap modelMap, SystemEvent systemEvent) throws Exception {

        systemEvent.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        systemEventService.save(systemEvent);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/systemEvent/update", method = RequestMethod.POST)
    public void updateSystemEvent(ModelMap modelMap, SystemEvent systemEvent) throws Exception {

        systemEvent.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        systemEventService.save(systemEvent);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/systemEvent/load/{objectId}", method = RequestMethod.GET)
    public void loadSystemEvent(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        SystemEvent systemEvent = (SystemEvent) systemEventService.get(SystemEvent.class, objectId);
        Map model = new HashMap();
        model.put("objectId", systemEvent.getObjectId());
        model.put("objectId", systemEvent.getObjectId());
        model.put("name", systemEvent.getName());
        model.put("className", systemEvent.getClassName());
        model.put("shouldSerializable", systemEvent.getShouldSerializable());
        model.put("entityProperty", systemEvent.getEntityProperty());
        model.put("enableNotify", systemEvent.getEnableNotify());
        model.put("priority", systemEvent.getPriority());
        model.put("delay", systemEvent.getDelay());
        model.put("intervalValue", systemEvent.getIntervalValue());
        model.put("intervalTimeUnit.objectId", systemEvent.getIntervalTimeUnit().getObjectId());
        model.put("retrySendCount", systemEvent.getRetrySendCount());
        model.put("accepterServiceMethod", systemEvent.getAccepterServiceMethod());
        model.put("userProperty", systemEvent.getUserProperty());
        model.put("description", systemEvent.getDescription());
        model.put("orderNo", systemEvent.getOrderNo());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/systemEvent/delete/{objectId}", method = RequestMethod.GET)
    public void deleteSystemEvent(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        systemEventService.logicDelete(SystemEvent.class, objectId);
        modelMap.put("success", true);
    }


    @RequestMapping(value = "/eventNotifyServiceType/update", method = RequestMethod.POST)
    public void updateEventNotifyServiceType(ModelMap modelMap, EventNotifyServiceType eventNotifyServiceType) throws Exception {

        eventNotifyServiceType.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        systemEventService.save(eventNotifyServiceType);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/eventNotifyServiceType/load/{notifyServiceTypeId}/{systemEventId}", method = RequestMethod.GET)
    public void loadEventNotifyServiceType(ModelMap modelMap, @PathVariable(value = "notifyServiceTypeId") String notifyServiceTypeId,
                                           @PathVariable(value = "systemEventId") String systemEventId) throws Exception {

        EventNotifyServiceType eventNotifyServiceType = systemEventService.loadEventNotifyServiceType(notifyServiceTypeId, systemEventId);

        Map model = new HashMap();

        if (eventNotifyServiceType != null) {
            model.put("objectId", eventNotifyServiceType.getObjectId());
            model.put("systemEvent.objectId", eventNotifyServiceType.getSystemEvent().getObjectId());
            model.put("notifyServiceType.objectId", eventNotifyServiceType.getNotifyServiceType().getObjectId());
            model.put("titleTemplate", eventNotifyServiceType.getTitleTemplate());
            model.put("contentTemplate", eventNotifyServiceType.getContentTemplate());
            model.put("defaultTemplate", eventNotifyServiceType.getDefaultTemplate());
        }

        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/eventNotifyServiceType/loadManagerTree", method = RequestMethod.GET)
    public void loadManagerTree(@RequestParam(value = "node") String nodeId, @RequestParam(value = "systemEventId") String systemEventId, @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        SystemEvent systemEvent = systemEventService.get(SystemEvent.class, systemEventId);

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(NotifyServiceType.class);

        List notifyServiceTypeList = systemEventService.list(queryInfo);

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            TreeNode node = new TreeNode();
            node.setObjectId(systemEvent.getObjectId());
            node.setText(systemEvent.getName());
            node.setType(SystemEvent.class.getSimpleName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            if (CollectionUtil.isEmpty(notifyServiceTypeList)) {
                return;
            }
            for (NotifyServiceType notifyServiceType : (List<NotifyServiceType>) notifyServiceTypeList) {
                TreeNode node = new TreeNode();
                node.setObjectId(notifyServiceType.getObjectId());
                node.setText(notifyServiceType.getName());
                node.setLeaf(true);
                node.setType(Code.class.getSimpleName());
                node.setOrderNo(notifyServiceType.getOrderNo());
                nodes.add(node);
            }
        }
    }


    //======================= 属性方法 ===================================================

    public SystemEventService getSystemEventService() {
        return systemEventService;
    }

    public void setSystemEventService(SystemEventService systemEventService) {
        this.systemEventService = systemEventService;
    }

}
