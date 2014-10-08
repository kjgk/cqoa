package com.withub.dao;


import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.common.util.ReflectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.entity.AbstractEntity;
import com.withub.model.entity.enumeration.PropertyDataType;
import com.withub.model.entity.enumeration.SqlLogicPredication;
import com.withub.model.entity.query.*;
import org.hibernate.type.DbTimestampType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import java.util.Date;
import java.util.List;

/**
 * HQL 工具类.
 */
public final class HqlUtil {

    public static QueryArgs generateQueryArgs(QueryInfo queryInfo) throws Exception {

        // 定义返回值
        QueryArgs queryArgs = new QueryArgs();

        // 定义查询语句
        String selectHql = "select o from " + queryInfo.getTargetEntity().getName() + " o where 1=1";

        // 定义统计语句
        String countHql = "select count(*) from " + queryInfo.getTargetEntity().getName() + " o where 1=1";

        // 创建一个 TargetEntity 的实例,用于后续判断
        AbstractEntity entityInstance = queryInfo.getTargetEntity().newInstance();

        // 过滤 ObjectStatus
        if (entityInstance instanceof AbstractBaseEntity) {
            selectHql += " and o.objectStatus=1";
            countHql += " and o.objectStatus=1";
        }

        // 构造排序条件
        String orderByString = "";
        if (CollectionUtil.isNotEmpty(queryInfo.getOrderByPropertyList())) {
            orderByString = " order by ";
            for (OrderByProperty orderByProperty : queryInfo.getOrderByPropertyList()) {
                orderByString += " o." + orderByProperty.getPropertyName() + " " + orderByProperty.getOrderByType() + ",";
            }
            orderByString = StringUtil.trimEnd(orderByString, ",");
        } else {
            // TODO: 使用实体定义时的默认排序方式
        }

        StringBuilder sb = new StringBuilder();
        HqlParameterBinder hqlParameterBinder = new HqlParameterBinder();

        // 构建用户查询条件
        // 注意: 用户查询条件总是形成 AND 查询, 而且只有一层
        if (CollectionUtil.isNotEmpty(queryInfo.getQueryConditionTree().getUserConditionNode().getChildNodes())) {
            StringBuilder sbUser = new StringBuilder();
            for (int i = 0; i < queryInfo.getQueryConditionTree().getUserConditionNode().getChildNodes().size(); i++) {
                QueryConditionNode node = queryInfo.getQueryConditionTree().getUserConditionNode().getChildNodes().get(i);
                if (i == 0) {
                    node.getSqlExpressionConfig().setSqlLogicPredication(SqlLogicPredication.Null);
                } else {
                    node.getSqlExpressionConfig().setSqlLogicPredication(SqlLogicPredication.And);
                }
                HqlExpression hqlExpression = generateHqlExpression(entityInstance, node.getSqlExpressionConfig());
                sbUser.append(hqlExpression.getExpression());
                for (int j = 0; j < hqlExpression.getHqlParameterBinder().getObjectValueList().size(); j++) {
                    hqlParameterBinder.appendParameter(
                            hqlExpression.getHqlParameterBinder().getObjectValueList().get(j),
                            hqlExpression.getHqlParameterBinder().getObjectValueTypeList().get(j));
                }
            }
            sb.append(" AND (" + sbUser + ")");
        }

        // 构建权限查询条件
        // 注意: 根据树的结构,认为同一级节点形成 OR 查询, 父子级节点形成 AND 查询
        if (CollectionUtil.isNotEmpty(queryInfo.getQueryConditionTree().getPermissionConditionNode().getChildNodes())) {
            StringBuilder sbPermission = new StringBuilder();
            for (int i = 0; i < queryInfo.getQueryConditionTree().getPermissionConditionNode().getChildNodes().size(); i++) {
                QueryConditionNode node = queryInfo.getQueryConditionTree().getPermissionConditionNode().getChildNodes().get(i);
                StringBuilder sbChildPermission = new StringBuilder();
                throughPermissionConditionTree(entityInstance, sbChildPermission, hqlParameterBinder, node);
                if (i == 0) {
                    sbPermission.append("(" + sbChildPermission + ")");
                } else {
                    sbPermission.append(" OR (" + sbChildPermission + ")");
                }
            }
            sb.append(" AND ((" + sbPermission + "))");
        }

        String hql = selectHql + sb.toString() + orderByString;
        queryArgs.setHql(hql);
        queryArgs.setCountHql(countHql + sb.toString());
        queryArgs.setHqlParameterBinder(hqlParameterBinder);

        return queryArgs;
    }

    private static void throughPermissionConditionTree(Object entityInstance, StringBuilder sb, HqlParameterBinder hqlParameterBinder, QueryConditionNode node) throws Exception {

        sb.append("(");

        HqlExpression hqlExpression = generateHqlExpression(entityInstance, node.getSqlExpressionConfig());
        sb.append(hqlExpression.getExpression());
        for (int i = 0; i < hqlExpression.getHqlParameterBinder().getObjectValueList().size(); i++) {
            hqlParameterBinder.appendParameter(
                    hqlExpression.getHqlParameterBinder().getObjectValueList().get(i),
                    hqlExpression.getHqlParameterBinder().getObjectValueTypeList().get(i));
        }

        if (CollectionUtil.isEmpty(node.getChildNodes())) {
            sb.append(")");
            return;
        }

        sb.append(" AND (");
        for (int i = 0; i < node.getChildNodes().size(); i++) {
            StringBuilder sbChildPermission = new StringBuilder();
            throughPermissionConditionTree(entityInstance, sbChildPermission, hqlParameterBinder, node.getChildNodes().get(i));
            if (i == 0) {
                sb.append("(" + sbChildPermission + ")");
            } else {
                sb.append(" OR (" + sbChildPermission + ")");
            }
        }
        sb.append("))");
    }

    private static HqlExpression generateHqlExpression(Object entityInstance, SqlExpressionConfig sqlExpressionConfig) throws Exception {

        HqlExpression hqlExpression = new HqlExpression();
        HqlParameterBinder hqlParameterBinder = new HqlParameterBinder();
        hqlExpression.setHqlParameterBinder(hqlParameterBinder);

        StringBuilder sb = new StringBuilder();
        if (sqlExpressionConfig.getSqlLogicPredication() != null && sqlExpressionConfig.getSqlLogicPredication() != SqlLogicPredication.Null) {
            sb.append(" " + sqlExpressionConfig.getSqlLogicPredication());
        }
        if (StringUtil.isEmpty(sqlExpressionConfig.getPropertyName())) {
            hqlExpression.setExpression(sb.toString());
            return hqlExpression;
        }

        // 判断多级属性中的第一个是否是列表类型
        int dotIndex = sqlExpressionConfig.getPropertyName().indexOf(".");
        if (dotIndex > 0) {
            String firstPropertyName = sqlExpressionConfig.getPropertyName().substring(0, dotIndex);
            String lastPropertyName = sqlExpressionConfig.getPropertyName().substring(dotIndex + 1);

            // 快速判断是否是列表类型
            if (firstPropertyName.endsWith("List")) {
                String containedClassName = ReflectionUtil.getFieldClassName(entityInstance.getClass(), firstPropertyName);
                String relatedProperty = "";
                try {
                    relatedProperty = ReflectionUtil.getFieldNameByClassName(Class.forName(containedClassName), entityInstance.getClass().getName());
                } catch (Exception e) {
                    // do nothing
                }
                sb.append(" exists( select b from " + containedClassName + " b where b." + relatedProperty + ".objectId = o.objectId");

                // TODO:只实现简单的字符串查询
                sb.append(" and lower(b." + lastPropertyName + ")");
                if (sqlExpressionConfig.getSqlExpressionOperation() == ExpressionOperation.Equals) {
                    sb.append("=?");
                    hqlParameterBinder.appendParameter(sqlExpressionConfig.getPropertyValue().toString().toLowerCase(), StringType.INSTANCE);
                } else if (sqlExpressionConfig.getSqlExpressionOperation() == ExpressionOperation.MatchBegin) {
                    sb.append(" like ?");
                    hqlParameterBinder.appendParameter(sqlExpressionConfig.getPropertyValue().toString().toLowerCase() + "%", StringType.INSTANCE);
                } else if (sqlExpressionConfig.getSqlExpressionOperation() == ExpressionOperation.MatchMiddle) {
                    sb.append(" like ?");
                    hqlParameterBinder.appendParameter("%" + sqlExpressionConfig.getPropertyValue().toString().toLowerCase() + "%", StringType.INSTANCE);
                } else if (sqlExpressionConfig.getSqlExpressionOperation() == ExpressionOperation.In || sqlExpressionConfig.getSqlExpressionOperation() == ExpressionOperation.NotIn) {
                    List<String> list = (List<String>) sqlExpressionConfig.getPropertyValue();
                    if (CollectionUtil.isNotEmpty(list)) {
                        if (sqlExpressionConfig.getSqlExpressionOperation() == ExpressionOperation.In) {
                            sb.append(" in");
                        } else {
                            sb.append(" not in");
                        }
                        sb.append("(");
                        for (int i = 0; i < list.size(); i++) {
                            if (i > 0) {
                                sb.append(",");
                            }
                            sb.append("?");
                            hqlParameterBinder.appendParameter(list.get(i).toLowerCase(), StringType.INSTANCE);
                        }
                        sb.append(")");
                    }
                }
                sb.append(")");
                hqlExpression.setExpression(sb.toString());
                return hqlExpression;
            } else {
                if (sqlExpressionConfig.getSqlExpressionOperation() == ExpressionOperation.In || sqlExpressionConfig.getSqlExpressionOperation() == ExpressionOperation.NotIn) {
                    List<String> list = (List<String>) sqlExpressionConfig.getPropertyValue();
                    if (CollectionUtil.isNotEmpty(list)) {
                        sb.append(" " + sqlExpressionConfig.getPropertyName());
                        if (sqlExpressionConfig.getSqlExpressionOperation() == ExpressionOperation.In) {
                            sb.append(" in ");
                        } else {
                            sb.append(" not in ");
                        }
                        sb.append("(");
                        for (int i = 0; i < list.size(); i++) {
                            if (i > 0) {
                                sb.append(",");
                            }
                            sb.append("?");
                            hqlParameterBinder.appendParameter(list.get(i), StringType.INSTANCE);
                        }
                        sb.append(")");
                        hqlExpression.setExpression(sb.toString());
                        return hqlExpression;
                    }
                }
            }
        }

        if (sqlExpressionConfig.getSqlExpressionOperation() != ExpressionOperation.Include) {
            if (sqlExpressionConfig.getPropertyDataType() == PropertyDataType.String && !sqlExpressionConfig.isCaseSensitive()) {
                sb.append(" (lower(" + sqlExpressionConfig.getPropertyName() + ")");
            } else {
                sb.append(" (" + sqlExpressionConfig.getPropertyName());
            }
        }
        if (sqlExpressionConfig.getPropertyDataType() == PropertyDataType.String) {
            parseStringCondition(sb, sqlExpressionConfig, hqlParameterBinder);
        } else if (sqlExpressionConfig.getPropertyDataType() == PropertyDataType.Date) {
            parseDateCondition(sb, sqlExpressionConfig, hqlParameterBinder);
        } else if (sqlExpressionConfig.getPropertyDataType() == PropertyDataType.Number) {
            parseNumberCondition(sb, sqlExpressionConfig, hqlParameterBinder);
        }

        sb.append(")");
        hqlExpression.setExpression(sb.toString());

        return hqlExpression;
    }

    private static void parseStringCondition(StringBuilder sb, SqlExpressionConfig sqlExpressionConfig, HqlParameterBinder hqlParameterBinder) {

        Object propertyValue = sqlExpressionConfig.getPropertyValue();

        // 用户字符串查询条件
        if (sqlExpressionConfig.getPropertyValue() != null && !sqlExpressionConfig.isCaseSensitive()) {
            propertyValue = sqlExpressionConfig.getPropertyValue().toString().trim().replace(" ", "").toLowerCase();
        }

        ExpressionOperation sqlExpressionOperation = sqlExpressionConfig.getSqlExpressionOperation();

        if (sqlExpressionOperation == ExpressionOperation.Equals) {
            if (propertyValue == null) {
                sb.append(" is null");
            } else {
                sb.append("=?");
                hqlParameterBinder.appendParameter(propertyValue, StringType.INSTANCE);
            }
        } else if (sqlExpressionOperation == ExpressionOperation.NotEquals) {
            if (propertyValue == null) {
                sb.append(" is not null");
            } else {
                sb.append("!=?");
                hqlParameterBinder.appendParameter(propertyValue, StringType.INSTANCE);
            }
        } else if (sqlExpressionOperation == ExpressionOperation.MatchBegin) {
            sb.append(" like ?");
            if (sqlExpressionConfig.isNodeLevelCodeQuery()) {
                if (sqlExpressionConfig.getChildDepth() > 0) {
                    hqlParameterBinder.appendParameter(propertyValue + StringUtil.duplicateString("__", sqlExpressionConfig.getChildDepth()), StringType.INSTANCE);
                    sb.append(" or " + sqlExpressionConfig.getPropertyName() + "=?");
                    hqlParameterBinder.appendParameter(propertyValue, StringType.INSTANCE);
                } else {
                    hqlParameterBinder.appendParameter(propertyValue + "%", StringType.INSTANCE);
                }
            } else {
                hqlParameterBinder.appendParameter(propertyValue + "%", StringType.INSTANCE);
            }
        } else if (sqlExpressionOperation == ExpressionOperation.MatchEnd) {
            sb.append(" like ?");
            hqlParameterBinder.appendParameter("%" + propertyValue, StringType.INSTANCE);
        } else if (sqlExpressionOperation == ExpressionOperation.MatchMiddle) {
            sb.append(" like ?");
            hqlParameterBinder.appendParameter("%" + propertyValue + "%", StringType.INSTANCE);
        } else if (sqlExpressionOperation == ExpressionOperation.In || sqlExpressionOperation == ExpressionOperation.NotIn) {
            List<String> list = (List<String>) propertyValue;
            if (CollectionUtil.isNotEmpty(list)) {
                if (sqlExpressionOperation == ExpressionOperation.In) {
                    sb.append(" in");
                } else {
                    sb.append(" not in");
                }
                sb.append("(");
                for (int i = 0; i < list.size(); i++) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    sb.append("?");
                    hqlParameterBinder.appendParameter(list.get(i), StringType.INSTANCE);
                }
                sb.append(")");
            }
        } else if (sqlExpressionOperation == ExpressionOperation.Include) {
            sb.append(" instr(?," + sqlExpressionConfig.getPropertyName() + ")=1");
            hqlParameterBinder.appendParameter(propertyValue, StringType.INSTANCE);
        } else if (sqlExpressionOperation == ExpressionOperation.InMatchBegin) {
            List<String> list = (List<String>) propertyValue;
            if (CollectionUtil.isNotEmpty(list)) {
                sb.append("(");
                for (int i = 0; i < list.size(); i++) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    sb.append(" like ?");
                    hqlParameterBinder.appendParameter(list.get(i) + "%", StringType.INSTANCE);
                }
                sb.append(")");
            }
        }
    }

    private static void parseDateCondition(StringBuilder sb, SqlExpressionConfig sqlExpressionConfig, HqlParameterBinder hqlParameterBinder) {

        Object propertyValue = sqlExpressionConfig.getPropertyValue();
        ExpressionOperation sqlExpressionOperation = sqlExpressionConfig.getSqlExpressionOperation();

        if (sqlExpressionOperation == ExpressionOperation.Equals) {
            sb.append("=?");
            hqlParameterBinder.appendParameter(propertyValue, DbTimestampType.INSTANCE);
        } else if (sqlExpressionOperation == ExpressionOperation.Between) {
            List<Date> dateList = (List<Date>) propertyValue;
            sb.append(" between ?");
            Date date = DateUtil.convertStringToDate(DateUtil.getStandardDateString(dateList.get(0)) + " 00:00:00", "yyyy-MM-dd HH:mm:ss", DateUtil.getCurrentTime());
            hqlParameterBinder.appendParameter(date, DbTimestampType.INSTANCE);
            sb.append(" and ?");
            date = DateUtil.convertStringToDate(DateUtil.getStandardDateString(dateList.get(1)) + " 23:59:59", "yyyy-MM-dd HH:mm:ss", DateUtil.getCurrentTime());
            hqlParameterBinder.appendParameter(date, DbTimestampType.INSTANCE);
        } else if (sqlExpressionOperation == ExpressionOperation.GreaterThanOrEquals) {
            sb.append(">=?");
            Date date = DateUtil.convertStringToDate(DateUtil.getStandardDateString((Date) propertyValue) + " 00:00:00", "yyyy-MM-dd HH:mm:ss", DateUtil.getCurrentTime());
            hqlParameterBinder.appendParameter(date, DbTimestampType.INSTANCE);
        } else if (sqlExpressionOperation == ExpressionOperation.GreaterThan) {
            sb.append(">?");
            Date date = DateUtil.convertStringToDate(DateUtil.getStandardDateString((Date) propertyValue) + " 00:00:00", "yyyy-MM-dd HH:mm:ss", DateUtil.getCurrentTime());
            hqlParameterBinder.appendParameter(date, DbTimestampType.INSTANCE);
        } else if (sqlExpressionOperation == ExpressionOperation.LessThanOrEquals) {
            sb.append("<=?");
            Date date = DateUtil.convertStringToDate(DateUtil.getStandardDateString((Date) propertyValue) + " 23:59:59", "yyyy-MM-dd HH:mm:ss", DateUtil.getCurrentTime());
            hqlParameterBinder.appendParameter(date, DbTimestampType.INSTANCE);
        } else if (sqlExpressionOperation == ExpressionOperation.LessThan) {
            sb.append("<?");
            Date date = DateUtil.convertStringToDate(DateUtil.getStandardDateString((Date) propertyValue) + " 23:59:59", "yyyy-MM-dd HH:mm:ss", DateUtil.getCurrentTime());
            hqlParameterBinder.appendParameter(date, DbTimestampType.INSTANCE);
        }
    }

    private static void parseNumberCondition(StringBuilder sb, SqlExpressionConfig sqlExpressionConfig, HqlParameterBinder hqlParameterBinder) {

        Object propertyValue = sqlExpressionConfig.getPropertyValue();
        ExpressionOperation sqlExpressionOperation = sqlExpressionConfig.getSqlExpressionOperation();

        if (sqlExpressionOperation == ExpressionOperation.Equals) {
            sb.append("=?");
            hqlParameterBinder.appendParameter(propertyValue, IntegerType.INSTANCE);
        } else if (sqlExpressionOperation == ExpressionOperation.NotEquals) {
            if (propertyValue == null) {
                sb.append(" is not null");
            } else {
                sb.append("!=?");
                hqlParameterBinder.appendParameter(propertyValue, IntegerType.INSTANCE);
            }
        } else if (sqlExpressionOperation == ExpressionOperation.GreaterThan) {
            sb.append(">?");
            hqlParameterBinder.appendParameter(propertyValue, IntegerType.INSTANCE);
        } else if (sqlExpressionOperation == ExpressionOperation.GreaterThanOrEquals) {
            sb.append(">=?");
            hqlParameterBinder.appendParameter(propertyValue, IntegerType.INSTANCE);
        } else if (sqlExpressionOperation == ExpressionOperation.In || sqlExpressionOperation == ExpressionOperation.NotIn) {
            List list = (List) propertyValue;
            if (CollectionUtil.isNotEmpty(list)) {
                if (sqlExpressionOperation == ExpressionOperation.In) {
                    sb.append(" in");
                } else {
                    sb.append(" not in");
                }
                sb.append("(");
                for (int i = 0; i < list.size(); i++) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    sb.append("?");
                    hqlParameterBinder.appendParameter(list.get(i), IntegerType.INSTANCE);
                }
                sb.append(")");
            }
        }
    }
}
