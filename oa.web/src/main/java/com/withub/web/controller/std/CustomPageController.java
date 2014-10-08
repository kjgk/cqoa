package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.std.po.CustomPage;
import com.withub.model.std.po.CustomPageCategory;
import com.withub.model.std.po.FileInfo;
import com.withub.service.std.CustomPageService;
import com.withub.service.std.FileService;
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
@RequestMapping(value = "/std")
public class CustomPageController extends BaseController{

    //============================ 属性声明 ==============================================================

    @Autowired
    private CustomPageService customPageService;

    @Autowired
    private FileService fileService;

    //============================ Controller 方法 =======================================================

    @RequestMapping(value = "/customPage/loadManagerTree", method = RequestMethod.GET)
    public void loadManagerTree(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            CustomPageCategory root = customPageService.getRootEntity(CustomPageCategory.class);
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setType(CustomPageCategory.class.getSimpleName());
            node.setText(root.getName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            String customPageCategoryId = nodeId.split("_")[1];
            CustomPageCategory customPageCategory = (CustomPageCategory) customPageService.get(CustomPageCategory.class, customPageCategoryId);
            if (CollectionUtil.isEmpty(customPageCategory.getChildList())) {
                return;
            }
            for (CustomPageCategory child : (customPageCategory.getChildList())) {
                TreeNode node = new TreeNode();
                node.setText(child.getName());
                node.setObjectId(child.getObjectId());
                node.setType(CustomPageCategory.class.getSimpleName());
                node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
                nodes.add(node);
            }
        }
    }

    @RequestMapping(value = "/customPage/loadTree", method = RequestMethod.GET)
    public void loadTree(@RequestParam(value = "node") String nodeId, @RequestParam(value = "depth") int depth
            , @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            CustomPageCategory root = customPageService.getRootEntity(CustomPageCategory.class);
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setType(CustomPageCategory.class.getSimpleName());
            node.setText(root.getName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            String id = nodeId.split("_")[1];
            String type = nodeId.split("_")[0];
            if (StringUtil.compareValue(CustomPageCategory.class.getSimpleName(), type)) {
                CustomPageCategory category = customPageService.get(CustomPageCategory.class, id);
                if (CollectionUtil.isNotEmpty(category.getChildList())) {
                    for (CustomPageCategory child : category.getChildList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(child.getObjectId());
                        node.setText(child.getName());
                        node.setType(CustomPageCategory.class.getSimpleName());
                        node.setLeaf(depth == 1 && CollectionUtil.isEmpty(child.getChildList()));
                        nodes.add(node);
                    }
                }
                if (depth > 1 && CollectionUtil.isNotEmpty(category.getCustomPageList())) {
                    for (CustomPage customPage : category.getCustomPageList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(customPage.getObjectId());
                        node.setText(customPage.getName());
                        node.setType(CustomPage.class.getSimpleName());
                        node.setLeaf(true);
                        nodes.add(node);
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/customPage/loadTreePath", method = RequestMethod.GET)
    public void loadTreePath(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "node") ArrayList<String>
            nodes) throws Exception {

        String type = nodeId.split("_")[0];
        String id = nodeId.split("_")[1];

        String parentId = "";
        if (StringUtil.compareValue(CustomPageCategory.class.getSimpleName(), type)) {
            CustomPageCategory category = customPageService.get(CustomPageCategory.class, id);
            if (category.getNodeLevel() == 1) {
                return;
            }
            parentId = CustomPageCategory.class.getSimpleName() + "_" + category.getParent().getObjectId();
        }

        if (StringUtil.compareValue(CustomPage.class.getSimpleName(), type)) {
            CustomPage customPage = customPageService.get(CustomPage.class, id);
            parentId = CustomPageCategory.class.getSimpleName() + "_" + customPage.getCustomPageCategory().getObjectId();
        }
        nodes.add(parentId);
        loadTreePath(parentId, nodes);
    }

    @RequestMapping(value = "/customPage/create", method = RequestMethod.POST)
    public void createCustomPage(ModelMap modelMap, CustomPage customPage) throws Exception {

        customPageService.save(customPage);
    }

    @RequestMapping(value = "/customPage/update", method = RequestMethod.POST)
    public void updateCustomPage(ModelMap modelMap, CustomPage customPage) throws Exception {

        customPageService.save(customPage);
    }

    @RequestMapping(value = "/customPage/load/{objectId}", method = RequestMethod.GET)
    public void loadCustomPage(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        CustomPage customPage =  customPageService.get(CustomPage.class, objectId);
        Map model = new HashMap();
        model.put("objectId", customPage.getObjectId());
        model.put("name", customPage.getName());
        model.put("code", customPage.getCode());
        model.put("customPageCategory.objectId", CustomPageCategory.class.getSimpleName() + "_"
                + customPage.getCustomPageCategory().getObjectId());
        FileInfo fileInfo = fileService.getFileInfoByRelatedObjectId(customPage.getObjectId());
        model.put("fileInfoId", fileInfo == null ? "" : fileInfo.getObjectId());
        modelMap.put("items", customPage.getCustomPageItemList());
        modelMap.put("data", model);
    }

    @RequestMapping(value = "/customPage/save", method = RequestMethod.POST)
    public void saveCustomPage(ModelMap modelMap, CustomPage customPage) throws Exception {

        customPageService.saveCustomPage(customPage);
    }

    @RequestMapping(value = "/customPage/delete/{objectId}", method = RequestMethod.GET)
    public void deleteCustomPage(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        customPageService.logicDelete(CustomPage.class, objectId);
    }

    @RequestMapping(value = "/customPage/query", method = RequestMethod.GET)
    public void queryCustomPage(@RequestParam("id") String id, @ModelAttribute("items") ArrayList<HashMap> items)
            throws Exception {

        CustomPageCategory customPageCategory =  customPageService.get(CustomPageCategory.class, id);
        if (CollectionUtil.isEmpty(customPageCategory.getCustomPageList())) {
            return;
        }
        for (CustomPage customPage : customPageCategory.getCustomPageList()) {
            HashMap item = new HashMap();
            item.put("objectId", customPage.getObjectId());
            item.put("name", customPage.getName());
            item.put("code", customPage.getCode());
            items.add(item);
        }
    }


    @RequestMapping(value = "/customPageCategory/create", method = RequestMethod.POST)
    public void createCustomPageCategory(ModelMap modelMap, CustomPageCategory customPageCategory) throws Exception {

        customPageService.save(customPageCategory);
    }

    @RequestMapping(value = "/customPageCategory/update", method = RequestMethod.POST)
    public void updateCustomPageCategory(ModelMap modelMap, CustomPageCategory customPageCategory) throws Exception {

        customPageService.save(customPageCategory);
    }

    @RequestMapping(value = "/customPageCategory/load/{objectId}", method = RequestMethod.GET)
    public void loadCustomPageCategory(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        CustomPageCategory customPageCategory = customPageService.get(CustomPageCategory.class, objectId);
        Map model = new HashMap();
        model.put("objectId", customPageCategory.getObjectId());
        model.put("name", customPageCategory.getName());
        model.put("nodeTag", customPageCategory.getNodeTag());
        model.put("description", customPageCategory.getDescription());

        if (customPageCategory.getParent() != null) {
            model.put("parent.objectId", CustomPageCategory.class.getSimpleName() + "_" + customPageCategory.getParent().getObjectId());
        }
        modelMap.put("data", model);
    }

    @RequestMapping(value = "/customPageCategory/delete/{objectId}", method = RequestMethod.GET)
    public void deleteCustomPageCategory(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        customPageService.logicDelete(CustomPageCategory.class, objectId);
    }


    //============================ 属性方法 ==============================================================


    public CustomPageService getCustomPageService() {
        return customPageService;
    }

    public void setCustomPageService(CustomPageService customPageService) {
        this.customPageService = customPageService;
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
