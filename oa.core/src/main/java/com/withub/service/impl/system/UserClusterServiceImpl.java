package com.withub.service.impl.system;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.ReflectionUtil;
import com.withub.model.entity.AbstractRecursionEntity;
import com.withub.model.entity.enumeration.EntityType;
import com.withub.model.system.AbstractUserCluster;
import com.withub.model.system.enumeration.UserStatus;
import com.withub.model.system.po.*;
import com.withub.service.system.PermissionService;
import com.withub.service.system.UserClusterService;
import com.withub.util.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userClusterService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class UserClusterServiceImpl extends com.withub.service.EntityServiceImpl implements UserClusterService {

    //=================== 属性声明 =========================================================

    @Autowired
    private PermissionService permissionService;

    //=================== 接口实现 =========================================================

    public UserClusterCategory getRootUserClusterCategory() throws Exception {

        return getRootEntity(UserClusterCategory.class);
    }

    public UserClusterCategory getUserClusterCategoryByNodeTag(String UserClusterCategoryTag) throws Exception {

        UserClusterCategory userClusterCategory = (UserClusterCategory) getByPropertyValue(UserClusterCategory.class, "nodeTag", UserClusterCategoryTag);
        return userClusterCategory;
    }

    public void createUserCluster(UserCluster userCluster) throws Exception {

        save(userCluster);

        UserClusterRegulation userClusterRegulation = new UserClusterRegulation();
        userClusterRegulation.setName("用户簇规则模版");
        userClusterRegulation.setUserCluster(userCluster);
        userClusterRegulation.setCurrentUser(userCluster.getCurrentUser());
        save(userClusterRegulation);
    }

    public void updateUserCluster(UserCluster userCluster) throws Exception {

        save(userCluster);
    }

    public UserClusterRegulation getRootRegulationByUserClusterId(String userClusterId) throws Exception {

        String hql = "from " + UserClusterRegulation.class.getName() + " where userCluster.objectId = ?" +
                " and nodeLevel = 1";
        return (UserClusterRegulation) getByHql(hql, userClusterId);
    }

    public void saveUserClusterDetails(Entity userClusterEntity, String userClusterEntityInstanceId, List<UserClusterDetail> userClusterDetailList) throws Exception {

        executeHql("delete from " + UserClusterDetail.class.getName() + " where userClusterEntityInstanceId = ?", userClusterEntityInstanceId);

        for (UserClusterDetail userClusterDetail : userClusterDetailList) {
            userClusterDetail.setUserClusterEntity(userClusterEntity);
            userClusterDetail.setUserClusterEntityInstanceId(userClusterEntityInstanceId);
            save(userClusterDetail);
        }
    }

    public int updateUserClusterCache(AbstractUserCluster abstractUserCluster, User user) throws Exception {

        String hql = "delete from " + AuthorizationClusterCache.class.getName() + " o where o.authorizationCluster.objectId='" + abstractUserCluster.getObjectId() + "'";
        if (user != null) {
            hql += " and o.user.objectId='" + user.getObjectId() + "'";
        }
        executeHql(hql);

        hql = "select o from " + UserClusterDetail.class.getName() + " o"
                + " where o.userClusterEntityInstanceId='" + abstractUserCluster.getObjectId() + "'"
                + " and o.userClusterRegulation.objectStatus=1"
                + " and o.parent.objectId is null";
        List userObjectDetailList = listByHql(hql);
        if (CollectionUtil.isEmpty(userObjectDetailList)) {
            return 0;
        }

        hql = "select o.objectId from " + User.class.getName() + " o where o.objectStatus=1";
        if (user != null) {
            hql += " and o.objectId='" + user.getObjectId() + "'";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" and o.status.codeTag='" + UserStatus.Active.name() + "'");

        // 目标范围组织机构
        Organization organization = abstractUserCluster.getOrganization();
        if (organization.getParent() != null) {
            sb.append(" and instr(o.organization.nodeLevelCode,'" + organization.getNodeLevelCode() + "')=1");
        }

        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < userObjectDetailList.size(); i++) {
            StringBuilder sb3 = new StringBuilder();
            UserClusterDetail userClusterDetail = (UserClusterDetail) userObjectDetailList.get(i);
            throughUserClusterRegulationTree(userClusterDetail, sb3);
            if (i == 0) {
                sb2.append("(" + sb3 + ")");
            } else {
                sb2.append(" or (" + sb3 + ")");
            }
        }

        sb.append(" and (" + sb2 + ") ");

//        // TODO 奇怪,在执行translateHqlToSql前,运行下面 hql 后,保持了事务的一致性
        listByHql(hql + " and 1=0");

        String selectSql = translateHqlToSql(hql + sb.toString());
        int fromIndex = selectSql.indexOf(" from");
        String userObjectIdColumn = selectSql.substring(6, fromIndex).trim();
        String tableAlias = userObjectIdColumn.substring(0, userObjectIdColumn.indexOf("."));

        if (ConfigUtil.getSystemConfigInfo().getDatabaseType().equals("Oracle")) {
            selectSql = selectSql.replace(userObjectIdColumn, "Sys_Guid(),'"
                    + abstractUserCluster.getObjectId() + "'," + tableAlias + ".objectId");
        } else if (ConfigUtil.getSystemConfigInfo().getDatabaseType().equals("PostgreSQL")) {
            selectSql = selectSql.replace(userObjectIdColumn, "uuid_generate_v4(),'"
                    + abstractUserCluster.getObjectId() + "'," + tableAlias + ".objectId");
        } else {
            selectSql = selectSql.replace(userObjectIdColumn, "newid(),'"
                    + abstractUserCluster.getObjectId() + "'," + tableAlias + ".objectId");
        }

        String sql = "insert into SYS_AUTHORIZATIONCLUSTERCACHE(OBJECTID,AUTHORIZATIONCLUSTERID,USERID)"
                + " " + selectSql;
        int count = executeSql(sql);

        return count;
    }

    private void throughUserClusterRegulationTree(UserClusterDetail userClusterDetail, StringBuilder sb) throws Exception {

        sb.append("(");
        UserClusterRegulation userClusterRegulation = get(UserClusterRegulation.class, userClusterDetail.getUserClusterRegulation().getObjectId());
        Entity entity = userClusterRegulation.getEntity();
        EntityType entityType = EntityType.valueOf(entity.getEntityType().getCodeTag());

        // 获取规则映射的用户属性,这个属性值是一个 ObjectId
        String userProperty = userClusterRegulation.getUserProperty();
        int dotIndex = userProperty.indexOf(".");
        String firstPropertyName = userProperty.substring(0, dotIndex);

        // 处理列表属性
        if (firstPropertyName.endsWith("List")) {
            String lastPropertyName = userProperty.substring(dotIndex + 1);
            Object userInstance = Class.forName(User.class.getName()).newInstance();
            String containedClassName = ReflectionUtil.getFieldClassName(userInstance.getClass(), firstPropertyName);
            String relatedProperty = "";
            Object containedInstance = Class.forName(containedClassName).newInstance();
            try {
                relatedProperty = ReflectionUtil.getFieldNameByClassName(Class.forName(containedClassName), User.class.getName());
            } catch (Exception e) {
                // do nothing
            }
            sb.append(" exists( select b from " + containedClassName + " b where b." + relatedProperty + ".objectId = o.objectId");

            if (lastPropertyName.endsWith("objectId")) {
                String className = ReflectionUtil.getDeclaredField(containedInstance, lastPropertyName.replace(".objectId", "")).getType().getName();
                Entity containedEntity = getEntityByClassName(className);
                if (containedEntity.getEntityType().getCodeTag().equalsIgnoreCase(EntityType.Recursion.name())) {
                    AbstractRecursionEntity recursionEntity = (AbstractRecursionEntity) getEntityByClassName(entity.getClassName(), userClusterDetail.getRelatedObjectId());
                    String nodeLevelCode = recursionEntity.getNodeLevelCode();
                    if (containedEntity.getEntityName().equals(Organization.class.getSimpleName())) {
                        if (ConfigUtil.getSystemConfigInfo().getDatabaseType().equalsIgnoreCase("Oracle")) {
                            sb.append(" and instr(user.organization.nodeLevelCode" + ",'" + nodeLevelCode + "')=0)");
                        } else {
                            sb.append(" and charindex('" + nodeLevelCode + "'," + "user.organization.nodeLevelCode)=1)");
                        }
                    } else {
                        // TODO
                    }
                } else {
                    sb.append(" and b." + lastPropertyName + "='" + userClusterDetail.getRelatedObjectId() + "')");
                }
            } else {
                sb.append(" and b." + lastPropertyName + "='" + userClusterDetail.getRelatedObjectId() + "')");
            }
        } else {
            if (entityType == EntityType.Recursion) {
                AbstractRecursionEntity recursionEntity = (AbstractRecursionEntity) getEntityByClassName(entity.getClassName(), userClusterDetail.getRelatedObjectId());
                String nodeLevelCode = recursionEntity.getNodeLevelCode();
                if (ConfigUtil.getSystemConfigInfo().getDatabaseType().equalsIgnoreCase("Oracle")) {
                    sb.append(" and instr(user.organization.nodeLevelCode" + ",'" + nodeLevelCode + "')=0)");
                } else {
                    sb.append(" and charindex('" + nodeLevelCode + "'," + "user.organization.nodeLevelCode)=1)");
                }
            } else {
                sb.append(userProperty + "='" + userClusterDetail.getRelatedObjectId() + "'");
            }
        }

        if (CollectionUtil.isEmpty(userClusterDetail.getChildList())) {
            sb.append(")");
            return;
        }

        sb.append(" and (");
        for (int i = 0; i < userClusterDetail.getChildList().size(); i++) {
            StringBuilder sb2 = new StringBuilder();
            throughUserClusterRegulationTree(userClusterDetail.getChildList().get(i), sb2);
            if (i == 0) {
                sb.append("(" + sb2 + ")");
            } else {
                sb.append(" or (" + sb2 + ")");
            }
        }
        sb.append("))");
    }

    public UserClusterRegulation getRootUserClusterRegulation() throws Exception {

        return getRootEntity(UserClusterRegulation.class);
    }

    //==================== 属性方法 ====================================================

    public PermissionService getPermissionService() {

        return permissionService;
    }

    public void setPermissionService(PermissionService permissionService) {

        this.permissionService = permissionService;
    }
}