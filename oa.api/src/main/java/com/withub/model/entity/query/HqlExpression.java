package com.withub.model.entity.query;

public final class HqlExpression {

    private String expression;

    private HqlParameterBinder hqlParameterBinder;

    public String getExpression() {

        return expression;
    }

    public void setExpression(String expression) {

        this.expression = expression;
    }

    public HqlParameterBinder getHqlParameterBinder() {

        return hqlParameterBinder;
    }

    public void setHqlParameterBinder(HqlParameterBinder hqlParameterBinder) {

        this.hqlParameterBinder = hqlParameterBinder;
    }
}
