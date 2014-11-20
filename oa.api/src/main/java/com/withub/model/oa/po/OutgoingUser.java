package com.withub.model.oa.po;

import com.withub.model.entity.AbstractEntity;
import com.withub.model.system.po.User;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "OA_OUTGOINGUSER")
public class OutgoingUser extends AbstractEntity {

    //================================ 属性声明 ==========================================================

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(targetEntity = Outgoing.class)
    @JoinColumn(name = "outGoingId")
    @JsonIgnore
    private Outgoing outGoing;

    //================================ 属性方法 ==========================================================

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Outgoing getOutgoing() {
        return outGoing;
    }

    public void setOutgoing(Outgoing outGoing) {
        this.outGoing = outGoing;
    }

}

