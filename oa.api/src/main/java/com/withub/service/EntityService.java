package com.withub.service;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.entity.AbstractEntity;
import com.withub.model.entity.AbstractRecursionEntity;
import com.withub.model.entity.enumeration.EntityRowMoveType;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.system.po.Entity;
import com.withub.model.system.po.User;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface EntityService {

    //============================== 保存操作 =============================================================

    public void save(AbstractEntity entityInstance) throws Exception;

    public void update(AbstractEntity entityInstance) throws Exception;

    //============================= 删除操作 ==============================================================

    public void delete(AbstractEntity entityInstance) throws Exception;

    public void delete(Class<? extends AbstractEntity> clazz, final String objectId) throws Exception;

    public void delete(Collection<? extends AbstractEntity> entityList) throws Exception;

    public void logicDelete(AbstractBaseEntity entity) throws Exception;

    public void logicDelete(Class<? extends AbstractEntity> clazz, final String objectId) throws Exception;

    //============================= 执行操作 ==============================================================

    public int executeHql(final String hql, Object... params) throws Exception;

    public int executeSql(final String sql, Object... params) throws Exception;

    public Object executeScalar(final String hql, Object... params) throws Exception;

    public String executeStringScalar(final String hql, Object... params) throws Exception;

    public Integer executeIntegerScalar(final String hql, Object... params) throws Exception;

    public Object executeSqlScalar(final String sql, Object... params) throws Exception;

    public String executeSqlStringScalar(final String sql, Object... params) throws Exception;

    public Integer executeSqlIntegerScalar(final String sql, Object... params) throws Exception;

    public String translateHqlToSql(final String hql) throws Exception;

    //=========================== GET 操作 ===============================================================

    public <T extends AbstractEntity> T get(final Class<T> clazz, final String objectId) throws Exception;

    public AbstractEntity getEntityByClassName(final String className, final String objectId) throws Exception;

    public AbstractEntity getEntityByEntityName(final String entityName, final String objectId) throws Exception;

    public AbstractEntity getByPropertyValue(Class<? extends AbstractEntity> clazz, final String property, final String value) throws Exception;

    public AbstractEntity getByPropertyValue(Class<? extends AbstractEntity> clazz, final String property, final Integer value) throws Exception;

    public AbstractEntity getByPropertyValue(final String className, final String property, final String value) throws Exception;

    public AbstractEntity getByPropertyValue(final String className, final String property, final Integer value) throws Exception;

    public AbstractEntity getByHql(final String hql, Object... params) throws Exception;

    public <T extends AbstractRecursionEntity> T getRootEntity(Class<T> clazz) throws Exception;

    public AbstractRecursionEntity getRootEntity(final String className) throws Exception;

    //=========================== 查询操作 ================================================================

    public RecordsetInfo query(QueryInfo queryInfo) throws Exception;

    public RecordsetInfo queryDeleted(QueryInfo queryInfo) throws Exception;

    public List<AbstractEntity> list(QueryInfo queryInfo) throws Exception;

    public List<AbstractEntity> listByPropertyValue(Class<? extends AbstractEntity> clazz, final String property, final String value) throws Exception;

    public List<AbstractEntity> listByPropertyValue(final String className, final String property, final String value) throws Exception;

    public List<AbstractEntity> listByPropertyValue(Class<? extends AbstractEntity> clazz, final String property, final Integer value) throws Exception;

    public List<AbstractEntity> listByPropertyValue(final String className, final String property, final Integer value) throws Exception;

    public List<AbstractEntity> listByHql(final String hql, Object... params) throws Exception;

    public List<AbstractEntity> listByHql(final int start, final int limit, final String hql, Object... params) throws Exception;

    public List listBySql(final String sql, Object... params) throws Exception;

    public List listBySql(final int start, final int limit, final String sql, Object... params) throws Exception;

    //============================= 属性操作  =============================================================

    public Object getPropertyValue(Object object, final String property) throws Exception;

    public String getPropertyStringValue(AbstractEntity entityInstance, final String property) throws Exception;

    public Integer getPropertyIntegerValue(AbstractEntity entityInstance, final String property) throws Exception;

    public Date getPropertyDateValue(AbstractEntity entityInstance, final String property) throws Exception;

    public Object getSimplePropertyValue(Object object, final String property) throws Exception;

    public void setRecursionEntityProperty(AbstractRecursionEntity entityInstance, AbstractRecursionEntity old) throws Exception;

    public void setChildEntityProperty(AbstractRecursionEntity parentEntity) throws Exception;

    public String getNextVersion(AbstractBaseEntity entityInstance) throws Exception;

    public AbstractEntity getAbstractEntityPropertyValue(AbstractBaseEntity entityInstance, final String property) throws Exception;

    public Entity getPropertyRelatedEntity(AbstractBaseEntity entityInstance, final String property) throws Exception;

    //============================= 实用操作 ==============================================================

    public Entity getEntityById(final String entityId) throws Exception;

    public Entity getEntityByClassName(final String className) throws Exception;

    public Entity getEntityByEntityName(final String entityName) throws Exception;

    public boolean checkPropertyValueExists(final String entityName, final String property, final String value) throws Exception;

    public Integer getNextOrderNo(AbstractEntity entityInstance) throws Exception;

    public List<AbstractRecursionEntity> listByParentId(Class<? extends AbstractRecursionEntity> entity, final String parentId) throws Exception;

    public void moveEntityRow(User currentUser, final String entityName, final String objectId, final EntityRowMoveType moveType) throws Exception;

    public void moveEntityRowFirst(User currentUser, final String entityName, final String objectId) throws Exception;

    public void moveEntityRowLast(User currentUser, final String entityName, final String objectId) throws Exception;

    public void moveEntityRowUp(User currentUser, final String entityName, final String objectId) throws Exception;

    public void moveEntityRowDown(User currentUser, final String entityName, final String objectId) throws Exception;

    public void setEntityOrderNo(User currentUser, final String entityName, final String objectId, Integer orderNo) throws Exception;

    public boolean hasOrderNoProperty(final String className) throws Exception;

    public void resetEntityOrderNo(User currentUser, final String entityName) throws Exception;

    public void resetRecursionEntityOrderNo(User currentUser, final String entityName, final String parentId) throws Exception;

    public void copySimpleProperty(AbstractEntity dest, AbstractEntity source) throws Exception;

    public void setStringValueEqualsQueryCondition(QueryInfo queryInfo, final String property, final String value);

    public void setStringValueNotEqualsQueryCondition(QueryInfo queryInfo, final String property, final String value);

    public void setIntegerValueEqualsQueryCondition(QueryInfo queryInfo, final String property, final Integer value);

    public void setIntegerValueNotEqualsQueryCondition(QueryInfo queryInfo, final String property, final Integer value);

    public void setNodeLevelCodeQueryCondition(QueryInfo queryInfo, final String property, AbstractRecursionEntity entityInstance) throws Exception;

    public String quoteSqlQueryStringValue(final List dataList, final String propery) throws Exception;

    public void setPropertyValue(AbstractEntity entity, String property, Object value) throws Exception;

    public String getNamePropertyValue(AbstractEntity entityInstance) throws Exception;

    public Integer getOrderNoPropertyValue(AbstractEntity entityInstance) throws Exception;

    public boolean hasNameProperty(AbstractEntity entityInstance) throws Exception;

    public boolean hasPinYinProperty(AbstractEntity entityInstance) throws Exception;

    public boolean hasOrderNoProperty(AbstractEntity entityInstance) throws Exception;

    public boolean hasCreatorProperty(AbstractEntity entityInstance) throws Exception;

    public AbstractRecursionEntity getParent(AbstractRecursionEntity entityInstance) throws Exception;

    public String getParentObjectId(AbstractRecursionEntity entityInstance) throws Exception;

    public List<AbstractRecursionEntity> getChildList(AbstractRecursionEntity entityInstance) throws Exception;
}