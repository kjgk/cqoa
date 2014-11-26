package com.withub.model.oa.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.Organization;
import com.withub.model.system.po.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@javax.persistence.Entity
@Table(name = "OA_USERGROUP")
public class UserGroup extends AbstractBaseEntity {

    //================================ 属性声明 ==========================================================

    private String name;

    private String tag;

    @OneToMany(targetEntity = UserGroupDetail.class, mappedBy = "userGroup", fetch = FetchType.LAZY)
    private List<UserGroupDetail> userGroupDetailList;

    //================================ 属性方法 ==========================================================

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<UserGroupDetail> getUserGroupDetailList() {
        return userGroupDetailList;
    }

    public void setUserGroupDetailList(List<UserGroupDetail> userGroupDetailList) {
        this.userGroupDetailList = userGroupDetailList;
    }

}