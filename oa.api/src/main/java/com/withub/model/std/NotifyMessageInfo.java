package com.withub.model.std;

/**
 * 定义发送消息
 */
public class NotifyMessageInfo {

    //===================================属性声明====================================

    private String address;

    private String title;

    private String content;

    //==================================属性方法=====================================

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }
}
