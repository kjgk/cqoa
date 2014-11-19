package com.withub.service.impl.oa;

import com.withub.common.util.CollectionUtil;
import com.withub.model.system.po.*;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.DataImportService;
import com.withub.service.system.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("dataImportService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class DataImportServiceImpl extends EntityServiceImpl implements DataImportService {

    private static final Logger logger = LoggerFactory.getLogger(DataImportServiceImpl.class);

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private UserClusterService userClusterService;

    public void importOrganization() throws Exception {

        logger.info("开始同步组织机构");

        List list = listBySql("select organize_id, organize_name from t_sys_organize where use_flag = 1 order by organize_id");

        Organization root = organizationService.getRootOrganization();
        User systemUser = userService.getSystemUser();
        List<String> organizationCodeList = new ArrayList();
        if (CollectionUtil.isNotEmpty(root.getChildList())) {
            for (Organization organization : root.getChildList()) {
                organizationCodeList.add(organization.getCode());
            }
        }

        if (CollectionUtil.isNotEmpty(list)) {
            for (Object[] data : (List<Object[]>) list) {
                String orgId = (String) data[0];
                if (orgId != null && orgId.length() > 12 && !organizationCodeList.contains(orgId)) {
                    Organization organization = new Organization();
                    organization.setParent(root);
                    organization.setCurrentUser(systemUser);
                    organization.setName((String) data[1]);
                    organization.setCode(orgId);
                    organization.setOrganizationType(codeService.getCodeByTag("OrganizationType", "Department"));
                    organizationService.addOrganization(organization);
                    logger.info("正在导入组织机构：" + organization.getName());
                }
            }
        }
        logger.info("完成同步组织机构");
    }

    public void importUser() throws Exception {

        logger.info("开始同步用户");

        List list = listBySql("select user_id, user_name, login_name, user_cryptogram, salt, organize_id, mobile_phone1 from t_sys_user " +
                " where use_flag = 1 and user_id not in('000', '0000', '0000000000', '0000000001')  order by user_id");

        List userList = listByHql("from " + User.class.getName() + " where objectStatus = 1");
        List userCodeList = new ArrayList();
        if (CollectionUtil.isNotEmpty(userList)) {
            for (User user : (List<User>) userList) {
                userCodeList.add(user.getCode());
            }
        }

        AuthorizationCluster roleAuthorizationCluster = get(AuthorizationCluster.class, "632080FD-BA38-4B7E-BD1C-DD44463708C3"); //员工的授权对象
        Organization rootOrganization = organizationService.getRootOrganization();
        Role rolePersonnel = roleService.getRoleByTag("Personnel");
        Code genderUnknow = codeService.getCodeByTag("sex", "unknown");
        User systemUser = userService.getSystemUser();

        if (CollectionUtil.isNotEmpty(list)) {
            boolean flag = false;
            for (Object[] data : (List<Object[]>) list) {
                String userId = (String) data[0];
                if (userId != null && !userCodeList.contains(userId)) {
                    Organization organization = organizationService.getOrganizationByCode((String) data[5]);
                    if (organization == null) {
                        organization = rootOrganization;
                    }
                    User user = new User();
                    user.setCurrentUser(systemUser);
                    user.setCode(userId);
                    user.setName((String) data[1]);
                    user.setSex(genderUnknow);
                    user.setRole(rolePersonnel);
                    user.setOrganization(organization);
                    user.setMobile((String) data[6]);
                    userService.addUser(user, false);
                    flag = true;
                    logger.info("正在导入用户：" + user.getName());
                }
            }

            if (flag) {
                logger.info("更新用户缓存");
                roleAuthorizationCluster.setCurrentUser(systemUser);
                userClusterService.updateUserClusterCache(roleAuthorizationCluster, null);
            }
        }

        logger.info("完成同步用户");
    }

}
