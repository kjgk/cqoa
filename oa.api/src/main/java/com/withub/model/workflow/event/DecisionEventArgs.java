package com.withub.model.workflow.event;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.User;
import org.springframework.context.ApplicationEvent;

public class DecisionEventArgs {

    private AbstractBaseEntity entityInstance;

    private Class<? extends ApplicationEvent> applicationEvent;

    private User user;

    private String opinion;

    public AbstractBaseEntity getEntityInstance() {

        return entityInstance;
    }

    public void setEntityInstance(AbstractBaseEntity entityInstance) {

        this.entityInstance = entityInstance;
    }

    public Class<? extends ApplicationEvent> getApplicationEvent() {

        return applicationEvent;
    }

    public void setApplicationEvent(Class<? extends ApplicationEvent> applicationEvent) {

        this.applicationEvent = applicationEvent;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }

    public String getOpinion() {

        return opinion;
    }

    public void setOpinion(String opinion) {

        this.opinion = opinion;
    }
}
