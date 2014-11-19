package com.withub.service.impl.system;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.exception.BaseBusinessException;
import com.withub.model.system.enumeration.SystemConstant;
import com.withub.model.system.enumeration.UserStatus;
import com.withub.model.system.event.SystemEventPublisher;
import com.withub.model.system.po.*;
import com.withub.service.EntityServiceImpl;
import com.withub.service.system.CodeService;
import com.withub.service.system.OrganizationService;
import com.withub.service.system.UserService;
import com.withub.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class UserServiceImpl extends EntityServiceImpl implements UserService {

    //============================== 属性声明 =============================================================

//    @Autowired
//    private AccountService accountService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CodeService codeService;

    //=============================== 接口实现 ============================================================

    public RecordsetInfo queryUser(QueryInfo queryInfo) throws Exception {

        /*SqlExpressionConfig sqlExpressionConfig = new SqlExpressionConfig();
        sqlExpressionConfig.setPropertyName("objectId");
        sqlExpressionConfig.setPropertyValue(SystemConstant.USER_ADMINISTOR);
        sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.NotEquals);
        QueryConditionNode queryConditionNode = new QueryConditionNode();
        queryConditionNode.setSqlExpressionConfig(sqlExpressionConfig);
        queryInfo.getQueryConditionTree().getUserConditionNode().appendNode(queryConditionNode);*/

        RecordsetInfo recordsetInfo = query(queryInfo);

        return recordsetInfo;
    }

    public User getUserById(String userId) throws Exception {

        User user = (User) get(User.class, userId);
        return user;
    }

    public User getUserByCode(String code) throws Exception {

        User user = (User) getByPropertyValue(User.class, "code", code);

        return user;
    }

    public User getSystemUser() throws Exception {

        User user = getUserById(SystemConstant.USER_SYSTEM);
        return user;
    }

    public void addUser(User user) throws Exception {

        addUser(user, true);
    }

    public void addUser(User user, boolean event) throws Exception {

        user.setObjectId(StringUtil.getUuid());
        user.setAdministrator(0);

        Code activeStatus = codeService.getCodeByEnum(UserStatus.Active);
        user.setStatus(activeStatus);

        if (CollectionUtil.isNotEmpty(user.getOrganizationRoleList())) {
            user.setOrganization(user.getOrganizationRoleList().get(0).getOrganization());
            user.setRole(user.getOrganizationRoleList().get(0).getRole());
        }

        save(user);

        // 保存用户角色
        UserOrganizationRole userOrganizationRole = new UserOrganizationRole();
        userOrganizationRole.setOrganization(user.getOrganization());
        userOrganizationRole.setRole(user.getRole());
        userOrganizationRole.setUser(user);
        userOrganizationRole.setOrderNo(1);
        save(userOrganizationRole);

        if (CollectionUtil.isNotEmpty(user.getOrganizationRoleList())) {
            int orderNo = 2;
            for (UserOrganizationRole uor : user.getOrganizationRoleList()) {
                userOrganizationRole.setUser(user);
                userOrganizationRole.setOrderNo(orderNo++);
                save(uor);
            }
        }

        // 保存用户组织机构
        UserOrganizationHistory userOrganizationHistory = new UserOrganizationHistory();
        userOrganizationHistory.setUser(user);
        userOrganizationHistory.setOrganization(user.getOrganization());
        userOrganizationHistory.setEnterDate(DateUtil.getCurrentTime());
        userOrganizationHistory.setStatus(1);
        save(userOrganizationHistory);

        if (event) {
            SystemEventPublisher.publishUserSaveEvent(this, user);
        }
    }

    public void updateUser(User user) throws Exception {

        User oldUser = getUserById(user.getObjectId());
//        delete(oldUser.getOrganizationRoleList());

        user.setStatus(oldUser.getStatus());
        user.setAdministrator(oldUser.getAdministrator());

        // 保存用户角色
        UserOrganizationRole userOrganizationRole = new UserOrganizationRole();
        userOrganizationRole.setOrganization(user.getOrganization());
        userOrganizationRole.setRole(user.getRole());
        userOrganizationRole.setUser(user);
        userOrganizationRole.setOrderNo(1);
        save(userOrganizationRole);

        if (CollectionUtil.isNotEmpty(user.getOrganizationRoleList())) {
            int orderNo = 2;
            for (UserOrganizationRole uor : user.getOrganizationRoleList()) {
                uor.setUser(user);
                uor.setOrderNo(orderNo++);
                save(uor);
            }
        }

        // 保存用户组织机构
        String hql = "select o from " + UserOrganizationHistory.class.getName() + " o"
                + " where o.user.objectId=? and o.status=1";
        UserOrganizationHistory last = (UserOrganizationHistory) getByHql(hql, user.getObjectId());
        if (last == null) {
            UserOrganizationHistory userOrganizationHistory = new UserOrganizationHistory();
            userOrganizationHistory.setUser(user);
            userOrganizationHistory.setOrganization(user.getOrganization());
            userOrganizationHistory.setEnterDate(DateUtil.getCurrentTime());
            userOrganizationHistory.setStatus(1);
            save(userOrganizationHistory);
        } else {
            if (!oldUser.getOrganization().getObjectId().equals(user.getOrganization().getObjectId())) {
                last.setLeaveDate(DateUtil.getCurrentTime());
                last.setStatus(0);
                save(last);
            }
        }

        save(user);

        SystemEventPublisher.publishUserSaveEvent(this, user);
    }

    public void archiveUser(String userId) throws Exception {

        User user = getUserById(userId);
        if (user.getObjectId().equals(SystemConstant.USER_ADMINISTOR) ||
                user.getObjectId().equals(SystemConstant.USER_SYSTEM)) {
            throw new BaseBusinessException("", "不允许归档系统内置用户!");
        }

        // 设置用户状态
        user = getUserById(user.getObjectId());
        user.setStatus(codeService.getCodeByEnum(UserStatus.Archive));
        save(user);


        // 删除相关角色
        delete(user.getOrganizationRoleList());

        // 发布用户归档事件
        SystemEventPublisher.publishUserArchiveEvent(this, user);
    }

    public void activeUser(String userId) throws Exception {

        User user = getUserById(userId);
        user.setStatus(codeService.getCodeByEnum(UserStatus.Active));
        save(user);

    }

    public void setUserAdministrator(String userId) throws Exception {

        User user = getUserById(userId);
        /*if (getCurrentUser().getAdministrator() == 0) {
            throw new BaseBusinessException("", "没有权限!");
        }*/

        User temp = getUserById(user.getObjectId());
        if (temp.getAdministrator() == 1) {
            return;
        }
        temp.setAdministrator(1);
        temp.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        save(temp);
    }

    public void cancelUserAdministrator(String userId) throws Exception {

        User user = getUserById(userId);
        /*if (getCurrentUser().getAdministrator() == 0) {
            throw new BaseBusinessException("", "没有权限!");
        }*/

        User temp = getUserById(user.getObjectId());
        if (temp.getAdministrator() == 0) {
            return;
        }
        temp.setAdministrator(0);
        temp.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        save(temp);
    }

    public void deleteUser(String userId) throws Exception {

        User user = getUserById(userId);
        if (user.getObjectId().equals(SystemConstant.USER_ADMINISTOR) ||
                user.getObjectId().equals(SystemConstant.USER_SYSTEM)) {
            throw new BaseBusinessException("", "不允许删除系统内置用户!");
        }

        user = getUserById(user.getObjectId());
        logicDelete(user);
//        logicDelete(user.getAccount());
        delete(user.getOrganizationRoleList());

        SystemEventPublisher.publishUserDeleteEvent(this, user);
    }

    public UserPhoto getPhotoByUserId(String userId) throws Exception {

        UserPhoto photo = (UserPhoto) getByPropertyValue(UserPhoto.class, "userId", userId);
        return photo;
    }

    public List searchUser(String keyword) throws Exception {

        String hql = "from User where name like ? or pinYin like ?" +
                " and objectStatus = 1 order by name";
        return listByHql(hql, "%" + keyword + "%", keyword + "%");
    }

    public List listByRoleTag(String roleTag) throws Exception {

        String hql = "from User where role.roleTag = ? and status.codeTag = ? and objectStatus = 1 order by name";
        return listByHql(hql, roleTag, UserStatus.Active.name());
    }

    //============================ 属性方法 ===============================================================


    public OrganizationService getOrganizationService() {

        return organizationService;
    }

    public void setOrganizationService(OrganizationService organizationService) {

        this.organizationService = organizationService;
    }

    public CodeService getCodeService() {

        return codeService;
    }

    public void setCodeService(CodeService codeService) {

        this.codeService = codeService;
    }
}