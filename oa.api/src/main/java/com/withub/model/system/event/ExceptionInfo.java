package com.withub.model.system.event;

import org.aspectj.lang.Signature;

import java.io.Serializable;
import java.util.Date;

/**
 * 定义异常信息对象
 */
public class ExceptionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    //========================= 属性方法 ==========================================

    private Class clazz;

    private Signature signature;

    private Exception exception;

    private String userId;

    private Date happenTime;

    //========================= 属性方法 ==========================================

    public Class getClazz() {

        return clazz;
    }

    public void setClazz(Class clazz) {

        this.clazz = clazz;
    }

    public Signature getSignature() {

        return signature;
    }

    public void setSignature(Signature signature) {

        this.signature = signature;
    }

    public Exception getException() {

        return exception;
    }

    public void setException(Exception exception) {

        this.exception = exception;
    }

    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }

    public Date getHappenTime() {

        return happenTime;
    }

    public void setHappenTime(Date happenTime) {

        this.happenTime = happenTime;
    }
}
