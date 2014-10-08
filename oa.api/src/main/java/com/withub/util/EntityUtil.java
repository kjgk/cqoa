package com.withub.util;

import com.withub.common.util.ReflectionUtil;
import com.withub.model.entity.AbstractEntity;
import com.withub.model.entity.AbstractRecursionEntity;
import org.hibernate.proxy.HibernateProxy;

import java.lang.reflect.Field;
import java.util.List;

public final class EntityUtil {

    public static String getObjectId(AbstractEntity entityInstance) throws Exception {

        String objectId = "";
        Object value = ReflectionUtil.getFieldValue(entityInstance, "objectId");
        if (value != null) {
            objectId = (String) value;
        }
        return objectId;
    }

    public static String getName(AbstractEntity entityInstance) throws Exception {

        String objectId = "";
        Object value = ReflectionUtil.getFieldValue(entityInstance, "name");
        if (value != null) {
            objectId = (String) value;
        }
        return objectId;
    }

    public static Integer getOrdreNo(AbstractEntity entityInstance) throws Exception {

        Integer orderNo = null;
        Object object = entityInstance;
        if (object instanceof HibernateProxy) {
            HibernateProxy proxy = (HibernateProxy) object;
            try {
                object = proxy.getHibernateLazyInitializer().getImplementation();
            } catch (Exception e) {
                // do nothing
            }
        }

        Object value = ReflectionUtil.getFieldValue(object, "orderNo");
        if (value != null) {
            orderNo = (Integer) value;
        }
        return orderNo;
    }

    public static boolean hasOrderNoProperty(AbstractEntity entityInstance) {

        if (entityInstance instanceof AbstractRecursionEntity) {
            return true;
        }

        Field field = null;
        try {
            field = entityInstance.getClass().getDeclaredField("orderNo");
        } catch (NoSuchFieldException e) {
            // do nothing
        }
        return field != null;
    }

    public static boolean hasNameProperty(AbstractEntity entityInstance) {

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

    public static boolean hasPinYinProperty(AbstractEntity entityInstance) {

        Field field = null;
        try {
            field = entityInstance.getClass().getDeclaredField("pinYin");
        } catch (NoSuchFieldException e) {
            // do nothing
        }
        return field != null;
    }

    public static boolean hasCreatorProperty(AbstractEntity entityInstance) {

        Field field = null;
        try {
            field = entityInstance.getClass().getDeclaredField("creator");
        } catch (NoSuchFieldException e) {
            // do nothing
        }
        return field != null;
    }

    public static Integer getNodeLevel(AbstractRecursionEntity entityInstance) throws Exception {

        Integer nodeLevel = (Integer) getPropertyValue(entityInstance, "nodeLevel");
        return nodeLevel;
    }

    public static AbstractRecursionEntity getParent(AbstractRecursionEntity entityInstance) throws Exception {

        AbstractRecursionEntity parent = null;
        try {
            parent = (AbstractRecursionEntity) EntityUtil.getSimplePropertyValue(entityInstance, "parent");
        } catch (Exception e) {
            // do nothing
        }

        return parent;
    }

    public static List<AbstractRecursionEntity> getChildList(AbstractRecursionEntity entityInstance) throws Exception {

        List<AbstractRecursionEntity> childList = (List<AbstractRecursionEntity>) EntityUtil.getPropertyValue(entityInstance, "childList");
        return childList;
    }

    public static String getParentObjectId(AbstractRecursionEntity entityInstance) throws Exception {

        AbstractRecursionEntity parent = getParent(entityInstance);
        String objectId = getObjectId(parent);

        return objectId;
    }

    public static Object getPropertyValue(Object object, String property) throws Exception {

        Object propertyValue = null;

        if (!property.contains(".")) {
            propertyValue = getSimplePropertyValue(object, property);
            return propertyValue;
        }

        String[] propertyArray = property.split("[.]");
        Object tempObject = object;
        for (int i = 0; i < propertyArray.length; i++) {
            if (tempObject == null) {
                break;
            }
            if (i == propertyArray.length - 1) {
                propertyValue = getSimplePropertyValue(tempObject, propertyArray[i]);
            } else {
                tempObject = getSimplePropertyValue(tempObject, propertyArray[i]);
            }
        }

        return propertyValue;
    }

    public static void setPropertyValue(AbstractEntity entity, String property, Object value) throws Exception {

        ReflectionUtil.setFieldValue(entity, property, value);
    }

    private static Object getSimplePropertyValue(Object object, String property) throws Exception {

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
                // do nothing
            }
        }

        return propertyValue;
    }
}
