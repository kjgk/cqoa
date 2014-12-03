package com.withub.service.oa;

import com.withub.model.system.po.User;
import com.withub.model.workflow.WFArguments;
import com.withub.service.EntityService;

import java.util.List;

public interface TaskHandlerFetchService extends EntityService {

    public List<User> fetchOrganizationManager() throws Exception;

    public List<User> fetchOrganizationManager(WFArguments wfArguments) throws Exception;

    public List<User> fetchOrganizationManager(String organizationCode) throws Exception;

    public List<User> fetchLeader() throws Exception;

    public List<User> fetchBoss() throws Exception;

}
