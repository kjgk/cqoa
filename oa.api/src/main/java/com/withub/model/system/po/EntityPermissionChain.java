package com.withub.model.system.po;

import com.withub.model.entity.AbstractRecursionEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_ENTITYPERMISSIONCHAIN")
public class EntityPermissionChain extends AbstractRecursionEntity {

    //=================== 属性声明 ============================================

    @OneToOne(targetEntity = Entity.class)
    @JoinColumn(name = "EntityId")
    private Entity entity;

    private String dependProperty;

    @ManyToOne(targetEntity = EntityPermissionChain.class)
    @JoinColumn(name = "parentId")
    private EntityPermissionChain parent;

    @OneToMany(targetEntity = EntityPermissionChain.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<EntityPermissionChain> childList = new ArrayList<EntityPermissionChain>();

    //=================== 属性方法 ============================================

    public Entity getEntity() {

        return entity;
    }

    public void setEntity(Entity entity) {

        this.entity = entity;
    }

    public String getDependProperty() {

        return dependProperty;
    }

    public void setDependProperty(String dependProperty) {

        this.dependProperty = dependProperty;
    }

    public EntityPermissionChain getParent() {

        return parent;
    }

    public void setParent(EntityPermissionChain parent) {

        this.parent = parent;
    }

    public List<EntityPermissionChain> getChildList() {

        return childList;
    }

    public void setChildList(List<EntityPermissionChain> childList) {

        this.childList = childList;
    }
}