package com.withub.service.system;

import com.withub.model.system.po.Organization;


public interface OrganizationService extends com.withub.service.EntityService {

    public Organization getRootOrganization() throws Exception;

    public Organization getOrganizationByCode(String code) throws Exception;

    public void addOrganization(Organization organization) throws Exception;

    public void updateOrganization(Organization organization) throws Exception;
}