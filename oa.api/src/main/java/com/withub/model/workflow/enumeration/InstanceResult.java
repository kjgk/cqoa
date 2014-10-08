package com.withub.model.workflow.enumeration;

/**
 * 定义流程结果.
 */
public enum InstanceResult {

    /**
     * 通过
     */
    Pass,

    /**
     * 退回
     */
    Return,

    /**
     * 否决
     */
    Reject,

    /**
     * 结束
     */
    Complete,
}
