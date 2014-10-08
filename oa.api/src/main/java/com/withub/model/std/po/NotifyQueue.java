package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Entity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "std_notifyqueue")
public class NotifyQueue extends AbstractBaseEntity {

    //======================= 属性声明 ============================================

    @OneToOne(targetEntity = SystemEvent.class)
    @JoinColumn(name = "systemEventId")
    private SystemEvent systemEvent;

    @OneToOne(targetEntity = Entity.class)
    @JoinColumn(name = "entityId")
    private Entity entity;

    private String entityInstanceId;

    private String serializableValue;

    //======================= 属性方法 ============================================

    public SystemEvent getSystemEvent() {

        return systemEvent;
    }

    public void setSystemEvent(SystemEvent systemEvent) {

        this.systemEvent = systemEvent;
    }

    public Entity getEntity() {

        return entity;
    }

    public void setEntity(Entity entity) {

        this.entity = entity;
    }

    public String getEntityInstanceId() {

        return entityInstanceId;
    }

    public void setEntityInstanceId(String entityInstanceId) {

        this.entityInstanceId = entityInstanceId;
    }

    public String getSerializableValue() {

        return serializableValue;
    }

    public void setSerializableValue(String serializableValue) {

        this.serializableValue = serializableValue;
    }
}
