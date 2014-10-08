package com.withub.model.entity.query;

import java.util.ArrayList;
import java.util.List;

public final class QueryConditionNode {

    //=============================== 属性声明 ============================================================

    private String id;

    private QueryConditionNode parent = null;

    private SqlExpressionConfig sqlExpressionConfig;

    private List<QueryConditionNode> nodeList = new ArrayList<QueryConditionNode>();

    //=============================== 公共方法 ============================================================

    public void appendNode(QueryConditionNode node) {

        node.parent = this;
        nodeList.add(node);
    }

    public List<QueryConditionNode> getChildNodes() {

        return this.nodeList;
    }

    public QueryConditionNode getParent() {

        return parent;
    }

    //=============================== 属性方法 ============================================================

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public SqlExpressionConfig getSqlExpressionConfig() {

        return sqlExpressionConfig;
    }

    public void setSqlExpressionConfig(SqlExpressionConfig sqlExpressionConfig) {

        this.sqlExpressionConfig = sqlExpressionConfig;
    }
}
