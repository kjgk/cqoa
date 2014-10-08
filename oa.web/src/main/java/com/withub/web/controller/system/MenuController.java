package com.withub.web.controller.system;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.system.po.Menu;
import com.withub.service.system.MenuService;
import com.withub.util.SpringSecurityUtil;
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
public class MenuController {

    //============================== 属性声明 =============================================================

    @Autowired
    private MenuService menuService;

    //============================== Controller 方法 ======================================================

    @RequestMapping(value = "/menu/loadManagerTree", method = RequestMethod.GET)
    public void loadManagerTree(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            Menu root = menuService.getRootEntity(Menu.class);
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setLeaf(false);
            node.setType(Menu.class.getSimpleName());
            node.setExpanded(true);
            nodes.add(node);
            return;
        }

        String id = nodeId.split("_")[1];
        Menu menu = menuService.get(Menu.class, id);
        if (CollectionUtil.isEmpty(menu.getChildList())) {
            return;
        }
        for (Menu child : (menu.getChildList())) {
            TreeNode node = new TreeNode();
            node.setObjectId(child.getObjectId());
            node.setText(child.getName());
            node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
            node.setType(Menu.class.getSimpleName());
            node.setOrderNo(child.getOrderNo());
            nodes.add(node);
        }
    }

    @RequestMapping(value = "/menu/loadTree", method = RequestMethod.GET)
    public void loadTree(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            Menu root = menuService.getRootEntity(Menu.class);
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            Menu menu = menuService.get(Menu.class, nodeId);
            if (CollectionUtil.isEmpty(menu.getChildList())) {
                return;
            }
            for (Menu child : (menu.getChildList())) {
                TreeNode node = new TreeNode();
                node.setObjectId(child.getObjectId());
                node.setText(child.getName());
                node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
                node.getAttributes().put("page", child.getUrl());
                node.getAttributes().put("openMode", 1);
                nodes.add(node);
            }
        }
    }

    @RequestMapping(value = "/menu/loadTreePath", method = RequestMethod.GET)
    public void loadTreePath(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "node") ArrayList<String> nodes) throws Exception {

        Menu menu = menuService.get(Menu.class, nodeId);
        if (menu.getNodeLevel() == 1) {
            return;
        } else {
            nodes.add(menu.getParent().getObjectId());
            loadTreePath(menu.getParent().getObjectId(), nodes);
        }
    }

    @RequestMapping(value = "/menu/create", method = RequestMethod.POST)
    public void createMenu(ModelMap modelMap, Menu menu) throws Exception {

        menu.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        menuService.save(menu);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/menu/update", method = RequestMethod.POST)
    public void updateMenu(ModelMap modelMap, Menu menu) throws Exception {

        menu.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        menuService.save(menu);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/menu/load/{objectId}", method = RequestMethod.GET)
    public void loadMenu(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        Menu menu = menuService.get(Menu.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", menu.getObjectId());
        model.put("name", menu.getName());
        model.put("permission", menu.getPermission());
        model.put("subMenuMethod", menu.getSubMenuMethod());
        model.put("url", menu.getUrl());
        model.put("image", menu.getImage());
        model.put("expand", menu.getExpand());
        model.put("openMode", menu.getOpenMode());
        model.put("visible", menu.getVisible());
        model.put("requiredLogin", menu.getRequiredLogin());
        model.put("parent.objectId", menu.getParent().getObjectId());

        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/menu/delete/{objectId}", method = RequestMethod.GET)
    public void deleteMenu(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        menuService.deleteMenu(objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/menu/query", method = RequestMethod.GET)
    public void listByParentId(@RequestParam("id") String id, @ModelAttribute("items") ArrayList<HashMap> items) throws Exception {

        List<Menu> menuList;
        if (StringUtil.isEmpty(id)) {
            List list = menuService.listByParentId(Menu.class, null);
            menuList = (List<Menu>) list;
        } else {
            Menu menu = menuService.get(Menu.class, id);
            menuList = menu.getChildList();
        }
        if (CollectionUtil.isEmpty(menuList)) {
            return;
        }

        for (Menu menu : menuList) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", menu.getObjectId());
            item.put("name", menu.getName());
            item.put("permission", menu.getPermission());
            item.put("url", menu.getUrl());
            items.add(item);
        }
    }

}
