package com.withub.model.std.event;

import com.withub.model.std.po.Blacklist;
import com.withub.spring.SpringContextUtil;

public final class BlacklistEventPublisher {

    public static void publishBlacklistCreateEvent(Object source, Blacklist blacklist) {

        BlacklistCreateEvent event = new BlacklistCreateEvent(source, blacklist);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishBlacklistRemoveEvent(Object source, Blacklist blacklist) {

        BlacklistCreateEvent event = new BlacklistCreateEvent(source, blacklist);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }
}