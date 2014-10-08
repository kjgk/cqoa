package com.withub.model.std.event.agencyevent;

import org.springframework.context.ApplicationEvent;

public class BaseAgencyEvent extends ApplicationEvent {

    private AgencyEventArgs agencyEventArgs;

    public BaseAgencyEvent(Object source, AgencyEventArgs args) {

        super(source);
        agencyEventArgs = args;
    }

    public AgencyEventArgs getAgencyEventArgs() {

        return agencyEventArgs;
    }

    public void setAgencyEventArgs(AgencyEventArgs agencyEventArgs) {

        this.agencyEventArgs = agencyEventArgs;
    }

}
