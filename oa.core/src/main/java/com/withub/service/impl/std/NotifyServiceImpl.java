package com.withub.service.impl.std;

import com.withub.common.util.*;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.entity.AbstractEntity;
import com.withub.model.std.NotifyTemplateInfo;
import com.withub.model.std.po.*;
import com.withub.model.system.po.User;
import com.withub.notifyservice.NotifyMessageInfo;
import com.withub.notifyservice.NotifyMessageSendResult;
import com.withub.notifyservice.NotifyMessageSender;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.NotifyService;
import com.withub.spring.SpringContextUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("notifyService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class NotifyServiceImpl extends EntityServiceImpl implements NotifyService {

    private List queryNotifyQueue(int recordsetSize) throws Exception {

        String hql = "select o from " + NotifyQueue.class.getName() + " o order by o.createTime ";
        return listByHql(0, recordsetSize, hql);
    }

    public void putNotifyQueueToSendQueue() throws Exception {

        int recordsetSize = 10;
        List<NotifyQueue> notifyQueueList = queryNotifyQueue(recordsetSize);
        if (CollectionUtil.isEmpty(notifyQueueList)) {
            return;
        }
        for (NotifyQueue notifyQueue : notifyQueueList) {

            NotifyQueueHistory notifyQueueHistory = putNotifyQueueToHistory(notifyQueue);//移动到通知队列历史列表里
            parseNotifyQueue(notifyQueue, notifyQueueHistory); //保存到发送队列
        }
        delete(notifyQueueList);//删除通知队列
    }

    private NotifyQueueHistory putNotifyQueueToHistory(NotifyQueue notifyQueue) throws Exception {

        NotifyQueueHistory notifyQueueHistory = new NotifyQueueHistory();
        notifyQueueHistory.setObjectId(notifyQueue.getObjectId());
        notifyQueueHistory.setEntity(notifyQueue.getEntity());
        notifyQueueHistory.setEntityInstanceId(notifyQueue.getEntityInstanceId());
        notifyQueueHistory.setSystemEvent(notifyQueue.getSystemEvent());
        notifyQueueHistory.setSerializableValue(notifyQueue.getSerializableValue());
        notifyQueueHistory.setCreateTime(notifyQueue.getCreateTime());
        notifyQueueHistory.setCreator(notifyQueue.getCreator());
        notifyQueueHistory.setLastEditor(notifyQueue.getLastEditor());
        save(notifyQueueHistory);
        return notifyQueueHistory;
    }

    public void parseNotifyQueue(NotifyQueue notifyQueue, NotifyQueueHistory notifyQueueHistory) throws Exception {

        String hql = "select o from " + EventNotifyServiceType.class.getName() + " o where systemEvent.objectId = ? and enable = ? order by orderNo";
        List<EventNotifyServiceType> eventnotifyServiceTypeList = (List) listByHql(hql, notifyQueue.getSystemEvent().getObjectId(), 1);//多个通知类型，邮件，短信，或者移动终端

        if (StringUtil.isNotEmpty(notifyQueue.getSerializableValue())) {

        } else {
            for (EventNotifyServiceType eventNotifyServiceType : eventnotifyServiceTypeList) {
                SendQueue sendQueue = new SendQueue();
                AbstractBaseEntity abstractBaseEntity = (AbstractBaseEntity) getEntityByClassName(notifyQueue.getEntity().getClassName(), notifyQueue.getEntityInstanceId());
                String userProperty = notifyQueue.getSystemEvent().getUserProperty();
                User user = (User) ReflectionUtil.getFieldValue(abstractBaseEntity, userProperty);
                sendQueue.setUser(user);
                sendQueue.setNotifyQueue(notifyQueueHistory);
                NotifyTemplateInfo notifyTemplate = new NotifyTemplateInfo();
                notifyTemplate.setUserName(user.getName());
                notifyTemplate.setMessage(notifyQueue.getSystemEvent().getName());
                sendQueue.setEnterTime(DateUtil.getCurrentTime());
                sendQueue.setLastSendTime(DateUtil.getCurrentTime());
                sendQueue.setSendTimes(0);
                NotifyServiceType notifyServiceType = eventNotifyServiceType.getNotifyServiceType();
                sendQueue.setNotifyServiceType(notifyServiceType);
                String titleTemplate = VelocityUtil.getVelocityContent(eventNotifyServiceType.getTitleTemplate(), "notifyTemplate", notifyTemplate);
                sendQueue.setTitle(titleTemplate);
                String contentTemplate = VelocityUtil.getVelocityContent(eventNotifyServiceType.getContentTemplate(), "notifyTemplate", notifyTemplate);
                sendQueue.setContent(contentTemplate);
                sendQueue.setAddress(notifyServiceType.getDescription());
                save(sendQueue);
            }
        }
    }


    public NotifyMessageSender getNotifyMessageSender(final String notifyServiceId) throws Exception {

        Map<String, String> eventNotifyServiceTypeConfig = (Map<String, String>) SpringContextUtil.getInstance().getBean("eventNotifyServiceTypeConfig");

        NotifyMessageSender sender = null;
        if (eventNotifyServiceTypeConfig.get(notifyServiceId) != null) {
            sender = (NotifyMessageSender) SpringContextUtil.getInstance().getBean(eventNotifyServiceTypeConfig.get(notifyServiceId));
        }

        return sender;
    }

    public void sendNotifyMessage() throws Exception {

        String hql = "select o from " + SendQueue.class.getName() + " o where 1=1 order by o.enterTime";
        List<AbstractEntity> list = listByHql(hql);
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        int start = 0;
        int limit = 5;
        int total = list.size();
        int times = total % limit == 0 ? total / limit : total / limit + 1;
        for (int i = 0; i < times; i++) {
            List list1 = listByHql(start, limit, hql);
            for (SendQueue sendQueue : (List<SendQueue>) list1) {
                sendNotifyMessage(sendQueue);
            }
            start += limit;
        }
    }

    public void sendNotifyMessage(SendQueue sendQueue) throws Exception {

        NotifyMessageInfo notifyMessageInfo = new NotifyMessageInfo();
        notifyMessageInfo.setAddress(sendQueue.getAddress());
        notifyMessageInfo.setTitle(sendQueue.getTitle());
        notifyMessageInfo.setContent(sendQueue.getContent());

        NotifyMessageSender messageSender = getNotifyMessageSender(sendQueue.getNotifyServiceType().getNotifyServiceTypeTag());
        if (messageSender != null) {
            NotifyMessageSendResult notifyMessageSendResult = messageSender.sendMessage(notifyMessageInfo);
            if (notifyMessageSendResult.getResultCode() == 1) {
                putSendQueueToHistory(sendQueue);  //移动到发送队列历史列表里
            } else {
                //Todo
            }
        } else {
            System.out.println("不存在" + sendQueue.getNotifyServiceType().getName() + "的发送服务！");
        }
    }

    private void putSendQueueToHistory(SendQueue sendQueue) throws Exception {

        SendQueueHistory sendQueueHistory = new SendQueueHistory();
        sendQueueHistory.setObjectId(sendQueue.getObjectId());
        sendQueueHistory.setNotifyQueue(sendQueue.getNotifyQueue());
        sendQueueHistory.setNotifyServiceType(sendQueue.getNotifyServiceType());
        sendQueueHistory.setUser(sendQueue.getUser());
        sendQueueHistory.setAddress(sendQueue.getAddress());
        sendQueueHistory.setTitle(sendQueue.getTitle());
        sendQueueHistory.setContent(sendQueue.getContent());
        sendQueueHistory.setEnterTime(sendQueue.getEnterTime());
        sendQueueHistory.setLastSendTime(DateUtil.getCurrentTime());
        sendQueueHistory.setSendTimes(sendQueue.getSendTimes() + 1);

        save(sendQueueHistory);
        delete(sendQueue);
    }
}
