package com.withub.service.std;


import com.withub.model.std.po.NotifyQueue;
import com.withub.model.std.po.NotifyQueueHistory;
import com.withub.model.std.po.SendQueue;
import com.withub.notifyservice.NotifyMessageSender;
import com.withub.service.EntityService;

public interface NotifyService extends EntityService {

    public NotifyMessageSender getNotifyMessageSender(final String notifyServiceId) throws Exception;

    public void putNotifyQueueToSendQueue() throws Exception;

    public void parseNotifyQueue(NotifyQueue notifyQueue, NotifyQueueHistory notifyQueueHistory) throws Exception;

    public void sendNotifyMessage() throws Exception;

    public void sendNotifyMessage(SendQueue sendQueue) throws Exception;

}
