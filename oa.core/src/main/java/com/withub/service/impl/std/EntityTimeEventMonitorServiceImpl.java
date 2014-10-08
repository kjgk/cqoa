package com.withub.service.impl.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.ReflectionUtil;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.std.event.EntityTimeEventArgs;
import com.withub.model.std.event.EntityTimeEventPublisher;
import com.withub.model.std.po.EntityTimeEventMonitor;
import com.withub.model.system.po.Account;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.EntityTimeEventMonitorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("entityTimeEventMonitorService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class EntityTimeEventMonitorServiceImpl extends EntityServiceImpl implements EntityTimeEventMonitorService {

    public List queryEntityTimeEventMonitorList() throws Exception {

        String hql = "select o from " + EntityTimeEventMonitor.class.getName() + " o where o.enable=1";
        return listByHql(hql);
    }

    public void publishEntityTimeEvent() throws Exception {

        List<EntityTimeEventMonitor> entityTimeEventMonitorList = queryEntityTimeEventMonitorList();
        if (CollectionUtil.isEmpty(entityTimeEventMonitorList)) {
            return;
        }

        for (EntityTimeEventMonitor entityTimeEventMonitor : entityTimeEventMonitorList) {
            String eventClassName = entityTimeEventMonitor.getEventClassName();
            String eventPublishMethod = "publish" + eventClassName.substring(eventClassName.lastIndexOf(".") + 1);

//            Integer expiredEvent = entityTimeEventMonitor.getExpiredEvent(); //是否是过期事件
//
//            Integer entityTimePropertyType = entityTimeEventMonitor.getEntityTimePropertyType();//提醒的时间属性的类型
//            String entityTimeProperty = entityTimeEventMonitor.getEntityTimeProperty();//提醒的时间属性
//
//            String remindTimeValueTimeUnit = entityTimeEventMonitor.getRemindTimeValueTimeUnit().getName();//提醒时间的单位,天,时,分
//            Integer remindTimeValue = entityTimeEventMonitor.getRemindTimeValue();//提醒时间
//
//            String expiredTimeValueTimeUnit = entityTimeEventMonitor.getExpiredTimeValueTimeUnit().getName();//过期时间的单位,天,时,分
//            Integer expiredTimeValue = entityTimeEventMonitor.getExpiredTimeValue();//过期时间
//
//            Integer intervalHour = entityTimeEventMonitor.getIntervalHour();//间隔时间
//
//            Date currentDate = DateUtil.getCurrentTime();

            StringBuilder hql = new StringBuilder();
            hql.append("select o from " + Account.class.getName() + " o where 1=1 ");
//            hql.append("select o from " + entityTimeEventMonitor.getEntity().getClassName() + " o where 1=1 ");
//            if (StringUtils.isNotBlank(entityTimeProperty)) {
//                hql.append(" o." + entityTimeProperty);
//
//            }

            List entityList = listByHql(0, entityTimeEventMonitor.getRecordSetSize(), hql.toString());
            for (int i = 0; i < entityList.size(); i++) {
                EntityTimeEventArgs entityTimeEventArgse = new EntityTimeEventArgs();
                AbstractBaseEntity entityInstance = (AbstractBaseEntity) entityList.get(i);
                entityTimeEventArgse.setEntity(entityTimeEventMonitor.getEntity());
                entityTimeEventArgse.setEntityInstance(entityInstance);
                try {
                    ReflectionUtil.invokeMethod(EntityTimeEventPublisher.class.newInstance(), eventPublishMethod, new Object[]{entityTimeEventArgse});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
