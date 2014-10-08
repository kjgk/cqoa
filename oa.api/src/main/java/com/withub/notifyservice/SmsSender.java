package com.withub.notifyservice;


public final class SmsSender implements NotifyMessageSender {

    public NotifyMessageSendResult sendMessage(NotifyMessageInfo notifyMessageInfo) {

        System.out.println("正在发送短信......");

        NotifyMessageSendResult notifyMessageSendResult = new NotifyMessageSendResult();
        notifyMessageSendResult.setResultCode(0);
        return notifyMessageSendResult;
    }
}
