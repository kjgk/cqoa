package com.withub.model.system.event;

import com.withub.model.system.po.Organization;
import org.springframework.context.ApplicationEvent;

public class OrganizationUpdateEvent extends ApplicationEvent {

    private Organization organization;

    public OrganizationUpdateEvent(Object source, Organization organization) {
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
