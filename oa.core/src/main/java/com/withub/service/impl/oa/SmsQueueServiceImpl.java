package com.withub.service.impl.oa;

import com.withub.service.oa.SmsQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("smsQueueService")
public class SmsQueueServiceImpl implements SmsQueueService {

    @Value(value = "${sms.creatorId}")
    private String creatorId = "";

    @Value(value = "${sms.creatorName}")
    private String creatorName = "";

    @Value(value = "${sms.businessNo}")
    private String businessNo = "";

    @Value(value = "${sms.retryCount}")
    private Integer retryCount;

    private static final Logger logger = LoggerFactory.getLogger(SmsQueueServiceImpl.class);

    private ExecutorService threadPool;

    public SmsQueueServiceImpl() {

        this.threadPool = Executors.newCachedThreadPool();
    }

    public void messageToQueue(String phones, String content) {

        logger.info("发送短信，手机号：" + phones + "，内容：" + content);

        this.threadPool.submit(new SendMessage(phones, content));
    }

    class SendMessage implements Callable {

        private String phones;

        private String content;

        SendMessage(String phones, String content) {
            this.phones = phones;
            this.content = content;
        }

        public Integer call() throws Exception {

            for (int i = 0; i < retryCount; i++) {
                try {
                    SmsQueueWebServiceImpServiceStub smsQueueStub = new SmsQueueWebServiceImpServiceStub();
                    SmsQueueWebServiceImpServiceStub.TSmspQueueInfo tSmspQueueInfo = new SmsQueueWebServiceImpServiceStub.TSmspQueueInfo();
                    Calendar failDate = Calendar.getInstance();
                    failDate.add(Calendar.HOUR, 1);
                    tSmspQueueInfo.setCreatorId(creatorId);
                    tSmspQueueInfo.setCreatorName(creatorName);
                    tSmspQueueInfo.setBusinessNo(businessNo);
                    tSmspQueueInfo.setFailDate(failDate);
                    tSmspQueueInfo.setPhones(phones);
                    tSmspQueueInfo.setContent(content);

                    SmsQueueWebServiceImpServiceStub.MessageToQueue messageToQueue = new SmsQueueWebServiceImpServiceStub.MessageToQueue();
                    SmsQueueWebServiceImpServiceStub.MessageToQueueE messageToQueueE = new SmsQueueWebServiceImpServiceStub.MessageToQueueE();
                    messageToQueue.setArg0(tSmspQueueInfo);
                    messageToQueueE.setMessageToQueue(messageToQueue);
                    smsQueueStub.messageToQueue(messageToQueueE);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                return 1;
            }
            return 0;
        }
    }
}
