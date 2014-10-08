package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.std.po.Region;
import com.withub.service.std.RegionService;
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
@RequestMapping(value = "/std")
public class RegionController {

    //======================= 属性声明 ===================================================

    @Autowired
    private RegionService regionService;

    //======================= Controller 方法 =============================================

    @RequestMapping(value = "/region/loadManagerTree", method = RequestMethod.GET)
    public void loadManagerTree(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            Region root = regionService.getRootEntity(Region.class);
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setType(Region.class.getSimpleName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            String id = nodeId.split("_")[1];
            Region region = regionService.get(Region.class, id);
            if (CollectionUtil.isEmpty(region.getChildList())) {
                return;
            }
            for (Region child : (region.getChildList())) {
                TreeNode node = new TreeNode();
                node.setObjectId(child.getObjectId());
                node.setText(child.getName());
                node.setType(Region.class.getSimpleName());
                node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
                node.setOrderNo(child.getOrderNo());
                nodes.add(node);
            }
        }
    }

    @RequestMapping(value = "/region/loadTree", method = RequestMethod.GET)
    public void loadTree(@RequestParam(value = "node") String nodeId
            , @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            Region root = regionService.getRootEntity(Region.class);
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setLeaf(false);
            Map<String, String> map = new HashMap<String, String>();
            map.put("regionType", "root");
            node.setAttributes(map);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            Region region = (Region) regionService.get(Region.class, nodeId);
            if (CollectionUtil.isEmpty(region.getChildList())) {
                return;
            }
            for (Region child : (region.getChildList())) {
                TreeNode node = new TreeNode();
                node.setObjectId(child.getObjectId());
                node.setText(child.getName());
                Map<String, String> map = new HashMap<String, String>();
                map.put("regionType", child.getRegionType().getCodeTag());
                node.setAttributes(map);
                node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
                node.setOrderNo(child.getOrderNo());
                nodes.add(node);
            }
        }
    }

    @RequestMapping(value = "/region/loadTreePath", method = RequestMethod.GET)
    public void loadTreePath(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "node") ArrayList<String>
            nodes) throws Exception {

        Region region = (Region) regionService.get(Region.class, nodeId);
        if (region.getNodeLevel() == 1) {
            return;
        } else {
            nodes.add(region.getParent().getObjectId());
            loadTreePath(region.getParent().getObjectId(), nodes);
        }
    }


    @RequestMapping(value = "/region/create", method = RequestMethod.POST)
    public void createRegion(ModelMap modelMap, Region region) throws Exception {

        region.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        regionService.save(region);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/region/update", method = RequestMethod.POST)
    public void updateRegion(ModelMap modelMap, Region region) throws Exception {

        region.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        regionService.save(region);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/region/load/{objectId}", method = RequestMethod.GET)
    public void loadRegion(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        Region region = (Region) regionService.get(Region.class, objectId);
        Map model = new HashMap();
        model.put("objectId", region.getObjectId());
        model.put("name", region.getName());
        model.put("regionType.objectId", region.getRegionType().getObjectId());
        model.put("description", region.getDescription());
        model.put("nodeTag", region.getNodeTag());
        if (region.getParent() != null) {
            model.put("parent.objectId", region.getParent().getObjectId());
        }
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/region/delete/{objectId}", method = RequestMethod.GET)
    public void deleteRegion(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        regionService.logicDelete(Region.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/region/query", method = RequestMethod.GET)
    public void listByParentId(@RequestParam("id") String id, @ModelAttribute("items") ArrayList<HashMap> items) throws Exception {

        List<Region> regionList;
        if (StringUtil.isEmpty(id)) {
            List list = regionService.listByParentId(Region.class, null);
            regionList = (List<Region>) list;
        } else {
            Region region = (Region) regionService.get(Region.class, id);
            regionList = region.getChildList();
        }
        if (CollectionUtil.isEmpty(regionList)) {
            return;
        }

        for (Region region : regionList) {
            HashMap item = new HashMap();
            item.put("objectId", region.getObjectId());
            item.put("name", region.getName());
            items.add(item);
        }
    }

    //======================= 属性方法 ===================================================

    public RegionService getRegionService() {

        return regionService;
    }

    public void setRegionService(RegionService regionService) {

        this.regionService = regionService;
    }
}
