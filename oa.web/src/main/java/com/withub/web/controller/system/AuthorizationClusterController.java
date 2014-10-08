package com.withub.web.controller.system;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.system.po.*;
import com.withub.service.system.AuthorizationClusterService;
import com.withub.service.system.PermissionService;
import com.withub.util.SpringSecurityUtil;
import com.withub.web.common.BaseController;
import com.withub.web.common.ext.CheckedTreeNode;
import com.withub.web.common.ext.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/system")
public class AuthorizationClusterController extends BaseController {

    //======================= 属性声明 ===================================================

    @Autowired
    private AuthorizationClusterService authorizationClusterService;

    @Autowired
    private PermissionService permissionService;

    //======================= Controller 方法 =============================================

    @RequestMapping(value = "/authorizationCluster/query", method = RequestMethod.GET)
    public void queryAuthorizationCluster(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(AuthorizationCluster.class);

        this.setPageInfoQueryCondition(request, queryInfo);
        this.setInputFieldQueryCondition(request, queryInfo, "name");

        this.setAscOrderBy(queryInfo, "orderNo");

        RecordsetInfo recordsetInfo = authorizationClusterService.query(queryInfo);

        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (AuthorizationCluster authorizationCluster : (List<AuthorizationCluster>) list) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", authorizationCluster.getObjectId());
            item.put("name", authorizationCluster.getName());
            item.put("allowRepetition", authorizationCluster.getAllowRepetition());
            item.put("priority", authorizationCluster.getPriority());
            item.put("enable", authorizationCluster.getEnable());
            item.put("description", authorizationCluster.getDescription());
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/authorizationCluster/create", method = RequestMethod.POST)
    public void createAuthorizationCluster(ModelMap modelMap, AuthorizationCluster authorizationCluster) throws Exception {

        authorizationCluster.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        authorizationClusterService.createAuthorizationCluster(authorizationCluster);
    }

    @RequestMapping(value = "/authorizationCluster/update", method = RequestMethod.POST)
    public void updateAuthorizationCluster(ModelMap modelMap, AuthorizationCluster authorizationCluster) throws Exception {

        authorizationCluster.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        authorizationClusterService.updateAuthorizationCluster(authorizationCluster);
    }

    @RequestMapping(value = "/authorizationCluster/load/{objectId}", method = RequestMethod.GET)
    public void loadAuthorizationCluster(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        AuthorizationCluster authorizationCluster = authorizationClusterService.get(AuthorizationCluster.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", authorizationCluster.getObjectId());
        model.put("name", authorizationCluster.getName());
        model.put("priority", authorizationCluster.getPriority());
        model.put("enable", authorizationCluster.getEnable());
        model.put("allowRepetition", authorizationCluster.getAllowRepetition());
        model.put("description", authorizationCluster.getDescription());


        List userClusterDetails = new ArrayList();
        for (UserClusterDetail userClusterDetail : authorizationCluster.getUserClusterDetailList()) {
            userClusterDetails.add(userClusterDetail.getUserClusterRegulation().getEntity().getEntityType().getCodeTag() + "_" + userClusterDetail.getUserClusterRegulation().getObjectId() + "_" + userClusterDetail.getRelatedObjectId());
        }
        model.put("userClusterDetails", userClusterDetails);
        modelMap.put("data", model);
    }

    @RequestMapping(value = "/authorizationCluster/delete/{objectId}", method = RequestMethod.GET)
    public void deleteAuthorizationCluster(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        authorizationClusterService.deleteAuthorizationCluster(objectId);
    }

    @RequestMapping(value = "/authorizationCluster/loadPermissionRegulationTree", method = RequestMethod.GET)
    public void loadPermissionRegulationTree(@RequestParam(value = "node") String nodeId
            , @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (nodeId.equals("Root")) {
            return;
        }
        String type = nodeId.split("_")[0];
        String id = nodeId.split("_")[1];

        if (StringUtil.compareValue(type, Permission.class.getSimpleName())) {
            Permission permission = authorizationClusterService.get(Permission.class, id);
            if (CollectionUtil.isNotEmpty(permission.getPermissionRegulationList())) {
                for (PermissionRegulation permissionRegulation : permission.getPermissionRegulationList()) {
                    CheckedTreeNode node = new CheckedTreeNode();
                    node.setObjectId(permissionRegulation.getObjectId());
                    node.setText(permissionRegulation.getName());
                    node.setType(PermissionRegulation.class.getSimpleName());
                    node.setLeaf(false);
                    node.setExpanded(true);
                    nodes.add(node);
                }
            }
            return;
        }

        PermissionRegulation permissionRegulation = authorizationClusterService.get(PermissionRegulation.class, id);

        if (CollectionUtil.isNotEmpty(permissionRegulation.getChildList())) {
            for (PermissionRegulation child : permissionRegulation.getChildList()) {
                CheckedTreeNode node = new CheckedTreeNode();
                node.setObjectId(child.getObjectId());
                node.setText(child.getName());
                node.setType(PermissionRegulation.class.getSimpleName());
                node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
                node.setExpanded(true);
                nodes.add(node);
            }
        }
    }

    @RequestMapping(value = "/authorizationCluster/assignPermission", method = RequestMethod.POST)
    public void assignPermission(ModelMap modelMap, AuthorizationPermission authorizationPermission) throws Exception {

        permissionService.assignPermission(authorizationPermission);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/authorizationCluster/getAssignedPermission", method = RequestMethod.POST)
    public void getAssignedPermission(ModelMap modelMap, @RequestParam(value = "authorizationClusterId") String authorizationClusterId
            , @RequestParam(value = "permissionId") String permissionId) throws Exception {

        AuthorizationPermission authorizationPermission = permissionService.getAssignedPermission(authorizationClusterId, permissionId);
        if (authorizationPermission != null) {
            String hql = "select o from " + AuthorizationRegulation.class.getName() + " o"
                    + " where o.authorizationPermission.objectId=?";
            List list = permissionService.listByHql(hql, authorizationPermission.getObjectId());
            if (CollectionUtil.isNotEmpty(list)) {
                List<String> idList = new ArrayList();
                for (AuthorizationRegulation authorizationRegulation : (List<AuthorizationRegulation>) list) {
                    if (!idList.contains(authorizationRegulation.getPermissionRegulation().getObjectId())) {
                        idList.add(authorizationRegulation.getPermissionRegulation().getObjectId());
                    }
                }
                modelMap.put("permissionRegualtionIdList", idList);
            }

            modelMap.put("assigned", true);
        }
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/authorizationCluster/cancelPermission", method = RequestMethod.POST)
    public void cancelPermission(ModelMap modelMap, @RequestParam(value = "authorizationClusterId") String authorizationClusterId
            , @RequestParam(value = "permissionId") String permissionId) throws Exception {

        permissionService.cancelPermission(authorizationClusterId, permissionId);
        modelMap.put("success", true);
    }

}
