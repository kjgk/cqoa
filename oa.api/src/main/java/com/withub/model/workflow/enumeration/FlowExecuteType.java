package com.withub.model.workflow.enumeration;

/**
 * 定义流程的执行方式
 */
public enum FlowExecuteType {

    /**
     * 必须审批通过
     */
    MustApprovePass,

    /**
     * 允许否决
     */
    AllowReject,
}