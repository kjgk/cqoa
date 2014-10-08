package com.withub.model.workflow.po;

import com.withub.model.entity.AbstractEntity;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "WF_SUBINSTANCE")
public class SubInstance extends AbstractEntity {

    @ManyToOne(targetEntity = Instance.class)
    @JoinColumn(name = "instanceId")
    private Instance instance;

    @OneToMany(targetEntity = MasterTask.class, mappedBy = "subInstance", fetch = FetchType.LAZY)
    @OrderBy(value = "createTime asc")
    private List<MasterTask> masterTaskList = new ArrayList<MasterTask>();

    private Integer rank;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "creator")
    private User creator;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "status")
    private Code status;

    private Date createTime;

    private Date finishTime;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "result")
    private Code result;

    //=================== 属性方法 ============================================

    public Instance getInstance() {

        return instance;
    }

    public void setInstance(Instance instance) {

        this.instance = instance;
    }

    public List<MasterTask> getMasterTaskList() {

        return masterTaskList;
    }

    public void setMasterTaskList(List<MasterTask> masterTaskList) {

        this.masterTaskList = masterTaskList;
    }

    public Integer getRank() {

        return rank;
    }

    public void setRank(Integer rank) {

        this.rank = rank;
    }

    public User getCreator() {

        return creator;
    }

    public void setCreator(User creator) {

        this.creator = creator;
    }

    public Code getStatus() {

        return status;
    }

    public void setStatus(Code status) {

        this.status = status;
    }

    public Date getCreateTime() {

        return createTime;
    }

    public void setCreateTime(Date createTime) {

        this.createTime = createTime;
    }

    public Date getFinishTime() {

        return finishTime;
    }

    public void setFinishTime(Date finishTime) {

        this.finishTime = finishTime;
    }

    public Code getResult() {

        return result;
    }

    public void setResult(Code result) {

        this.result = result;
    }
}