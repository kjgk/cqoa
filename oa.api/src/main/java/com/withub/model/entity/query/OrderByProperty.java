package com.withub.model.entity.query;

import com.withub.model.entity.enumeration.OrderByType;

import java.io.Serializable;

public final class OrderByProperty implements Serializable {

    private String propertyName;

    private OrderByType orderByType;

    public String getPropertyName() {

        return propertyName;
    }

    public void setPropertyName(String propertyName) {

        this.propertyName = propertyName;
    }

    public OrderByType getOrderByType() {

        return orderByType;
    }

    public void setOrderByType(OrderByType orderByType) {

        this.orderByType = orderByType;
    }
}
