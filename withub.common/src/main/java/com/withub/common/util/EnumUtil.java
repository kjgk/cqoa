package com.withub.common.util;

import java.lang.reflect.Field;

/**
 * 枚举工具类.
 */
public final class EnumUtil {

    /**
     * 根据属性名获取枚举对象
     *
     * @param enumObject 枚举对象
     * @param fieldName  属性名
     * @return Enum
     * @throws Exception 异常
     */
    public static Enum getEnumByFieldName(Class enumObject, String fieldName) throws Exception {

        Enum retValue = null;
        Field fieldList[] = enumObject.getDeclaredFields();
        for (Field field : fieldList) {
            if (field.isEnumConstant()) {
                if (field.getName().equalsIgnoreCase(fieldName)) {
                    retValue = (Enum) field.get(enumObject);
                    break;
                }
            }
        }
        return retValue;
    }
}
