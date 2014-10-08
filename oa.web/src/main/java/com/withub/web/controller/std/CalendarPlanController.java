package com.withub.web.controller.std;


import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.po.CalendarPlan;
import com.withub.service.std.CalendarPlanService;
import com.withub.util.SpringSecurityUtil;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/std")
public class CalendarPlanController extends BaseController {

    //================================ 属性声明 ===========================================================

    @Autowired
    private CalendarPlanService calendarPlanService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {

        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(true);
            binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        }
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateFormat.setLenient(true);
            binder.registerCustomEditor(Date.class, "startTime", new CustomDateEditor(dateFormat, true));
            binder.registerCustomEditor(Date.class, "endTime", new CustomDateEditor(dateFormat, true));
        }
    }

    //=============================== Controller 方法 ====================================================

    @RequestMapping(value = "/calendarPlan/query", method = RequestMethod.GET)
    public void queryCalendarPlan(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(CalendarPlan.class);
        this.setStringValueEqualsQueryCondition(request, queryInfo, "owner.objectId", "ownerObjectId");

        String start = request.getParameter("start");
        String end = request.getParameter("end");
//
//        if (StringUtil.isEmpty(start)) {
//            Date startTime = DateUtil.convertStringToDate(start, "yyyy-MM-dd");
//
//            SqlExpressionConfig sqlExpressionConfig = new SqlExpressionConfig();
//            sqlExpressionConfig.setPropertyName("startTime");
//            sqlExpressionConfig.setPropertyDataType(PropertyDataType.Date);
//            sqlExpressionConfig.setPropertyValue(startTime);
//            sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.GreaterThanOrEquals);
//
//            QueryConditionNode queryConditionNode = new QueryConditionNode();
//            queryConditionNode.setSqlExpressionConfig(sqlExpressionConfig);
//            queryInfo.getQueryConditionTree().getUserConditionNode().appendNode(queryConditionNode);
//
//        }
//        if (StringUtil.isEmpty(end)) {
//            Date endTime = DateUtil.convertStringToDate(end, "yyyy-MM-dd");
//
//            SqlExpressionConfig sqlExpressionConfig = new SqlExpressionConfig();
//            sqlExpressionConfig.setPropertyName("endTime");
//            sqlExpressionConfig.setPropertyDataType(PropertyDataType.Date);
//            sqlExpressionConfig.setPropertyValue(endTime);
//            sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.LessThanOrEquals);
//
//            QueryConditionNode queryConditionNode = new QueryConditionNode();
//            queryConditionNode.setSqlExpressionConfig(sqlExpressionConfig);
//            queryInfo.getQueryConditionTree().getUserConditionNode().appendNode(queryConditionNode);
//
//        }

        RecordsetInfo recordsetInfo = calendarPlanService.query(queryInfo);

        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (CalendarPlan calendarPlan : (List<CalendarPlan>) list) {
            HashMap item = new HashMap();
            item.put("objectId", calendarPlan.getObjectId());
            item.put("cid", calendarPlan.getImportanceLevel());
//            item.put("id", calendarPlan.getObjectId());
            item.put("title", calendarPlan.getTitle());
            item.put("importanceLevel", calendarPlan.getImportanceLevel());
            item.put("start", DateUtil.getDateFormatString(calendarPlan.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            item.put("end", DateUtil.getDateFormatString(calendarPlan.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
            item.put("location", calendarPlan.getLocation());
            item.put("notes", calendarPlan.getDescription());
            item.put("owner", calendarPlan.getOwner().getName());
            items.add(item);

        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/calendarPlan/create", method = RequestMethod.POST)
    public void createCalendarPlan(CalendarPlan calendarPlan, ModelMap modelMap) throws Exception {

        calendarPlan.setOwner(SpringSecurityUtil.getCurrentUser());
        calendarPlan.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        calendarPlanService.save(calendarPlan);

        modelMap.put("success", true);
    }

    @RequestMapping(value = "/calendarPlan/update", method = RequestMethod.POST)
    public void updateCalendarPlan(CalendarPlan calendarPlan, ModelMap modelMap) throws Exception {

        calendarPlan.setOwner(SpringSecurityUtil.getCurrentUser());
        calendarPlan.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        calendarPlanService.save(calendarPlan);

        modelMap.put("success", true);
    }


    @RequestMapping(value = "/calendarPlan/load/{objectId}", method = RequestMethod.GET)
    public void loadCalendarPlan(@PathVariable(value = "objectId") String objectId, ModelMap modelMap) throws Exception {

        CalendarPlan calendarPlan = calendarPlanService.get(CalendarPlan.class, objectId);
        HashMap model = new HashMap();

        model.put("objectId", calendarPlan.getObjectId());
        model.put("title", calendarPlan.getTitle());
        model.put("startTime", DateUtil.getDateFormatString(calendarPlan.getStartTime(), "yyyy-MM-dd HH:mm"));
        model.put("endTime", DateUtil.getDateFormatString(calendarPlan.getEndTime(), "yyyy-MM-dd HH:mm"));
        model.put("location", calendarPlan.getLocation());
        model.put("importanceLevel", calendarPlan.getImportanceLevel());
        model.put("description", calendarPlan.getDescription());
        model.put("notes", calendarPlan.getDescription());
        model.put("owner", calendarPlan.getOwner().getName());

        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/calendarPlan/delete/{objectId}", method = RequestMethod.GET)
    public void deleteCalendarPlan(@PathVariable(value = "objectId") String objectId, ModelMap modelMap) throws Exception {

        CalendarPlan calendarPlan = calendarPlanService.get(CalendarPlan.class, objectId);
        calendarPlanService.logicDelete(calendarPlan);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/calendarPlan/importanceLevel/query", method = RequestMethod.GET)
    public void queryImportanceLevel(HttpServletRequest request, ModelMap modelMap) throws Exception {

        HashMap tempMap = new HashMap();
        tempMap.put(1, "高");
        tempMap.put(2, "中");
        tempMap.put(3, "低");

        HashMap colorMap = new HashMap();
        colorMap.put(1, 2);
        colorMap.put(2, 20);
        colorMap.put(3, 28);

        List items = new ArrayList();
        for (int i = 1; i < 4; i++) {
            HashMap item = new HashMap();
            item.put("id", i);
            item.put("title", tempMap.get(i));
            item.put("color", colorMap.get(i));
            items.add(item);
        }

        modelMap.put("total", 3);
        modelMap.put("items", items);
    }
}
