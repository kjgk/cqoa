package com.withub.model.std.po;

import com.withub.model.entity.AbstractEntity;
import com.withub.model.system.po.Entity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "std_entitycurrentsequence")
public class EntityCurrentSequence extends AbstractEntity {

    //================================ 属性声明 ==========================================================
    @OneToOne(targetEntity = Entity.class)
    @JoinColumn(name = "entityId")
    private Entity entity;

    @OneToOne(targetEntity = EntitySequenceRegulation.class)
    @JoinColumn(name = "entitySequenceRegulationId")
    private EntitySequenceRegulation entitySequenceRegulation;

    private String Rank;

    private String currentSequence;

    private Date lastUpdateTime;

    //================================ 属性方法 ==========================================================

    public Entity getEntity() {

        return entity;
    }

    public void setEntity(Entity entity) {

        this.entity = entity;
    }

    public EntitySequenceRegulation getEntitySequenceRegulation() {

        return entitySequenceRegulation;
    }

    public void setEntitySequenceRegulation(EntitySequenceRegulation entitySequenceRegulation) {

        this.entitySequenceRegulation = entitySequenceRegulation;
    }

    public String getRank() {

        return Rank;
    }

    public void setRank(String rank) {

        Rank = rank;
    }

    public String getCurrentSequence() {

        return currentSequence;
    }

    public void setCurrentSequence(String currentSequence) {

        this.currentSequence = currentSequence;
    }

    public Date getLastUpdateTime() {

        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {

        this.lastUpdateTime = lastUpdateTime;
    }
}
