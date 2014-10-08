package com.withub.service.impl.std;


import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.entity.event.EntityAddEvent;
import com.withub.model.entity.event.EntityLogicDeleteEvent;
import com.withub.model.entity.event.EntityUpdateEvent;
import com.withub.model.std.EntityDataCache;
import com.withub.model.std.po.Document;
import com.withub.model.std.po.EntityCacheConfig;
import com.withub.model.system.po.Entity;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.EntityCacheService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("entityCacheService")
public class EntityCacheServiceImpl extends EntityServiceImpl implements EntityCacheService {

    public synchronized void refreshCache() throws Exception {

        String hql = "select o from " + EntityCacheConfig.class.getName() + " o where o.objectStatus=1"
                + " and o.enable=1";
        List list = listByHql(hql);

        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        EntityDataCache.getDataMap().clear();
        for (EntityCacheConfig entityCacheConfig : (List<EntityCacheConfig>) list) {

            try {
                List entityInstanceList;
                String className = entityCacheConfig.getEntity().getClassName();
                hql = "select o from " + className + " o where 1=1";
                if (className.equals(Document.class.getName()) && entityCacheConfig.getDocumentType() != null) {
                    hql += " and o.documentType.objectId='" + entityCacheConfig.getDocumentType().getObjectId() + "'";
                }
                if (StringUtil.isNotEmpty(entityCacheConfig.getAddition())) {
                    hql += " and (" + entityCacheConfig.getAddition() + ")";
                }
                if (entityCacheConfig.getCacheCount() == 0) {
                    hql += " order by o.orderNo";
                    entityInstanceList = listByHql(hql);
                } else {
                    hql += " order by o." + entityCacheConfig.getTimestampProperty() + " desc";
                    entityInstanceList = listByHql(0, entityCacheConfig.getCacheCount() - 0, hql);
                }
                if (CollectionUtil.isNotEmpty(entityInstanceList)) {
                    EntityDataCache.getDataMap().put(entityCacheConfig.getCacheKey(), entityInstanceList);
                }
            } catch (Exception e) {
                System.out.println(entityCacheConfig.getCacheKey());
                e.printStackTrace();
            }
        }
    }

    public void refreshCache(String cacheKey) throws Exception {

        EntityDataCache.getDataMap().remove(cacheKey);
        String hql = "select o from " + EntityCacheConfig.class.getName() + " o where o.objectStatus=1"
                + " and o.enable=1 and o.cacheKey=?";
        List list = listByHql(hql, cacheKey);

        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        EntityCacheConfig entityCacheConfig = (EntityCacheConfig) list.get(0);
        try {
            List entityInstanceList;
            String className = entityCacheConfig.getEntity().getClassName();
            hql = "select o from " + className + " o where 1=1";
            if (className.equals(Document.class.getName()) && entityCacheConfig.getDocumentType() != null) {
                hql += " and o.documentType.objectId='" + entityCacheConfig.getDocumentType().getObjectId() + "'";
            }
            if (StringUtil.isNotEmpty(entityCacheConfig.getAddition())) {
                hql += " and (" + entityCacheConfig.getAddition() + ")";
            }
            if (entityCacheConfig.getCacheCount() == 0) {
                hql += " order by o.orderNo";
                entityInstanceList = listByHql(hql);
            } else {
                hql += " order by o." + entityCacheConfig.getTimestampProperty() + " desc";
                entityInstanceList = listByHql(0, entityCacheConfig.getCacheCount() - 0, hql);
            }
            if (CollectionUtil.isNotEmpty(entityInstanceList)) {
                EntityDataCache.getDataMap().put(entityCacheConfig.getCacheKey(), entityInstanceList);
            }
        } catch (Exception e) {
            System.out.println(entityCacheConfig.getCacheKey());
            e.printStackTrace();
        }
    }

    public void refreshCache(AbstractBaseEntity entityInstance) throws Exception {

        Entity entity = getEntityByEntityName(entityInstance.getClass().getSimpleName());
        if (entity == null) {
            return;
        }
        String hql = "select o from " + EntityCacheConfig.class.getName() + " o where o.objectStatus=1"
                + " and o.enable=1 and o.entity.objectId=?";
        List list = listByHql(hql, entity.getObjectId());

        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        for (EntityCacheConfig entityCacheConfig : (List<EntityCacheConfig>) list) {
            refreshCache(entityCacheConfig.getCacheKey());
        }
    }

    public void onEntityAddEvent(EntityAddEvent event) throws Exception {

        if (event.getEntityInstance() instanceof AbstractBaseEntity) {
            refreshCache((AbstractBaseEntity) event.getEntityInstance());
        }
    }

    public void onEntityUpdateEvent(EntityUpdateEvent event) throws Exception {

        if (event.getEntityInstance() instanceof AbstractBaseEntity) {
            refreshCache((AbstractBaseEntity) event.getEntityInstance());
        }
    }

    public void onEntityLogicDeleteEvent(EntityLogicDeleteEvent event) throws Exception {

        if (event.getEntityInstance() instanceof AbstractBaseEntity) {
            refreshCache((AbstractBaseEntity) event.getEntityInstance());
        }
    }
}
