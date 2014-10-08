package com.withub.model.std.event;

import com.withub.model.std.po.Blacklist;
import org.springframework.context.ApplicationEvent;

public class BlacklistRemoveEvent extends ApplicationEvent {

    private Blacklist blacklist;

    public BlacklistRemoveEvent(Object source, Blacklist blacklist) {

        super(source);
        this.blacklist = blacklist;
    }

    public Blacklist getBlacklist() {

        return blacklist;
    }

    public void setBlacklist(Blacklist blacklist) {

        this.blacklist = blacklist;
    }
}
