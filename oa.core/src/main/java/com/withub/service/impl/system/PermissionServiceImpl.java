package com.withub.service.impl.system;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.entity.AbstractEntity;
import com.withub.model.entity.enumeration.PropertyDataType;
import com.withub.model.entity.query.ExpressionOperation;
import com.withub.model.entity.query.QueryConditionNode;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.SqlExpressionConfig;
import com.withub.model.exception.BaseBusinessException;
import com.withub.model.system.enumeration.SystemConstant;
import com.withub.model.system.po.*;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.AgencyService;
import com.withub.service.system.MenuService;
import com.withub.service.system.PermissionService;
import com.withub.service.system.UserClusterService;
import com.withub.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("permissionService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class PermissionServiceImpl extends EntityServiceImpl implements PermissionService {

    //=================== 属性声明 =========================================================

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserClusterService userClusterService;

    @Autowired
    private AgencyService agencyService;

    //=================== 接口实现 =========================================================

    public void addPermissionRegulation(PermissionRegulation permissionRegulation) throws Exception {

        save(permissionRegulation);
    }

    public void updatePermissionRegulation(PermissionRegulation permissionRegulation) throws Exception {

        if (StringUtil.isEmpty(permissionRegulation.getParent().getObjectId())) {
            permissionRegulation.setParent(null);
        }
        save(permissionRegulation);
    }

    public void deletePermissionRegulation(PermissionRegulation permissionRegulation) throws Exception {

        List list = listByHql("from " + AuthorizationRegulation.class.getName() + " where permissionRegulation.objectId = ?", permissionRegulation.getObjectId());
        if (CollectionUtil.isNotEmpty(list)) {
            delete(list);
        }
        logicDelete(permissionRegulation);
    }

    public void setEntityAdministrator(String entityId, String authorizationClusterId) throws Exception {

        Entity entity = getEntityById(entityId);

        String hql = "select o from " + EntityAdministrator.class.getName() + " o"
                + " where o.entity.objectId=? and o.entityInstanceId is null"
                + " and .objectType=?";

        List list = listByHql(hql, entityId, AuthorizationCluster.class.getSimpleName());
        delete(list);


        EntityAdministrator entityAdministrator = new EntityAdministrator();
        entityAdministrator.setObjectId(StringUtil.getUuid());
        entityAdministrator.setEntity(entity);
        entityAdministrator.setObjectType(AuthorizationCluster.class.getSimpleName());
        entityAdministrator.setRelatedObjectId(authorizationClusterId);
        entityAdministrator.setOrderNo(1);
        save(entityAdministrator);

        AuthorizationCluster authorizationCluster = get(AuthorizationCluster.class, authorizationClusterId);
        assignAuthorizationClusterMenu(authorizationClusterId, entity.getMenu().getObjectId());
    }

    public void setEntityInstanceAdministrator(String entityName, String entityInstanceId, String authorizationClusterId) throws Exception {

        Entity entity = getEntityByEntityName(entityName);

        String hql = "select o from " + EntityAdministrator.class.getName() + " o"
                + " where o.entity.objectId=? and o.entityInstanceId=?"
                + " and o.objectType=?";

        List list = listByHql(hql, entity.getObjectId(), entityInstanceId, User.class.getSimpleName());
        delete(list);

        EntityAdministrator entityAdministrator = new EntityAdministrator();
        entityAdministrator.setObjectId(StringUtil.getUuid());
        entityAdministrator.setEntity(entity);
        entityAdministrator.setEntityInstanceId(entityInstanceId);
        entityAdministrator.setObjectType(AuthorizationCluster.class.getSimpleName());
        entityAdministrator.setRelatedObjectId(authorizationClusterId);
        entityAdministrator.setOrderNo(1);
        save(entityAdministrator);

        AuthorizationCluster authorizationCluster = get(AuthorizationCluster.class, authorizationClusterId);
        assignAuthorizationClusterMenu(authorizationClusterId, entity.getMenu().getObjectId());

    }

    public boolean isEntityAdministrator(String entityName, User user) throws Exception {

        Entity entity = getEntityByEntityName(entityName);
        String sql = "select count(*) from Sys_AuthorizationPermission a"
                + " inner join Sys_UserObjectCache b on a.AuthorizationClusterId=b.RelatedObjectId"
                + " inner join Sys_Permission c on a.PermissionId=c.ObjectId"
                + " where c.PermissionTag='Admin' and c.EntityId='" + entity.getObjectId() + "'";
        List<User> ownerList = new ArrayList<User>();
        agencyService.getOwner(user, ownerList);
        if (CollectionUtil.isEmpty(ownerList)) {
            sql += " and b.UserId='" + user.getObjectId() + "'";
        } else {
            String userIds = "'" + user.getObjectId() + "',";
            for (User owner : ownerList) {
                userIds += "'" + owner.getObjectId() + "',";
            }
            userIds = StringUtil.trimEnd(userIds, ",");
            sql += " and c.UserId in (" + userIds + ")";
        }

        int count = executeSqlIntegerScalar(sql);
        return count > 0;
    }

    public boolean isEntityInstanceAdministrator(AbstractEntity entityInstance, User user) throws Exception {

        String hql = "select o from " + EntityAdministrator.class.getName() + " o"
                + " where o.entity.entityName=? and o.entityInstanceId=? and o.objectType=? and o.relatedObjectId=?";
        List list = listByHql(hql, entityInstance.getClass().getSimpleName(), entityInstance.getObjectId(), User.class.getSimpleName(), user.getObjectId());
        if (CollectionUtil.isNotEmpty(list)) {
            return true;
        }

        hql = "select o from " + EntityAdministrator.class.getName() + " o"
                + " where o.entity.entityName=? and o.objectType=? and o.entityInstanceId=?";
        list = listByHql(hql, entityInstance.getClass().getSimpleName(), AuthorizationCluster.class.getSimpleName(), entityInstance.getObjectId());
        if (CollectionUtil.isEmpty(list)) {
            return false;
        }

        /*List<AuthorizationCluster> authorizationObjectList = userClusterService.listCurrentUserAuthorizationCluster();
        if (CollectionUtil.isEmpty(authorizationObjectList)) {
            // 不可能发生的
            return false;
        }

        for (EntityAdministrator entityAdministrator : (List<EntityAdministrator>) list) {
            for (AuthorizationCluster authorizationObject : authorizationObjectList) {
                if (entityAdministrator.getRelatedObjectId().equals(authorizationObject.getObjectId())) {
                    return true;
                }
            }
        }*/
        return false;
    }

    public void assignPermission(AuthorizationPermission authorizationPermission) throws Exception {

        AuthorizationPermission old = getAssignedPermission(authorizationPermission.getAuthorizationCluster().getObjectId(), authorizationPermission.getPermission().getObjectId());
        if (old != null) {
            String hql = "delete from " + AuthorizationRegulation.class.getName() + " where authorizationPermission.objectId='" + old.getObjectId() + "'";
            executeHql(hql);
            delete(old);
        }

        authorizationPermission.setObjectId(StringUtil.getUuid());
        save(authorizationPermission);
        if (CollectionUtil.isNotEmpty(authorizationPermission.getAuthorizationRegulationList())) {
            for (AuthorizationRegulation authorizationRegulation : authorizationPermission.getAuthorizationRegulationList()) {
                authorizationRegulation.setAuthorizationPermission(authorizationPermission);
                save(authorizationRegulation);
            }
        }

        Permission permission = get(Permission.class, authorizationPermission.getPermission().getObjectId());

        // 分配菜单
        if (permission.getMenu() == null || permission.getMenu().getPermission() != 3) {
            return;
        }

        assignAuthorizationClusterMenu(authorizationPermission.getAuthorizationCluster().getObjectId(), permission.getMenu().getObjectId());

        // 根据实体权限控制链分配对应的菜单
        Entity entity = permission.getEntity();
        EntityPermissionChain entityPermissionChain = (EntityPermissionChain) getByPropertyValue(EntityPermissionChain.class, "entity.objectId", entity.getObjectId());
        if (entityPermissionChain != null) {
            recursionAssignAuthorizationClusterMenu(entityPermissionChain, authorizationPermission.getAuthorizationCluster().getObjectId());
        }
    }

    public AuthorizationPermission getAssignedPermission(String authorizationClusterId, String permissionId) throws Exception {

        String hql = "select o from " + AuthorizationPermission.class.getName() + " o"
                + " where o.authorizationCluster.objectId=?"
                + " and o.permission.objectId=?";
        AuthorizationPermission authorizationPermission = (AuthorizationPermission) getByHql(hql, authorizationClusterId, permissionId);
        return authorizationPermission;
    }

    public void cancelPermission(String authorizationClusterId, String permissionId) throws Exception {

        String hql = "select o from " + AuthorizationPermission.class.getName() + " o"
                + " where o.authorizationCluster.objectId=?"
                + " and o.permission.objectId=?";
        AuthorizationPermission authorizationPermission = (AuthorizationPermission) getByHql(hql, authorizationClusterId, permissionId);
        if (authorizationPermission == null) {
            return;
        }

        // 删除菜单
        deleteAuthorizationClusterMenu(authorizationClusterId, authorizationPermission.getPermission().getMenu().getObjectId());

        delete(authorizationPermission.getAuthorizationRegulationList());
        delete(authorizationPermission);
    }

    public void recursionAssignAuthorizationClusterMenu(EntityPermissionChain entityPermissionChain, String authorizationClusterId) throws Exception {

        assignAuthorizationClusterMenu(authorizationClusterId, entityPermissionChain.getEntity().getMenu().getObjectId());
        if (CollectionUtil.isEmpty(entityPermissionChain.getChildList())) {
            return;
        }
        for (EntityPermissionChain child : entityPermissionChain.getChildList()) {
            recursionAssignAuthorizationClusterMenu(child, authorizationClusterId);
        }
    }

    public List<Menu> listCurrentUserMenuByParentId(String menuParentId) throws Exception {

        User user = SpringSecurityUtil.getCurrentUser();
        if (user == null || user.getObjectId().equals(SystemConstant.USER_ADMINISTOR) || user.getAdministrator() == 1) {
            String hql = "select o from " + Menu.class.getName() + " o where o.objectStatus=1 and o.visible=1";
            if (StringUtil.isEmpty(menuParentId)) {
                hql += " and o.nodeLevel=2";
            } else {
                hql += " and o.parent.objectId='" + menuParentId + "'";
            }
            if (user == null) {
                hql += " and o.permission=3";
            } else {
                if (user.getObjectId().equals(SystemConstant.USER_ADMINISTOR)) {
                    // do nothing
                } else if (user.getAdministrator() == 1) {
                    hql += " and o.permission>1";
                }
            }
            hql += " order by o.orderNo";
            List list = listByHql(hql);
            return (List<Menu>) list;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("select distinct t.ObjectId,t.Name,t.url,t.image,t.expand,t.orderNo,t.parentId,t.nodeLevel,t.openMode,t.leaf from (");

        sb.append(" select a.ObjectId,a.Name,a.url,a.image,a.expand,a.orderNo,a.parentId,a.nodeLevel,a.openMode,a.leaf from Sys_Menu a");
        sb.append(" where a.objectStatus=1 and a.visible=1 and a.Permission > 3");

        List<User> ownerList = new ArrayList<User>();
        agencyService.getOwner(user, ownerList);

        sb.append(" union");
        sb.append(" select distinct a.ObjectId,a.Name,a.url,a.image,a.expand,a.orderNo,a.parentId,a.nodeLevel,a.openMode,a.leaf from Sys_Menu a inner join SYS_AUTHORIZATIONCLUSTERMENU b");
        sb.append(" on a.ObjectId = b.MenuId and a.objectStatus=1");
        sb.append(" inner join SYS_AUTHORIZATIONCLUSTERCACHE c on b.AUTHORIZATIONCLUSTERID = c.AUTHORIZATIONCLUSTERID ");
        sb.append(" where a.objectStatus=1 and a.visible=1 and a.Permission>1");
        if (CollectionUtil.isEmpty(ownerList)) {
            sb.append(" and c.UserId='" + user.getObjectId() + "'");
        } else {
            String userIds = "'" + user.getObjectId() + "',";
            for (User owner : ownerList) {
                userIds += "'" + owner.getObjectId() + "',";
            }
            userIds = StringUtil.trimEnd(userIds, ",");
            sb.append(" and c.UserId in (" + userIds + ")");
        }

        sb.append(") t");
        if (StringUtil.isEmpty(menuParentId)) {
            sb.append(" where t.nodeLevel=2");
        } else {
            sb.append(" where t.parentId='" + menuParentId + "'");
        }
        sb.append(" order by t.orderNo");
        String sql = sb.toString();
        List list = listBySql(sql);

        List menuList = new ArrayList();
        if (CollectionUtil.isNotEmpty(list)) {
            for (Object[] objects : (List<Object[]>) list) {
                Menu menu = new Menu();
                menu.setObjectId((String) objects[0]);
                menu.setName((String) objects[1]);
                menu.setUrl((String) objects[2]);
                menu.setImage((String) objects[3]);
                menu.setExpand(Integer.parseInt(objects[4].toString()));
                menu.setNodeLevel(Integer.parseInt(objects[7].toString()));
                menu.setOpenMode(Integer.parseInt(objects[8].toString()));
                menu.setLeaf(Integer.parseInt(objects[9].toString()));
                menuList.add(menu);
            }
        }
        return menuList;
    }

    public void addQueryPermissionCondition(QueryInfo queryInfo) throws Exception {

        // 获取业务层方法对应的权限
        Permission permission = (Permission) getByPropertyValue(Permission.class, "serviceMethod", queryInfo.getServiceMethod());
        if (permission == null) {
            return;
        }

        User currentUser = queryInfo.getCurrentUser();

        // 不可能发生的情况:如果允许不登录,则 serviceMethod 必定为空
        if (currentUser == null) {
            return;
        }

        // 判断是否为系统管理员
        if (currentUser.getAdministrator() == 1) {
            return;
        }

        // 判断是否为实体管理员
        if (isEntityAdministrator(queryInfo.getTargetEntity().getSimpleName(), currentUser)) {
            return;
        }

        /*// 判断是否是实体权限控制链上的管理员
        boolean entityInstanceAdministrator = false;
        EntityPermissionChain entityPermissionChain = (EntityPermissionChain) getByPropertyValue(EntityPermissionChain.class, "entity.className", queryInfo.getTargetEntity().getName());
        List<EntityPermissionChain> entityPermissionChainList = new ArrayList<EntityPermissionChain>();
        if (entityPermissionChain != null) {
            EntityPermissionChain parent = entityPermissionChain.getParent();
            while (parent.getNodeLevel() > 1) {
                Class clazz = Class.forName(parent.getEntity().getClassName());
                if (isEntityAdministrator(clazz.getSimpleName(), currentUser)) {
                    return;
                }
                entityPermissionChainList.add(parent);
                parent = parent.getParent();
            }

            // 如果没有实体管理员权限,则约束查询记录的条件是实体权限控制链的最上层定义的实例管理员权限
            int index;
            List<EntityAdministrator> entityInstanceAdministratorList = null;
            for (index = entityPermissionChainList.size() - 1; index > -1; index--) {
                Class clazz = Class.forName(entityPermissionChainList.get(index).getEntity().getClassName());
                entityInstanceAdministratorList = listEntityInstanceAdministrator(clazz, currentUser);
                if (CollectionUtil.isNotEmpty(entityInstanceAdministratorList)) {
                    entityInstanceAdministrator = true;
                    break;
                }
            }

            if (entityInstanceAdministrator) {
                // 构建查询属性名
                String propertyName = entityPermissionChain.getDependProperty();
                for (int i = 0; i <= index; i++) {
                    propertyName += "." + entityPermissionChainList.get(i).getDependProperty();
                }

                // 构建查询表达式
                SqlExpressionConfig sqlExpressionConfig = new SqlExpressionConfig();
                sqlExpressionConfig.setPropertyName(propertyName);

                EntityType entityType = EntityType.valueOf(parent.getEntity().getEntityType().getCodeTag());
                if (entityType == EntityType.Recursion) {
                    if (entityInstanceAdministratorList.size() == 1) {
                        sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.MatchBegin);
                        AbstractEntity abstractEntity = getEntityByClassName(entityPermissionChainList.get(index).getEntity().getClassName(), entityInstanceAdministratorList.get(0).getRelatedObjectId());
                        String nodeLevelCode = getPropertyValue(abstractEntity, "nodeLevelCode").toString();
                        sqlExpressionConfig.setPropertyValue(nodeLevelCode);
                    } else {
                        sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.InMatchBegin);
                        List<String> nodeLevelCodeList = new ArrayList<String>();
                        for (EntityAdministrator entityAdministrator : entityInstanceAdministratorList) {
                            AbstractEntity abstractEntity = getEntityByClassName(entityPermissionChainList.get(index).getEntity().getClassName(), entityAdministrator.getRelatedObjectId());
                            String nodeLevelCode = getPropertyValue(abstractEntity, "nodeLevelCode").toString();
                            nodeLevelCodeList.add(nodeLevelCode);
                        }
                        sqlExpressionConfig.setPropertyValue(nodeLevelCodeList);
                    }
                } else {
                    if (entityInstanceAdministratorList.size() == 1) {
                        sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.Equals);
                        AbstractEntity abstractEntity = getEntityByClassName(entityPermissionChainList.get(index).getEntity().getClassName(), entityInstanceAdministratorList.get(0).getRelatedObjectId());
                        String objectId = getPropertyValue(abstractEntity, "objectId").toString();
                        sqlExpressionConfig.setPropertyValue(objectId);
                    } else {
                        sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.In);
                        List<String> objectIdList = new ArrayList<String>();
                        for (EntityAdministrator entityAdministrator : entityInstanceAdministratorList) {
                            AbstractEntity abstractEntity = getEntityByClassName(entityPermissionChainList.get(index).getEntity().getClassName(), entityAdministrator.getRelatedObjectId());
                            String objectId = getPropertyValue(abstractEntity, "objectId").toString();
                            objectIdList.add(objectId);
                        }
                        sqlExpressionConfig.setPropertyValue(objectIdList);
                    }
                }

                QueryConditionNode queryConditionNode = new QueryConditionNode();
                queryConditionNode.setSqlExpressionConfig(sqlExpressionConfig);
                queryInfo.getQueryConditionTree().getPermissionConditionNode().appendNode(queryConditionNode);
            }
        }
*/
        // 获取当前用户对应的授权对象
        StringBuilder sb = new StringBuilder();
        sb.append("select b.objectId from sys_UserObjectCache a");
        sb.append(" inner join sys_AuthorizationCluster b on a.relatedObjectId=b.objectId");
        sb.append(" inner join sys_AuthorizationPermission c on b.objectId=c.AuthorizationClusterId");
        sb.append(" where a.userId='" + currentUser.getObjectId() + "'");
        sb.append(" and c.permissionId='" + permission.getObjectId() + "'");
        String sql = sb.toString();
        List list = listBySql(sql);

        if (CollectionUtil.isEmpty(list)) {
            throw new BaseBusinessException("", "当前用户[" + currentUser.getName() + "]未授予权限[" + permission.getName() + "].");
        }

        // 遍历当前用户所在的授权对象列表
        List<AuthorizationPermission> authorizationPermissionList = new ArrayList<AuthorizationPermission>();
        for (String authorizationClusterId : (List<String>) list) {
            String hql = "select o from " + AuthorizationPermission.class.getName() + " o"
                    + " where o.authorizationCluster.objectId=? and o.permission.objectId=?";
            AuthorizationPermission authorizationPermission = (AuthorizationPermission) getByHql(hql, authorizationClusterId, permission.getObjectId());

            if (CollectionUtil.isEmpty(authorizationPermission.getAuthorizationRegulationList())) {
                // 存在一个无限制查询权限,则立即返回.
                return;
            } else {
                authorizationPermissionList.add(authorizationPermission);
            }
        }

        // 解析权限规则树: 根据权限规则树,为每一个节点创建一个查询表达式
        for (AuthorizationPermission authorizationPermission : authorizationPermissionList) {
            for (AuthorizationRegulation authorizationRegulation : authorizationPermission.getAuthorizationRegulationList()) {
                for (UserOrganizationRole userOrganizationRole : currentUser.getOrganizationRoleList()) {
                    throughPermissionConditionTree(userOrganizationRole, authorizationRegulation, queryInfo.getQueryConditionTree().getPermissionConditionNode());
                }
            }
        }
    }

    public boolean hasPermission(Permission permission, AbstractBaseEntity entityInstance) throws Exception {

        User currentUser = entityInstance.getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // 判断是否为系统管理员
        if (currentUser.getAdministrator() == 1) {
            return true;
        }

        // 判断是否为实体管理员
        if (isEntityAdministrator(permission.getEntity().getEntityName(), currentUser)) {
            return true;
        }

        /* // 判断是否为实体实例管理员
        if (entityInstance != null && isEntityInstanceAdministrator(entityInstance, currentUser)) {
            return true;
        }*/

        /* // 判断是否是实体权限控制链上的管理员
        if (entityInstance != null) {
            AbstractEntity currentInstance = entityInstance;
            EntityPermissionChain entityPermissionChain = (EntityPermissionChain) getByPropertyValue(EntityPermissionChain.class, "entity.className", entityInstance.getClass().getName());
            if (entityPermissionChain != null) {
                EntityPermissionChain parent = entityPermissionChain.getParent();
                while (parent.getNodeLevel() > 1) {
                    Class clazz = Class.forName(parent.getEntity().getClassName());
                    if (isEntityAdministrator(clazz.getSimpleName(), currentUser)) {
                        return true;
                    }
                    AbstractEntity parentInstance = (AbstractEntity) getPropertyValue(currentInstance, entityPermissionChain.getDependProperty());
                    if (isEntityInstanceAdministrator(parentInstance, currentUser)) {
                        return true;
                    }
                    currentInstance = parentInstance;
                    parent = parent.getParent();
                }
            }
        }*/

        // 判断当前用户是否拥有权限
        List<String> authorizationClusterIdList = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select b.objectId,b.instanceAuthorization from sys_UserObjectCache a");
        sb.append(" inner join sys_AuthorizationCluster b on a.relatedObjectId=b.objectId");
        sb.append(" inner join sys_AuthorizationPermission c on b.objectId=c.AuthorizationClusterId");
        sb.append(" where c.permissionId='" + permission.getObjectId() + "'");
        List<User> ownerList = new ArrayList<User>();
        agencyService.getOwner(currentUser, ownerList);
        if (CollectionUtil.isEmpty(ownerList)) {
            sb.append(" and a.UserId='" + currentUser.getObjectId() + "'");
        } else {
            String userIds = "'" + currentUser.getObjectId() + "',";
            for (User owner : ownerList) {
                userIds += "'" + owner.getObjectId() + "',";
            }
            userIds = StringUtil.trimEnd(userIds, ",");
            sb.append(" and a.UserId in (" + userIds + ")");
        }

        List list = listBySql(sb.toString());
        if (CollectionUtil.isNotEmpty(list)) {
            for (Object[] authorizationObject : (List<Object[]>) list) {
                if (Integer.parseInt(authorizationObject[1].toString()) == 0) {
                    authorizationClusterIdList.add(authorizationObject[0].toString());
                } else {
                    // 解析授权对象的角色属性
                    if (entityInstance != null) {

                    }
                }
            }
        }

        if (CollectionUtil.isEmpty(authorizationClusterIdList)) {
            return false;
        }

        List<AuthorizationPermission> authorizationPermissionList = new ArrayList<AuthorizationPermission>();
        for (String authorizationClusterId : authorizationClusterIdList) {
            String hql = "select o from " + AuthorizationPermission.class.getName() + " o"
                    + " where o.authorizationCluster.objectId=? and o.permission.objectId=?";
            AuthorizationPermission authorizationPermission = (AuthorizationPermission) getByHql(hql, authorizationClusterId, permission.getObjectId());
            if (CollectionUtil.isEmpty(authorizationPermission.getAuthorizationRegulationList())) {
                // 存在一个无限制操作权限,则立即返回.
                return true;
            } else {
                authorizationPermissionList.add(authorizationPermission);
            }
        }

        // 解析权限规则树
        sb = new StringBuilder();
        for (int i = 0; i < authorizationPermissionList.size(); i++) {
            AuthorizationPermission authorizationPermission = authorizationPermissionList.get(i);
            StringBuilder sb2 = new StringBuilder();
            for (int j = 0; j < authorizationPermission.getAuthorizationRegulationList().size(); j++) {
                AuthorizationRegulation authorizationRegulation = authorizationPermission.getAuthorizationRegulationList().get(j);
                StringBuilder sb3 = new StringBuilder();
                for (int k = 0; k < currentUser.getOrganizationRoleList().size(); k++) {
                    UserOrganizationRole userOrganizationRole = currentUser.getOrganizationRoleList().get(k);
                    StringBuilder sb4 = new StringBuilder();
                    for (int l = 0; l < authorizationPermission.getAuthorizationRegulationList().size(); l++) {
                        StringBuilder sb5 = new StringBuilder();
                        throughPermissionConditionTree(entityInstance, sb5, userOrganizationRole, authorizationRegulation);
                        if (l == 0) {
                            sb4.append("(" + sb5 + ")");
                        } else {
                            sb4.append(" OR (" + sb5 + ")");
                        }
                    }
                    if (k == 0) {
                        sb3.append("(" + sb4 + ")");
                    } else {
                        sb3.append(" OR (" + sb4 + ")");
                    }
                }
                if (j == 0) {
                    sb2.append("(" + sb3 + ")");
                } else {
                    sb2.append(" OR (" + sb3 + ")");
                }
            }
            if (i == 0) {
                sb.append("(" + sb2 + ")");
            } else {
                sb.append(" OR (" + sb2 + ")");
            }
        }

        // 解析权限规则表达式
        ExpressionParser parser = new SpelExpressionParser();
        String expression = sb.toString();
        //System.out.println(permission.getName() + "[" + expression + "]");
        boolean retValue;

        try {
            retValue = parser.parseExpression(expression).getValue(Boolean.class);
        } catch (Exception e) {
            throw new BaseBusinessException("", "无法解析实体[" + permission.getEntity().getName() + "]上的权限[" + permission.getName() + "]定义的业务规则表达式.");
        }

        return retValue;
    }

    public boolean checkSavePermission(AbstractBaseEntity entityInstance) throws Exception {

        User currentUser = entityInstance.getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // 判断是否为系统管理员
        if (currentUser.getObjectId().equals(SystemConstant.USER_ADMINISTOR) || currentUser.getAdministrator() == 1) {
            return true;
        }

        String hql = "select o from " + Permission.class.getName() + " o where o.objectStatus=1"
                + " and o.permissionTag=? and entity.entityName=?";
        List list = listByHql(hql, "Save", entityInstance.getClass().getSimpleName());
        if (CollectionUtil.isEmpty(list)) {
            return true;
        }

        Permission permission = (Permission) list.get(0);

        // 判断是否为实体管理员
        if (isEntityAdministrator(permission.getEntity().getEntityName(), currentUser)) {
            return true;
        }


        // 判断是否是实体权限控制链上的管理员
        if (entityInstance != null) {
            AbstractEntity currentInstance = entityInstance;
            EntityPermissionChain entityPermissionChain = (EntityPermissionChain) getByPropertyValue(EntityPermissionChain.class, "entity.className", entityInstance.getClass().getName());
            if (entityPermissionChain != null) {
                EntityPermissionChain parent = entityPermissionChain.getParent();
                while (parent.getNodeLevel() > 1) {
                    Class clazz = Class.forName(parent.getEntity().getClassName());
                    if (isEntityAdministrator(clazz.getSimpleName(), currentUser)) {
                        return true;
                    }
                    AbstractEntity parentInstance = (AbstractEntity) getPropertyValue(currentInstance, entityPermissionChain.getDependProperty());
                    if (isEntityInstanceAdministrator(parentInstance, currentUser)) {
                        return true;
                    }
                    currentInstance = parentInstance;
                    parent = parent.getParent();
                }
            }
        }

        // 判断当前用户是否拥有权限
        List<String> authorizationClusterIdList = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select b.objectId,b.instanceAuthorization from sys_UserObjectCache a");
        sb.append(" inner join sys_AuthorizationCluster b on a.relatedObjectId=b.objectId");
        sb.append(" inner join sys_AuthorizationPermission c on b.objectId=c.AuthorizationClusterId");
        sb.append(" where c.permissionId='" + permission.getObjectId() + "'");
        sb.append(" and a.UserId='" + currentUser.getObjectId() + "'");

        list = listBySql(sb.toString());
        if (CollectionUtil.isNotEmpty(list)) {
            for (Object[] authorizationObject : (List<Object[]>) list) {
                if (Integer.parseInt(authorizationObject[1].toString()) == 0) {
                    authorizationClusterIdList.add(authorizationObject[0].toString());
                } else {
                    // 解析授权对象的角色属性
                    if (entityInstance != null) {

                    }
                }
            }
        }

        if (CollectionUtil.isEmpty(authorizationClusterIdList)) {
            return false;
        }

        List<AuthorizationPermission> authorizationPermissionList = new ArrayList<AuthorizationPermission>();
        for (String authorizationClusterId : authorizationClusterIdList) {
            hql = "select o from " + AuthorizationPermission.class.getName() + " o"
                    + " where o.authorizationCluster.objectId=? and o.permission.objectId=?";
            AuthorizationPermission authorizationPermission = (AuthorizationPermission) getByHql(hql, authorizationClusterId, permission.getObjectId());
            if (CollectionUtil.isEmpty(authorizationPermission.getAuthorizationRegulationList())) {
                // 存在一个无限制操作权限,则立即返回.
                return true;
            } else {
                authorizationPermissionList.add(authorizationPermission);
            }
        }

        // 解析权限规则树
        sb = new StringBuilder();
        for (int i = 0; i < authorizationPermissionList.size(); i++) {
            AuthorizationPermission authorizationPermission = authorizationPermissionList.get(i);
            StringBuilder sb2 = new StringBuilder();
            for (int j = 0; j < authorizationPermission.getAuthorizationRegulationList().size(); j++) {
                AuthorizationRegulation authorizationRegulation = authorizationPermission.getAuthorizationRegulationList().get(j);
                StringBuilder sb3 = new StringBuilder();
                for (int k = 0; k < currentUser.getOrganizationRoleList().size(); k++) {
                    UserOrganizationRole userOrganizationRole = currentUser.getOrganizationRoleList().get(k);
                    StringBuilder sb4 = new StringBuilder();
                    for (int l = 0; l < authorizationPermission.getAuthorizationRegulationList().size(); l++) {
                        StringBuilder sb5 = new StringBuilder();
                        throughPermissionConditionTree(entityInstance, sb5, userOrganizationRole, authorizationRegulation);
                        if (l == 0) {
                            sb4.append("(" + sb5 + ")");
                        } else {
                            sb4.append(" OR (" + sb5 + ")");
                        }
                    }
                    if (k == 0) {
                        sb3.append("(" + sb4 + ")");
                    } else {
                        sb3.append(" OR (" + sb4 + ")");
                    }
                }
                if (j == 0) {
                    sb2.append("(" + sb3 + ")");
                } else {
                    sb2.append(" OR (" + sb3 + ")");
                }
            }
            if (i == 0) {
                sb.append("(" + sb2 + ")");
            } else {
                sb.append(" OR (" + sb2 + ")");
            }
        }

        // 解析权限规则表达式
        ExpressionParser parser = new SpelExpressionParser();
        String expression = sb.toString();
        //System.out.println(permission.getName() + "[" + expression + "]");
        boolean retValue;

        try {
            retValue = parser.parseExpression(expression).getValue(Boolean.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseBusinessException("", "无法解析实体[" + permission.getEntity().getName() + "]上的权限[" + permission.getName() + "]定义的业务规则表达式.");
        }

        return retValue;
    }

    public EntityPermissionChain getRootEntityPermissionChain() throws Exception {

        return getRootEntity(EntityPermissionChain.class);
    }

    public void deleteEntityPermissionChain(String entityPermissionChainId) throws Exception {

        logicDelete(EntityPermissionChain.class, entityPermissionChainId);
    }

    //==================== 私有方法 ========================================================

    private List<AuthorizationClusterMenu> findAuthorizationClusterMenu(String authorizationClusterId, String menuId) throws Exception {

        String hql = "from " + AuthorizationClusterMenu.class.getName() + " where authorizationCluster.objectId = ?";
        List list;
        if (StringUtil.isNotEmpty(menuId)) {
            hql += " and menu.objectId= ?";
            list = listByHql(hql, authorizationClusterId, menuId);
        } else {
            list = listByHql(hql, authorizationClusterId);
        }
        return list;
    }

    private void assignAuthorizationClusterMenu(String authorizationClusterId, String menuId) throws Exception {

        List<AuthorizationClusterMenu> list = findAuthorizationClusterMenu(authorizationClusterId, menuId);
        if (CollectionUtil.isNotEmpty(list)) {
            return;
        }

        AuthorizationCluster authorizationCluster = get(AuthorizationCluster.class, authorizationClusterId);
        Menu menu = get(Menu.class, menuId);
        while (menu.getNodeLevel() != 1) {
            list = findAuthorizationClusterMenu(authorizationClusterId, menu.getObjectId());
            if (CollectionUtil.isNotEmpty(list)) {
                menu = menu.getParent();
                continue;
            }

            AuthorizationClusterMenu authorizationClusterMenu = new AuthorizationClusterMenu();
            authorizationClusterMenu.setObjectId(StringUtil.getUuid());
            authorizationClusterMenu.setAuthorizationCluster(authorizationCluster);
            authorizationClusterMenu.setMenu(menu);
            save(authorizationClusterMenu);
            menu = menu.getParent();
        }
    }

    private void deleteAuthorizationClusterMenu(String authorizationClusterId, String menuId) throws Exception {

        Menu menu = get(Menu.class, menuId);
        List<AuthorizationClusterMenu> list = findAuthorizationClusterMenu(authorizationClusterId, null);
        List<Menu> menuList = new ArrayList();
        for (AuthorizationClusterMenu authorizationClusterMenu : list) {
            menuList.add(authorizationClusterMenu.getMenu());
        }

        String deletSql = "delete from SYS_AUTHORIZATIONCLUSTERMENU where AUTHORIZATIONCLUSTERID = ? and MENUID = ?";
        while (menu.getNodeLevel() != 1) {
            if (menu.getLeaf() == 1) {
                getEntityDao().executeSql(deletSql, authorizationClusterId, menu.getObjectId());
            } else {
                boolean flag = true;
                for (Menu temp : menuList) {
                    if (StringUtil.compareValue(menu.getObjectId(), temp.getParent().getObjectId())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    getEntityDao().executeSql(deletSql, authorizationClusterId, menu.getObjectId());
                } else {
                    break;
                }
            }

            menuList.remove(menu);
            menu = menu.getParent();
        }
    }


    private void throughPermissionConditionTree(UserOrganizationRole userOrganizationRole, AuthorizationRegulation authorizationRegulation, QueryConditionNode parentNode) throws Exception {

        QueryConditionNode node = new QueryConditionNode();
        PermissionRegulation permissionRegulation = authorizationRegulation.getPermissionRegulation();
        SqlExpressionConfig sqlExpressionConfig = new SqlExpressionConfig();
        sqlExpressionConfig.setPropertyDataType(PropertyDataType.valueOf(permissionRegulation.getPropertyDataType().getCodeTag()));
        sqlExpressionConfig.setPropertyName(permissionRegulation.getEntityProperty());

        if (StringUtil.isNotEmpty(permissionRegulation.getUserProperty())) {
            Object propertyValue = getPropertyValue(userOrganizationRole, permissionRegulation.getUserProperty());
            sqlExpressionConfig.setPropertyValue(propertyValue);
        } else {
            // TODO: 1. Between 之类的查询 2.复杂条件:如查询3个月之前的数据
            if (sqlExpressionConfig.getPropertyDataType() == PropertyDataType.Date) {
                sqlExpressionConfig.setPropertyValue(DateUtil.convertStringToDate(permissionRegulation.getEntityPropertyValue(), "yyyy-MM-dd"));
            } else if (sqlExpressionConfig.getPropertyDataType() == PropertyDataType.String) {
                sqlExpressionConfig.setPropertyValue(permissionRegulation.getEntityPropertyValue());
            }
        }

        sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.valueOf(permissionRegulation.getExpressionOperation().getCodeTag()));
        node.setSqlExpressionConfig(sqlExpressionConfig);
        parentNode.appendNode(node);

        if (CollectionUtil.isNotEmpty(authorizationRegulation.getChildList())) {
            for (AuthorizationRegulation child : authorizationRegulation.getChildList()) {
                throughPermissionConditionTree(userOrganizationRole, child, node);
            }
        }
    }

    private void throughPermissionConditionTree(AbstractEntity entityInstance, StringBuilder sb, UserOrganizationRole userOrganizationRole, AuthorizationRegulation authorizationRegulation) throws Exception {

        sb.append("(");

        // 构建表达式
        PermissionRegulation permissionRegulation = authorizationRegulation.getPermissionRegulation();
        Object entityPropertyValue = getPropertyValue(entityInstance, permissionRegulation.getEntityProperty());
        String expressionOperation = permissionRegulation.getExpressionOperation().getCodeTag();
        if (StringUtil.isNotEmpty(permissionRegulation.getUserProperty())) {
            Object userPropertyValue = getPropertyValue(userOrganizationRole, permissionRegulation.getUserProperty());
            if (expressionOperation.equalsIgnoreCase("Equals")) {
                sb.append(" \"" + entityPropertyValue + "\".equalsIgnoreCase(\"" + userPropertyValue + "\")");
            } else if (expressionOperation.equalsIgnoreCase("NotEquals")) {
                sb.append(" Not\"" + entityPropertyValue + "\".equalsIgnoreCase(\"" + userPropertyValue + "\")");
            } else if (expressionOperation.equalsIgnoreCase("MatchBegin")) {
                sb.append(" \"" + entityPropertyValue + "\".startsWith(\"" + userPropertyValue + "\")");
            } else if (expressionOperation.equalsIgnoreCase("Include")) {
                String sql = permissionRegulation.getRegulation().replace("{#userPropertyValue#}", userPropertyValue.toString());
                sql = sql.replace("{#entityPropertyValue#}", entityPropertyValue.toString());
                int count = executeSqlIntegerScalar(sql);
                if (count == 0) {
                    sb.append(" 1==0 ");
                } else {
                    sb.append(" 1==1 ");
                }
            } else {
                throw new BaseBusinessException("", "表达式操作符[" + expressionOperation + "]未实现解析方法.");
            }
        } else {
            if (expressionOperation.equalsIgnoreCase("Equals")) {
                sb.append(" \"" + entityPropertyValue + "\".equalsIgnoreCase(\"" + permissionRegulation.getEntityPropertyValue() + "\")");
            } else if (expressionOperation.equalsIgnoreCase("NotEquals")) {
                sb.append(" Not \"" + entityPropertyValue + "\".equalsIgnoreCase(\"" + permissionRegulation.getEntityPropertyValue() + "\")");
            } else {
                throw new BaseBusinessException("", "表达式操作符[" + expressionOperation + "]未实现解析方法.");
            }
        }

        if (CollectionUtil.isEmpty(authorizationRegulation.getChildList())) {
            sb.append(")");
            return;
        }

        sb.append(" And (");
        for (int i = 0; i < authorizationRegulation.getChildList().size(); i++) {
            if (i > 0) {
                sb.append(" Or ");
            }
            throughPermissionConditionTree(entityInstance, sb, userOrganizationRole, authorizationRegulation.getChildList().get(i));
        }
        sb.append("))");
    }

    //=================== 属性方法 =========================================================

    public MenuService getMenuService() {

        return menuService;
    }

    public void setMenuService(MenuService menuService) {

        this.menuService = menuService;
    }

    public UserClusterService getUserClusterService() {

        return userClusterService;
    }

    public void setUserClusterService(UserClusterService userClusterService) {

        this.userClusterService = userClusterService;
    }

    public AgencyService getAgencyService() {

        return agencyService;
    }

    public void setAgencyService(AgencyService agencyService) {

        this.agencyService = agencyService;
    }
}