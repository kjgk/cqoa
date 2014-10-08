package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.po.WorkCalendar;
import com.withub.model.std.po.WorkCalendarHoliday;
import com.withub.service.std.WorkCalendarService;
import com.withub.util.SpringSecurityUtil;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "/std")
public class WorkCalendarController extends BaseController {

    //======================= 属性声明 ===================================================

    @Autowired
    private WorkCalendarService workCalendarService;

    //======================= Controller 方法 =============================================

    @RequestMapping(value = "/workCalendar/create", method = RequestMethod.POST)
    public void createWorkCalendar(ModelMap modelMap, WorkCalendar workCalendar) throws Exception {

        workCalendar.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        workCalendarService.save(workCalendar);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/workCalendar/update", method = RequestMethod.POST)
    public void updateWorkCalendar(ModelMap modelMap, WorkCalendar workCalendar) throws Exception {

        workCalendar.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        workCalendarService.save(workCalendar);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/workCalendar/load/{objectId}", method = RequestMethod.GET)
    public void loadWorkCalendar(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        WorkCalendar workCalendar = (WorkCalendar) workCalendarService.get(WorkCalendar.class, objectId);
        Map model = new HashMap();
        model.put("objectId", workCalendar.getObjectId());
        model.put("name", workCalendar.getName());
        model.put("defaultCalendar", workCalendar.getDefaultCalendar());
        model.put("weekendHoliday", workCalendar.getWeekendHoliday());
        model.put("forenoonStartTime", workCalendar.getForenoonStartTime());
        model.put("forenoonEndTime", workCalendar.getForenoonEndTime());
        model.put("afternoonStartTime", workCalendar.getAfternoonStartTime());
        model.put("afternoonEndTime", workCalendar.getAfternoonEndTime());
        model.put("dayHours", workCalendar.getDayHours());
        model.put("description", workCalendar.getDescription());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/workCalendar/delete/{objectId}", method = RequestMethod.GET)
    public void deleteWorkCalendar(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        workCalendarService.logicDelete(WorkCalendar.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/workCalendar/query", method = RequestMethod.GET)
    public void listWorkCalendar(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(WorkCalendar.class);

        this.setPageInfoQueryCondition(request, queryInfo);
        this.setInputFieldQueryCondition(request, queryInfo, "name");
        this.setAscOrderBy(queryInfo, "orderNo");

        RecordsetInfo recordsetInfo = workCalendarService.query(queryInfo);

        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (WorkCalendar workCalendar : (List<WorkCalendar>) list) {
            HashMap item = new HashMap();

            item.put("objectId", workCalendar.getObjectId());
            item.put("name", workCalendar.getName());
            item.put("defaultCalendar", workCalendar.getDefaultCalendar());
            item.put("weekendHoliday", workCalendar.getWeekendHoliday());
            item.put("forenoonStartTime", workCalendar.getForenoonStartTime());
            item.put("forenoonEndTime", workCalendar.getForenoonEndTime());
            item.put("afternoonStartTime", workCalendar.getAfternoonStartTime());
            item.put("afternoonEndTime", workCalendar.getAfternoonEndTime());
            item.put("dayHours", workCalendar.getDayHours());
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/workCalendarHoliday/query", method = RequestMethod.POST)
    public void listWorkCalendarHoliday(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(WorkCalendarHoliday.class);

        this.setDateRangeQueryCondition(request, queryInfo, "day");
        this.setStringValueEqualsQueryCondition(request, queryInfo, "workCalendar.objectId", "workCalendarId");
        this.setDescOrderBy(queryInfo, "day");

        RecordsetInfo recordsetInfo = workCalendarService.query(queryInfo);

        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (WorkCalendarHoliday workCalendarHoliday : (List<WorkCalendarHoliday>) list) {
            HashMap item = new HashMap();

            item.put("objectId", workCalendarHoliday.getObjectId());
            item.put("day", workCalendarHoliday.getDay().getTime());
            item.put("holiday", workCalendarHoliday.getHoliday());
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/workCalendarHoliday/set", method = RequestMethod.POST)
    public void setWorkCalendarHoliday(ModelMap modelMap, WorkCalendarHoliday workCalendarHoliday) throws Exception {
        workCalendarService.setWorkCalendarHoliday(workCalendarHoliday);
        modelMap.put("success", true);
    }


    @RequestMapping(value = "/workCalendarHoliday/cancel", method = RequestMethod.POST)
    public void cancelWorkCalendarHoliday(ModelMap modelMap, WorkCalendarHoliday workCalendarHoliday) throws Exception {
        workCalendarService.cancelWorkCalendarHoliday(workCalendarHoliday);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/workCalendarHoliday/delete/{objectId}", method = RequestMethod.GET)
    public void deleteWorkCalendarHoliday(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        workCalendarService.delete(WorkCalendarHoliday.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/workCalendar/getEndWorkTime", method = RequestMethod.POST)
    public void getEndWorkTime(HttpServletRequest request, ModelMap modelMap) throws Exception {

        String start = request.getParameter("startTime");
        int workHoursLimit = Integer.parseInt(request.getParameter("hours"));
        String workCalendarId = request.getParameter("workCalendarId");

        Date startTime = DateUtil.convertStringToDate(start, "yyyy-MM-dd HH:mm");

        Date endTime = workCalendarService.getWorkTimeExpiration(startTime, workHoursLimit);

        modelMap.put("result", DateUtil.getStandardMinuteString(endTime));
        modelMap.put("success", true);
    }

    //======================= 属性方法 ===================================================

    public WorkCalendarService getWorkCalendarService() {

        return workCalendarService;
    }

    public void setWorkCalendarService(WorkCalendarService workCalendarService) {

        this.workCalendarService = workCalendarService;
    }
}
