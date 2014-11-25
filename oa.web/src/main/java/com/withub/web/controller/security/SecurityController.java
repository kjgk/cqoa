package com.withub.web.controller.security;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.exception.BaseBusinessException;
import com.withub.model.system.enumeration.PasswordChangeResult;
import com.withub.model.system.po.*;
import com.withub.service.system.*;
import com.withub.util.SpringSecurityUtil;
import com.withub.web.common.BaseController;
import com.withub.web.common.ext.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping(value = "/security")
public class SecurityController extends BaseController {

    //============================= 属性声明 =============================================================

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private CodeService codeService;


    //============================= Controller 方法 ======================================================

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String main(HttpServletRequest request, HttpServletResponse response) throws Exception {

        return "main";
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {

        return "login";
    }

    @RequestMapping(value = "/loadMenu", method = RequestMethod.GET)
    public void loadMenu(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            List<Menu> menuList = permissionService.listCurrentUserMenuByParentId("");
            for (Menu menu : menuList) {
                TreeNode node = new TreeNode();
                node.setId(menu.getObjectId());
                node.setText(menu.getName());
                node.setIconCls(menu.getImage());
                node.setExpanded(menu.getExpand() == 1);
                node.setLeaf(false);
                node.setCls("menu-blue menu-bold");
                nodes.add(node);
            }
        } else {
            List<Menu> menuList = permissionService.listCurrentUserMenuByParentId(nodeId);
            for (Menu menu : menuList) {
                TreeNode node = new TreeNode();
                node.setId(menu.getObjectId());
                node.setText(menu.getName());
                node.setIconCls(menu.getImage());
                //node.setExpanded(false);
                node.setExpanded(menu.getExpand() == 1);
                node.setCls("menu-blue");
                node.setLeaf(menu.getLeaf() == 1);
                if (StringUtil.isNotEmpty(menu.getUrl()) && StringUtil.isEmpty(menu.getSubMenuMethod())) {
                    node.getAttributes().put("url", menu.getUrl());
                    node.getAttributes().put("page", menu.getUrl().split("[?]")[0]);
                    node.getAttributes().put("pageParams", bindUrlArgs(menu.getUrl()));
                    node.getAttributes().put("openMode", menu.getOpenMode());
                    node.getAttributes().put("requiredLogin", menu.getRequiredLogin());
                }
                nodes.add(node);
            }
        }
    }

    @RequestMapping(value = "/getCurrentUserInfo", method = RequestMethod.GET)
    public void getCurrentUserInfo(HttpSession session, ModelMap modelMap) throws Exception {

        User currentUser = SpringSecurityUtil.getCurrentUser();
        if (currentUser != null) {
            Map<String, Object> userInfo = new HashMap<String, Object>();
            userInfo.put("objectId", currentUser.getObjectId());
            userInfo.put("name", currentUser.getName());
            userInfo.put("account", currentUser.getAccount().getName());
            userInfo.put("isAdmin", currentUser.getAdministrator());
            String parentOrganizationId = "";
            if (currentUser.getAdministrator() == 1) {
                parentOrganizationId = organizationService.getRootEntity(Organization.class).getObjectId();
            } else {
                if (currentUser.getOrganization() != null && currentUser.getOrganization().getParent() != null && currentUser.getOrganization().getNodeLevel() > 3) {
                    parentOrganizationId = currentUser.getOrganization().getParent().getObjectId();
                }
            }

            userInfo.put("parentOrganizationId", parentOrganizationId);
            userInfo.put("organizationId", currentUser.getOrganization() == null ? "" : currentUser.getOrganization().getObjectId());
            userInfo.put("organizationName", currentUser.getOrganization() == null ? "" : currentUser.getOrganization().getFullName());
            modelMap.put("userInfo", userInfo);
        } else {
            modelMap.put("userInfo", null);
        }

    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public void changePassword(@RequestParam("account") String account, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, ModelMap modelMap) throws Exception {

        String accountId = SpringSecurityUtil.getAccountId();
        PasswordChangeResult result = accountService.changePassword(accountId, account, oldPassword, newPassword);

        if (result == PasswordChangeResult.OldPasswordError) {
            throw new BaseBusinessException("", "原始密码输入错误！");
        }
    }

    @RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
    public void modifyPassword(@RequestBody Map data, ModelMap modelMap) throws Exception {

        String accountId = SpringSecurityUtil.getAccountId();
        PasswordChangeResult result = accountService.changePassword(accountId, "", (String) data.get("oldPassword"), (String) data.get("newPassword"));

        if (result == PasswordChangeResult.OldPasswordError) {
            throw new BaseBusinessException("", "原始密码输入错误！");
        }
    }

    @RequestMapping(value = "/onlineUser/query", method = RequestMethod.GET)
    public void queryOnlineUser(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(OnlineUser.class);
        this.setPageInfoQueryCondition(request, queryInfo);
        this.setDateRangeQueryCondition(request, queryInfo, "loginTime");

        this.setInputFieldQueryCondition(request, queryInfo, "user.name", "name");
        String organizationId = request.getParameter("organizationId");

        if (StringUtil.isNotEmpty(organizationId)) {
            Organization organization = securityService.get(Organization.class, organizationId);
            this.setNodeLevelCodeQueryCondition(queryInfo, "user.organization.nodeLevelCode", organization);
        }

        this.setDescOrderBy(queryInfo, "loginTime");

        RecordsetInfo recordsetInfo = securityService.queryOnlineUser(queryInfo);

        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        for (OnlineUser onlineUser : (List<OnlineUser>) list) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", onlineUser.getObjectId());
            item.put("name", onlineUser.getUser().getName());
            item.put("account", onlineUser.getAccount().getName());
            item.put("code", onlineUser.getUser().getCode());
            item.put("account", onlineUser.getUser().getAccount().getName());
            item.put("loginType", onlineUser.getLoginType().getName());
            if (onlineUser.getUser().getOrganization() != null) {
                item.put("organization", onlineUser.getUser().getOrganization().getFullName());
            }
            item.put("clientIdentityCode", onlineUser.getClientIdentityCode());
            Date loginTime = onlineUser.getLoginTime();
            item.put("loginTime", loginTime.getTime());
            String accountId = onlineUser.getAccount().getObjectId();
            item.put("accountId", accountId);
            Date lastActivityDate = new Date();
            long timeDiff = lastActivityDate.getTime() - loginTime.getTime();
            long second = (long) Math.ceil(timeDiff / 1000 / 60);
            item.put("onlineTime", second);
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/kickoutOnlineUser", method = RequestMethod.GET)
    public void kickoutOnlineUser(HttpServletRequest request, ModelMap modelMap) throws Exception {

        String objectId = request.getParameter("objectId");
        String accountId = request.getParameter("accountId");

        securityService.kickoutAccount(accountId, "");

        SecurityLog securityLog = (SecurityLog) securityService.getByPropertyValue(SecurityLog.class, "onlineUserId", objectId);
        Code logoutType = codeService.getCodeByTag("LogoutType", "KickOut");
        securityLog.setLogoutType(logoutType);

        securityService.update(securityLog);

        securityService.delete(OnlineUser.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/securityLog/query", method = RequestMethod.GET)
    public void querySecurityLog(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(SecurityLog.class);
        this.setPageInfoQueryCondition(request, queryInfo);
        this.setDateRangeQueryCondition(request, queryInfo, "loginTime");

        this.setInputFieldQueryCondition(request, queryInfo, "user.name", "name");
        String organizationId = request.getParameter("organizationId");

        if (StringUtil.isNotEmpty(organizationId)) {
            Organization organization = securityService.get(Organization.class, organizationId);
            this.setNodeLevelCodeQueryCondition(queryInfo, "user.organization.nodeLevelCode", organization);
        }

        this.setDescOrderBy(queryInfo, "loginTime");

        RecordsetInfo recordsetInfo = securityService.querySecurityLog(queryInfo);

        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        for (SecurityLog securityLog : (List<SecurityLog>) list) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", securityLog.getObjectId());
            item.put("name", securityLog.getUser().getName());
            item.put("account", securityLog.getUser().getAccount().getName());
            item.put("loginType", securityLog.getLoginType().getName());
            if (securityLog.getUser().getOrganization() != null) {
                item.put("organization", securityLog.getUser().getOrganization().getFullName());
            }
            item.put("clientIdentityCode", securityLog.getClientIdentityCode());
            item.put("loginTime", securityLog.getLoginTime().getTime());
            if (securityLog.getLogoutTime() != null) {
                if (String.valueOf(securityLog.getLogoutTime().getTime()).equals("-2209017600000"))
                    item.put("logoutTime", null);
                else
                    item.put("logoutTime", securityLog.getLogoutTime().getTime());
            }
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    private Map bindUrlArgs(String url) {

        Map map = new HashMap();
        if (url.indexOf("?") == -1) {
            return map;
        }
        try {
            String argStr = url.split("[?]")[1];
            for (String arg : argStr.split("&")) {
                String key = arg.substring(0, arg.indexOf("="));
                String value = arg.replaceAll(key + "=", "");
                map.put(key, value);
            }
        } catch (Exception e) {
            return map;
        }
        return map;
    }

    //============================== 属性方法 =============================================================

    public PermissionService getPermissionService() {

        return permissionService;
    }

    public void setPermissionService(PermissionService permissionService) {

        this.permissionService = permissionService;
    }

    public AccountService getAccountService() {

        return accountService;
    }

    public void setAccountService(AccountService accountService) {

        this.accountService = accountService;
    }

    public OrganizationService getOrganizationService() {

        return organizationService;
    }

    public void setOrganizationService(OrganizationService organizationService) {

        this.organizationService = organizationService;
    }

    public MenuService getMenuService() {

        return menuService;
    }

    public void setMenuService(MenuService menuService) {

        this.menuService = menuService;
    }

    public SecurityService getSecurityService() {

        return securityService;
    }

    public void setSecurityService(SecurityService securityService) {

        this.securityService = securityService;
    }

    public CodeService getCodeService() {

        return codeService;
    }

    public void setCodeService(CodeService codeService) {

        this.codeService = codeService;
    }
}
