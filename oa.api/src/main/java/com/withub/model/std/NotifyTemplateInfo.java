package com.withub.model.std;


import java.io.Serializable;

public class NotifyTemplateInfo implements Serializable {

    private String userName;

    private String message;

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }
}
