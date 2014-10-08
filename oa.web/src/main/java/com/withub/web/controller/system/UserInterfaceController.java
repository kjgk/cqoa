package com.withub.web.controller.system;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.system.po.Permission;
import com.withub.model.system.po.UserInterface;
import com.withub.model.system.po.UserInterfaceCategory;
import com.withub.model.system.po.UserInterfaceMenu;
import com.withub.service.system.UserInterfaceService;
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
public class UserInterfaceController {

    //======================= 属性声明 ===================================================

    @Autowired
    private UserInterfaceService userInterfaceService;

    //======================= Controller 方法 =============================================

    @RequestMapping(value = "/userInterface/loadManagerTree", method = RequestMethod.GET)
    public void loadManagerTree(@RequestParam(value = "node") String nodeId
            , @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            UserInterfaceCategory root = userInterfaceService.getRootUserInterfaceCategory();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setType(UserInterfaceCategory.class.getSimpleName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            String id = nodeId.split("_")[1];
            String type = nodeId.split("_")[0];
            if (StringUtil.compareValue(UserInterfaceCategory.class.getSimpleName(), type)) {
                UserInterfaceCategory category = userInterfaceService.get(UserInterfaceCategory.class, id);
                if (CollectionUtil.isNotEmpty(category.getChildList())) {
                    for (UserInterfaceCategory child : category.getChildList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(child.getObjectId());
                        node.setText(child.getName());
                        node.setType(UserInterfaceCategory.class.getSimpleName());
                        node.setLeaf(false);
                        nodes.add(node);
                    }
                }

                if (CollectionUtil.isNotEmpty(category.getUserInterfaceList())) {
                    for (UserInterface userInterface : category.getUserInterfaceList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(userInterface.getObjectId());
                        node.setText(userInterface.getName());
                        node.setType(UserInterface.class.getSimpleName());
                        node.setLeaf(true);
                        nodes.add(node);
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/userInterface/loadTree", method = RequestMethod.GET)
    public void loadTree(@RequestParam(value = "node") String nodeId, @RequestParam(value = "depth") int depth,
                         @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            UserInterfaceCategory root = userInterfaceService.getRootUserInterfaceCategory();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setType(UserInterfaceCategory.class.getSimpleName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            String id = nodeId.split("_")[1];
            String type = nodeId.split("_")[0];
            if (StringUtil.compareValue(UserInterfaceCategory.class.getSimpleName(), type)) {
                UserInterfaceCategory category = userInterfaceService.get(UserInterfaceCategory.class, id);
                if (CollectionUtil.isNotEmpty(category.getChildList())) {
                    for (UserInterfaceCategory child : category.getChildList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(child.getObjectId());
                        node.setText(child.getName());
                        node.setType(UserInterfaceCategory.class.getSimpleName());
                        node.setLeaf(depth == 1 && CollectionUtil.isEmpty(child.getChildList()));
                        nodes.add(node);
                    }
                }
                if (depth > 1 && CollectionUtil.isNotEmpty(category.getUserInterfaceList())) {
                    for (UserInterface userInterface : category.getUserInterfaceList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(userInterface.getObjectId());
                        node.setText(userInterface.getName());
                        node.setType(UserInterface.class.getSimpleName());
                        node.setLeaf(true);
                        nodes.add(node);
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/userInterface/loadTreePath", method = RequestMethod.GET)
    public void loadTreePath(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "node") ArrayList<String> nodes) throws Exception {

        String type = nodeId.split("_")[0];
        String id = nodeId.split("_")[1];

        String parentId = "";
        if (StringUtil.compareValue(UserInterfaceCategory.class.getSimpleName(), type)) {
            UserInterfaceCategory category = userInterfaceService.get(UserInterfaceCategory.class, id);
            if (category.getNodeLevel() == 1) {
                return;
            }
            parentId = UserInterfaceCategory.class.getSimpleName() + "_" + category.getParent().getObjectId();
        }

        if (StringUtil.compareValue(UserInterface.class.getSimpleName(), type)) {
            UserInterface userInterface = userInterfaceService.get(UserInterface.class, id);
            parentId = UserInterfaceCategory.class.getSimpleName() + "_" + userInterface.getUserInterfaceCategory().getObjectId();
        }
        nodes.add(parentId);
        loadTreePath(parentId, nodes);
    }

    @RequestMapping(value = "/userInterfaceCategory/create", method = RequestMethod.POST)
    public void createUserInterfaceCategory(ModelMap modelMap, UserInterfaceCategory userInterfaceCategory) throws Exception {

        userInterfaceService.save(userInterfaceCategory);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/userInterfaceCategory/update", method = RequestMethod.POST)
    public void updateUserInterfaceCategory(ModelMap modelMap, UserInterfaceCategory userInterfaceCategory) throws Exception {

        userInterfaceService.save(userInterfaceCategory);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/userInterfaceCategory/load/{objectId}", method = RequestMethod.GET)
    public void loadUserInterfaceCategory(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        UserInterfaceCategory category = userInterfaceService.get(UserInterfaceCategory.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", category.getObjectId());
        model.put("name", category.getName());
        model.put("description", category.getDescription());
        model.put("parent.objectId", UserInterfaceCategory.class.getSimpleName() + "_" + category.getParent().getObjectId());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/userInterfaceCategory/delete/{objectId}", method = RequestMethod.GET)
    public void deleteUserInterfaceCategory(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        userInterfaceService.logicDelete(UserInterfaceCategory.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/userInterface/create", method = RequestMethod.POST)
    public void createUserInterface(ModelMap modelMap, UserInterface userInterface) throws Exception {

        userInterfaceService.save(userInterface);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/userInterface/update", method = RequestMethod.POST)
    public void updateUserInterface(ModelMap modelMap, UserInterface userInterface) throws Exception {

        userInterfaceService.save(userInterface);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/userInterface/delete/{objectId}", method = RequestMethod.GET)
    public void deleteUserInterface(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        userInterfaceService.logicDelete(UserInterface.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/userInterface/load/{objectId}", method = RequestMethod.GET)
    public void loadUserInterface(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        UserInterface userInterface = userInterfaceService.get(UserInterface.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", userInterface.getObjectId());
        model.put("name", userInterface.getName());
        model.put("userInterfaceTag", userInterface.getUserInterfaceTag());
        model.put("userInterfaceCategory.objectId", UserInterfaceCategory.class.getSimpleName() + "_" + userInterface.getUserInterfaceCategory().getObjectId());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/userInterfaceMenu/create", method = RequestMethod.POST)
    public void createUserInterfaceMenu(ModelMap modelMap, UserInterfaceMenu userInterfaceMenu) throws Exception {

        userInterfaceService.save(userInterfaceMenu);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/userInterfaceMenu/update", method = RequestMethod.POST)
    public void updateUserInterfaceMenu(ModelMap modelMap, UserInterfaceMenu userInterfaceMenu) throws Exception {

        userInterfaceService.save(userInterfaceMenu);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/userInterfaceMenu/delete/{objectId}", method = RequestMethod.GET)
    public void deleteUserInterfaceMenu(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        userInterfaceService.logicDelete(UserInterfaceMenu.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/userInterfaceMenu/load/{objectId}", method = RequestMethod.GET)
    public void loadUserInterfaceMenu(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        UserInterfaceMenu userInterfaceMenu = userInterfaceService.get(UserInterfaceMenu.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", userInterfaceMenu.getObjectId());
        model.put("name", userInterfaceMenu.getName());
        model.put("menuTag", userInterfaceMenu.getMenuTag());
        model.put("menuType", userInterfaceMenu.getMenuType());
        model.put("permission.objectId", Permission.class.getSimpleName() + "_" + userInterfaceMenu.getPermission().getObjectId());
        model.put("userInterface.objectId", UserInterface.class.getSimpleName() + "_" + userInterfaceMenu.getUserInterface().getObjectId());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/userInterfaceMenu/query", method = RequestMethod.GET)
    public void listUserInterfaceMenu(@RequestParam("id") String id, @ModelAttribute("items") ArrayList<HashMap> items) throws Exception {

        UserInterface userInterface = userInterfaceService.get(UserInterface.class, id);
        if (CollectionUtil.isEmpty(userInterface.getMenuList())) {
            return;
        }
        for (UserInterfaceMenu userInterfaceMenu : userInterface.getMenuList()) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", userInterfaceMenu.getObjectId());
            item.put("name", userInterfaceMenu.getPermission().getName());
            item.put("menuTag", userInterfaceMenu.getMenuTag());
            item.put("menuType", userInterfaceMenu.getMenuType());
            items.add(item);
        }
    }

    @RequestMapping(value = "/userInterfaceMenu/listCurrentUserDenyMenu", method = RequestMethod.GET)
    public void listCurrentUserDenyMenu(ModelMap modelMap, @RequestParam("userInterfaceTag") String userInterfaceTag, @RequestParam(value = "objectId", required = false) String objectId) throws Exception {

        List<UserInterfaceMenu> userInterfaceMenuList;
        if (StringUtil.isEmpty(objectId)) {
            userInterfaceMenuList = userInterfaceService.listCurrentUserInterfaceDenyToolbarMenu(userInterfaceTag);
        } else {
            userInterfaceMenuList = userInterfaceService.listCurrentUserInterfaceDenyContextMenu(userInterfaceTag, objectId);
        }
        List list = new ArrayList();
        for (UserInterfaceMenu userInterfaceMenu : userInterfaceMenuList) {
            list.add(userInterfaceMenu.getMenuTag());
        }

        modelMap.put("success", true);
        modelMap.put("data", list);

    }
}
