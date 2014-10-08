package com.withub.model.system.event;

import com.withub.model.system.po.Organization;
import org.springframework.context.ApplicationEvent;

public class OrganizationMergEvent extends ApplicationEvent {

    private Organization sourceOrganization;

    private Organization targetOrganization;

    public OrganizationMergEvent(Object source, Organization sourceOrganization, Organization targetOrganization) {
        super(source);
        this.sourceOrganization = sourceOrganization;
        this.targetOrganization = targetOrganization;
    }

    public Organization getSourceOrganization() {
        return sourceOrganization;
    }

    public Organization getTargetOrganization() {
        return targetOrganization;
    }
}
