package com.withub.service.oa;


public interface SmsQueueService {

    public void messageToQueue(String phones, String content);
}
