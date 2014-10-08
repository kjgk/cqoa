package com.withub.service.system;


import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.entity.AbstractEntity;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.system.po.*;
import com.withub.service.EntityService;

import java.util.List;

public interface PermissionService extends EntityService {

    public void addPermissionRegulation(PermissionRegulation permissionRegulation) throws Exception;

    public void updatePermissionRegulation(PermissionRegulation permissionRegulation) throws Exception;

    public void deletePermissionRegulation(PermissionRegulation permissionRegulation) throws Exception;

    public void setEntityAdministrator(String entityId, String authorizationClusterId) throws Exception;

    public boolean isEntityAdministrator(String entityName, User user) throws Exception;

    public boolean isEntityInstanceAdministrator(AbstractEntity entityInstance, User user) throws Exception;

    public void assignPermission(AuthorizationPermission authorizationPermission) throws Exception;

    public AuthorizationPermission getAssignedPermission(String authorizationClusterId, String permissionId) throws Exception;

    public void cancelPermission(String authorizationClusterId, String permissionId) throws Exception;

    public List<Menu> listCurrentUserMenuByParentId(String menuParentId) throws Exception;

    public boolean hasPermission(Permission permission, AbstractBaseEntity entityBaseInstance) throws Exception;

    public boolean checkSavePermission(AbstractBaseEntity baseEntityInstance) throws Exception;

    public void addQueryPermissionCondition(QueryInfo queryInfo) throws Exception;

    public EntityPermissionChain getRootEntityPermissionChain() throws Exception;

    public void deleteEntityPermissionChain(String entityPermissionChainId) throws Exception;
}