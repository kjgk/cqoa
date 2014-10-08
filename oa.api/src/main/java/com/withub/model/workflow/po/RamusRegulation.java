package com.withub.model.workflow.po;

import com.withub.model.entity.AbstractRecursionEntity;
import com.withub.model.system.po.Code;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "WF_RAMUSREGULATION")
public class RamusRegulation extends AbstractRecursionEntity {

    //=============== 属性声明 =======================================================

    @ManyToOne(targetEntity = Ramus.class)
    @JoinColumn(name = "RamusId")
    private Ramus ramus;

    private String expression;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "PropertyDataType")
    private Code propertyDataType;

    private String instanceProperty;

    private String instancePropertyValue;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "ExpressionOperation")
    private Code expressionOperation;

    @ManyToOne(targetEntity = RamusRegulation.class)
    @JoinColumn(name = "ParentId")
    private RamusRegulation parent;

    private String regulation;

    @OneToMany(targetEntity = RamusRegulation.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<RamusRegulation> childList = new ArrayList<RamusRegulation>();

    //=============== 属性方法 =======================================================

    public Ramus getRamus() {

        return ramus;
    }

    public void setRamus(Ramus ramus) {

        this.ramus = ramus;
    }

    public String getExpression() {

        return expression;
    }

    public void setExpression(String expression) {

        this.expression = expression;
    }

    public Code getPropertyDataType() {

        return propertyDataType;
    }

    public void setPropertyDataType(Code propertyDataType) {

        this.propertyDataType = propertyDataType;
    }

    public String getInstanceProperty() {

        return instanceProperty;
    }

    public void setInstanceProperty(String instanceProperty) {

        this.instanceProperty = instanceProperty;
    }

    public String getInstancePropertyValue() {

        return instancePropertyValue;
    }

    public void setInstancePropertyValue(String instancePropertyValue) {

        this.instancePropertyValue = instancePropertyValue;
    }

    public Code getExpressionOperation() {

        return expressionOperation;
    }

    public void setExpressionOperation(Code expressionOperation) {

        this.expressionOperation = expressionOperation;
    }

    public RamusRegulation getParent() {

        return parent;
    }

    public void setParent(RamusRegulation parent) {

        this.parent = parent;
    }

    public String getRegulation() {

        return regulation;
    }

    public void setRegulation(String regulation) {

        this.regulation = regulation;
    }

    public List<RamusRegulation> getChildList() {

        return childList;
    }

    public void setChildList(List<RamusRegulation> childList) {

        this.childList = childList;
    }
}