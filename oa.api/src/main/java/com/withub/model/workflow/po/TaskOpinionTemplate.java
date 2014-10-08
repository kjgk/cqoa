package com.withub.model.workflow.po;


import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "WF_TASKOPINIONTEMPLATE")
public class TaskOpinionTemplate extends AbstractBaseEntity {

    //=================== 属性声明============================================

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "userId")
    private User user;

    private String opinion;

    private Integer orderNo;


    //=================== 属性方法 ============================================


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}
