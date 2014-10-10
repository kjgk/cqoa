package com.withub.model.oa.po;

import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "OA_MEETINGROOM")
public class MeetingRoom extends AbstractBaseEntity {

    //================================ 属性声明 ==========================================================

    private String name;

    private String description;

    //================================ 属性方法 ==========================================================


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}