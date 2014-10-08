package com.withub.model.std.event.agencyevent;

public class AgencyFinishedEvent extends BaseAgencyEvent {

    private static final long serialVersionUID = 1L;

    public AgencyFinishedEvent(Object source, AgencyEventArgs args) {

        super(source, args);

    }
}
