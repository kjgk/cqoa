package com.withub.service.impl.oa;

import com.withub.service.oa.SmsQueueService;
import org.apache.axis2.AxisFault;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.Calendar;

@Service("smsQueueService")
public class SmsQueueServiceImpl implements SmsQueueService {

    public void messageToQueue() {
        try {
            SmsQueueWebServiceImpServiceStub smsQueueStub = new SmsQueueWebServiceImpServiceStub();

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, 1);

            SmsQueueWebServiceImpServiceStub.TSmspQueueInfo tSmspQueueInfo = new SmsQueueWebServiceImpServiceStub.TSmspQueueInfo();
            tSmspQueueInfo.setCreatorId("1470");
            tSmspQueueInfo.setCreatorName("办公室短信发送人");
            tSmspQueueInfo.setPhones("13072320098");
            tSmspQueueInfo.setContent("测试短信");
            tSmspQueueInfo.setBusinessNo("BS000004");
            tSmspQueueInfo.setFailDate(calendar);


            SmsQueueWebServiceImpServiceStub.MessageToQueue messageToQueue = new SmsQueueWebServiceImpServiceStub.MessageToQueue();

            SmsQueueWebServiceImpServiceStub.MessageToQueueE messageToQueueE = new SmsQueueWebServiceImpServiceStub.MessageToQueueE();

            messageToQueue.setArg0(tSmspQueueInfo);

            messageToQueueE.setMessageToQueue(messageToQueue);

            smsQueueStub.messageToQueue(messageToQueueE);

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


}
