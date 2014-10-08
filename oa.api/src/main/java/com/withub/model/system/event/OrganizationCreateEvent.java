package com.withub.model.system.event;

import com.withub.model.system.po.Organization;
import org.springframework.context.ApplicationEvent;

public class OrganizationCreateEvent extends ApplicationEvent {

    private Organization organization;

    public OrganizationCreateEvent(Object source, Organization organization) {
        super(source);
        this.organization = organization;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
