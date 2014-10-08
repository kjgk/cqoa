package com.withub.model.workflow.enumeration;

/**
 * 定义任务处理结果.
 */
public enum TaskHandleResult {

    /**
     * 启动
     */
    Start,

    /**
     * 通过
     */
    Pass,

    /**
     * 退回修改
     */
    Return,

    /**
     * 否决
     */
    Reject,

    /**
     * 提交
     */
    Submit,

    /**
     * 中止
     */
    Abort,

    /**
     * 转发
     */
    Transmit,

    /**
     * 弃权
     */
    Discard,

    /**
     * 完成
     */
    Complete,

    /**
     * 取消
     */
    Cancel,

    /**
     * 关闭
     */
    Close,

    /**
     * 挂起流程
     */
    Suspend,
}
