package com.withub.model.workflow.enumeration;

/**
 * 定义工作流节点执行人的取人方式
 */
public enum HandlerFetchType {

    /**
     * 随机
     */
    Random,

    /**
     * 最空闲(当前任务最少)
     */
    IdleMost,

    /**
     * 同类任务最少
     */
    TaskLeast,
}
