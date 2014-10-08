package com.withub.model.entity.query;

import com.withub.model.entity.enumeration.PropertyDataType;
import com.withub.model.entity.enumeration.SqlLogicPredication;

import java.io.Serializable;

public final class SqlExpressionConfig implements Serializable {

    //=============================== 属性声明 ============================================================

    // 属性名
    private String propertyName = "";

    // 属性的数据类型
    private PropertyDataType propertyDataType = PropertyDataType.String;

    // SQL 逻辑前导符
    private SqlLogicPredication sqlLogicPredication;

    // 属性名与属性值的 SQL 操作符
    private ExpressionOperation sqlExpressionOperation;

    // 属性值
    private Object propertyValue;

    // 字符串是否大小写敏感
    private boolean caseSensitive = true;

    private boolean nodeLevelCodeQuery = false;

    private Integer childDepth = 1;

    //=============================== 属性方法 ============================================================

    public String getPropertyName() {

        return propertyName;
    }

    public void setPropertyName(String propertyName) {

        this.propertyName = propertyName;
    }

    public PropertyDataType getPropertyDataType() {

        return propertyDataType;
    }

    public void setPropertyDataType(PropertyDataType propertyDataType) {

        this.propertyDataType = propertyDataType;
    }

    public SqlLogicPredication getSqlLogicPredication() {

        return sqlLogicPredication;
    }

    public void setSqlLogicPredication(SqlLogicPredication sqlLogicPredication) {

        this.sqlLogicPredication = sqlLogicPredication;
    }

    public ExpressionOperation getSqlExpressionOperation() {

        return sqlExpressionOperation;
    }

    public void setSqlExpressionOperation(ExpressionOperation sqlExpressionOperation) {

        this.sqlExpressionOperation = sqlExpressionOperation;
    }

    public Object getPropertyValue() {

        return propertyValue;
    }

    public void setPropertyValue(Object propertyValue) {

        this.propertyValue = propertyValue;
    }

    public boolean isCaseSensitive() {

        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {

        this.caseSensitive = caseSensitive;
    }

    public boolean isNodeLevelCodeQuery() {

        return nodeLevelCodeQuery;
    }

    public void setNodeLevelCodeQuery(boolean nodeLevelCodeQuery) {

        this.nodeLevelCodeQuery = nodeLevelCodeQuery;
    }

    public Integer getChildDepth() {

        return childDepth;
    }

    public void setChildDepth(Integer childDepth) {

        this.childDepth = childDepth;
    }
}
