package com.withub.model.system.po;


import com.withub.model.entity.AbstractBaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_ENTITY")
public class Entity extends AbstractBaseEntity {

    //=================== 属性声明 ============================================

    private String name;

    private String entityName;

    private String tableName;

    @ManyToOne(targetEntity = EntityCategory.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "EntityCategoryId")
    private EntityCategory entityCategory;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "EntityTypeId")
    private Code entityType;

    @OneToMany(targetEntity = Property.class, mappedBy = "entity", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    private List<Property> propertyList = new ArrayList<Property>();

    @OneToMany(targetEntity = Permission.class, mappedBy = "entity", fetch = FetchType.LAZY)
    @Where(clause = "objectStatus = 1")
    @OrderBy(value = "orderNo asc")
    private List<Permission> permissionList = new ArrayList<Permission>();

    private String className;

    private String identifierTemplate;

    private String defaultOrderProperty;

    private String defaultOrderType;

    private String orderNoDependProperty;

    private Integer allowAuthorization;

    private String fullNameRegulation;

    private Integer enableFullTextSearch;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "fullTextSearchColumn")
    private Code fullTextSearchColumn;

    private Integer extendFullTextSearch;

    private Integer fullTextSearchPermission;

    private Integer attachmentFullTextSearch;

    @OneToOne(targetEntity = Menu.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "MenuId")
    private Menu menu;

    private String description;

    private Integer orderNo;

    //=================== 属性方法 ============================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getEntityName() {

        return entityName;
    }

    public void setEntityName(String entityName) {

        this.entityName = entityName;
    }

    public String getTableName() {

        return tableName;
    }

    public void setTableName(String tableName) {

        this.tableName = tableName;
    }

    public EntityCategory getEntityCategory() {

        return entityCategory;
    }

    public void setEntityCategory(EntityCategory entityCategory) {

        this.entityCategory = entityCategory;
    }

    public Code getEntityType() {

        return entityType;
    }

    public void setEntityType(Code entityType) {

        this.entityType = entityType;
    }

    public List<Property> getPropertyList() {

        return propertyList;
    }

    public void setPropertyList(List<Property> propertyList) {

        this.propertyList = propertyList;
    }

    public List<Permission> getPermissionList() {

        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {

        this.permissionList = permissionList;
    }

    public String getClassName() {

        return className;
    }

    public void setClassName(String className) {

        this.className = className;
    }

    public String getIdentifierTemplate() {

        return identifierTemplate;
    }

    public void setIdentifierTemplate(String identifierTemplate) {

        this.identifierTemplate = identifierTemplate;
    }

    public String getDefaultOrderProperty() {

        return defaultOrderProperty;
    }

    public void setDefaultOrderProperty(String defaultOrderProperty) {

        this.defaultOrderProperty = defaultOrderProperty;
    }

    public String getDefaultOrderType() {

        return defaultOrderType;
    }

    public void setDefaultOrderType(String defaultOrderType) {

        this.defaultOrderType = defaultOrderType;
    }

    public String getOrderNoDependProperty() {

        return orderNoDependProperty;
    }

    public void setOrderNoDependProperty(String orderNoDependProperty) {

        this.orderNoDependProperty = orderNoDependProperty;
    }

    public Integer getAllowAuthorization() {

        return allowAuthorization;
    }

    public void setAllowAuthorization(Integer allowAuthorization) {

        this.allowAuthorization = allowAuthorization;
    }

    public String getFullNameRegulation() {

        return fullNameRegulation;
    }

    public void setFullNameRegulation(String fullNameRegulation) {

        this.fullNameRegulation = fullNameRegulation;
    }

    public Integer getEnableFullTextSearch() {

        return enableFullTextSearch;
    }

    public void setEnableFullTextSearch(Integer enableFullTextSearch) {

        this.enableFullTextSearch = enableFullTextSearch;
    }

    public Code getFullTextSearchColumn() {

        return fullTextSearchColumn;
    }

    public void setFullTextSearchColumn(Code fullTextSearchColumn) {

        this.fullTextSearchColumn = fullTextSearchColumn;
    }

    public Integer getExtendFullTextSearch() {

        return extendFullTextSearch;
    }

    public void setExtendFullTextSearch(Integer extendFullTextSearch) {

        this.extendFullTextSearch = extendFullTextSearch;
    }

    public Integer getFullTextSearchPermission() {

        return fullTextSearchPermission;
    }

    public void setFullTextSearchPermission(Integer fullTextSearchPermission) {

        this.fullTextSearchPermission = fullTextSearchPermission;
    }

    public Integer getAttachmentFullTextSearch() {

        return attachmentFullTextSearch;
    }

    public void setAttachmentFullTextSearch(Integer attachmentFullTextSearch) {

        this.attachmentFullTextSearch = attachmentFullTextSearch;
    }

    public Menu getMenu() {

        return menu;
    }

    public void setMenu(Menu menu) {

        this.menu = menu;
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
}