package com.withub.web.controller.system;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.ReflectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.AbstractEntity;
import com.withub.model.entity.AbstractRecursionEntity;
import com.withub.model.entity.enumeration.EntityType;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.system.po.*;
import com.withub.service.system.CodeService;
import com.withub.service.system.UserClusterService;
import com.withub.util.SpringSecurityUtil;
import com.withub.web.common.ext.StaticCheckedTreeNode;
import com.withub.web.common.ext.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/system")
public class UserClusterController {

    //======================= 属性声明 ===================================================

    @Autowired
    private UserClusterService userClusterService;

    @Autowired
    private CodeService codeService;

    //======================= Controller 方法 =============================================


    @RequestMapping(value = "/userCluster/loadManagerTree", method = RequestMethod.GET)
    public void loadManagerTree(@RequestParam(value = "node") String nodeId
            , @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            UserClusterCategory root = userClusterService.getRootUserClusterCategory();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setType(UserClusterCategory.class.getSimpleName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            String id = nodeId.split("_")[1];
            String type = nodeId.split("_")[0];
            if (StringUtil.compareValue(UserClusterCategory.class.getSimpleName(), type)) {
                UserClusterCategory category = (UserClusterCategory) userClusterService.get(UserClusterCategory.class, id);
                if (CollectionUtil.isNotEmpty(category.getChildList())) {
                    for (UserClusterCategory child : category.getChildList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(child.getObjectId());
                        node.setText(child.getName());
                        node.setType(UserClusterCategory.class.getSimpleName());
                        node.setLeaf(false);
                        nodes.add(node);
                    }
                }

                if (CollectionUtil.isNotEmpty(category.getUserClusterList())) {
                    for (UserCluster userCluster : category.getUserClusterList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(userCluster.getObjectId());
                        node.setText(userCluster.getName());
                        node.setType(UserCluster.class.getSimpleName());
                        node.setLeaf(true);
                        nodes.add(node);
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/userCluster/loadTree", method = RequestMethod.GET)
    public void loadTree(@RequestParam(value = "node") String nodeId, @RequestParam(value = "depth") int depth,
                         @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            UserClusterCategory root = userClusterService.getRootUserClusterCategory();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setType(UserClusterCategory.class.getSimpleName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            String id = nodeId.split("_")[1];
            String type = nodeId.split("_")[0];
            if (StringUtil.compareValue(UserClusterCategory.class.getSimpleName(), type)) {
                UserClusterCategory category = (UserClusterCategory) userClusterService.get(UserClusterCategory.class, id);
                if (CollectionUtil.isNotEmpty(category.getChildList())) {
                    for (UserClusterCategory child : category.getChildList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(child.getObjectId());
                        node.setText(child.getName());
                        node.setType(UserClusterCategory.class.getSimpleName());
                        node.setLeaf(depth == 1 && CollectionUtil.isEmpty(child.getChildList()));
                        nodes.add(node);
                    }
                }
                if (depth > 1 && CollectionUtil.isNotEmpty(category.getUserClusterList())) {
                    for (UserCluster userCluster : category.getUserClusterList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(userCluster.getObjectId());
                        node.setText(userCluster.getName());
                        node.setType(UserCluster.class.getSimpleName());
                        node.setLeaf(true);
                        nodes.add(node);
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/userCluster/loadTreePath", method = RequestMethod.GET)
    public void loadTreePath(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "node") ArrayList<String> nodes) throws Exception {

        String type = nodeId.split("_")[0];
        String id = nodeId.split("_")[1];

        String parentId = "";
        if (StringUtil.compareValue(UserClusterCategory.class.getSimpleName(), type)) {
            UserClusterCategory category = userClusterService.get(UserClusterCategory.class, id);
            if (category.getNodeLevel() == 1) {
                return;
            }
            parentId = UserClusterCategory.class.getSimpleName() + "_" + category.getParent().getObjectId();
        }

        if (StringUtil.compareValue(UserCluster.class.getSimpleName(), type)) {
            UserCluster userCluster = userClusterService.get(UserCluster.class, id);
            parentId = UserClusterCategory.class.getSimpleName() + "_" + userCluster.getUserClusterCategory().getObjectId();
        }
        nodes.add(parentId);
        loadTreePath(parentId, nodes);
    }
    
    /* ======================= userClusterCategory ======================= */

    @RequestMapping(value = "/userClusterCategory/create", method = RequestMethod.POST)
    public void createUserClusterCategory(ModelMap modelMap, UserClusterCategory userClusterCategory) throws Exception {

        userClusterService.save(userClusterCategory);
    }

    @RequestMapping(value = "/userClusterCategory/update", method = RequestMethod.POST)
    public void updateUserClusterCategory(ModelMap modelMap, UserClusterCategory userClusterCategory) throws Exception {

        userClusterService.save(userClusterCategory);
    }

    @RequestMapping(value = "/userClusterCategory/load/{objectId}", method = RequestMethod.GET)
    public void loadUserClusterCategory(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        UserClusterCategory userClusterCategory = userClusterService.get(UserClusterCategory.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", userClusterCategory.getObjectId());
        model.put("name", userClusterCategory.getName());
        model.put("nodeTag", userClusterCategory.getNodeTag());
        model.put("cacheEntityName", userClusterCategory.getCacheEntityName());
        model.put("description", userClusterCategory.getDescription());

        if (userClusterCategory.getParent() != null) {
            model.put("parent.objectId", UserClusterCategory.class.getSimpleName() + "_" + userClusterCategory.getParent().getObjectId());
        }
        modelMap.put("data", model);
    }

    @RequestMapping(value = "/userClusterCategory/delete/{objectId}", method = RequestMethod.GET)
    public void deleteUserClusterCategory(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        userClusterService.logicDelete(UserClusterCategory.class, objectId);
    }

    /* ======================= userCluster ======================= */

    @RequestMapping(value = "/userCluster/create", method = RequestMethod.POST)
    public void createUserCluster(ModelMap modelMap, UserCluster userCluster) throws Exception {

        userCluster.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        userClusterService.createUserCluster(userCluster);
    }

    @RequestMapping(value = "/userCluster/update", method = RequestMethod.POST)
    public void updateUserCluster(ModelMap modelMap, UserCluster userCluster) throws Exception {

        userCluster.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        userClusterService.updateUserCluster(userCluster);
    }

    @RequestMapping(value = "/userCluster/delete/{objectId}", method = RequestMethod.GET)
    public void deleteUserCluster(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        userClusterService.logicDelete(UserCluster.class, objectId);
    }

    @RequestMapping(value = "/userCluster/load/{objectId}", method = RequestMethod.GET)
    public void loadUserCluster(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        UserCluster userCluster = userClusterService.get(UserCluster.class, objectId);
        Map model = new HashMap();
        model.put("objectId", userCluster.getObjectId());
        model.put("name", userCluster.getName());
        model.put("userClusterTag", userCluster.getUserClusterTag());
        model.put("entityInstanceCluster", userCluster.getEntityInstanceCluster());
        model.put("runtime", userCluster.getRuntime());
        model.put("userClusterCategory.objectId", UserClusterCategory.class.getSimpleName() + "_" + userCluster.getUserClusterCategory().getObjectId());
        modelMap.put("data", model);
    }

    /* ======================= UserClusterRegulation ======================= */

    @RequestMapping(value = "/userClusterRegulation/loadTree", method = RequestMethod.GET)
    public void loadUserClusterRegulationTree(@RequestParam(value = "node") String nodeId
            , @RequestParam(value = "userClusterId", required = false) String userClusterId
            , @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            if (StringUtil.isEmpty(userClusterId)) {
                return;
            }
            UserClusterRegulation userClusterRegulation = userClusterService.getRootRegulationByUserClusterId(userClusterId);
            TreeNode node = new TreeNode();
            node.setObjectId(userClusterRegulation.getObjectId());
            node.setText(userClusterRegulation.getName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            UserClusterRegulation userClusterRegulation = userClusterService.get(UserClusterRegulation.class, nodeId);
            for (UserClusterRegulation regulation : userClusterRegulation.getChildList()) {
                TreeNode node = new TreeNode();
                node.setObjectId(regulation.getObjectId());
                node.setText(regulation.getName());
                node.setLeaf(true);
                nodes.add(node);
            }
        }
    }

    @RequestMapping(value = "/userClusterRegulation/create", method = RequestMethod.POST)
    public void createUserClusterRegulation(ModelMap modelMap, UserClusterRegulation userClusterRegulation) throws Exception {

        userClusterRegulation.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        userClusterService.save(userClusterRegulation);
    }

    @RequestMapping(value = "/userClusterRegulation/update", method = RequestMethod.POST)
    public void updateUserClusterRegulation(ModelMap modelMap, UserClusterRegulation userClusterRegulation) throws Exception {

        userClusterRegulation.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        userClusterService.save(userClusterRegulation);
    }

    @RequestMapping(value = "/userClusterRegulation/delete/{objectId}", method = RequestMethod.GET)
    public void deleteUserClusterRegulation(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        userClusterService.logicDelete(UserClusterRegulation.class, objectId);
    }

    @RequestMapping(value = "/userClusterRegulation/load/{objectId}", method = RequestMethod.GET)
    public void loadUserClusterRegulation(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        UserClusterRegulation userClusterRegulation = userClusterService.get(UserClusterRegulation.class, objectId);
        Map model = new HashMap();
        model.put("objectId", userClusterRegulation.getObjectId());
        model.put("name", userClusterRegulation.getName());
        model.put("userCluster.objectId", userClusterRegulation.getUserCluster().getObjectId());
        model.put("parent.objectId", userClusterRegulation.getParent().getObjectId());
        if (userClusterRegulation.getEntity() != null) {
            model.put("entity.objectId", Entity.class.getSimpleName() + "_" + userClusterRegulation.getEntity().getObjectId());
        }
        model.put("codeColumnTag", userClusterRegulation.getCodeColumnTag());
        model.put("userProperty", userClusterRegulation.getUserProperty());
        model.put("extandedRoleProperty", userClusterRegulation.getExtandedRoleProperty());
        if (userClusterRegulation.getExtandedPropertyEntity() != null) {
            model.put("extandedPropertyEntity.objectId", Entity.class.getSimpleName() + "_" + userClusterRegulation.getExtandedPropertyEntity().getObjectId());
        }
        model.put("extandedPropertyCodeColumnTag", userClusterRegulation.getExtandedPropertyCodeColumnTag());
        modelMap.put("data", model);
    }


    @RequestMapping(value = "/userClusterRegulation/loadCheckTree", method = RequestMethod.GET)
    public void loadUserClusterRegulationCheckTree(@RequestParam(value = "node") String nodeId
            , @RequestParam(value = "userClusterId", required = false) String userClusterId
            , @RequestParam(value = "userClusterCategoryTag", required = false) String userClusterCategoryTag
            , @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            if (StringUtil.isNotEmpty(userClusterId)) {
                UserClusterRegulation userClusterRegulation = userClusterService.getRootRegulationByUserClusterId(userClusterId);
                UserCluster userCluster = userClusterService.get(UserCluster.class, userClusterId);
                TreeNode node = new TreeNode();
                node.setObjectId(userClusterRegulation.getObjectId());
                node.setType(UserClusterRegulation.class.getSimpleName());
                node.setText(userCluster.getName());
                node.setLeaf(false);
                node.setExpanded(true);
                nodes.add(node);
            } else if (StringUtil.isNotEmpty(userClusterCategoryTag)) {
                UserClusterCategory userClusterCategory = (UserClusterCategory) userClusterService.getByPropertyValue(UserClusterCategory.class, "nodeTag", userClusterCategoryTag);
                for (UserCluster userCluster : userClusterCategory.getUserClusterList()) {
                    UserClusterRegulation userClusterRegulation = userClusterService.getRootRegulationByUserClusterId(userCluster.getObjectId());
                    TreeNode node = new TreeNode();
                    node.setObjectId(userClusterRegulation.getObjectId());
                    node.setType(UserClusterRegulation.class.getSimpleName());
                    node.setText(userCluster.getName());
                    node.setLeaf(false);
                    nodes.add(node);
                }
            }

        } else {
            String[] nodeIds = nodeId.split("_");
            String type = nodeIds[0];
            String userClusterRegulationId = nodeIds[1];
            UserClusterRegulation userClusterRegulation = userClusterService.get(UserClusterRegulation.class, userClusterRegulationId);
            if (userClusterRegulation.getNodeLevel() == 1) {
                for (UserClusterRegulation regulation : userClusterRegulation.getChildList()) {
                    TreeNode node = new TreeNode();
                    node.setObjectId(regulation.getObjectId());
                    node.setType(UserClusterRegulation.class.getSimpleName());
                    node.setText(regulation.getName());
                    node.setLeaf(false);
                    nodes.add(node);
                }
            } else {
                Entity entity = userClusterRegulation.getEntity();
                EntityType entityType = EntityType.valueOf(entity.getEntityType().getCodeTag());
                Class<? extends AbstractEntity> entityClass = (Class<? extends AbstractEntity>) Class.forName(entity.getClassName());

                // 代码表
                if (StringUtil.compareValue(entity.getClassName(), Code.class.getName())) {
                    CodeColumn codeColumn = codeService.getCodeColumnByTag(userClusterRegulation.getCodeColumnTag());
                    for (Code code : codeColumn.getCodeList()) {
                        StaticCheckedTreeNode node = new StaticCheckedTreeNode();
                        node.setId(Code.class.getSimpleName() + "_" + userClusterRegulationId + "_" + code.getObjectId());
                        node.setText(code.getName());
                        node.setLeaf(true);
                        nodes.add(node);
                    }
                    return;
                }

                // 递归表
                if (entityType == EntityType.Recursion) {
                    if (StringUtil.compareValue(type, UserClusterRegulation.class.getSimpleName())) {
                        AbstractRecursionEntity rootObject = userClusterService.getRootEntity((Class<? extends AbstractRecursionEntity>) entityClass);
                        for (AbstractRecursionEntity object : (List<AbstractRecursionEntity>) ReflectionUtil.getFieldObjectValue(rootObject, "childList")) {
                            StaticCheckedTreeNode node = new StaticCheckedTreeNode();
                            node.setId(EntityType.Recursion + "_" + userClusterRegulationId + "_" + object.getObjectId());
                            node.setText((String) ReflectionUtil.getFieldObjectValue(object, "name"));
                            node.setLeaf(CollectionUtil.isEmpty((List) ReflectionUtil.getFieldObjectValue(object, "childList")));
                            nodes.add(node);
                        }
                    } else {
                        String relatedObjectId = nodeIds[2];
                        AbstractRecursionEntity parentObject = userClusterService.get((Class<? extends AbstractRecursionEntity>) entityClass, relatedObjectId);
                        for (AbstractRecursionEntity object : (List<AbstractRecursionEntity>) ReflectionUtil.getFieldObjectValue(parentObject, "childList")) {
                            StaticCheckedTreeNode node = new StaticCheckedTreeNode();
                            node.setId(EntityType.Recursion + "_" + userClusterRegulationId + "_" + object.getObjectId());
                            node.setText((String) ReflectionUtil.getFieldObjectValue(object, "name"));
                            node.setLeaf(CollectionUtil.isEmpty((List) ReflectionUtil.getFieldObjectValue(object, "childList")));
                            nodes.add(node);
                        }
                    }
                }

                // 基础数据表
                if (entityType == EntityType.BaseData) {
                    QueryInfo queryInfo = new QueryInfo();
                    queryInfo.setTargetEntity(entityClass);
                    List<AbstractEntity> list = userClusterService.list(queryInfo);
                    for (AbstractEntity object : list) {
                        StaticCheckedTreeNode node = new StaticCheckedTreeNode();
                        node.setId(EntityType.BaseData + "_" + userClusterRegulationId + "_" + object.getObjectId());
                        node.setText((String) ReflectionUtil.getFieldObjectValue(object, "name"));
                        node.setLeaf(true);
                        nodes.add(node);
                    }
                }
            }
        }
    }


    @RequestMapping(value = "/userClusterRegulation/loadCheckTreePath", method = RequestMethod.GET)
    public void loadUserClusterRegulationCheckTreePath(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "node") ArrayList<String> nodes) throws Exception {

        String[] nodeIds = nodeId.split("_");
        String type = nodeIds[0];
        String userClusterRegulationId = nodeIds[1];

        String parentId = "";
        if (StringUtil.compareValue(UserClusterRegulation.class.getSimpleName(), type)) {
            UserClusterRegulation userClusterRegulation = userClusterService.get(UserClusterRegulation.class, userClusterRegulationId);
            if (userClusterRegulation.getNodeLevel() == 1) {
                return;
            } else {
                parentId = UserClusterRegulation.class.getSimpleName() + "_" + userClusterRegulation.getParent().getObjectId();
            }
        }
        if (StringUtil.compareValue(EntityType.BaseData.name(), type) || StringUtil.compareValue(Code.class.getSimpleName(), type)) {
            parentId = UserClusterRegulation.class.getSimpleName() + "_" + userClusterRegulationId;
        }
        if (StringUtil.compareValue(EntityType.Recursion.name(), type)) {
            String relatedObjectId = nodeIds[2];
            UserClusterRegulation userClusterRegulation = userClusterService.get(UserClusterRegulation.class, userClusterRegulationId);
            Entity entity = userClusterRegulation.getEntity();
            Class<? extends AbstractRecursionEntity> entityClass = (Class<? extends AbstractRecursionEntity>) Class.forName(entity.getClassName());
            AbstractRecursionEntity object = userClusterService.get(entityClass, relatedObjectId);
            if (object.getNodeLevel() == 2) {
                parentId = UserClusterRegulation.class.getSimpleName() + "_" + userClusterRegulationId;
            } else {
                parentId = EntityType.Recursion.name() + "_" + userClusterRegulationId + "_" + ReflectionUtil.getFieldObjectValue(object, "parent.objectId");
            }
        }
        nodes.add(parentId);
        loadUserClusterRegulationCheckTreePath(parentId, nodes);
    }

    //======================= 属性方法 ===================================================

    public UserClusterService getUserClusterService() {

        return userClusterService;
    }

    public void setUserClusterService(UserClusterService userClusterService) {

        this.userClusterService = userClusterService;
    }

    public CodeService getCodeService() {

        return codeService;
    }

    public void setCodeService(CodeService codeService) {

        this.codeService = codeService;
    }
}
