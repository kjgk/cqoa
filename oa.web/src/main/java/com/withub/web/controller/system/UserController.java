package com.withub.web.controller.system;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.system.po.Organization;
import com.withub.model.system.po.User;
import com.withub.model.system.po.UserOrganizationHistory;
import com.withub.model.system.po.UserOrganizationRole;
import com.withub.service.system.UserService;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/system")
public class UserController extends BaseController {

    //================================== 属性声明 =========================================================

    @Autowired
    private UserService userService;

    //================================= Controller 方法 ==================================================

    @RequestMapping(value = "/user/query", method = RequestMethod.GET)
    public void queryUser(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(User.class);
        this.setPageInfoQueryCondition(request, queryInfo);

        this.setStringValueEqualsQueryCondition(request, queryInfo, "status.codeTag", "statusTag");
        this.setStringValueEqualsQueryCondition(request, queryInfo, "status.objectId", "statusId");

        String organizationId = request.getParameter("organizationId");
        if (StringUtil.isNotEmpty(organizationId)) {
            Organization organization = userService.get(Organization.class, organizationId);
            this.setNodeLevelCodeQueryCondition(queryInfo, "organization.nodeLevelCode", organization);
        }

        this.setInputFieldQueryCondition(request, queryInfo, "name");
        this.setStringValueEqualsQueryCondition(request, queryInfo, "role.objectId", "roleId");

        this.setAscOrderBy(queryInfo, "name");

        RecordsetInfo recordsetInfo = userService.queryUser(queryInfo);

        List list = recordsetInfo.getEntityList();
        List items = new ArrayList();
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        for (User user : (List<User>) list) {
            Map<String, Object> item = new HashMap();
            item.put("objectId", user.getObjectId());
            item.put("name", user.getName());
            item.put("code", user.getCode());
            item.put("administrator", user.getAdministrator());
            if (user.getOrganization() != null) {
                item.put("organization", user.getOrganization().getFullName());
            }
            if (user.getRole() != null) {
                item.put("role", user.getRole().getName());
            }
            if (user.getSex() != null) {
                item.put("sex", user.getSex().getName());
            }
            item.put("birthday", DateUtil.getDateFormatString(user.getBirthday(), "yyyy-MM-dd"));
            item.put("idCard", user.getIdCard());
            if (user.getNation() != null) {
                item.put("nation", user.getNation().getName());
            }
            item.put("email", user.getEmail());
            if (user.getAccount() != null) {
                item.put("accountId", user.getAccount().getObjectId());
                item.put("account", user.getAccount().getName());
            }
            item.put("mobile", user.getMobile());
            item.put("employmentDate", DateUtil.getDateFormatString(user.getEmploymentDate(), "yyyy-MM-dd"));
            item.put("status", user.getStatus().getName());
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public void createUser(ModelMap modelMap, User user) throws Exception {

        userService.addUser(user);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public void updateUser(ModelMap modelMap, User user) throws Exception {

        userService.updateUser(user);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/user/load/{objectId}", method = RequestMethod.GET)
    public void loadUser(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        User user = userService.get(User.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", user.getObjectId());
        model.put("name", user.getName());
        model.put("code", user.getCode());
        if (user.getNation() != null) {
            model.put("nation.objectId", user.getNation().getObjectId());
        }
        model.put("sex.objectId", user.getSex().getObjectId());
        model.put("birthday", DateUtil.getDateFormatString(user.getBirthday(), "yyyy-MM-dd"));
        model.put("idCard", user.getIdCard());
        model.put("mobile", user.getMobile());
        model.put("email", user.getEmail());
        if (user.getOrganization() != null) {
            model.put("organization.objectId", user.getOrganization().getObjectId());
        }
        if (user.getRole() != null) {
            model.put("role.objectId", user.getRole().getObjectId());
        }
        model.put("employmentDate", DateUtil.getDateFormatString(user.getEmploymentDate(), "yyyy-MM-dd"));
        modelMap.put("data", model);

        List organizationRoleList = new ArrayList();
        if (CollectionUtil.isNotEmpty(user.getOrganizationRoleList())) {
            for (UserOrganizationRole userOrganizationRole : user.getOrganizationRoleList()) {
                if (userOrganizationRole.getOrderNo() == 1) {
                    continue;
                }
                Map organizatonRole = new HashMap();
                organizatonRole.put("organizationId", userOrganizationRole.getOrganization().getObjectId());
                organizatonRole.put("organizationName", userOrganizationRole.getOrganization().getName());
                organizatonRole.put("roleId", userOrganizationRole.getRole().getObjectId());
                organizatonRole.put("roleName", userOrganizationRole.getRole().getName());
                organizationRoleList.add(organizatonRole);
            }
        }
        modelMap.put("organizationRoleList", organizationRoleList);

        modelMap.put("success", true);
    }

    @RequestMapping(value = "/user/delete/{objectId}", method = RequestMethod.GET)
    public void deleteUser(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        userService.deleteUser(objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/user/search", method = RequestMethod.GET)
    public void searchUser(@RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "objectId", required = false) String objectId, ModelMap modelMap) throws Exception {

        List<User> list = new ArrayList();
        List items = new ArrayList();
        if (StringUtil.isNotEmpty(keyword)) {
            list = userService.searchUser(keyword);
        } else if (StringUtil.isNotEmpty(objectId)) {
            User user = (User) userService.get(User.class, objectId);
            list.add(user);
        }

        for (User user : list) {
            HashMap item = new HashMap();
            item.put("objectId", user.getObjectId());
            item.put("name", user.getName());
            item.put("code", user.getCode());
            if (user.getOrganization() != null) {
                item.put("organizationName", user.getOrganization().getName());
            }
            if (user.getRole() != null) {
                item.put("roleName", user.getRole().getName());
            }
            items.add(item);
        }
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/user/setAdministrator/{objectId}", method = RequestMethod.GET)
    public void setAdministrator(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        userService.setUserAdministrator(objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/user/cancelUserAdministrator/{objectId}", method = RequestMethod.GET)
    public void cancelUserAdministrator(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        userService.cancelUserAdministrator(objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/user/activeUser/{objectId}", method = RequestMethod.GET)
    public void activeUser(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        userService.activeUser(objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/user/archiveUser/{objectId}", method = RequestMethod.GET)
    public void archiveUser(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        userService.archiveUser(objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/user/UserOrganizationHistory", method = RequestMethod.GET)
    public void listUserOrganizationHistory(@RequestParam("userId") String userId, ModelMap modelMap) throws Exception {

        String hql = "select o from " + UserOrganizationHistory.class.getName() + " o"
                + " where o.user.objectId=? order by o.enterDate desc";
        List list = userService.listByHql(hql, userId);
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (UserOrganizationHistory userOrganizationHistory : (List<UserOrganizationHistory>) list) {
            HashMap item = new HashMap();
            item.put("objectId", userOrganizationHistory.getObjectId());
            item.put("user", userOrganizationHistory.getUser().getName());
            item.put("organization", userOrganizationHistory.getOrganization().getName());
            item.put("enterDate", userOrganizationHistory.getEnterDate().getTime());
            if (userOrganizationHistory.getLeaveDate() != null) {
                item.put("leaveDate", userOrganizationHistory.getLeaveDate().getTime());
            }
            items.add(item);
        }

        modelMap.put("total", items.size());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/user/listByRoleTag/{roleTag}", method = RequestMethod.GET)
    public void listByRoleTag(ModelMap modelMap, @PathVariable("roleTag") String roleTag) throws Exception {

        List<User> userList = userService.listByRoleTag(roleTag);
        List list = new ArrayList();
        if (CollectionUtil.isNotEmpty(userList)) {
            for (User user : userList) {
                Map item = new HashMap();
                item.put("objectId", user.getObjectId());
                item.put("name", user.getName());
                item.put("organizationId", user.getOrganization().getObjectId());
                list.add(item);
            }
        }
        modelMap.put("items", list);
    }

    @RequestMapping(value = "/user/listByOrganizationId/{organizationId}", method = RequestMethod.GET)
    public void listByOrganizationId(ModelMap modelMap, @PathVariable("organizationId") String organizationId) throws Exception {

        List<User> userList = userService.listByOrganizationId(organizationId);
        List list = new ArrayList();
        if (CollectionUtil.isNotEmpty(userList)) {
            for (User user : userList) {
                Map item = new HashMap();
                item.put("objectId", user.getObjectId());
                item.put("name", user.getName());
                item.put("organizationId", user.getOrganization().getObjectId());
                list.add(item);
            }
        }
        modelMap.put("items", list);
    }
}
