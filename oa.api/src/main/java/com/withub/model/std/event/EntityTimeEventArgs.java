package com.withub.model.std.event;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Entity;

public class EntityTimeEventArgs {

    Entity entity;
    AbstractBaseEntity entityInstance;

    public Entity getEntity() {

        return entity;
    }

    public void setEntity(Entity entity) {

        this.entity = entity;
    }

    public AbstractBaseEntity getEntityInstance() {

        return entityInstance;
    }

    public void setEntityInstance(AbstractBaseEntity entityInstance) {

        this.entityInstance = entityInstance;
    }
}
