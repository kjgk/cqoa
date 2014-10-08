package com.withub.dao;

import com.withub.model.entity.AbstractEntity;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import org.hibernate.Session;

import java.util.Collection;
import java.util.List;

public interface EntityDao {

    public Session getSession();

    public void save(Object entity) throws Exception;

    public void save(AbstractEntity entity) throws Exception;

    public void update(AbstractEntity entity) throws Exception;

    public void delete(AbstractEntity entity) throws Exception;

    public void delete(Collection<? extends AbstractEntity> entityList) throws Exception;

    public AbstractEntity get(Class<? extends AbstractEntity> clazz, String objectId) throws Exception;

    public int executeHql(final String hql, Object... params) throws Exception;

    public int executeSql(final String sql, Object... params) throws Exception;

    public RecordsetInfo query(QueryInfo queryInfo) throws Exception;

    public RecordsetInfo queryDeleted(QueryInfo queryInfo) throws Exception;

    public List<AbstractEntity> list(QueryInfo queryInfo) throws Exception;

    public List listByHql(String hql, Object... params) throws Exception;

    public List listByHql(int start, int limit, String hql, Object... params) throws Exception;

    public List listBySql(String sql, Object... params) throws Exception;

    public List listBySql(int start, int limit, String sql, Object... params) throws Exception;

    public String translateHqlToSql(String hql) throws Exception;


}
