package com.withub.model.workflow.enumeration;

/**
 * 定义流程节点类型.
 */
public enum FlowNodeType {

    /**
     * 起始节点
     */
    Begin,

    /**
     * 第一个节点
     */
    First,

    /**
     * 审批节点
     */
    AndSign,

    /**
     * 修改节点
     */
    Modify,

    /**
     * 提交
     */
    Submit,

    /**
     * 通知节点
     */
    Notify,

    /**
     * 转发节点
     */
    Transmit,

    /**
     * 投票节点
     * 必须实现投票规则
     */
    Vote,

    /**
     * 完成
     */
    Finish,

    /**
     * 结束节点
     */
    End,
}
