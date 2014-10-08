package com.withub.service.std;

import com.withub.model.std.po.EntityTimeEventMonitor;
import com.withub.service.EntityService;

import java.util.List;

public interface EntityTimeEventMonitorService extends EntityService {
    public List<EntityTimeEventMonitor> queryEntityTimeEventMonitorList() throws Exception;

    public void publishEntityTimeEvent() throws Exception;
}
