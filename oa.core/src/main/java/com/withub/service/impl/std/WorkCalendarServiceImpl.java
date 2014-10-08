package com.withub.service.impl.std;


import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.model.std.po.WorkCalendar;
import com.withub.model.std.po.WorkCalendarHoliday;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.WorkCalendarService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("workCalendarService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class WorkCalendarServiceImpl extends EntityServiceImpl implements WorkCalendarService {

    public void setWorkCalendarHoliday(WorkCalendarHoliday workCalendarHoliday) throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.setTime(workCalendarHoliday.getDay());

        int weekNumber = cal.get(Calendar.DAY_OF_WEEK);

        WorkCalendar workCalendar = get(WorkCalendar.class, workCalendarHoliday.getWorkCalendar().getObjectId());

        if ((weekNumber == 1 || weekNumber == 7) && workCalendar.getWeekendHoliday() == 1) {

            String hql = "select o from " + WorkCalendarHoliday.class.getName()
                    + " o where o.workCalendar.objectId=? and o.day=? ";
            WorkCalendarHoliday oldWorkCalendarHoliday = (WorkCalendarHoliday) getByHql(hql, workCalendarHoliday.getWorkCalendar().getObjectId(), workCalendarHoliday.getDay());
            delete(oldWorkCalendarHoliday);
        } else {
            save(workCalendarHoliday);

        }

    }

    public void cancelWorkCalendarHoliday(WorkCalendarHoliday workCalendarHoliday) throws Exception {

        String hql = "select o from " + WorkCalendarHoliday.class.getName()
                + " o where o.workCalendar.objectId=? and o.day=? ";
        WorkCalendarHoliday oldWorkCalendarHoliday = (WorkCalendarHoliday) getByHql(hql, workCalendarHoliday.getWorkCalendar().getObjectId(), workCalendarHoliday.getDay());

        if (oldWorkCalendarHoliday != null) {
            delete(oldWorkCalendarHoliday);
        } else {
            save(workCalendarHoliday);
        }

    }

    public WorkCalendar getDefaultWorkCalendar() throws Exception {

        WorkCalendar workCalendar = (WorkCalendar) getByPropertyValue(WorkCalendar.class, "defaultCalendar", 1);
        return workCalendar;
    }

    public boolean isHoliday(Date dateTime) throws Exception {

        // 根据工作日历表判断
        String hql = "select o from " + WorkCalendarHoliday.class.getName() + " o"
                + " where o.workCalendar.defaultCalendar=0 and o.workCalendar.objectStatus=1 and o.day=?";
        List list = listByHql(hql, dateTime);
        if (CollectionUtil.isNotEmpty(list)) {
            WorkCalendarHoliday workCalendarHoliday = (WorkCalendarHoliday) list.get(0);
            return workCalendarHoliday.getHoliday() == 1;
        }

        // 判断周末
        if (DateUtil.isWeekend(dateTime)) {
            WorkCalendar workCalendar = getDefaultWorkCalendar();
            if (workCalendar.getWeekendHoliday() == 1) {
                return true;
            }
        }

        return false;
    }

    public Date getFirstWorkDayAfterHoliday(Date holiday) throws Exception {

        holiday = DateUtil.convertStringToDate(DateUtil.getStandardDateString(holiday), "yyyy-MM-dd");
        Date firstWorkDay;
        String hql = "select o from " + WorkCalendarHoliday.class.getName() + " o"
                + " where o.workCalendar.defaultCalendar=1 and o.workCalendar.objectStatus=1"
                + " and o.day>? order by o.day";
        List list = listByHql(hql, holiday);
        if (CollectionUtil.isEmpty(list)) {
            firstWorkDay = DateUtil.addDays(holiday, 1);
            return firstWorkDay;
        }

        // 判断是否连续节假日
        Date previousDay = holiday;
        for (int i = 0; i < list.size(); i++) {
            WorkCalendarHoliday workCalendarHoliday = (WorkCalendarHoliday) list.get(i);

            if (workCalendarHoliday.getHoliday() == 0) {
                return workCalendarHoliday.getDay();
            }

            if (DateUtil.getDiffDays(previousDay, workCalendarHoliday.getDay()) > 1) {
                break;
            } else {
                previousDay = workCalendarHoliday.getDay();
            }
        }

        firstWorkDay = DateUtil.addDays(previousDay, 1);

        return firstWorkDay;
    }

    public Date getWorkTimeExpiration(Date dateTime, final Integer workHoursLimit) throws Exception {

        WorkCalendar workCalendar = getDefaultWorkCalendar();

        // 判断节假日的情况
        boolean startTimeIsHoliday = isHoliday(dateTime);
        if (startTimeIsHoliday) {
            Date firstWorkDayAfterHoliday = getFirstWorkDayAfterHoliday(dateTime);
            return getWorkDayExpiration(firstWorkDayAfterHoliday, workHoursLimit, workCalendar);
        }

        String timeString = DateUtil.getStandardMinuteString(dateTime).substring(11);
        Date today = DateUtil.convertStringToDate(DateUtil.getStandardDateString(dateTime), "yyyy-MM-dd");

        // 上班之前
        if (timeGreater(workCalendar.getForenoonStartTime(), timeString)) {
            return getWorkDayExpiration(today, workHoursLimit, workCalendar);
        }

        // 下班之后
        if (timeGreater(timeString, workCalendar.getAfternoonEndTime())) {
            Date nextDay = DateUtil.addDays(today, 1);
            if (!isHoliday(nextDay)) {
                return getWorkDayExpiration(nextDay, workHoursLimit, workCalendar);
            }
            Date firstWorkDayAfterHoliday = getFirstWorkDayAfterHoliday(dateTime);
            return getWorkDayExpiration(firstWorkDayAfterHoliday, workHoursLimit, workCalendar);
        }

        // 获取到今天下班还有多少工作小时
        long hours = DateUtil.getDiffHours(dateTime, DateUtil.convertStringToDate(DateUtil.getStandardDateString(dateTime) + " " + workCalendar.getAfternoonEndTime(), "yyyy-MM-dd HH:mm"));
        if (hours >= workHoursLimit) {
            return DateUtil.addHours(dateTime, workHoursLimit);
        }

        int leaveHours = Integer.parseInt((workHoursLimit - hours) + "");
        Date nextDay = DateUtil.addDays(today, 1);
        if (isHoliday(nextDay)) {
            Date firstWorkDayAfterHoliday = getFirstWorkDayAfterHoliday(nextDay);
            return getWorkDayExpiration(firstWorkDayAfterHoliday, workHoursLimit, workCalendar);
        } else {
            return getWorkDayExpiration(nextDay, leaveHours, workCalendar);
        }
    }

    public Date getWorkDayExpiration(Date startWorkDate, final Integer workHoursLimit, WorkCalendar workCalendar) throws Exception {

        if (workHoursLimit <= workCalendar.getDayHours()) {
            Date startTime = DateUtil.convertStringToDate(DateUtil.getStandardDateString(startWorkDate) + " " + workCalendar.getForenoonStartTime(), "yyyy-MM-dd HH:mm");
            return DateUtil.addHours(startTime, workHoursLimit);
        }

        int days = workHoursLimit / workCalendar.getDayHours();
        int i = 0;
        while (days > 0) {
            Date nextDay = DateUtil.addDays(startWorkDate, i + 1);
            if (!isHoliday(nextDay)) {
                days--;
            }
            i++;
        }
        Date endDate = DateUtil.addDays(startWorkDate, i);
        int hours = workHoursLimit % workCalendar.getDayHours();

        Date startTime = DateUtil.convertStringToDate(DateUtil.getStandardDateString(endDate) + " " + workCalendar.getForenoonStartTime(), "yyyy-MM-dd HH:mm");
        return DateUtil.addHours(startTime, hours);
    }

    private boolean timeGreater(String startTime, String endTime) {

        startTime = startTime.trim().replace(":", "");
        endTime = endTime.trim().replace(":", "");
        return Integer.parseInt(startTime) > Integer.parseInt(endTime);
    }

}
