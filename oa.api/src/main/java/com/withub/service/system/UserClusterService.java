package com.withub.service.system;

import com.withub.model.system.AbstractUserCluster;
import com.withub.model.system.po.*;

import java.util.List;


public interface UserClusterService extends com.withub.service.EntityService {

    public UserClusterCategory getRootUserClusterCategory() throws Exception;

    public UserClusterCategory getUserClusterCategoryByNodeTag(final String UserClusterCategoryTag) throws Exception;

    public void createUserCluster(UserCluster userCluster) throws Exception;

    public void updateUserCluster(UserCluster userCluster) throws Exception;

    public UserClusterRegulation getRootRegulationByUserClusterId(String userClusterId) throws Exception;

    public void saveUserClusterDetails(Entity userClusterEntity, String userClusterEntityInstanceId, List<UserClusterDetail> userClusterDetailList) throws Exception;

    public int updateUserClusterCache(AbstractUserCluster abstractUserCluster, User user) throws Exception;

}