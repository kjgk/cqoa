package com.withub.model.std.po;


import com.withub.model.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "STD_WORKCALENDARHOLIDAY")
public class WorkCalendarHoliday extends AbstractEntity {

    //================= 属性声明 ========================================

    @ManyToOne(targetEntity = WorkCalendar.class)
    @JoinColumn(name = "WorkCalendarId")
    private WorkCalendar workCalendar;

    private Date day;

    private Integer holiday;

    //================= 属性方法 ========================================

    public WorkCalendar getWorkCalendar() {

        return workCalendar;
    }

    public void setWorkCalendar(WorkCalendar workCalendar) {

        this.workCalendar = workCalendar;
    }

    public Date getDay() {

        return day;
    }

    public void setDay(Date day) {

        this.day = day;
    }

    public Integer getHoliday() {

        return holiday;
    }

    public void setHoliday(Integer holiday) {

        this.holiday = holiday;
    }
}