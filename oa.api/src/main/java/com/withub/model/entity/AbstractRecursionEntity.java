package com.withub.model.entity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractRecursionEntity extends AbstractBaseEntity {

    //============================== 属性声明 ============================================================

    private String name;

    private String pinYin;

    private String nodeTag;

    private String fullName;

    private String description;

    private Integer orderNo;

    private Integer leaf;

    private Integer nodeLevel;

    private String nodeLevelCode;

    //============================= 属性方法 =============================================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getPinYin() {

        return pinYin;
    }

    public void setPinYin(String pinYin) {

        this.pinYin = pinYin;
    }

    public String getNodeTag() {

        return nodeTag;
    }

    public void setNodeTag(String nodeTag) {

        this.nodeTag = nodeTag;
    }

    public String getFullName() {

        return fullName;
    }

    public void setFullName(String fullName) {

        this.fullName = fullName;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }

    public Integer getLeaf() {

        return leaf;
    }

    public void setLeaf(Integer leaf) {

        this.leaf = leaf;
    }

    public Integer getNodeLevel() {

        return nodeLevel;
    }

    public void setNodeLevel(Integer nodeLevel) {

        this.nodeLevel = nodeLevel;
    }

    public String getNodeLevelCode() {

        return nodeLevelCode;
    }

    public void setNodeLevelCode(String nodeLevelCode) {

        this.nodeLevelCode = nodeLevelCode;
    }
}
