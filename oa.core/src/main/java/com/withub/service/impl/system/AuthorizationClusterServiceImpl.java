package com.withub.service.impl.system;

import com.withub.common.util.CollectionUtil;
import com.withub.model.system.enumeration.SystemConstant;
import com.withub.model.system.event.UserSaveEvent;
import com.withub.model.system.po.*;
import com.withub.service.EntityServiceImpl;
import com.withub.service.system.AuthorizationClusterService;
import com.withub.service.system.OrganizationService;
import com.withub.service.system.UserClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("authorizationClusterService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class AuthorizationClusterServiceImpl extends EntityServiceImpl implements AuthorizationClusterService {

    //============================== 属性声明 =============================================================

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserClusterService userClusterService;


    //============================== 接口实现 =============================================================

    public void createAuthorizationCluster(AuthorizationCluster authorizationCluster) throws Exception {

        Organization organization = organizationService.getRootOrganization();
        authorizationCluster.setOrganization(organization);
        authorizationCluster.setUserClusterCategory(userClusterService.getUserClusterCategoryByNodeTag("AuthorizationUserCluster"));
        save(authorizationCluster);

        Entity userClusterEntity = userClusterService.getEntityByClassName(AuthorizationCluster.class.getName());
        userClusterService.saveUserClusterDetails(userClusterEntity, authorizationCluster.getObjectId(), authorizationCluster.getUserClusterDetailList());

        userClusterService.updateUserClusterCache(authorizationCluster, null);
    }

    public void updateAuthorizationCluster(AuthorizationCluster authorizationCluster) throws Exception {

        Organization organization = organizationService.getRootOrganization();
        authorizationCluster.setOrganization(organization);
        authorizationCluster.setUserClusterCategory(userClusterService.getUserClusterCategoryByNodeTag("AuthorizationUserCluster"));
        save(authorizationCluster);

        Entity userClusterEntity = userClusterService.getEntityByClassName(AuthorizationCluster.class.getName());
        userClusterService.saveUserClusterDetails(userClusterEntity, authorizationCluster.getObjectId(), authorizationCluster.getUserClusterDetailList());

        userClusterService.updateUserClusterCache(authorizationCluster, null);
    }

    public void deleteAuthorizationCluster(String authorizationClusterId) throws Exception {

        executeHql("delete from " + AuthorizationPermission.class.getName() + " where authorizationCluster.objectId = ?", authorizationClusterId);
        executeHql("delete from " + AuthorizationClusterMenu.class.getName() + " where authorizationCluster.objectId = ?", authorizationClusterId);
        executeHql("delete from " + AuthorizationClusterCache.class.getName() + " where authorizationCluster.objectId = ?", authorizationClusterId);
        executeHql("delete from " + UserClusterDetail.class.getName() + " where userClusterEntityInstanceId = ?", authorizationClusterId);

        delete(AuthorizationCluster.class, authorizationClusterId);
    }

    public void onUserSaveEvent(UserSaveEvent event) throws Exception {

        User user = event.getUser();
        if (user.getObjectId().equals(SystemConstant.USER_ADMINISTOR) || user.getObjectId().equals(SystemConstant.USER_SYSTEM)) {
            return;
        }

        if (user.getAdministrator() == 1) {
            return;
        }

        // 更新授权用户簇缓存

        saveUserClusterCache(user);

    }

    private void saveUserClusterCache(User user) throws Exception {

        String hql = "select o from " + AuthorizationCluster.class.getName() + " o where o.objectStatus=1"
                + " order by o.orderNo";
        List list = listByHql(hql);

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        for (AuthorizationCluster authorizationCluster : (List<AuthorizationCluster>) list) {
            userClusterService.updateUserClusterCache(authorizationCluster, user);
        }
    }

    //============================== 属性方法 =============================================================


    public OrganizationService getOrganizationService() {

        return organizationService;
    }

    public void setOrganizationService(OrganizationService organizationService) {

        this.organizationService = organizationService;
    }

    public UserClusterService getUserClusterService() {

        return userClusterService;
    }

    public void setUserClusterService(UserClusterService userClusterService) {

        this.userClusterService = userClusterService;
    }
}
