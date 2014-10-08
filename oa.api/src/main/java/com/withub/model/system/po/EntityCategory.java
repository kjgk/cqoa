package com.withub.model.system.po;

import com.withub.model.entity.AbstractRecursionEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_ENTITYCATEGORY")
public class EntityCategory extends AbstractRecursionEntity {

    //=================== 属性声明 ============================================

    @ManyToOne(targetEntity = EntityCategory.class)
    @JoinColumn(name = "parentId")
    private EntityCategory parent;

    @OneToMany(targetEntity = EntityCategory.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<EntityCategory> childList = new ArrayList<EntityCategory>();

    @OneToMany(targetEntity = Entity.class, mappedBy = "entityCategory", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<Entity> entityList = new ArrayList<Entity>();

    //=================== 属性方法 ============================================

    public EntityCategory getParent() {

        return parent;
    }

    public void setParent(EntityCategory parent) {

        this.parent = parent;
    }

    public List<EntityCategory> getChildList() {

        return childList;
    }

    public void setChildList(List<EntityCategory> childList) {

        this.childList = childList;
    }

    public List<Entity> getEntityList() {

        return entityList;
    }

    public void setEntityList(List<Entity> entityList) {

        this.entityList = entityList;
    }
}