package com.withub.service;

import com.withub.common.util.*;
import com.withub.dao.EntityDao;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.entity.AbstractEntity;
import com.withub.model.entity.AbstractRecursionEntity;
import com.withub.model.entity.enumeration.EntityRowMoveType;
import com.withub.model.entity.enumeration.EntityType;
import com.withub.model.entity.enumeration.PropertyDataType;
import com.withub.model.entity.event.EntityEventPublisher;
import com.withub.model.entity.query.*;
import com.withub.model.exception.BaseBusinessException;
import com.withub.model.system.po.Entity;
import com.withub.model.system.po.User;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service("entityService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class EntityServiceImpl implements EntityService {

    //=============================== 属性声明 ===========================================================

    @Autowired
    private EntityDao entityDao;

    //=============================== 接口实现 ============================================================

    public void save(AbstractEntity entityInstance) throws Exception {

        if (StringUtil.isEmpty(entityInstance.getObjectId())) {
            entityInstance.setObjectId(StringUtil.getUuid());
        }

        if (entityInstance instanceof AbstractRecursionEntity) {
            String pinYin = AlphabetUtil.getAlphabetList(((AbstractRecursionEntity) entityInstance).getName());
            ((AbstractRecursionEntity) entityInstance).setPinYin(pinYin);
        } else if (hasPinYinProperty(entityInstance) && hasNameProperty(entityInstance)) {
            String pinYin = AlphabetUtil.getAlphabetList(getNamePropertyValue(entityInstance));
            setPropertyValue(entityInstance, "pinYin", pinYin);
        }

        AbstractEntity oldEntity = get(entityInstance.getClass(), entityInstance.getObjectId());
        if (entityInstance instanceof AbstractBaseEntity) {
            if (oldEntity == null) {
                setPropertyValue(entityInstance, "objectVersion", 1);
                setPropertyValue(entityInstance, "objectStatus", 1);
                if (entityInstance instanceof AbstractBaseEntity) {
                    User user = ((AbstractBaseEntity) entityInstance).getCreator();
                    if (user == null) {
                        setPropertyValue(entityInstance, "creator", ((AbstractBaseEntity) entityInstance).getCurrentUser());
                        setPropertyValue(entityInstance, "lastEditor", ((AbstractBaseEntity) entityInstance).getCurrentUser());
                    }
                }
                // 计算排序号
                if (hasOrderNoProperty(entityInstance) && getOrderNoPropertyValue(entityInstance) == null) {
                    Integer orderNo = getNextOrderNo(entityInstance);
                    setPropertyValue(entityInstance, "orderNo", orderNo);
                }
                Date currentTime = DateUtil.getCurrentTime();
                setPropertyValue(entityInstance, "createTime", currentTime);
                setPropertyValue(entityInstance, "lastUpdateTime", currentTime);
            }

            if (oldEntity != null) {
                setPropertyValue(entityInstance, "lastUpdateTime", DateUtil.getCurrentTime());
                setPropertyValue(entityInstance, "createTime", getPropertyValue(oldEntity, "createTime"));
                Integer oldObjectVersion = Integer.parseInt(getPropertyValue(oldEntity, "objectVersion").toString());
                setPropertyValue(entityInstance, "objectVersion", ++oldObjectVersion);
                setPropertyValue(entityInstance, "objectStatus", Integer.parseInt(getPropertyValue(oldEntity, "objectStatus").toString()));

                if (entityInstance instanceof AbstractBaseEntity) {
                    setPropertyValue(entityInstance, "creator", getPropertyValue(oldEntity, "creator"));
                    setPropertyValue(entityInstance, "lastEditor", ((AbstractBaseEntity) entityInstance).getCurrentUser());
                }
                if (hasOrderNoProperty(entityInstance) && getOrderNoPropertyValue(entityInstance) == null) {
                    if (!(entityInstance instanceof AbstractRecursionEntity)) {
                        Integer orderNo = getOrderNoPropertyValue(oldEntity);
                        setPropertyValue(entityInstance, "orderNo", orderNo);
                    }
                }
            }
        }

        if (entityInstance instanceof AbstractRecursionEntity) {
            if (oldEntity == null) {
                setRecursionEntityProperty((AbstractRecursionEntity) entityInstance, null);
            } else {
                setRecursionEntityProperty((AbstractRecursionEntity) entityInstance, (AbstractRecursionEntity) oldEntity);
            }
        }

        if (oldEntity == null) {
            entityDao.save(entityInstance);
            if (!entityInstance.getClass().getName().startsWith("com.withub.model.workflow.po")) {
                EntityEventPublisher.publishEntityAddEvent(this, entityInstance);
            }

        } else {
            entityDao.update(entityInstance);
            if (!entityInstance.getClass().getName().startsWith("com.withub.model.workflow.po")) {
                EntityEventPublisher.publishEntityUpdateEvent(this, oldEntity, entityInstance);
            }
        }
    }

    public void update(AbstractEntity entityInstance) throws Exception {

        entityDao.update(entityInstance);
    }

    public void delete(AbstractEntity entityInstance) throws Exception {

        if (entityInstance == null) {
            return;
        }

        if (entityInstance instanceof AbstractBaseEntity) {
            logicDelete((AbstractBaseEntity) entityInstance);
            EntityEventPublisher.publishEntityDeleteEvent(this, entityInstance);
        } else {
            entityDao.delete(entityInstance);
        }
    }

    public void delete(Class<? extends AbstractEntity> clazz, final String objectId) throws Exception {

        AbstractEntity entity = get(clazz, objectId);
        delete(entity);
    }

    public void delete(Collection<? extends AbstractEntity> entityList) throws Exception {

        if (CollectionUtil.isNotEmpty(entityList)) {
            for (AbstractEntity entity : entityList) {
                EntityEventPublisher.publishEntityDeleteEvent(this, entity);
            }
            entityDao.delete(entityList);
        }
    }

    public void logicDelete(AbstractBaseEntity entity) throws Exception {

        entity = get(entity.getClass(), entity.getObjectId());

        entity.setLastUpdateTime(DateUtil.getCurrentTime());
        entity.setObjectStatus(0);

        if (entity instanceof AbstractRecursionEntity) {
            // TODO
        }

        entityDao.update(entity);

        EntityEventPublisher.publishEntityLogicDeleteEvent(this, entity);
    }

    public void logicDelete(Class<? extends AbstractEntity> clazz, final String objectId) throws Exception {

        AbstractBaseEntity entity = (AbstractBaseEntity) get(clazz, objectId);
        logicDelete(entity);
    }

    public <T extends AbstractEntity> T get(Class<T> clazz, final String objectId) throws Exception {

        return (T) entityDao.get(clazz, objectId);
    }

    public AbstractEntity getEntityByClassName(final String className, final String objectId) throws Exception {

        Class clazz = Class.forName(className);
        return get(clazz, objectId);
    }

    public AbstractEntity getEntityByEntityName(final String entityName, final String objectId) throws Exception {

        Entity entity = (Entity) getByPropertyValue(Entity.class, "entityName", entityName);
        return getEntityByClassName(entity.getClassName(), objectId);
    }

    public AbstractEntity getByPropertyValue(Class<? extends AbstractEntity> clazz, final String property, final String value) throws Exception {

        AbstractEntity entity = null;
        List list = listByPropertyValue(clazz, property, value);
        if (CollectionUtil.isNotEmpty(list)) {
            entity = (AbstractEntity) list.get(0);
        }
        return entity;
    }

    public AbstractEntity getByPropertyValue(Class<? extends AbstractEntity> clazz, final String property, final Integer value) throws Exception {

        AbstractEntity entity = null;
        List list = listByPropertyValue(clazz, property, value);
        if (CollectionUtil.isNotEmpty(list)) {
            entity = (AbstractBaseEntity) list.get(0);
        }
        return entity;
    }

    public AbstractEntity getByPropertyValue(final String className, final String property, final String value) throws Exception {

        Class clazz = Class.forName(className);
        return getByPropertyValue(clazz, property, value);
    }

    public AbstractEntity getByPropertyValue(final String className, final String property, final Integer value) throws Exception {

        Class clazz = Class.forName(className);
        return getByPropertyValue(clazz, property, value);
    }

    public AbstractEntity getByHql(final String hql, Object... params) throws Exception {

        AbstractEntity entity = null;
        List list = entityDao.listByHql(hql, params);
        if (CollectionUtil.isNotEmpty(list)) {
            entity = (AbstractEntity) list.get(0);
        }
        return entity;
    }

    public <T extends AbstractRecursionEntity> T getRootEntity(Class<T> clazz) throws Exception {

        return (T) getRootEntity(clazz.getName());
    }

    public AbstractRecursionEntity getRootEntity(final String className) throws Exception {

        String hql = "from " + className + " where nodeLevel=1 and objectStatus=1 and parent.objectId is null";
        return (AbstractRecursionEntity) getByHql(hql);
    }

    public int executeHql(final String hql, Object... params) throws Exception {

        return entityDao.executeHql(hql, params);
    }

    public int executeSql(final String sql, Object... params) throws Exception {

        return entityDao.executeSql(sql, params);
    }

    public Object executeScalar(final String hql, Object... params) throws Exception {

        Object value = null;

        List list = listByHql(hql, params);
        if (CollectionUtil.isNotEmpty(list)) {
            value = list.get(0);
        }

        return value;
    }

    public String executeStringScalar(final String hql, Object... params) throws Exception {

        String retValue = "";
        Object value = executeScalar(hql, params);

        if (value != null) {
            retValue = value.toString();
        }

        return retValue;

    }

    public Integer executeIntegerScalar(final String hql, Object... params) throws Exception {

        Integer retValue = 0;
        Object value = executeScalar(hql, params);

        if (value != null) {
            retValue = Integer.parseInt(value.toString());
        }

        return retValue;
    }

    public Object executeSqlScalar(final String sql, Object... params) throws Exception {

        Object value = null;

        List list = listBySql(sql, params);
        if (CollectionUtil.isNotEmpty(list)) {
            value = list.get(0);
        }

        return value;
    }

    public String executeSqlStringScalar(final String sql, Object... params) throws Exception {

        String retValue = "";
        Object value = executeSqlScalar(sql, params);

        if (value != null) {
            retValue = value.toString();
        }

        return retValue;

    }

    public Integer executeSqlIntegerScalar(final String sql, Object... params) throws Exception {

        Integer retValue = null;
        Object value = executeSqlScalar(sql, params);

        if (value != null) {
            retValue = Integer.parseInt(value.toString());
        }

        return retValue;
    }

    public String translateHqlToSql(final String hql) throws Exception {

        return entityDao.translateHqlToSql(hql);
    }

    public RecordsetInfo query(QueryInfo queryInfo) throws Exception {

        RecordsetInfo recordsetInfo = entityDao.query(queryInfo);
        return recordsetInfo;
    }

    public RecordsetInfo queryDeleted(QueryInfo queryInfo) throws Exception {

        RecordsetInfo recordsetInfo = entityDao.queryDeleted(queryInfo);
        return recordsetInfo;
    }

    public List<AbstractEntity> list(QueryInfo queryInfo) throws Exception {

        List<AbstractEntity> list = entityDao.list(queryInfo);

        return list;
    }

    public List<AbstractEntity> listByPropertyValue(Class<? extends AbstractEntity> clazz, final String property, final String value) throws Exception {

        String hql = "select o from " + clazz.getName() + " o where 1=1"
                + " and lower(o." + property + ")=?";
        Object instance = Class.forName(clazz.getName()).newInstance();
        if (instance instanceof AbstractBaseEntity) {
            hql += " and o.objectStatus=1";
        }
        List list = listByHql(hql, value.trim().toLowerCase());

        return list;
    }

    public List<AbstractEntity> listByPropertyValue(final String className, final String property, final String value) throws Exception {

        Class clazz = Class.forName(className);
        return listByPropertyValue(clazz, property, value);
    }

    public List<AbstractEntity> listByPropertyValue(Class<? extends AbstractEntity> clazz, final String property, final Integer value) throws Exception {

        String hql = "select o from " + clazz.getName() + " o where 1=1"
                + " and o." + property + "=?";
        Object instance = Class.forName(clazz.getName()).newInstance();
        if (instance instanceof AbstractBaseEntity) {
            hql += " and o.objectStatus=1";
        }
        List list = listByHql(hql, value);

        return list;
    }

    public List<AbstractEntity> listByPropertyValue(final String className, final String property, final Integer value) throws Exception {

        Class clazz = Class.forName(className);
        return listByPropertyValue(clazz, property, value);
    }

    public List<AbstractEntity> listByHql(final String hql, Object... params) throws Exception {

        List list = entityDao.listByHql(hql, params);
        return (List<AbstractEntity>) list;
    }

    public List<AbstractEntity> listByHql(final int start, final int limit, final String hql, Object... params) throws Exception {

        List list = entityDao.listByHql(start, limit, hql, params);
        return (List<AbstractEntity>) list;
    }

    public List listBySql(final String sql, Object... params) throws Exception {

        List list = entityDao.listBySql(sql, params);
        return list;
    }

    public List listBySql(final int start, final int limit, final String sql, Object... params) throws Exception {

        List list = entityDao.listBySql(start, limit, sql, params);
        return list;
    }

    public Entity getEntityById(final String entityId) throws Exception {

        Entity entity = (Entity) get(Entity.class, entityId);
        return entity;
    }

    public Entity getEntityByClassName(final String className) throws Exception {

        Entity entity = (Entity) getByPropertyValue(Entity.class, "className", className);
        return entity;
    }

    public Entity getEntityByEntityName(final String entityName) throws Exception {

        Entity entity = (Entity) getByPropertyValue(Entity.class, "entityName", entityName);
        return entity;
    }

    public boolean checkPropertyValueExists(final String entityName, final String property, final String value) throws Exception {

        String hql = "select objectId, " + property + " from " + entityName + " o"
                + " where lower(o." + property + ") = ?"
                + " and o.objectStatus = 1";
        List list = listByHql(hql, value.trim().toLowerCase());
        return CollectionUtil.isNotEmpty(list);
    }

    public Integer getNextOrderNo(AbstractEntity entityInstance) throws Exception {

        Integer nextOrderNo = 1;

        if (entityInstance instanceof Entity) {
            Entity entity = (Entity) entityInstance;
            String hql = "select max(o.orderNo) from " + Entity.class.getName() + " o where 1=1"
                    + " and o.entityCategory.objectId='" + entity.getEntityCategory().getObjectId() + "'";
            Integer maxOrderNo = executeIntegerScalar(hql);
            if (maxOrderNo != null) {
                nextOrderNo = maxOrderNo + 1;
            }
            return nextOrderNo;
        }

        Entity entity = (Entity) getByPropertyValue(Entity.class, "className", entityInstance.getClass().getName());
        EntityType entityType = EntityType.valueOf(entity.getEntityType().getCodeTag());
        String parentId = "";
        if (entityType == EntityType.Recursion) {
            parentId = getParentObjectId((AbstractRecursionEntity) entityInstance);
        }

        String dependEntityObjectId = "";
        if (StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
            dependEntityObjectId = getPropertyValue(entityInstance, entity.getOrderNoDependProperty()).toString();
        }

        String hql = "select max(o.orderNo) from " + entityInstance.getClass().getName() + " o where 1=1";

        if (StringUtil.isNotEmpty(parentId)) {
            hql += " and o.parent.objectId='" + parentId + "'";
        } else {
            if (StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
                hql += " and o." + entity.getOrderNoDependProperty() + "='" + dependEntityObjectId + "'";
            }
        }

        Integer maxOrderNo = executeIntegerScalar(hql);
        if (maxOrderNo != null) {
            nextOrderNo = maxOrderNo + 1;
        }

        return nextOrderNo;
    }

    public List<AbstractRecursionEntity> listByParentId(Class<? extends AbstractRecursionEntity> entity, final String parentId) throws Exception {

        String hql = "select o from " + entity.getName() + " o where o.objectStatus=1";
        if (StringUtil.isEmpty(parentId)) {
            hql += " and o.parent.objectId is null";
        } else {
            hql += " and o.parent.objectId='" + parentId + "'";
        }
        hql += " order by o.orderNo";
        List list = listByHql(hql);

        return (List<AbstractRecursionEntity>) list;
    }

    public void moveEntityRow(User currentUser, final String entityName, final String objectId, final EntityRowMoveType moveType) throws Exception {

        if (moveType == EntityRowMoveType.First) {
            moveEntityRowFirst(currentUser, entityName, objectId);
        } else if (moveType == EntityRowMoveType.Last) {
            moveEntityRowLast(currentUser, entityName, objectId);
        } else if (moveType == EntityRowMoveType.Up) {
            moveEntityRowUp(currentUser, entityName, objectId);
        } else if (moveType == EntityRowMoveType.Down) {
            moveEntityRowDown(currentUser, entityName, objectId);
        }
    }

    public void moveEntityRowFirst(User currentUser, final String entityName, final String objectId) throws Exception {

        AbstractEntity entityInstance = getEntityByEntityName(entityName, objectId);
        Integer orderNo = getOrderNoPropertyValue(entityInstance);

        Entity entity = (Entity) getByPropertyValue(Entity.class, "className", entityInstance.getClass().getName());
        EntityType entityType = EntityType.valueOf(entity.getEntityType().getCodeTag());

        String parentId = "";
        if (entityType == EntityType.Recursion) {
            parentId = getParentObjectId((AbstractRecursionEntity) entityInstance);
        }

        String dependEntityObjectId = "";
        if (StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
            dependEntityObjectId = getPropertyValue(entityInstance, entity.getOrderNoDependProperty()).toString();
        }

        String hql = "update " + entityInstance.getClass().getName() + " o set o.orderNo=1 where o.objectId='" + objectId + "'";
        executeHql(hql);
        hql = "update " + entityInstance.getClass().getName() + " o set o.orderNo=o.orderNo+1"
                + " where o.objectId<>'" + objectId + "'"
                + " and o.orderNo<" + orderNo;
        if (StringUtil.isNotEmpty(parentId)) {
            hql += " and o.parent.objectId='" + parentId + "'";
        } else {
            if (entityType == EntityType.Recursion) {
                hql += " and o.parent.objectId is null";
            }
            if (StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
                hql += " and o." + entity.getOrderNoDependProperty() + "='" + dependEntityObjectId + "'";
            }
        }
        executeHql(hql);

        if (entityType == EntityType.Recursion) {
            resetRecursionEntityOrderNo(currentUser, entityName, parentId);
        }
    }

    public void moveEntityRowLast(User currentUser, final String entityName, final String objectId) throws Exception {

        AbstractEntity entityInstance = getEntityByEntityName(entityName, objectId);
        Integer orderNo = getOrderNoPropertyValue(entityInstance);

        Entity entity = (Entity) getByPropertyValue(Entity.class, "className", entityInstance.getClass().getName());
        EntityType entityType = EntityType.valueOf(entity.getEntityType().getCodeTag());

        String parentId = "";
        if (entityType == EntityType.Recursion) {
            parentId = getParentObjectId((AbstractRecursionEntity) entityInstance);
        }

        String dependEntityObjectId = "";
        if (StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
            dependEntityObjectId = getPropertyValue(entityInstance, entity.getOrderNoDependProperty()).toString();
        }

        String hql = "select max(orderNo) from " + entityInstance.getClass().getName() + " o where 1=1";
        if (StringUtil.isNotEmpty(parentId)) {
            hql += " and o.parent.objectId='" + parentId + "'";
        } else {
            if (entityType == EntityType.Recursion) {
                hql += " and o.parent.objectId is null";
            }
            if (StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
                hql += " and o." + entity.getOrderNoDependProperty() + "='" + dependEntityObjectId + "'";
            }
        }
        Integer maxOrderNo = (Integer) executeScalar(hql);
        hql = "update " + entityInstance.getClass().getName() + " o set o.orderNo=" + maxOrderNo + " where o.objectId='" + objectId + "'";
        executeHql(hql);

        hql = "update " + entityInstance.getClass().getName() + " o set o.orderNo=o.orderNo-1"
                + " where o.objectId<>'" + objectId + "'"
                + " and o.orderNo>" + orderNo;
        if (StringUtil.isNotEmpty(parentId)) {
            hql += " and o.parent.objectId='" + parentId + "'";
        } else {
            if (entityType == EntityType.Recursion) {
                hql += " and o.parent.objectId is null";
            }
            if (StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
                hql += " and o." + entity.getOrderNoDependProperty() + "='" + dependEntityObjectId + "'";
            }
        }
        executeHql(hql);

        if (entityType == EntityType.Recursion) {
            resetRecursionEntityOrderNo(currentUser, entityName, parentId);
        }
    }

    public void moveEntityRowUp(User currentUser, final String entityName, final String objectId) throws Exception {

        AbstractEntity entityInstance = getEntityByEntityName(entityName, objectId);
        Integer orderNo = getOrderNoPropertyValue(entityInstance);

        Entity entity = (Entity) getByPropertyValue(Entity.class, "className", entityInstance.getClass().getName());
        EntityType entityType = EntityType.valueOf(entity.getEntityType().getCodeTag());

        String parentId = "";
        if (entityType == EntityType.Recursion) {
            parentId = getParentObjectId((AbstractRecursionEntity) entityInstance);
        }

        String dependEntityObjectId = "";
        if (StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
            dependEntityObjectId = getPropertyValue(entityInstance, entity.getOrderNoDependProperty()).toString();
        }
        String hql = "select o from " + entityInstance.getClass().getName() + " o where o.orderNo<" + orderNo;
        if (entityType == EntityType.BaseData) {
            hql += " and o.objectStatus=1";
        }
        if (StringUtil.isNotEmpty(parentId)) {
            hql += " and o.parent.objectId='" + parentId + "'";
        } else {
            if (entityType == EntityType.Recursion) {
                hql += " and o.parent.objectId is null";
            }
            if (StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
                hql += " and o." + entity.getOrderNoDependProperty() + "='" + dependEntityObjectId + "'";
            }
        }
        hql += " order by o.orderNo desc";
        List list = listByHql(hql);
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        AbstractEntity previous = (AbstractEntity) list.get(0);
        hql = "update " + entityInstance.getClass().getName() + " o set o.orderNo=" + getOrderNoPropertyValue(previous) + " where o.objectId='" + objectId + "'";
        executeHql(hql);

        // 同时调整 ObjectStatus=0 的记录
        hql = "update " + entityInstance.getClass().getName() + " o set o.orderNo=o.orderNo+1"
                + " where o.objectId<>'" + objectId + "'"
                + " and o.orderNo<" + orderNo
                + " and o.orderNo>=" + getOrderNoPropertyValue(previous);
        if (StringUtil.isNotEmpty(parentId)) {
            hql += " and o.parent.objectId='" + parentId + "'";
        } else {
            if (entityType == EntityType.Recursion) {
                hql += " and o.parent.objectId is null";
            }
            if (StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
                hql += " and o." + entity.getOrderNoDependProperty() + "='" + dependEntityObjectId + "'";
            }
        }
        executeHql(hql);

        if (entityType == EntityType.Recursion) {
            resetRecursionEntityOrderNo(currentUser, entityName, parentId);
        }
    }

    public void moveEntityRowDown(User currentUser, final String entityName, final String objectId) throws Exception {

        AbstractEntity entityInstance = getEntityByEntityName(entityName, objectId);
        Integer orderNo = getOrderNoPropertyValue(entityInstance);

        Entity entity = (Entity) getByPropertyValue(Entity.class, "className", entityInstance.getClass().getName());
        EntityType entityType = EntityType.valueOf(entity.getEntityType().getCodeTag());

        String parentId = "";
        if (entityType == EntityType.Recursion) {
            parentId = getParentObjectId((AbstractRecursionEntity) entityInstance);
        }

        String dependEntityObjectId = "";
        if (StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
            dependEntityObjectId = getPropertyValue(entityInstance, entity.getOrderNoDependProperty()).toString();
        }
        String hql = "select o from " + entityInstance.getClass().getName() + " o where o.orderNo>" + orderNo;
        if (entityType == EntityType.BaseData) {
            hql += " and o.objectStatus=1";
        }
        if (StringUtil.isNotEmpty(parentId)) {
            hql += " and o.parent.objectId='" + parentId + "'";
        } else {
            if (entityType == EntityType.Recursion) {
                hql += " and o.parent.objectId is null";
            }
            if (StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
                hql += " and o." + entity.getOrderNoDependProperty() + "='" + dependEntityObjectId + "'";
            }
        }
        hql += " order by o.orderNo";
        List list = listByHql(hql);
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        AbstractEntity next = (AbstractEntity) list.get(0);
        hql = "update " + entityInstance.getClass().getName() + " o set o.orderNo=" + getOrderNoPropertyValue(next) + " where o.objectId='" + objectId + "'";
        executeHql(hql);

        // 同时调整 ObjectStatus=0 的记录
        hql = "update " + entityInstance.getClass().getName() + " o set o.orderNo=o.orderNo-1"
                + " where o.objectId<>'" + objectId + "'"
                + " and o.orderNo<=" + getOrderNoPropertyValue(next)
                + " and o.orderNo>" + orderNo;
        if (StringUtil.isNotEmpty(parentId)) {
            hql += " and o.parent.objectId='" + parentId + "'";
        } else {
            if (entityType == EntityType.Recursion) {
                hql += " and o.parent.objectId is null";
            }
            if (StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
                hql += " and o." + entity.getOrderNoDependProperty() + "='" + dependEntityObjectId + "'";
            }
        }
        executeHql(hql);

        if (entityType == EntityType.Recursion) {
            resetRecursionEntityOrderNo(currentUser, entityName, parentId);
        }
    }

    public void setEntityOrderNo(User currentUser, final String entityName, final String objectId, Integer orderNo) throws Exception {

        AbstractEntity entityInstance = getEntityByEntityName(entityName, objectId);
        Integer oldOrderNo = getOrderNoPropertyValue(entityInstance);
        if (orderNo == oldOrderNo) {
            return;
        }

        if (orderNo == 1) {
            moveEntityRowFirst(currentUser, entityName, objectId);
            return;
        }

        Entity entity = (Entity) getByPropertyValue(Entity.class, "className", entityInstance.getClass().getName());
        EntityType entityType = EntityType.valueOf(entity.getEntityType().getCodeTag());

        String parentId = "";
        if (entityType == EntityType.Recursion) {
            parentId = getParentObjectId((AbstractRecursionEntity) entityInstance);
        }

        String dependEntityObjectId = "";
        if (StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
            dependEntityObjectId = getPropertyValue(entityInstance, entity.getOrderNoDependProperty()).toString();
        }

        boolean moveUp = oldOrderNo > orderNo;
        String hql = "select o from " + entityInstance.getClass().getName() + " o where o.objectStatus=1";
        if (moveUp) {
            hql += " and o.orderNo<=" + orderNo;
        } else {
            hql += " and o.orderNo>=" + orderNo;
        }

        if (StringUtil.isNotEmpty(parentId)) {
            hql += " and o.parent.objectId='" + parentId + "'";
        } else {
            if (entityType == EntityType.Recursion) {
                hql += " and o.parent.objectId is null";
            }
            if (StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
                hql += " and o." + entity.getOrderNoDependProperty() + "='" + dependEntityObjectId + "'";
            }
        }

        if (moveUp) {
            hql += " order by o.orderNo desc";
        } else {
            hql += " order by o.orderNo";
        }
        List list = listByHql(hql);
        AbstractBaseEntity swapEntityInstance = (AbstractBaseEntity) list.get(0);
        Integer realOrderNo = getOrderNoPropertyValue(swapEntityInstance);

        if (moveUp) {
            hql = "update " + entityInstance.getClass().getName() + " o set o.orderNo=o.orderNo+1"
                    + " where o.orderNo>=" + realOrderNo + " and o.orderNo<=" + oldOrderNo
                    + " and o.objectId<>'" + entityInstance.getObjectId() + "'";
        } else {
            hql = "update " + entityInstance.getClass().getName() + " o set o.orderNo=o.orderNo-1"
                    + " where o.orderNo>=" + oldOrderNo + " and o.orderNo<=" + realOrderNo
                    + " and o.objectId<>'" + entityInstance.getObjectId() + "'";
        }
        executeHql(hql);

        hql = "update " + entityInstance.getClass().getName() + " o set o.orderNo=" + realOrderNo
                + " where o.objectId='" + objectId + "'";
        executeHql(hql);

        if (entityType == EntityType.Recursion) {
            resetRecursionEntityOrderNo(currentUser, entityName, parentId);
        }
    }

    public boolean hasOrderNoProperty(final String className) throws Exception {

        Entity entity = (Entity) getByPropertyValue(Entity.class, "className", className);
        EntityType entityType = EntityType.valueOf(entity.getEntityType().getCodeTag());

        if (entityType == EntityType.Recursion) {
            return true;
        }

        Class clazz = Class.forName(className);

        Field field = null;
        try {
            field = clazz.getDeclaredField("orderNo");
        } catch (NoSuchFieldException e) {
            // do nothing
        }
        return field != null;
    }

    public void resetEntityOrderNo(User currentUser, final String entityName) throws Exception {

        Entity entity = (Entity) getByPropertyValue(Entity.class, "entityName", entityName);
        if (!hasOrderNoProperty(entity.getClassName())) {
            return;
        }

        EntityType entityType = EntityType.valueOf(entity.getEntityType().getCodeTag());

        if (entityType != EntityType.Recursion && StringUtil.isEmpty(entity.getOrderNoDependProperty())) {
            String hql = "select o from " + entity.getClassName() + " o order by o.orderNo";
            List<AbstractEntity> list = listByHql(hql);
            if (CollectionUtil.isEmpty(list)) {
                return;
            }
            int i = 0;
            for (AbstractEntity entityInstance : list) {
                setPropertyValue(entityInstance, "orderNo", ++i);
                save(entityInstance);
            }
            return;
        }

        if (entityType != EntityType.Recursion && StringUtil.isNotEmpty(entity.getOrderNoDependProperty())) {
            String hql = "select o from " + entity.getClassName() + " o order by o." + entity.getOrderNoDependProperty() + ",o.orderNo";
            List<AbstractEntity> list = listByHql(hql);
            if (CollectionUtil.isEmpty(list)) {
                return;
            }
            int i = 0;
            String previousId = "";
            for (AbstractEntity entityInstance : list) {
                String currentId = getPropertyStringValue(entityInstance, entity.getOrderNoDependProperty());
                if (!currentId.equals(previousId)) {
                    i = 0;
                    previousId = currentId;
                }
                setPropertyValue(entityInstance, "orderNo", ++i);
                save(entityInstance);
            }
            return;
        }

        resetRecursionEntityOrderNo(currentUser, entityName, "");
    }

    public void resetRecursionEntityOrderNo(User currentUser, final String entityName, final String parentId) throws Exception {

        Entity entity = (Entity) getByPropertyValue(Entity.class, "entityName", entityName);

        String hql;
        if (StringUtil.isEmpty(parentId)) {
            hql = "select o from " + entity.getClassName() + " o where o.parent.objectId is null order by o.orderNo";
        } else {
            hql = "select o from " + entity.getClassName() + " o where o.parent.objectId='" + parentId + "' order by o.orderNo";
        }

        List list = null;
        try {
            list = listByHql(hql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        int i = 0;
        String nodeLevelCode = "01";
        for (AbstractRecursionEntity entityInstance : (List<AbstractRecursionEntity>) list) {
            String objectId = entityInstance.getObjectId();
            entityInstance.setOrderNo(++i);
            String parentNodeLevelCode;
            if (StringUtil.isEmpty(parentId)) {
                entityInstance.setNodeLevel(1);
                entityInstance.setLeaf(0);
                entityInstance.setNodeLevelCode("01");
            } else {
                AbstractRecursionEntity parentEntity = (AbstractRecursionEntity) getEntityByClassName(entity.getClassName(), parentId);
                parentNodeLevelCode = parentEntity.getNodeLevelCode();
                if (i > 1) {
                    nodeLevelCode = StringUtil.increaseAlphabet(nodeLevelCode, 2);
                }
                entityInstance.setNodeLevelCode(parentNodeLevelCode + nodeLevelCode);
                entityInstance.setNodeLevel((parentNodeLevelCode + nodeLevelCode).length() / 2);
                List childList = (List) getPropertyValue(entityInstance, "childList");
                if (CollectionUtil.isEmpty(childList)) {
                    entityInstance.setLeaf(1);
                } else {
                    entityInstance.setLeaf(0);
                }
            }
            entityInstance.setCurrentUser(currentUser);
            save(entityInstance);
            resetRecursionEntityOrderNo(currentUser, entityName, objectId);
        }
    }

    public void copySimpleProperty(AbstractEntity dest, AbstractEntity source) throws Exception {

        Field[] fieldArray = dest.getClass().getDeclaredFields();
        for (Field field : fieldArray) {
            if (field.getName().endsWith("List")) {
                // do nothing
            } else if (field.getType().getName().startsWith("com.withub")) {
                // do nothing
            } else {
                ReflectionUtil.setFieldValue(dest, field.getName(), getPropertyValue(source, field.getName()));
            }
        }
    }

    //============================ 属性操作================================================================

    public Object getPropertyValue(Object objectInstance, final String property) throws Exception {

        Object propertyValue = null;

        if (!property.contains(".")) {
            propertyValue = getSimplePropertyValue(objectInstance, property);
            return propertyValue;
        }

        String[] propertyArray = property.split("[.]");
        Object object = objectInstance;
        for (int i = 0; i < propertyArray.length; i++) {
            if (object == null) {
                break;
            }
            if (i == propertyArray.length - 1) {
                propertyValue = getSimplePropertyValue(object, propertyArray[i]);
            } else {
                object = getSimplePropertyValue(object, propertyArray[i]);
            }
        }

        return propertyValue;
    }

    public String getPropertyStringValue(AbstractEntity entityInstance, final String property) throws Exception {

        String retValue = "";

        Object value = getPropertyValue(entityInstance, property);
        if (value != null) {
            retValue = value.toString();
        }
        return retValue;
    }

    public Integer getPropertyIntegerValue(AbstractEntity entityInstance, final String property) throws Exception {

        Integer retValue = null;

        Object value = getPropertyValue(entityInstance, property);
        if (value != null) {
            retValue = Integer.parseInt(value.toString());
        }

        return retValue;
    }

    public Date getPropertyDateValue(AbstractEntity entityInstance, final String property) throws Exception {

        Date retValue = null;

        Object value = getPropertyValue(entityInstance, property);
        if (value != null) {
            retValue = (Date) value;
        }

        return retValue;
    }

    public Object getSimplePropertyValue(Object object, final String property) throws Exception {

        if (object == null) {
            return null;
        }

        Field field = ReflectionUtil.getDeclaredField(object, property);
        if (field == null) {
            return null;
        }

        ReflectionUtil.setFieldAccessible(field);
        Object propertyValue = field.get(object);

        if (propertyValue == null) {
            return null;
        }

        if (propertyValue instanceof HibernateProxy) {
            HibernateProxy proxy = (HibernateProxy) propertyValue;
            try {
                propertyValue = proxy.getHibernateLazyInitializer().getImplementation();
            } catch (Exception e) {
                throw new BaseBusinessException("", "EntityService.getSimplePropertyValue获取代理对象失败!");
            }
        } else if (propertyValue instanceof AbstractEntity) {
            AbstractEntity entityInstance = (AbstractEntity) propertyValue;
            propertyValue = get(entityInstance.getClass(), entityInstance.getObjectId());
        }

        return propertyValue;
    }

    public void setRecursionEntityProperty(AbstractRecursionEntity entity, AbstractRecursionEntity old) throws Exception {

        AbstractRecursionEntity parent = null;
        try {
            parent = getParent(entity);
        } catch (Exception e) {
            // do nothing
        }
        // NOTE: Controller的空值
        if (parent != null && StringUtil.isEmpty(parent.getObjectId())) {
            setPropertyValue(entity, "parent", null);
            parent = null;
        }
        if (parent == null) {
            if (old == null) {
                entity.setNodeLevel(1);
                entity.setLeaf(1);
                String hql = "select o from " + entity.getClass().getName() + " o where o.parent.objectId is null";
                Entity tempEntity = getEntityByClassName(entity.getClass().getName());
                if (StringUtil.isNotEmpty(tempEntity.getOrderNoDependProperty())) {
                    hql += " and o." + tempEntity.getOrderNoDependProperty() + "='" + getSimplePropertyValue(entity, tempEntity.getOrderNoDependProperty()) + "'";
                }
                hql += " order by o.orderNo desc";
                AbstractRecursionEntity last = (AbstractRecursionEntity) getByHql(hql);
                if (last == null) {
                    entity.setOrderNo(1);
                    entity.setNodeLevelCode("01");
                } else {
                    entity.setOrderNo(last.getOrderNo() + 1);
                    entity.setNodeLevelCode(StringUtil.increaseAlphabet(last.getNodeLevelCode(), 2));
                }
            } else {
                entity.setNodeLevel(1);
                entity.setOrderNo(old.getOrderNo());
                entity.setNodeLevelCode(old.getNodeLevelCode());
                entity.setLeaf(0);
                entityDao.update(entity);
            }
            return;
        }

        // 确保父节点的属性是完整的
        parent = get(entity.getClass(), parent.getObjectId());

        // 设置父节点属性
        setPropertyValue(entity, "parent", parent);

        // 设置 NodeLevel 属性
        Integer nodeLevel = parent.getNodeLevel();
        entity.setNodeLevel(nodeLevel + 1);

        // 处理新增的情况
        if (old == null) {
            // 设置 NodeLevelCode 属性
            String hql = "select max(o.nodeLevelCode) from " + entity.getClass().getName() + " o"
                    + " where o.parent.objectId=?";
            String maxNodeLevelCode = executeStringScalar(hql, parent.getObjectId());

            if (StringUtil.isEmpty(maxNodeLevelCode)) {
                entity.setNodeLevelCode(parent.getNodeLevelCode() + "01");
            } else {
                entity.setNodeLevelCode(StringUtil.increaseAlphabet(maxNodeLevelCode, 2));
            }

            // 设置 Leaf 属性
            entity.setLeaf(1);
            parent.setLeaf(0);
            entityDao.update(parent);
            return;
        }

        // 处理编辑的情况
        parent.setLeaf(0);
        entityDao.update(parent);
        entity.setLeaf(old.getLeaf());

        // 判断是否变更了父节点
        AbstractRecursionEntity oldParent = getParent(old);
        String oldParentId = oldParent.getObjectId();
        String parentId = parent.getObjectId();

        if (oldParentId.equals(parentId)) {
            entity.setNodeLevelCode(old.getNodeLevelCode());
            entity.setOrderNo(old.getOrderNo());
            entityDao.update(entity);
            return;
        }

        // 如果当前节点变更了父节点,则重新计算当前节点及所有子节点的 NodeLevelCode,NodeLevel属性
        String hql = "select max(o.nodeLevelCode) from " + entity.getClass().getName() + " o"
                + " where o.parent.objectId=?";
        String maxNodeLevelCode = executeStringScalar(hql, parent.getObjectId());
        if (StringUtil.isEmpty(maxNodeLevelCode)) {
            entity.setNodeLevelCode(parent.getNodeLevelCode() + "01");
        } else {
            entity.setNodeLevelCode(StringUtil.increaseAlphabet(maxNodeLevelCode, 2));
        }

        hql = "select max(o.orderNo) from " + entity.getClass().getName() + " o"
                + " where o.parent.objectId=?";
        Integer maxOrderNo = executeIntegerScalar(hql, parent.getObjectId());
        if (maxOrderNo == null) {
            entity.setOrderNo(1);
        } else {
            entity.setOrderNo(maxOrderNo + 1);
        }

        setChildEntityProperty(entity);

        // 设置原先的父节点的 Leaf 属性
        List list = (List) getPropertyValue(oldParent, "childList");
        if (CollectionUtil.isNotEmpty(list) && list.size() == 1) {
            oldParent.setLeaf(1);
            entityDao.update(oldParent);
        }
    }

    public void setChildEntityProperty(AbstractRecursionEntity parentEntity) throws Exception {

        // 这里不使用 ChildList 属性,保证所有子节点都得到更新
        String hql = "select o from " + parentEntity.getClass().getName() + " o"
                + " where o.parent.objectId='" + parentEntity.getObjectId() + "'"
                + " order by o.orderNo";
        List<AbstractEntity> list = listByHql(hql);
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        String parentNodeLevelCode = parentEntity.getNodeLevelCode();
        Integer parentNodeLevel = parentEntity.getNodeLevel();

        String nodeLevelCode = parentNodeLevelCode + "01";
        for (int i = 0; i < list.size(); i++) {
            AbstractRecursionEntity recursionEntity = (AbstractRecursionEntity) list.get(i);
            if (i > 0) {
                nodeLevelCode = StringUtil.increaseAlphabet(nodeLevelCode, 2);
            }
            recursionEntity.setNodeLevelCode(nodeLevelCode);
            recursionEntity.setNodeLevel(parentNodeLevel + 1);
            recursionEntity.setOrderNo(i + 1);
            entityDao.update(recursionEntity);
            setChildEntityProperty(recursionEntity);
        }
    }

    public String getNextVersion(AbstractBaseEntity entityInstance) throws Exception {

        String nextVersion = VersionUtil.getInitialVersion();
        List list = (List) getPropertyValue(entityInstance, "historyList");
        if (CollectionUtil.isNotEmpty(list)) {
            String version = getPropertyStringValue((AbstractBaseEntity) list.get(0), "version");
            nextVersion = VersionUtil.increaseVersion(version);
        }

        return nextVersion;
    }

    public AbstractEntity getAbstractEntityPropertyValue(AbstractBaseEntity entityInstance, final String property) throws Exception {

        String filedProperty = property.replace(".objectId", "");
        String className = ReflectionUtil.getDeclaredField(entityInstance, filedProperty).getType().getName();
        String objectId = getPropertyStringValue(entityInstance, property);
        AbstractEntity entity = getEntityByClassName(className, objectId);

        return entity;
    }

    public Entity getPropertyRelatedEntity(AbstractBaseEntity entityInstance, final String property) throws Exception {

        String className = ReflectionUtil.getDeclaredField(entityInstance, property).getType().getName();

        Entity entity = getEntityByClassName(className);

        return entity;
    }

    public void setStringValueEqualsQueryCondition(QueryInfo queryInfo, final String property, final String value) {

        SqlExpressionConfig sqlExpressionConfig = new SqlExpressionConfig();
        sqlExpressionConfig.setPropertyName(property);
        sqlExpressionConfig.setPropertyValue(value);
        sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.Equals);
        QueryConditionNode queryConditionNode = new QueryConditionNode();
        queryConditionNode.setSqlExpressionConfig(sqlExpressionConfig);
        queryInfo.getQueryConditionTree().getUserConditionNode().appendNode(queryConditionNode);
    }

    public void setStringValueNotEqualsQueryCondition(QueryInfo queryInfo, final String property, final String value) {

        SqlExpressionConfig sqlExpressionConfig = new SqlExpressionConfig();
        sqlExpressionConfig.setPropertyName(property);
        sqlExpressionConfig.setPropertyValue(value);
        sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.NotEquals);
        QueryConditionNode queryConditionNode = new QueryConditionNode();
        queryConditionNode.setSqlExpressionConfig(sqlExpressionConfig);
        queryInfo.getQueryConditionTree().getUserConditionNode().appendNode(queryConditionNode);
    }

    public void setIntegerValueEqualsQueryCondition(QueryInfo queryInfo, final String property, final Integer value) {

        SqlExpressionConfig sqlExpressionConfig = new SqlExpressionConfig();
        sqlExpressionConfig.setPropertyName(property);
        sqlExpressionConfig.setPropertyValue(value);
        sqlExpressionConfig.setPropertyDataType(PropertyDataType.Number);
        sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.Equals);
        QueryConditionNode queryConditionNode = new QueryConditionNode();
        queryConditionNode.setSqlExpressionConfig(sqlExpressionConfig);
        queryInfo.getQueryConditionTree().getUserConditionNode().appendNode(queryConditionNode);
    }

    public void setIntegerValueNotEqualsQueryCondition(QueryInfo queryInfo, final String property, final Integer value) {

        SqlExpressionConfig sqlExpressionConfig = new SqlExpressionConfig();
        sqlExpressionConfig.setPropertyName(property);
        sqlExpressionConfig.setPropertyValue(value);
        sqlExpressionConfig.setPropertyDataType(PropertyDataType.Number);
        sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.NotEquals);
        QueryConditionNode queryConditionNode = new QueryConditionNode();
        queryConditionNode.setSqlExpressionConfig(sqlExpressionConfig);
        queryInfo.getQueryConditionTree().getUserConditionNode().appendNode(queryConditionNode);
    }

    public void setNodeLevelCodeQueryCondition(QueryInfo queryInfo, final String property, AbstractRecursionEntity entityInstance) throws Exception {

        SqlExpressionConfig sqlExpressionConfig = new SqlExpressionConfig();
        sqlExpressionConfig.setPropertyName(property);
        sqlExpressionConfig.setPropertyValue(entityInstance.getNodeLevelCode());
        sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.MatchBegin);
        QueryConditionNode queryConditionNode = new QueryConditionNode();
        queryConditionNode.setSqlExpressionConfig(sqlExpressionConfig);
        queryInfo.getQueryConditionTree().getUserConditionNode().appendNode(queryConditionNode);
    }

    public String quoteSqlQueryStringValue(final List dataList, final String propery) throws Exception {

        if (CollectionUtil.isEmpty(dataList)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < dataList.size(); i++) {
            if (i == 0) {
                if (dataList.get(i) instanceof AbstractEntity) {
                    sb.append("'" + getPropertyStringValue((AbstractEntity) dataList.get(i), propery) + "'");
                } else {
                    sb.append("'" + ReflectionUtil.getFieldValue(dataList.get(i), propery) + "'");
                }
            } else {
                if (dataList.get(i) instanceof AbstractEntity) {
                    sb.append(",'" + getPropertyStringValue((AbstractEntity) dataList.get(i), propery) + "'");
                } else {
                    sb.append(",'" + ReflectionUtil.getFieldValue(dataList.get(i), propery) + "'");
                }
            }
        }

        return sb.toString();
    }

    public void setPropertyValue(AbstractEntity entity, String property, Object value) throws Exception {

        ReflectionUtil.setFieldValue(entity, property, value);
    }

    public String getNamePropertyValue(AbstractEntity entityInstance) throws Exception {

        return getPropertyStringValue(entityInstance, "name");
    }

    public Integer getOrderNoPropertyValue(AbstractEntity entityInstance) throws Exception {

        return getPropertyIntegerValue(entityInstance, "orderNo");
    }

    public boolean hasNameProperty(AbstractEntity entityInstance) throws Exception {

        if (entityInstance instanceof AbstractRecursionEntity) {
            return true;
        }

        Field field = null;
        try {
            field = entityInstance.getClass().getDeclaredField("name");
        } catch (NoSuchFieldException e) {
            // do nothing
        }
        return field != null;
    }

    public boolean hasPinYinProperty(AbstractEntity entityInstance) throws Exception {

        Field field = null;
        try {
            field = entityInstance.getClass().getDeclaredField("pinYin");
        } catch (NoSuchFieldException e) {
            // do nothing
        }
        return field != null;
    }

    public boolean hasPinYinProperty(final String className) throws Exception {

        Entity entity = (Entity) getByPropertyValue(Entity.class, "className", className);

        Class clazz = Class.forName(className);

        Field field = null;
        try {
            field = clazz.getDeclaredField("pinYin");
        } catch (NoSuchFieldException e) {
            // do nothing
        }
        return field != null;
    }

    public boolean hasOrderNoProperty(AbstractEntity entityInstance) throws Exception {

        if (entityInstance instanceof AbstractRecursionEntity) {
            return true;
        }

        Field field = ReflectionUtil.getDeclaredField(entityInstance, "orderNo");

        return field != null;
    }

    public boolean hasCreatorProperty(AbstractEntity entityInstance) throws Exception {


        Field field = null;
        try {
            field = entityInstance.getClass().getDeclaredField("creator");
        } catch (NoSuchFieldException e) {
            // do nothing
        }
        return field != null;
    }

    public AbstractRecursionEntity getParent(AbstractRecursionEntity entityInstance) throws Exception {

        if (entityInstance == null) {
            return null;
        }
        Object parent = ReflectionUtil.getFieldValue(entityInstance, "parent");
        if (parent == null) {
            return null;
        }
        String parentId = ReflectionUtil.getFieldValue(parent, "objectId").toString();
        return get(entityInstance.getClass(), parentId);
    }


    public String getParentObjectId(AbstractRecursionEntity entityInstance) throws Exception {

        String objectId = "";
        AbstractRecursionEntity parent = getParent(entityInstance);
        if (parent != null) {
            objectId = parent.getObjectId();
        }
        return objectId;
    }

    public List<AbstractRecursionEntity> getChildList(AbstractRecursionEntity entityInstance) throws Exception {

        List<AbstractRecursionEntity> childList = (List<AbstractRecursionEntity>) getPropertyValue(entityInstance, "childList");
        return childList;
    }

    //=============================== 属性方法 ============================================================

    public EntityDao getEntityDao() {

        return entityDao;
    }

    public void setEntityDao(EntityDao entityDao) {

        this.entityDao = entityDao;
    }
}