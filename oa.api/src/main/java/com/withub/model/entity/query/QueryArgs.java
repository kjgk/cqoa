package com.withub.model.entity.query;

/**
 * 查询参数
 */
public final class QueryArgs {

    //================ 属性定义  =====================================

    /**
     * HQL 语句
     */
    private String hql;

    /**
     * 计数 HQL 语句
     */
    private String countHql;

    /**
     * HQL 语句参数占位符绑定器
     */
    private HqlParameterBinder hqlParameterBinder = new HqlParameterBinder();

    //================== 属性方法 ===================================

    public String getHql() {
        return hql;
    }

    public void setHql(String hql) {
        this.hql = hql;
    }

    public String getCountHql() {
        return countHql;
    }

    public void setCountHql(String countHql) {
        this.countHql = countHql;
    }

    public HqlParameterBinder getHqlParameterBinder() {
        return hqlParameterBinder;
    }

    public void setHqlParameterBinder(HqlParameterBinder hqlParameterBinder) {
        this.hqlParameterBinder = hqlParameterBinder;
    }
}
