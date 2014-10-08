package com.withub.service.impl.std;


import com.withub.model.std.event.BaseEntityTimeEvent;
import com.withub.model.std.po.EventNotifyServiceType;
import com.withub.model.std.po.NotifyQueue;
import com.withub.model.std.po.SystemEvent;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.Entity;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.SystemEventService;
import com.withub.service.system.CodeService;
import com.withub.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("systemEventService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class SystemEventServiceImpl extends EntityServiceImpl implements SystemEventService {

    //==============================属性声明==============================================
    @Autowired
    private UserService userService;

    @Autowired
    private CodeService codeService;

    //=============================接口方法==============================================
    @Override
    public void onEvent(ApplicationEvent event) throws Exception {

        String className = event.getClass().getName();
        SystemEvent systemEvent = (SystemEvent) getByPropertyValue(SystemEvent.class, "className", className);
        if (systemEvent == null) {
            return;
        }
        if (systemEvent.getEnableNotify() == 0) {
            return;
        }

        NotifyQueue notifyQueue = new NotifyQueue();
        notifyQueue.setSystemEvent(systemEvent);
        if (event instanceof BaseEntityTimeEvent) {
            Entity entity = ((BaseEntityTimeEvent) event).getEntityTimeEventArgs().getEntity();
            notifyQueue.setEntity(entity);

            String entityInstanceId = ((BaseEntityTimeEvent) event).getEntityTimeEventArgs().getEntityInstance().getObjectId();
            notifyQueue.setEntityInstanceId(entityInstanceId);
        } else {
            //Todo
            return;
        }

        notifyQueue.setCurrentUser(userService.getSystemUser());
        save(notifyQueue);
    }


    public List<Code> getEventNotifyServiceTypeList() throws Exception {

        return codeService.getCodeColumnByTag("NotifyServiceType").getCodeList();
    }

    @Override
    public EventNotifyServiceType loadEventNotifyServiceType(String notifyServiceTypeId, String systemEventId) throws Exception {

        String hql = "select o from " + EventNotifyServiceType.class.getName() + " o where  notifyServiceType.objectId =?" +
                " and  systemEvent.objectId =?";

        return (EventNotifyServiceType) getByHql(hql, notifyServiceTypeId, systemEventId);
    }

    //==================================属性方法==========================================

    public UserService getUserService() {

        return userService;
    }

    public void setUserService(UserService userService) {

        this.userService = userService;
    }

    public CodeService getCodeService() {
        return codeService;
    }

    public void setCodeService(CodeService codeService) {
        this.codeService = codeService;
    }
}
