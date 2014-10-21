package com.withub.model.workflow.po;

import com.withub.model.entity.AbstractEntity;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "WF_TASK")
public class Task extends AbstractEntity {

    //======================== 属性声明 ==================================================================

    @ManyToOne(targetEntity = MasterTask.class)
    @JoinColumn(name = "masterTaskId")
    private MasterTask masterTask;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "owner")
    private User owner;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "handler")
    private User handler;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "status")
    private Code status;

    private Date arriveTime;

    private Integer sourceType = 1;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "Deliver")
    private User deliver;

    private Integer expiration = 0;

    private Date expiredTime;

    private Integer timeout = 0;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "result")
    private Code result;

    private Long timeMillis;

    private Date createTime;

    private Date finishTime;

    private Integer locked = 0;

    @OneToOne(targetEntity = TaskOpinion.class, mappedBy = "task")
    private TaskOpinion taskOpinion;

    @OneToMany(targetEntity = TaskContext.class, mappedBy = "task", fetch = FetchType.LAZY)
    private List<TaskContext> contextList;

    //======================== 属性方法 ==================================================================

    public MasterTask getMasterTask() {

        return masterTask;
    }

    public void setMasterTask(MasterTask masterTask) {

        this.masterTask = masterTask;
    }

    public User getOwner() {

        return owner;
    }

    public void setOwner(User owner) {

        this.owner = owner;
    }

    public User getHandler() {

        return handler;
    }

    public void setHandler(User handler) {

        this.handler = handler;
    }

    public Code getStatus() {

        return status;
    }

    public void setStatus(Code status) {

        this.status = status;
    }

    public Date getArriveTime() {

        return arriveTime;
    }

    public void setArriveTime(Date arriveTime) {

        this.arriveTime = arriveTime;
    }

    public Integer getSourceType() {

        return sourceType;
    }

    public void setSourceType(Integer sourceType) {

        this.sourceType = sourceType;
    }

    public User getDeliver() {

        return deliver;
    }

    public void setDeliver(User deliver) {

        this.deliver = deliver;
    }

    public Integer getExpiration() {

        return expiration;
    }

    public void setExpiration(Integer expiration) {

        this.expiration = expiration;
    }

    public Date getExpiredTime() {

        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {

        this.expiredTime = expiredTime;
    }

    public Integer getTimeout() {

        return timeout;
    }

    public void setTimeout(Integer timeout) {

        this.timeout = timeout;
    }

    public Code getResult() {

        return result;
    }

    public void setResult(Code result) {

        this.result = result;
    }

    public Date getCreateTime() {

        return createTime;
    }

    public void setCreateTime(Date createTime) {

        this.createTime = createTime;
    }

    public Long getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(Long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public Date getFinishTime() {

        return finishTime;
    }

    public void setFinishTime(Date finishTime) {

        this.finishTime = finishTime;
    }

    public Integer getLocked() {

        return locked;
    }

    public void setLocked(Integer locked) {

        this.locked = locked;
    }

    public TaskOpinion getTaskOpinion() {

        return taskOpinion;
    }

    public void setTaskOpinion(TaskOpinion taskOpinion) {

        this.taskOpinion = taskOpinion;
    }

    public List<TaskContext> getContextList() {

        return contextList;
    }

    public void setContextList(List<TaskContext> contextList) {

        this.contextList = contextList;
    }
}
