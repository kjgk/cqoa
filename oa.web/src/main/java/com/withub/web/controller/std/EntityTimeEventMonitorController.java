package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.po.EntityTimeEventMonitor;
import com.withub.service.std.EntityTimeEventMonitorService;
import com.withub.util.SpringSecurityUtil;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/std")
public class EntityTimeEventMonitorController extends BaseController {

    //======================= 属性声明 ===================================================

    @Autowired
    private EntityTimeEventMonitorService entityTimeEventMonitorService;

    //======================= Controller 方法 =============================================

    @RequestMapping(value = "/entityTimeEventMonitor/query", method = RequestMethod.GET)
    public void listEntityTimeEventMonitor(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(EntityTimeEventMonitor.class);
        this.setPageInfoQueryCondition(request, queryInfo);
        this.setInputFieldQueryCondition(request, queryInfo, "entity.name", "entityName");
        this.setAscOrderBy(queryInfo, "orderNo");

        RecordsetInfo recordsetInfo = entityTimeEventMonitorService.query(queryInfo);

        List list = recordsetInfo.getEntityList();
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (EntityTimeEventMonitor entityTimeEventMonitor : (List<EntityTimeEventMonitor>) list) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", entityTimeEventMonitor.getObjectId());
            item.put("name", entityTimeEventMonitor.getName());
            item.put("entity", entityTimeEventMonitor.getEntity().getName());
            item.put("expiredEvent", entityTimeEventMonitor.getExpiredEvent());
            item.put("entityTimeProperty", entityTimeEventMonitor.getEntityTimeProperty());
            item.put("entityTimePropertyIsExpired", entityTimeEventMonitor.getEntityTimePropertyIsExpired());
            item.put("expiredTimeValue", entityTimeEventMonitor.getExpiredTimeValue() + " " + entityTimeEventMonitor.getExpiredTimeValueTimeUnit().getName());
            item.put("eventClassName", entityTimeEventMonitor.getEventClassName());
            item.put("UseworkCalendar", entityTimeEventMonitor.getUseWorkCalendar());
            item.put("startTime", entityTimeEventMonitor.getStartTime());
            item.put("endTime", entityTimeEventMonitor.getEndTime());
            item.put("intervalValue", entityTimeEventMonitor.getIntervalValue() + entityTimeEventMonitor.getIntervalValueTimeUnit().getName());
            item.put("priority", entityTimeEventMonitor.getPriority());
            item.put("recordSetSize", entityTimeEventMonitor.getRecordSetSize());
            item.put("additionalCondition", entityTimeEventMonitor.getAdditionalCondition());
            item.put("enable", entityTimeEventMonitor.getEnable());

            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/entityTimeEventMonitor/create", method = RequestMethod.POST)
    public void createEntityTimeEventMonitor(ModelMap modelMap, EntityTimeEventMonitor entityTimeEventMonitor) throws Exception {

        entityTimeEventMonitor.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        entityTimeEventMonitorService.save(entityTimeEventMonitor);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entityTimeEventMonitor/update", method = RequestMethod.POST)
    public void updateEntityTimeEventMonitor(ModelMap modelMap, EntityTimeEventMonitor entityTimeEventMonitor) throws Exception {

        entityTimeEventMonitor.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        entityTimeEventMonitorService.save(entityTimeEventMonitor);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entityTimeEventMonitor/load/{objectId}", method = RequestMethod.GET)
    public void loadEntityTimeEventMonitor(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        EntityTimeEventMonitor entityTimeEventMonitor = entityTimeEventMonitorService.get(EntityTimeEventMonitor.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", entityTimeEventMonitor.getObjectId());
        model.put("name", entityTimeEventMonitor.getName());
        model.put("entity.objectId", entityTimeEventMonitor.getEntity().getObjectId());
        model.put("expiredEvent", entityTimeEventMonitor.getExpiredEvent());
        model.put("entityTimeProperty", entityTimeEventMonitor.getEntityTimeProperty());
        model.put("entityTimePropertyIsExpired", entityTimeEventMonitor.getEntityTimePropertyIsExpired());
        model.put("expiredTimeValue", entityTimeEventMonitor.getExpiredTimeValue());
        model.put("expiredTimeValueTimeUnit.objectId", entityTimeEventMonitor.getExpiredTimeValueTimeUnit().getObjectId());
        model.put("eventClassName", entityTimeEventMonitor.getEventClassName());
        model.put("useWorkCalendar", entityTimeEventMonitor.getUseWorkCalendar());
        model.put("startTime", entityTimeEventMonitor.getStartTime());
        model.put("endTime", entityTimeEventMonitor.getEndTime());
        model.put("intervalValue", entityTimeEventMonitor.getIntervalValue());
        model.put("intervalValueTimeUnit.objectId", entityTimeEventMonitor.getIntervalValueTimeUnit().getObjectId());
        model.put("priority", entityTimeEventMonitor.getPriority());
        model.put("recordSetSize", entityTimeEventMonitor.getRecordSetSize());
        model.put("additionalCondition", entityTimeEventMonitor.getAdditionalCondition());
        model.put("enable", entityTimeEventMonitor.getEnable());

        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entityTimeEventMonitor/delete/{objectId}", method = RequestMethod.GET)
    public void deleteEntityTimeEventMonitor(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        entityTimeEventMonitorService.logicDelete(EntityTimeEventMonitor.class, objectId);
        modelMap.put("success", true);
    }

    //======================= 属性方法 ===================================================

    public EntityTimeEventMonitorService getEntityTimeEventMonitorService() {

        return entityTimeEventMonitorService;
    }

    public void setEntityTimeEventMonitorService(EntityTimeEventMonitorService entityTimeEventMonitorService) {

        this.entityTimeEventMonitorService = entityTimeEventMonitorService;
    }
}
