package com.withub.model.entity.query;

import org.hibernate.Query;
import org.hibernate.type.Type;

import java.util.LinkedList;
import java.util.List;

public final class HqlParameterBinder {

    /**
     * 查询参数值
     */
    private List<Object> objectValueList = new LinkedList<Object>();

    /**
     * 查询参数数据类型
     */
    private List<Type> objectValueTypeList = new LinkedList<Type>();

    /**
     * 添加查询参数
     *
     * @param objectValue
     * @param type
     */
    public void appendParameter(Object objectValue, Type type) {

        objectValueList.add(objectValue);
        objectValueTypeList.add(type);
    }

    /**
     * 将查询参数列表绑定到 Query 对象
     *
     * @param query
     */
    public void bindParameters(Query query) {

        for (int i = 0; i < objectValueList.size(); i++) {
            query.setParameter(i, objectValueList.get(i), objectValueTypeList.get(i));
        }
    }

    public List<Object> getObjectValueList() {

        return objectValueList;
    }

    public List<Type> getObjectValueTypeList() {

        return objectValueTypeList;
    }
}
