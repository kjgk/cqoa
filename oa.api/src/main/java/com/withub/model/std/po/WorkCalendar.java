package com.withub.model.std.po;


import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "STD_WORKCALENDAR")
public class WorkCalendar extends AbstractBaseEntity {

    //=================== 属性声明 ===========================================

    private String name;

    private Integer defaultCalendar;

    private Integer weekendHoliday;

    private String forenoonStartTime;

    private String forenoonEndTime;

    private String afternoonStartTime;

    private String afternoonEndTime;

    private String description;

    private Integer dayHours;

    private Integer orderNo;

    @OneToMany(targetEntity = WorkCalendarHoliday.class, mappedBy = "workCalendar", fetch = FetchType.LAZY)
    @OrderBy(value = "day desc")
    private List<WorkCalendarHoliday> holidayList = new ArrayList<WorkCalendarHoliday>();

    //=================== 属性方法 ===========================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Integer getDefaultCalendar() {

        return defaultCalendar;
    }

    public void setDefaultCalendar(Integer defaultCalendar) {

        this.defaultCalendar = defaultCalendar;
    }

    public Integer getWeekendHoliday() {

        return weekendHoliday;
    }

    public void setWeekendHoliday(Integer weekendHoliday) {

        this.weekendHoliday = weekendHoliday;
    }

    public String getForenoonStartTime() {

        return forenoonStartTime;
    }

    public void setForenoonStartTime(String forenoonStartTime) {

        this.forenoonStartTime = forenoonStartTime;
    }

    public String getForenoonEndTime() {

        return forenoonEndTime;
    }

    public void setForenoonEndTime(String forenoonEndTime) {

        this.forenoonEndTime = forenoonEndTime;
    }

    public String getAfternoonStartTime() {

        return afternoonStartTime;
    }

    public void setAfternoonStartTime(String afternoonStartTime) {

        this.afternoonStartTime = afternoonStartTime;
    }

    public String getAfternoonEndTime() {

        return afternoonEndTime;
    }

    public void setAfternoonEndTime(String afternoonEndTime) {

        this.afternoonEndTime = afternoonEndTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDayHours() {

        return dayHours;
    }

    public void setDayHours(Integer dayHours) {

        this.dayHours = dayHours;
    }

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }

    public List<WorkCalendarHoliday> getHolidayList() {

        return holidayList;
    }

    public void setHolidayList(List<WorkCalendarHoliday> holidayList) {

        this.holidayList = holidayList;
    }
}