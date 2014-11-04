package com.withub.model.entity;

import com.withub.model.system.po.User;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@MappedSuperclass
public abstract class AbstractBaseEntity extends AbstractEntity {

    //============================== 属性声明 ============================================================

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "Creator")
    @JsonIgnore
    private User creator;

    private Date createTime;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "LastEditor")
    @JsonIgnore
    private User lastEditor;

    private Date lastUpdateTime;

    private Integer objectVersion;

    private Integer objectStatus;

    @Transient
    @JsonIgnore
    private User currentUser;

    //============================== 属性方法 ============================================================

    public User getCreator() {

        return creator;
    }

    public void setCreator(User creator) {

        this.creator = creator;
    }

    public Date getCreateTime() {

        return createTime;
    }

    public void setCreateTime(Date createTime) {

        this.createTime = createTime;
    }

    public User getLastEditor() {

        return lastEditor;
    }

    public void setLastEditor(User lastEditor) {

        this.lastEditor = lastEditor;
    }

    public Date getLastUpdateTime() {

        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {

        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getObjectVersion() {

        return objectVersion;
    }

    public void setObjectVersion(Integer objectVersion) {

        this.objectVersion = objectVersion;
    }

    public Integer getObjectStatus() {

        return objectStatus;
    }

    public void setObjectStatus(Integer objectStatus) {

        this.objectStatus = objectStatus;
    }

    public User getCurrentUser() {

        return currentUser;
    }

    public void setCurrentUser(User currentUser) {

        this.currentUser = currentUser;
    }

    public Map getCreatorInfo() {

        Map data = new HashMap();
        if (getCreator() != null) {
            data.put("objectId", getCreator().getObjectId());
            data.put("name", getCreator().getName());
        }
        return data;
    }
}
