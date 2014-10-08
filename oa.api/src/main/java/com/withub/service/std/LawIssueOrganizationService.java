package com.withub.service.std;

import com.withub.model.std.po.LawIssueOrganization;
import com.withub.service.EntityService;

public interface LawIssueOrganizationService extends EntityService {
    public LawIssueOrganization getRootLawIssueOrganization() throws Exception;

    public void addLawIssueOrganization(LawIssueOrganization lawIssueOrganization) throws Exception;

    public void updateLawIssueOrganization(LawIssueOrganization lawIssueOrganization) throws Exception;
}
