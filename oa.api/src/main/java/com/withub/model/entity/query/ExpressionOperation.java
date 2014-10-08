package com.withub.model.entity.query;

/**
 * 定义表达式操作符.
 */
public enum ExpressionOperation {

    /**
     * 等于
     */
    Equals,

    /**
     * 不等于
     */
    NotEquals,

    /**
     * 小于
     */
    LessThan,

    /**
     * 小于等于
     */
    LessThanOrEquals,

    /**
     * 大于
     */
    GreaterThan,

    /**
     * 大于等于
     */
    GreaterThanOrEquals,

    /**
     * 介于
     * 使用 List 数据类型
     */
    Between,

    /**
     * 在...中
     * 使用 List 数据类型
     */
    In,

    /**
     * 不在...中
     * 使用 List 数据类型
     */
    NotIn,

    /**
     * 从开始处向后匹配
     */
    MatchBegin,

    /**
     * 从结束处向前匹配
     */
    MatchEnd,

    /**
     * 匹配中间部分
     */
    MatchMiddle,

    /**
     * 包含
     */
    Include,

    /**
     * 在... 从开始处向后匹配中
     * 使用 List 数据类型
     */
    InMatchBegin,
}
