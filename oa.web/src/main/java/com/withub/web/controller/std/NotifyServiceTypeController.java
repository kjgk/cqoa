package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.po.NotifyServiceType;
import com.withub.service.std.NotifyServiceTypeService;
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

@Controller
@RequestMapping(value = "/std")
public class NotifyServiceTypeController extends BaseController {


    //================================== 属性声明 =========================================================

    @Autowired
    private NotifyServiceTypeService notifyServiceTypeService;

    //================================== Controller 方法 =================================================

    @RequestMapping(value = "/notifyServiceType/query", method = RequestMethod.GET)
    public void queryNotifyServiceType(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(NotifyServiceType.class);
        this.setPageInfoQueryCondition(request, queryInfo);
        this.setInputFieldQueryCondition(request, queryInfo, "name");
        this.setDescOrderBy(queryInfo, "orderNo");

        RecordsetInfo recordsetInfo = notifyServiceTypeService.query(queryInfo);

        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (NotifyServiceType notifyServiceType : (List<NotifyServiceType>) list) {
            HashMap item = new HashMap();

            item.put("objectId", notifyServiceType.getObjectId());
            item.put("name", notifyServiceType.getName());
            item.put("notifyServiceTypeTag", notifyServiceType.getNotifyServiceTypeTag());
            item.put("userPropertyAddress", notifyServiceType.getUserPropertyAddress());
            item.put("customiseNotifyTime", notifyServiceType.getCustomiseNotifyTime());
            item.put("description", notifyServiceType.getDescription());
            item.put("enable", notifyServiceType.getEnable());
            item.put("orderNo", notifyServiceType.getOrderNo());

            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/notifyServiceType/create", method = RequestMethod.POST)
    public void createNotifyServiceType(NotifyServiceType notifyServiceType, ModelMap modelMap) throws Exception {

        notifyServiceType.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        notifyServiceTypeService.save(notifyServiceType);

        modelMap.put("success", true);
    }

    @RequestMapping(value = "/notifyServiceType/update", method = RequestMethod.POST)
    public void updateNotifyServiceType(NotifyServiceType notifyServiceType, ModelMap modelMap) throws Exception {

        notifyServiceType.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        notifyServiceTypeService.save(notifyServiceType);

        modelMap.put("success", true);
    }


    @RequestMapping(value = "/notifyServiceType/load/{objectId}", method = RequestMethod.GET)
    public void loadNotifyServiceType(@PathVariable(value = "objectId") String objectId, ModelMap modelMap) throws Exception {

        NotifyServiceType notifyServiceType = notifyServiceTypeService.get(NotifyServiceType.class, objectId);
        HashMap model = new HashMap();

        model.put("objectId", notifyServiceType.getObjectId());

        model.put("objectId", notifyServiceType.getObjectId());
        model.put("name", notifyServiceType.getName());
        model.put("notifyServiceTypeTag", notifyServiceType.getNotifyServiceTypeTag());
        model.put("userPropertyAddress", notifyServiceType.getUserPropertyAddress());
        model.put("customiseNotifyTime", notifyServiceType.getCustomiseNotifyTime());
        model.put("description", notifyServiceType.getDescription());
        model.put("enable", notifyServiceType.getEnable());
        model.put("orderNo", notifyServiceType.getOrderNo());

        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/notifyServiceType/delete/{objectId}", method = RequestMethod.GET)
    public void deleteNotifyServiceType(@PathVariable(value = "objectId") String objectId, ModelMap modelMap) throws Exception {

        NotifyServiceType notifyServiceType = notifyServiceTypeService.get(NotifyServiceType.class, objectId);
        notifyServiceTypeService.logicDelete(notifyServiceType);
        modelMap.put("success", true);
    }
}
