package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Entity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@javax.persistence.Entity
@Table(name = "std_entitysequence")
public class EntitySequence extends AbstractBaseEntity {

    //================================ 属性声明 ==========================================================
    private String name;

    @OneToOne(targetEntity = Entity.class)
    @JoinColumn(name = "entityId")
    private Entity entity;

    private String sequenceProperty;

    private Integer fixedLength;

    private Integer circleSequenceByYear;

    private String yearProperty;

    private String description;

    private Integer orderNo;

    @Transient
    private List<EntitySequenceRegulation> regulationList;

    //================================ 属性方法 ==========================================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Entity getEntity() {

        return entity;
    }

    public void setEntity(Entity entity) {

        this.entity = entity;
    }

    public String getSequenceProperty() {

        return sequenceProperty;
    }

    public void setSequenceProperty(String sequenceProperty) {

        this.sequenceProperty = sequenceProperty;
    }

    public Integer getFixedLength() {

        return fixedLength;
    }

    public void setFixedLength(Integer fixedLength) {

        this.fixedLength = fixedLength;
    }

    public Integer getCircleSequenceByYear() {

        return circleSequenceByYear;
    }

    public void setCircleSequenceByYear(Integer circleSequenceByYear) {

        this.circleSequenceByYear = circleSequenceByYear;
    }

    public String getYearProperty() {

        return yearProperty;
    }

    public void setYearProperty(String yearProperty) {

        this.yearProperty = yearProperty;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }

    public List<EntitySequenceRegulation> getRegulationList() {

        return regulationList;
    }

    public void setRegulationList(List<EntitySequenceRegulation> regulationList) {

        this.regulationList = regulationList;
    }
}
