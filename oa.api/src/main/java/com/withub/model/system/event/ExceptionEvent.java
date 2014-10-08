package com.withub.model.system.event;

import org.springframework.context.ApplicationEvent;

/**
 * 定义异常事件
 */
public class ExceptionEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private ExceptionInfo exceptionInfo;

    public ExceptionEvent(Object source, ExceptionInfo exceptionInfo) {

        super(source);
        this.exceptionInfo = exceptionInfo;
    }

    public ExceptionInfo getExceptionInfo() {

        return exceptionInfo;
    }

    public void setExceptionInfo(ExceptionInfo exceptionInfo) {

        this.exceptionInfo = exceptionInfo;
    }
}
