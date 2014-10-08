package com.withub.service.std;


import com.withub.model.std.po.WorkCalendar;
import com.withub.model.std.po.WorkCalendarHoliday;
import com.withub.service.EntityService;

import java.util.Date;

public interface WorkCalendarService extends EntityService {

    public void setWorkCalendarHoliday(WorkCalendarHoliday workCalendarHoliday) throws Exception;

    public void cancelWorkCalendarHoliday(WorkCalendarHoliday workCalendarHoliday) throws Exception;

    public WorkCalendar getDefaultWorkCalendar() throws Exception;

    public boolean isHoliday(Date dateTime) throws Exception;

    public Date getFirstWorkDayAfterHoliday(Date holiday) throws Exception;

    public Date getWorkTimeExpiration(Date dateTime, final Integer workHoursLimit) throws Exception;

    public Date getWorkDayExpiration(Date startWorkDate, final Integer workHoursLimit, WorkCalendar workCalendar) throws Exception;
}
