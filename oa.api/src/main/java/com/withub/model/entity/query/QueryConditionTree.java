package com.withub.model.entity.query;

import java.io.Serializable;

public final class QueryConditionTree implements Serializable {

    //=============================== 属性声明 ============================================================

    // 用户查询条件
    private QueryConditionNode userConditionNode;

    // 权限过滤条件
    private QueryConditionNode permissionConditionNode;

    //=============================== 公共方法 ============================================================

    public QueryConditionTree() {

        userConditionNode = new QueryConditionNode();
        userConditionNode.setId("User");

        permissionConditionNode = new QueryConditionNode();
        permissionConditionNode.setId("Permission");
    }

    public QueryConditionNode getUserConditionNode() {

        return userConditionNode;
    }

    public QueryConditionNode getPermissionConditionNode() {

        return permissionConditionNode;
    }
}