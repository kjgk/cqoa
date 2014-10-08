package com.withub.service.std;


import com.withub.model.std.po.EventNotifyServiceType;
import com.withub.model.system.po.Code;
import com.withub.service.EntityService;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public interface SystemEventService extends EntityService {

    /**
     * 事件处理
     *
     * @param event 事件
     * @throws Exception 异常
     */
    public void onEvent(ApplicationEvent event) throws Exception;

    public List<Code> getEventNotifyServiceTypeList() throws Exception;

    public EventNotifyServiceType loadEventNotifyServiceType(String notifyServiceTypeId, String systemEventId) throws Exception;
}
