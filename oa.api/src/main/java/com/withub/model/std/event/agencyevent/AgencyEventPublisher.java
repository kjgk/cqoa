package com.withub.model.std.event.agencyevent;


import com.withub.spring.SpringContextUtil;

public final class AgencyEventPublisher {

    public static void publishAgencyCreatedEvent(Object source, AgencyEventArgs args) {

        AgencyCreatedEvent event = new AgencyCreatedEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishAgencyFinishedEvent(Object source, AgencyEventArgs args) {

        AgencyFinishedEvent event = new AgencyFinishedEvent(source, args);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

}
