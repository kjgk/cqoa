package com.withub.model.entity;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @Id
    private String objectId;

    public String getObjectId() {

        return objectId;
    }

    public void setObjectId(String objectId) {

        this.objectId = objectId;
    }
}
