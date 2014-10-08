package com.withub.model.exception;

public class BaseBusinessException extends Exception {

    private String code;

    private String message;

    public BaseBusinessException() {
        super();
    }

    public BaseBusinessException(String code, String message) {
        super();
        this.message = message;
        this.code = code;
    }

    public String toString() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
