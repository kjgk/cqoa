package com.withub.web.controller.std;


import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.std.po.Industry;
import com.withub.service.std.IndustryService;
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
public class IndustryController {


    //======================= 属性声明 ===================================================

    @Autowired
    private IndustryService industryService;

    //======================= Controller 方法 =============================================

    @RequestMapping(value = "/industry/loadManagerTree", method = RequestMethod.GET)
    public void loadManagerTree(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            Industry root = industryService.getRootIndustry();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setType(Industry.class.getSimpleName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            String id = nodeId.split("_")[1];
            Industry industry = (Industry) industryService.get(Industry.class, id);
            if (CollectionUtil.isEmpty(industry.getChildList())) {
                return;
            }
            for (Industry child : (industry.getChildList())) {
                TreeNode node = new TreeNode();
                node.setObjectId(child.getObjectId());
                node.setText(child.getName());
                node.setType(Industry.class.getSimpleName());
                node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
                node.setOrderNo(child.getOrderNo());
                nodes.add(node);
            }
        }
    }

    @RequestMapping(value = "/industry/loadTree", method = RequestMethod.GET)
    public void loadTree(@RequestParam(value = "node") String nodeId
            , @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            Industry root = industryService.getRootIndustry();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setLeaf(false);
            Map<String, String> map = new HashMap<String, String>();
            node.setAttributes(map);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            Industry industry = (Industry) industryService.get(Industry.class, nodeId);
            if (CollectionUtil.isEmpty(industry.getChildList())) {
                return;
            }
            for (Industry child : (industry.getChildList())) {
                TreeNode node = new TreeNode();
                node.setObjectId(child.getObjectId());
                node.setText(child.getName());
                Map<String, String> map = new HashMap<String, String>();
                node.setAttributes(map);
                node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
                node.setOrderNo(child.getOrderNo());
                nodes.add(node);
            }
        }
    }

    @RequestMapping(value = "/industry/create", method = RequestMethod.POST)
    public void createIndustry(ModelMap modelMap, Industry industry) throws Exception {

        industryService.save(industry);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/industry/update", method = RequestMethod.POST)
    public void updateCode(ModelMap modelMap, Industry industry) throws Exception {

        industryService.save(industry);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/industry/load/{objectId}", method = RequestMethod.GET)
    public void loadIndustry(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        Industry industry = (Industry) industryService.get(Industry.class, objectId);
        Map model = new HashMap();
        model.put("objectId", industry.getObjectId());
        model.put("name", industry.getName());
        model.put("description", industry.getDescription());
        if (industry.getParent() != null) {
            model.put("parent.objectId", industry.getParent().getObjectId());
        }
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/industry/delete/{objectId}", method = RequestMethod.GET)
    public void deleteIndustry(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        industryService.logicDelete(Industry.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/industry/query", method = RequestMethod.GET)
    public void listByParentId(@RequestParam("id") String id, @ModelAttribute("items") ArrayList<HashMap> items) throws Exception {

        List<Industry> industryList;
        if (StringUtil.isEmpty(id)) {
            List list = industryService.listByParentId(Industry.class, null);
            industryList = (List<Industry>) list;
        } else {
            Industry industry = (Industry) industryService.get(Industry.class, id);
            industryList = industry.getChildList();
        }
        if (CollectionUtil.isEmpty(industryList)) {
            return;
        }

        for (Industry industry : industryList) {
            HashMap item = new HashMap();
            item.put("objectId", industry.getObjectId());
            item.put("name", industry.getName());
            items.add(item);
        }
    }



    //======================= 属性方法 ===================================================
    public IndustryService getIndustryService() {
        return industryService;
    }

    public void setIndustryService(IndustryService industryService) {
        this.industryService = industryService;
    }
}
