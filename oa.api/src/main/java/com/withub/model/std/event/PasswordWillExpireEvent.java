package com.withub.model.std.event;

public class PasswordWillExpireEvent extends BaseEntityTimeEvent {

    public PasswordWillExpireEvent(Object source, EntityTimeEventArgs args) {

        super(source, args);
    }
}
