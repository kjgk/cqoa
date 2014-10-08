package com.withub.model.std;

public final class NotifyQueueSendResult {

    //================================ 属性声明 ==========================================================

    /**
     * 1-成功
     * 2-待后续确认
     * 0-失败
     */
    private Integer resultCode = -1;

    private String queueNo;

    private String message;

    //================================ 属性方法 ==========================================================

    public Integer getResultCode() {

        return resultCode;
    }

    public void setResultCode(Integer resultCode) {

        this.resultCode = resultCode;
    }

    public String getQueueNo() {

        return queueNo;
    }

    public void setQueueNo(String queueNo) {

        this.queueNo = queueNo;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }
}
