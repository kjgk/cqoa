package com.withub.model.entity.query;

import com.withub.model.entity.AbstractEntity;
import com.withub.model.entity.enumeration.OrderByType;
import com.withub.model.entity.enumeration.PropertyDataType;
import com.withub.model.system.po.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定义实体查询信息类
 * 主要应用于用户视图层,实现数据的分页与动态权限控制
 */
public final class QueryInfo implements Serializable {

    //================================ 属性声明 ===========================================================

    private User currentUser;

    private String clientLoginType;

    private String clientIdentityCode;

    /**
     * 业务层方法,由 AOP 注入
     */
    private String serviceMethod;

    /**
     * 要查询的实体类
     */
    private Class<? extends AbstractEntity> targetEntity;

    /**
     * 要查询的实体属性列表
     * TODO 权限层实现动态过滤
     */
    private List<String> propertyList = new ArrayList<String>();

    /**
     * 实体查询的排序方式
     * 实体应该定义包含一个或者多个属性的默认排序方式
     */
    private List<OrderByProperty> orderByPropertyList = new ArrayList<OrderByProperty>();

    /**
     * 取前多少条数据
     */
    private Integer topRowCount = 0;

    /**
     * 记录集大小
     */
    private Integer recordsetSize = 0;

    /**
     * 记录集索引
     */
    private Integer recordsetIndex = 0;

    /**
     * 查询条件树
     */
    private QueryConditionTree queryConditionTree = new QueryConditionTree();

    //================================ 方法 ===========================================================

    public QueryInfo addCondition(String propertyName, Object propertyValue, ExpressionOperation operation) {

        SqlExpressionConfig sqlExpressionConfig = new SqlExpressionConfig();
        sqlExpressionConfig.setPropertyName(propertyName);
        sqlExpressionConfig.setPropertyValue(propertyValue);
        sqlExpressionConfig.setSqlExpressionOperation(operation);
        if (propertyValue instanceof Integer) {
            sqlExpressionConfig.setPropertyDataType(PropertyDataType.Number);
        }
        if (propertyValue instanceof Date) {
            sqlExpressionConfig.setPropertyDataType(PropertyDataType.Date);
        }
        if (propertyValue instanceof List) {
            List list = (List) propertyValue;
            Object objectValue = list.get(0);
            if (objectValue instanceof Integer) {
                sqlExpressionConfig.setPropertyDataType(PropertyDataType.Number);
            }
            if (objectValue instanceof Date) {
                sqlExpressionConfig.setPropertyDataType(PropertyDataType.Date);
            }
        }
        QueryConditionNode queryConditionNode = new QueryConditionNode();
        queryConditionNode.setSqlExpressionConfig(sqlExpressionConfig);
        this.getQueryConditionTree().getUserConditionNode().appendNode(queryConditionNode);

        return this;
    }

    public QueryInfo setAscOrderBy(String property) {

        OrderByProperty orderByProperty = new OrderByProperty();
        orderByProperty.setPropertyName(property);
        orderByProperty.setOrderByType(OrderByType.Asc);
        this.getOrderByPropertyList().add(orderByProperty);
        return this;
    }

    public QueryInfo setDescOrderBy(String property) {

        OrderByProperty orderByProperty = new OrderByProperty();
        orderByProperty.setPropertyName(property);
        orderByProperty.setOrderByType(OrderByType.Desc);
        this.getOrderByPropertyList().add(orderByProperty);
        return this;
    }

    //================================ 属性方法 ===========================================================

    public User getCurrentUser() {

        return currentUser;
    }

    public void setCurrentUser(User currentUser) {

        this.currentUser = currentUser;
    }

    public String getClientLoginType() {

        return clientLoginType;
    }

    public void setClientLoginType(String clientLoginType) {

        this.clientLoginType = clientLoginType;
    }

    public String getClientIdentityCode() {

        return clientIdentityCode;
    }

    public void setClientIdentityCode(String clientIdentityCode) {

        this.clientIdentityCode = clientIdentityCode;
    }

    public String getServiceMethod() {

        return serviceMethod;
    }

    public void setServiceMethod(String serviceMethod) {

        this.serviceMethod = serviceMethod;
    }

    public Class<? extends AbstractEntity> getTargetEntity() {

        return targetEntity;
    }

    public void setTargetEntity(Class<? extends AbstractEntity> targetEntity) {

        this.targetEntity = targetEntity;
    }

    public List<String> getPropertyList() {

        return propertyList;
    }

    public void setPropertyList(List<String> propertyList) {

        this.propertyList = propertyList;
    }

    public List<OrderByProperty> getOrderByPropertyList() {

        return orderByPropertyList;
    }

    public void setOrderByPropertyList(List<OrderByProperty> orderByPropertyList) {

        this.orderByPropertyList = orderByPropertyList;
    }

    public Integer getTopRowCount() {

        return topRowCount;
    }

    public void setTopRowCount(Integer topRowCount) {

        this.topRowCount = topRowCount;
    }

    public Integer getRecordsetSize() {

        return recordsetSize;
    }

    public void setRecordsetSize(Integer recordsetSize) {

        this.recordsetSize = recordsetSize;
    }

    public Integer getRecordsetIndex() {

        return recordsetIndex;
    }

    public void setRecordsetIndex(Integer recordsetIndex) {

        this.recordsetIndex = recordsetIndex;
    }

    public QueryConditionTree getQueryConditionTree() {

        return queryConditionTree;
    }

    public void setQueryConditionTree(QueryConditionTree queryConditionTree) {

        this.queryConditionTree = queryConditionTree;
    }
}