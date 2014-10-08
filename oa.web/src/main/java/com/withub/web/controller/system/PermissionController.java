package com.withub.web.controller.system;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.system.po.*;
import com.withub.service.system.MetadataService;
import com.withub.service.system.PermissionService;
import com.withub.web.common.BaseController;
import com.withub.web.common.ext.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/system")
public class PermissionController extends BaseController {

    //======================= 属性声明 ===================================================

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private PermissionService permissionService;

    //======================= Controller 方法 =============================================

    @RequestMapping(value = "/permission/loadManagerTree", method = RequestMethod.GET)
    public void loadManagerTree(@RequestParam(value = "node") String nodeId
            , @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            EntityCategory root = metadataService.getRootEntityCategory();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setType(EntityCategory.class.getSimpleName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
            return;
        }

        String id = nodeId.split("_")[1];
        String type = nodeId.split("_")[0];
        if (StringUtil.compareValue(EntityCategory.class.getSimpleName(), type)) {
            EntityCategory category = permissionService.get(EntityCategory.class, id);
            if (CollectionUtil.isNotEmpty(category.getChildList())) {
                for (EntityCategory child : category.getChildList()) {
                    TreeNode node = new TreeNode();
                    node.setObjectId(child.getObjectId());
                    node.setText(child.getName());
                    node.setType(EntityCategory.class.getSimpleName());
                    node.setLeaf(false);
                    nodes.add(node);
                }
            }

            if (CollectionUtil.isNotEmpty(category.getEntityList())) {
                for (Entity entity : category.getEntityList()) {
                    if (entity.getAllowAuthorization() == 0) {
                        continue;
                    }
                    TreeNode node = new TreeNode();
                    node.setObjectId(entity.getObjectId());
                    node.setText(entity.getName());
                    node.setType(Entity.class.getSimpleName());
                    node.setLeaf(false);
                    nodes.add(node);
                }
            }
        }

        if (StringUtil.compareValue(Entity.class.getSimpleName(), type)) {
            Entity entity = permissionService.get(Entity.class, id);
            if (CollectionUtil.isNotEmpty(entity.getPermissionList())) {
                for (Permission permission : entity.getPermissionList()) {
                    TreeNode node = new TreeNode();
                    node.setObjectId(permission.getObjectId());
                    node.setText(permission.getName());
                    node.setType(Permission.class.getSimpleName());
                    node.setLeaf(true);
                    nodes.add(node);
                }
            }
        }
    }

    @RequestMapping(value = "/permission/loadTree", method = RequestMethod.GET)
    public void loadTree(@RequestParam(value = "node") String nodeId, @RequestParam(value = "depth") int depth,
                         @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            EntityCategory root = metadataService.getRootEntityCategory();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setType(EntityCategory.class.getSimpleName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
            return;
        }

        String id = nodeId.split("_")[1];
        String type = nodeId.split("_")[0];
        if (StringUtil.compareValue(EntityCategory.class.getSimpleName(), type)) {
            EntityCategory category = permissionService.get(EntityCategory.class, id);
            if (CollectionUtil.isNotEmpty(category.getChildList())) {
                for (EntityCategory child : category.getChildList()) {
                    TreeNode node = new TreeNode();
                    node.setObjectId(child.getObjectId());
                    node.setText(child.getName());
                    node.setType(EntityCategory.class.getSimpleName());
                    node.setLeaf(depth == 1 && CollectionUtil.isEmpty(child.getChildList()));
                    nodes.add(node);
                }
            }
            if (depth > 1 && CollectionUtil.isNotEmpty(category.getEntityList())) {
                for (Entity entity : category.getEntityList()) {
                    TreeNode node = new TreeNode();
                    node.setObjectId(entity.getObjectId());
                    node.setText(entity.getName());
                    node.setType(Entity.class.getSimpleName());
                    node.setLeaf(false);
                    nodes.add(node);
                }
            }
        }

        if (StringUtil.compareValue(Entity.class.getSimpleName(), type)) {
            Entity entity = permissionService.get(Entity.class, id);
            if (depth > 2 && CollectionUtil.isNotEmpty(entity.getPermissionList())) {
                for (Permission permission : entity.getPermissionList()) {
                    TreeNode node = new TreeNode();
                    node.setObjectId(permission.getObjectId());
                    node.setText(permission.getName());
                    node.setType(Permission.class.getSimpleName());
                    node.setLeaf(true);
                    nodes.add(node);
                }
            }
        }
    }

    @RequestMapping(value = "/permission/loadTreePath", method = RequestMethod.GET)
    public void loadTreePath(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "node") ArrayList<String> nodes) throws Exception {

        String type = nodeId.split("_")[0];
        String id = nodeId.split("_")[1];

        String parentId = "";
        if (StringUtil.compareValue(EntityCategory.class.getSimpleName(), type)) {
            EntityCategory category = permissionService.get(EntityCategory.class, id);
            if (category.getNodeLevel() == 1) {
                return;
            }
            parentId = EntityCategory.class.getSimpleName() + "_" + category.getParent().getObjectId();
        }

        if (StringUtil.compareValue(Entity.class.getSimpleName(), type)) {
            Entity entity = permissionService.get(Entity.class, id);
            parentId = EntityCategory.class.getSimpleName() + "_" + entity.getEntityCategory().getObjectId();
        }

        if (StringUtil.compareValue(Permission.class.getSimpleName(), type)) {
            Permission permission = permissionService.get(Permission.class, id);
            parentId = Entity.class.getSimpleName() + "_" + permission.getEntity().getObjectId();
        }
        nodes.add(parentId);
        loadTreePath(parentId, nodes);
    }

    @RequestMapping(value = "/permission/create", method = RequestMethod.POST)
    public void createPermission(ModelMap modelMap, Permission permission) throws Exception {

        permissionService.save(permission);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/permission/update", method = RequestMethod.POST)
    public void updatePermission(ModelMap modelMap, Permission permission) throws Exception {

        permissionService.save(permission);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/permission/load/{objectId}", method = RequestMethod.GET)
    public void loadPermission(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        Permission permission = permissionService.get(Permission.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", permission.getObjectId());
        model.put("name", permission.getName());
        model.put("entity.objectId", Entity.class.getSimpleName() + "_" + permission.getEntity().getObjectId());
        if (permission.getMenu() != null) {
            model.put("menu.objectId", permission.getMenu().getObjectId());
        }
        model.put("permissionTag", permission.getPermissionTag());
        model.put("serviceMethod", permission.getServiceMethod());

        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/permission/delete/{objectId}", method = RequestMethod.GET)
    public void deletePermission(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        permissionService.logicDelete(Permission.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/permissionRegulation/loadManagerTree", method = RequestMethod.GET)
    public void loadPermissionRegulationManagerTree(@RequestParam(value = "node") String nodeId
            , @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (nodeId.equals("Root")) {
            return;
        }
        String type = nodeId.split("_")[0];
        String id = nodeId.split("_")[1];

        if (StringUtil.compareValue(type, Permission.class.getSimpleName())) {
            Permission permission = permissionService.get(Permission.class, id);
            if (CollectionUtil.isNotEmpty(permission.getPermissionRegulationList())) {
                for (PermissionRegulation permissionRegulation : permission.getPermissionRegulationList()) {
                    TreeNode node = new TreeNode();
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

        PermissionRegulation permissionRegulation = permissionService.get(PermissionRegulation.class, id);

        if (CollectionUtil.isNotEmpty(permissionRegulation.getChildList())) {
            for (PermissionRegulation child : permissionRegulation.getChildList()) {
                TreeNode node = new TreeNode();
                node.setObjectId(child.getObjectId());
                node.setText(child.getName());
                node.setType(PermissionRegulation.class.getSimpleName());
                node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
                nodes.add(node);
            }
        }
    }

    @RequestMapping(value = "/permissionRegulation/create", method = RequestMethod.POST)
    public void createPermissionRegulation(ModelMap modelMap, PermissionRegulation permissionRegulation) throws Exception {

        permissionService.addPermissionRegulation(permissionRegulation);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/permissionRegulation/update", method = RequestMethod.POST)
    public void updatePermissionRegulation(ModelMap modelMap, PermissionRegulation permissionRegulation) throws Exception {

        permissionService.updatePermissionRegulation(permissionRegulation);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/permissionRegulation/load/{objectId}", method = RequestMethod.GET)
    public void loadPermissionRegulation(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        PermissionRegulation permissionRegulation = (PermissionRegulation) permissionService.get(PermissionRegulation.class, objectId);
        Map model = new HashMap();
        model.put("objectId", permissionRegulation.getObjectId());
        if (permissionRegulation.getParent() != null) {
            model.put("parent.objectId", permissionRegulation.getParent().getObjectId());
        }
        model.put("name", permissionRegulation.getName());
        model.put("entityProperty", permissionRegulation.getEntityProperty());
        model.put("userProperty", permissionRegulation.getUserProperty());
        model.put("entityPropertyValue", permissionRegulation.getEntityPropertyValue());
        model.put("propertyDataType.objectId", permissionRegulation.getPropertyDataType().getObjectId());
        model.put("expressionOperation.objectId", permissionRegulation.getExpressionOperation().getObjectId());
        model.put("regulation", permissionRegulation.getRegulation());
        model.put("permission.objectId", permissionRegulation.getPermission().getObjectId());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/permissionRegulation/delete/{objectId}", method = RequestMethod.GET)
    public void deletePermissionRegulation(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        permissionService.logicDelete(PermissionRegulation.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entityPermissionChain/loadManagerTree", method = RequestMethod.GET)
    public void loadEntityPermissionChainManagerTree(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            EntityPermissionChain root = permissionService.getRootEntityPermissionChain();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setLeaf(false);
            node.setType(EntityPermissionChain.class.getSimpleName());
            node.setExpanded(true);
            nodes.add(node);
        } else {

            String id = nodeId.split("_")[1];
            EntityPermissionChain permission = permissionService.get(EntityPermissionChain.class, id);
            if (CollectionUtil.isEmpty(permission.getChildList())) {
                return;
            }
            for (EntityPermissionChain child : (permission.getChildList())) {
                TreeNode node = new TreeNode();
                node.setObjectId(child.getObjectId());
                node.setText(child.getName());
                node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
                node.setType(EntityPermissionChain.class.getSimpleName());
                nodes.add(node);
            }
        }
    }

    @RequestMapping(value = "/entityPermissionChain/loadTree", method = RequestMethod.GET)
    public void loadEntityPermissionChainTree(@RequestParam(value = "node") String nodeId
            , @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            EntityPermissionChain root = permissionService.getRootEntityPermissionChain();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            EntityPermissionChain entityPermissionChain = permissionService.get(EntityPermissionChain.class, nodeId);
            if (CollectionUtil.isEmpty(entityPermissionChain.getChildList())) {
                return;
            }
            for (EntityPermissionChain child : (entityPermissionChain.getChildList())) {
                TreeNode node = new TreeNode();
                node.setObjectId(child.getObjectId());
                node.setText(child.getName());
                node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
                nodes.add(node);
            }
        }
    }

    @RequestMapping(value = "/entityPermissionChain/loadTreePath", method = RequestMethod.GET)
    public void loadEntityPermissionChainTreePath(@RequestParam(value = "node") String nodeId
            , @ModelAttribute(value = "node") ArrayList<String> nodes) throws Exception {

        EntityPermissionChain entityPermissionChain = permissionService.get(EntityPermissionChain.class, nodeId);
        if (entityPermissionChain.getNodeLevel() == 1) {
            return;
        } else {
            nodes.add(entityPermissionChain.getParent().getObjectId());
            loadEntityPermissionChainTreePath(entityPermissionChain.getParent().getObjectId(), nodes);
        }
    }

    @RequestMapping(value = "/entityPermissionChain/create", method = RequestMethod.POST)
    public void createEntityPermissionChain(ModelMap modelMap, EntityPermissionChain entityPermissionChain) throws Exception {

        permissionService.save(entityPermissionChain);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entityPermissionChain/update", method = RequestMethod.POST)
    public void updateEntityPermissionChain(ModelMap modelMap, EntityPermissionChain entityPermissionChain) throws Exception {

        permissionService.save(entityPermissionChain);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entityPermissionChain/load/{objectId}", method = RequestMethod.GET)
    public void loadEntityPermissionChain(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        EntityPermissionChain entityPermissionChain = permissionService.get(EntityPermissionChain.class, objectId);
        Map model = new HashMap();
        model.put("objectId", entityPermissionChain.getObjectId());
        model.put("name", entityPermissionChain.getName());
        model.put("entity.objectId", Entity.class.getSimpleName() + "_" + entityPermissionChain.getEntity().getObjectId());
        model.put("parent.objectId", entityPermissionChain.getParent().getObjectId());
        model.put("dependProperty", entityPermissionChain.getDependProperty());

        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entityPermissionChain/delete/{objectId}", method = RequestMethod.GET)
    public void deleteEntityPermissionChain(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        permissionService.deleteEntityPermissionChain(objectId);
        modelMap.put("success", true);
    }

}
