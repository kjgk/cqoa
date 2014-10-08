package com.withub.model.workflow.po;

import com.withub.model.entity.AbstractBaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "WF_RAMUS")
public class Ramus extends AbstractBaseEntity {

    //=================== 属性声明 ============================================

    private String flowTypeId;

    private String name;

    private String ramusTag;

    private String event;

    private String statusTag;

    private Integer startWorkflow = 1;

    @OneToMany(targetEntity = RamusRegulation.class, mappedBy = "ramus", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1 and parentId is null")
    private List<RamusRegulation> regulationList = new ArrayList<RamusRegulation>();

    //=================== 属性方法 ============================================

    public String getFlowTypeId() {

        return flowTypeId;
    }

    public void setFlowTypeId(String flowTypeId) {

        this.flowTypeId = flowTypeId;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getRamusTag() {

        return ramusTag;
    }

    public void setRamusTag(String ramusTag) {

        this.ramusTag = ramusTag;
    }

    public String getEvent() {

        return event;
    }

    public void setEvent(String event) {

        this.event = event;
    }

    public String getStatusTag() {

        return statusTag;
    }

    public void setStatusTag(String statusTag) {

        this.statusTag = statusTag;
    }

    public Integer getStartWorkflow() {

        return startWorkflow;
    }

    public void setStartWorkflow(Integer startWorkflow) {

        this.startWorkflow = startWorkflow;
    }

    public List<RamusRegulation> getRegulationList() {

        return regulationList;
    }

    public void setRegulationList(List<RamusRegulation> regulationList) {

        this.regulationList = regulationList;
    }
}