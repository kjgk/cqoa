package com.withub.dao;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.AbstractEntity;
import com.withub.model.entity.query.QueryArgs;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;

import java.util.Collection;
import java.util.List;

public class EntityDaoImpl implements EntityDao {

    private SessionFactory sessionFactory;

    public Session getSession() {

        return sessionFactory.getCurrentSession();
    }

    public void save(Object entity) throws Exception {

        this.getSession().saveOrUpdate(entity);
    }

    public void save(AbstractEntity entity) throws Exception {

        this.getSession().saveOrUpdate(entity);
    }

    public void update(AbstractEntity entity) throws Exception {

        this.getSession().merge(entity);
    }

    public void delete(AbstractEntity entity) throws Exception {

        this.getSession().delete(entity);
    }

    public void delete(Collection<? extends AbstractEntity> entityList) throws Exception {

        if (CollectionUtil.isNotEmpty(entityList)) {
            for (AbstractEntity entity : entityList) {
                this.getSession().delete(entity);
            }
        }
    }

    public AbstractEntity get(Class<? extends AbstractEntity> clazz, String objectId) throws Exception {

        AbstractEntity entityIntance = null;

        if (StringUtil.isNotEmpty(objectId)) {
            entityIntance = (AbstractEntity) this.getSession().get(clazz, objectId);
        }

        return entityIntance;
    }

    public int executeHql(final String hql, Object... params) throws Exception {

        Session session = this.getSession();
        Query query = session.createQuery(hql);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i, params[i]);
        }
        return query.executeUpdate();
    }

    public int executeSql(final String sql, Object... params) throws Exception {

        Session session = this.getSession();
        Query query = session.createSQLQuery(sql);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i, params[i]);
        }
        return query.executeUpdate();
    }

    public RecordsetInfo query(QueryInfo queryInfo) throws Exception {

        RecordsetInfo recordsetInfo = new RecordsetInfo();
        recordsetInfo.setRecordsetSize(queryInfo.getRecordsetSize());
        recordsetInfo.setRecordsetIndex(queryInfo.getRecordsetIndex());

        QueryArgs queryArgs = HqlUtil.generateQueryArgs(queryInfo);

        // 查询记录总数
        Query countQuery = getSession().createQuery(queryArgs.getCountHql());
        queryArgs.getHqlParameterBinder().bindParameters(countQuery);
        int totalCount = Integer.parseInt(countQuery.uniqueResult().toString());
        recordsetInfo.setTotalRecordCount(totalCount);

        String hql = queryArgs.getHql();

        if (totalCount > 0) {
            Query query = getSession().createQuery(hql);
            queryArgs.getHqlParameterBinder().bindParameters(query);
            query.setFirstResult(recordsetInfo.getRecordsetIndex() * recordsetInfo.getRecordsetSize());
            query.setMaxResults(recordsetInfo.getRecordsetSize());
            List<AbstractEntity> entityList = query.list();

            recordsetInfo.setTotalRecordsetCount((int) Math.ceil((totalCount + 0.0) / recordsetInfo.getRecordsetSize()));
            recordsetInfo.setEntityList(entityList);
        }

        return recordsetInfo;
    }

    public RecordsetInfo queryDeleted(QueryInfo queryInfo) throws Exception {

        RecordsetInfo recordsetInfo = new RecordsetInfo();
        recordsetInfo.setRecordsetSize(queryInfo.getRecordsetSize());
        recordsetInfo.setRecordsetIndex(queryInfo.getRecordsetIndex());

        QueryArgs queryArgs = HqlUtilForDeleted.generateQueryArgs(queryInfo);

        // 查询记录总数
        Query countQuery = getSession().createQuery(queryArgs.getCountHql());
        queryArgs.getHqlParameterBinder().bindParameters(countQuery);
        int totalCount = Integer.parseInt(countQuery.uniqueResult().toString());
        recordsetInfo.setTotalRecordCount(totalCount);

        String hql = queryArgs.getHql();

        if (totalCount > 0) {
            Query query = getSession().createQuery(hql);
            queryArgs.getHqlParameterBinder().bindParameters(query);
            query.setFirstResult(recordsetInfo.getRecordsetIndex() * recordsetInfo.getRecordsetSize());
            query.setMaxResults(recordsetInfo.getRecordsetSize());
            List<AbstractEntity> entityList = query.list();

            recordsetInfo.setTotalRecordsetCount((int) Math.ceil((totalCount + 0.0) / recordsetInfo.getRecordsetSize()));
            recordsetInfo.setEntityList(entityList);
        }

        return recordsetInfo;
    }

    public List<AbstractEntity> list(QueryInfo queryInfo) throws Exception {

        QueryArgs queryArgs = HqlUtil.generateQueryArgs(queryInfo);

        Query query = getSession().createQuery(queryArgs.getHql());
        queryArgs.getHqlParameterBinder().bindParameters(query);

        if (queryInfo.getTopRowCount() > 0) {
            query.setMaxResults(queryInfo.getTopRowCount());
            query.setFirstResult(0);
        }

        List<AbstractEntity> list = query.list();

        return list;
    }

    public List<AbstractEntity> listByHql(String hql, Object... params) throws Exception {

        Query query = getSession().createQuery(hql);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i, params[i]);
        }
        List list = query.list();
        return (List<AbstractEntity>) list;
    }

    public List listByHql(int start, int limit, String hql, Object... params) throws Exception {

        Query query = getSession().createQuery(hql);
        query.setFirstResult(start);
        query.setMaxResults(limit);
        for (int i = 0; i < params.length; i++) {
            Object object = params[i];
            query.setParameter(i, object);
        }
        return query.list();
    }

    public List listBySql(String sql, Object... params) throws Exception {

        Session session = this.getSession();
        Query query = session.createSQLQuery(sql);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i, params[i]);
        }
        return query.list();
    }

    public List listBySql(int start, int limit, String sql, Object... params) throws Exception {

        Session session = this.getSession();
        Query query = session.createSQLQuery(sql);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i, params[i]);
        }
        query.setFirstResult(start);
        query.setMaxResults(limit);
        return query.list();
    }

    public String translateHqlToSql(String hql) throws Exception {

        QueryTranslatorImpl queryTranslator = new QueryTranslatorImpl(hql, hql, java.util.Collections.EMPTY_MAP,
                (org.hibernate.engine.spi.SessionFactoryImplementor) getSessionFactory());

        queryTranslator.compile(java.util.Collections.EMPTY_MAP, false);

        return queryTranslator.getSQLString();
    }

    public SessionFactory getSessionFactory() {

        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }
}
