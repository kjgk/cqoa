package com.withub.service.std;


import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.entity.event.EntityAddEvent;
import com.withub.model.entity.event.EntityLogicDeleteEvent;
import com.withub.model.entity.event.EntityUpdateEvent;
import com.withub.service.EntityService;

public interface EntityCacheService extends EntityService {

    public void refreshCache() throws Exception;

    public void refreshCache(String cacheKey) throws Exception;

    public void refreshCache(AbstractBaseEntity entityInstance) throws Exception;

    public void onEntityAddEvent(EntityAddEvent event) throws Exception;

    public void onEntityUpdateEvent(EntityUpdateEvent event) throws Exception;

    public void onEntityLogicDeleteEvent(EntityLogicDeleteEvent event) throws Exception;
}
