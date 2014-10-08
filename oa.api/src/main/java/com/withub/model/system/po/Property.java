package com.withub.model.system.po;


import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "SYS_PROPERTY")
public class Property extends AbstractBaseEntity {

    //=============================== 属性声明 ===========================================================

    @ManyToOne(targetEntity = Entity.class)
    @JoinColumn(name = "EntityId")
    private Entity entity;

    private String name;

    private String propertyName;

    private String columnName;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "propertyTypeId")
    private Code propertyType;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "propertyDataTypeId")
    private Code propertyDataType;

    private Integer dataLength;

    @OneToOne(targetEntity = Entity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "PropertEntityId")
    private Entity propertEntity;

    @OneToOne(targetEntity = CodeColumn.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "propertCodeColumnId")
    private CodeColumn propertCodeColumn;

    private Integer language;

    private Integer enableFullTextSearch;

    private String fullTextSearchAlias;

    private Integer fullTextSearchField;

    private String description;

    private Integer orderNo;

    //=============================== 属性方法 ===========================================================

    public Entity getEntity() {

        return entity;
    }

    public void setEntity(Entity entity) {

        this.entity = entity;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getPropertyName() {

        return propertyName;
    }

    public void setPropertyName(String propertyName) {

        this.propertyName = propertyName;
    }

    public String getColumnName() {

        return columnName;
    }

    public void setColumnName(String columnName) {

        this.columnName = columnName;
    }

    public Code getPropertyType() {

        return propertyType;
    }

    public void setPropertyType(Code propertyType) {

        this.propertyType = propertyType;
    }

    public Code getPropertyDataType() {

        return propertyDataType;
    }

    public void setPropertyDataType(Code propertyDataType) {

        this.propertyDataType = propertyDataType;
    }

    public Integer getDataLength() {

        return dataLength;
    }

    public void setDataLength(Integer dataLength) {

        this.dataLength = dataLength;
    }

    public Entity getPropertEntity() {

        return propertEntity;
    }

    public void setPropertEntity(Entity propertEntity) {

        this.propertEntity = propertEntity;
    }

    public CodeColumn getPropertCodeColumn() {

        return propertCodeColumn;
    }

    public void setPropertCodeColumn(CodeColumn propertCodeColumn) {

        this.propertCodeColumn = propertCodeColumn;
    }

    public Integer getLanguage() {

        return language;
    }

    public void setLanguage(Integer language) {

        this.language = language;
    }

    public Integer getEnableFullTextSearch() {

        return enableFullTextSearch;
    }

    public void setEnableFullTextSearch(Integer enableFullTextSearch) {

        this.enableFullTextSearch = enableFullTextSearch;
    }

    public String getFullTextSearchAlias() {

        return fullTextSearchAlias;
    }

    public void setFullTextSearchAlias(String fullTextSearchAlias) {

        this.fullTextSearchAlias = fullTextSearchAlias;
    }

    public Integer getFullTextSearchField() {

        return fullTextSearchField;
    }

    public void setFullTextSearchField(Integer fullTextSearchField) {

        this.fullTextSearchField = fullTextSearchField;
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