package com.withub.model.std.po;

import com.withub.model.entity.AbstractEntity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "std_entitysequenceregulation")
public class EntitySequenceRegulation extends AbstractEntity{

    //================================ 属性声明 ==========================================================
    private String name;

    @OneToOne(targetEntity = EntitySequence.class)
    @JoinColumn(name = "entitySequenceId")
    private EntitySequence entitySequence;

    private String regulationExpression;

    private Integer orderNo;

    //================================ 属性方法 ==========================================================

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public EntitySequence getEntitySequence() {

        return entitySequence;
    }

    public void setEntitySequence(EntitySequence entitySequence) {

        this.entitySequence = entitySequence;
    }

    public String getRegulationExpression() {

        return regulationExpression;
    }

    public void setRegulationExpression(String regulationExpression) {

        this.regulationExpression = regulationExpression;
    }
}
