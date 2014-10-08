package com.withub.model.std.event;

import com.withub.spring.SpringContextUtil;

public final class EntityTimeEventPublisher {

    public EntityTimeEventPublisher() {

    }

    public void publishPasswordWillExpireEvent(EntityTimeEventArgs args) {

        PasswordWillExpireEvent event = new PasswordWillExpireEvent(this, args);

        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

}