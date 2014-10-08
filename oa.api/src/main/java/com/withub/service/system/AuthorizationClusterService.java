package com.withub.service.system;

import com.withub.model.system.event.UserSaveEvent;
import com.withub.model.system.po.AuthorizationCluster;
import com.withub.service.EntityService;

public interface AuthorizationClusterService extends EntityService {

    public void createAuthorizationCluster(AuthorizationCluster authorizationCluster) throws Exception;

    public void updateAuthorizationCluster(AuthorizationCluster authorizationCluster) throws Exception;

    public void deleteAuthorizationCluster(String authorizationClusterId) throws Exception;

    public void onUserSaveEvent(UserSaveEvent event) throws Exception;
}