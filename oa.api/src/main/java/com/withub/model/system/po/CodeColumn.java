package com.withub.model.system.po;

import com.withub.model.entity.AbstractBaseEntity;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_CODECOLUMN")
public class CodeColumn extends AbstractBaseEntity {

    //=================== 属性声明 ============================================

    private String name;

    private String codeColumnTag;

    private Integer selectMode = 0;

    private Integer statusMode = 0;

    private String displayRegulation;

    @ManyToOne(targetEntity = CodeColumnCategory.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "CodeColumnCategoryId")
    @JsonIgnore
    private CodeColumnCategory codeColumnCategory;

    @OneToMany(targetEntity = Code.class, mappedBy = "codeColumn", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    @JsonIgnore
    private List<Code> codeList = new ArrayList<Code>();

    private Integer orderNo;

    private String description;

    //=================== 属性方法 ============================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getCodeColumnTag() {

        return codeColumnTag;
    }

    public void setCodeColumnTag(String codeColumnTag) {

        this.codeColumnTag = codeColumnTag;
    }

    public Integer getSelectMode() {

        return selectMode;
    }

    public void setSelectMode(Integer selectMode) {

        this.selectMode = selectMode;
    }

    public Integer getStatusMode() {

        return statusMode;
    }

    public void setStatusMode(Integer statusMode) {

        this.statusMode = statusMode;
    }

    public String getDisplayRegulation() {

        return displayRegulation;
    }

    public void setDisplayRegulation(String displayRegulation) {

        this.displayRegulation = displayRegulation;
    }

    public CodeColumnCategory getCodeColumnCategory() {

        return codeColumnCategory;
    }

    public void setCodeColumnCategory(CodeColumnCategory codeColumnCategory) {

        this.codeColumnCategory = codeColumnCategory;
    }

    public List<Code> getCodeList() {

        return codeList;
    }

    public void setCodeList(List<Code> codeList) {

        this.codeList = codeList;
    }

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }
}
