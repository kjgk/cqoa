package com.withub.model.system.event;

import com.withub.model.system.po.User;
import org.springframework.context.ApplicationEvent;

public class UserDeleteEvent extends ApplicationEvent {

    private User user;

    public UserDeleteEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
