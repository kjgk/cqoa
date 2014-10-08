package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.po.LawIssueOrganization;
import com.withub.service.std.LawIssueOrganizationService;
import com.withub.util.SpringSecurityUtil;
import com.withub.web.common.BaseController;
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
@RequestMapping(value = "/std")
public class LawIssueOrganizationController extends BaseController {

    //================================= 属性声明 ==========================================================

    @Autowired
    private LawIssueOrganizationService lawIssueOrganizationService;

    //================================= Controller 方法 ==================================================

    @RequestMapping(value = "/lawIssueOrganization/loadManagerTree", method = RequestMethod.GET)
    public void loadManagerTree(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            LawIssueOrganization root = lawIssueOrganizationService.getRootEntity(LawIssueOrganization.class);
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setType(LawIssueOrganization.class.getSimpleName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
            return;
        }

        String id = TreeNode.splitNode(nodeId, 1);
        LawIssueOrganization lawIssueOrganization = lawIssueOrganizationService.get(LawIssueOrganization.class, id);
        if (CollectionUtil.isEmpty(lawIssueOrganization.getChildList())) {
            return;
        }

        for (LawIssueOrganization child : (lawIssueOrganization.getChildList())) {
            TreeNode node = new TreeNode();
            node.setObjectId(child.getObjectId());
            node.setText(child.getName());
            node.setType(LawIssueOrganization.class.getSimpleName());
            node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
            node.setOrderNo(child.getOrderNo());
            nodes.add(node);
        }
    }

    @RequestMapping(value = "/lawIssueOrganization/loadTree", method = RequestMethod.GET)
    public void loadTree(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            LawIssueOrganization root = lawIssueOrganizationService.getRootLawIssueOrganization();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            LawIssueOrganization lawIssueOrganization = (LawIssueOrganization) lawIssueOrganizationService.get(LawIssueOrganization.class, nodeId);
            if (CollectionUtil.isEmpty(lawIssueOrganization.getChildList())) {
                return;
            }
            for (LawIssueOrganization child : (lawIssueOrganization.getChildList())) {
                TreeNode node = new TreeNode();
                node.setObjectId(child.getObjectId());
                node.setText(child.getName());
                node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
                node.setOrderNo(child.getOrderNo());
                nodes.add(node);
            }
        }
    }

    @RequestMapping(value = "/lawIssueOrganization/loadTreePath", method = RequestMethod.GET)
    public void loadTreePath(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "node") ArrayList<String> nodes) throws Exception {

        LawIssueOrganization lawIssueOrganization = (LawIssueOrganization) lawIssueOrganizationService.get(LawIssueOrganization.class, nodeId);
        if (lawIssueOrganization.getNodeLevel() == 1) {
            return;
        } else {
            nodes.add(lawIssueOrganization.getParent().getObjectId());
            loadTreePath(lawIssueOrganization.getParent().getObjectId(), nodes);
        }
    }

    @RequestMapping(value = "/lawIssueOrganization/update", method = RequestMethod.POST)
    public void updateLawIssueOrganization(ModelMap modelMap, LawIssueOrganization lawIssueOrganization) throws Exception {
        lawIssueOrganization.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        lawIssueOrganizationService.updateLawIssueOrganization(lawIssueOrganization);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/lawIssueOrganization/load/{objectId}", method = RequestMethod.GET)
    public void loadLawIssueOrganization(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        LawIssueOrganization lawLawIssueOrganization = (LawIssueOrganization) lawIssueOrganizationService.get(LawIssueOrganization.class, objectId);
        Map model = new HashMap();
        model.put("objectId", lawLawIssueOrganization.getObjectId());
        model.put("name", lawLawIssueOrganization.getName());
        model.put("description", lawLawIssueOrganization.getDescription());
        model.put("orderNo", lawLawIssueOrganization.getOrderNo());
        if (lawLawIssueOrganization.getParent() != null) {
            model.put("parent.objectId", lawLawIssueOrganization.getParent().getObjectId());
        }
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/lawIssueOrganization/create", method = RequestMethod.POST)
    public void createLawIssueOrganization(ModelMap modelMap, LawIssueOrganization lawIssueOrganization) throws Exception {
        lawIssueOrganization.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        //lawIssueOrganizationService.save(lawIssueOrganization);
        lawIssueOrganizationService.addLawIssueOrganization(lawIssueOrganization);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/lawIssueOrganization/delete/{objectId}", method = RequestMethod.GET)
    public void deleteLawIssueOrganization(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        lawIssueOrganizationService.logicDelete(LawIssueOrganization.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/lawIssueOrganization/query", method = RequestMethod.GET)
    public void queryLawIssueOrganization(HttpServletRequest request, ModelMap modelMap) throws Exception {


        String id = request.getParameter("id");
        List<LawIssueOrganization> lawIssueOrganizationList = null;
        int total = 0;
        List items = new ArrayList();
        if (StringUtil.isNotEmpty(id)) {
            LawIssueOrganization lawIssueOrganization = (LawIssueOrganization) lawIssueOrganizationService.get(LawIssueOrganization.class, id);
            lawIssueOrganizationList = lawIssueOrganization.getChildList();
        } else {
            QueryInfo queryInfo = new QueryInfo();
            queryInfo.setTargetEntity(LawIssueOrganization.class);
            this.setPageInfoQueryCondition(request, queryInfo);
            this.setAscOrderBy(queryInfo, "orderNo");

            RecordsetInfo recordsetInfo = lawIssueOrganizationService.query(queryInfo);
            lawIssueOrganizationList = recordsetInfo.getEntityList();
            total = recordsetInfo.getTotalRecordCount();
        }

        if (CollectionUtil.isEmpty(lawIssueOrganizationList)) {
            return;
        }

        for (LawIssueOrganization lawIssueOrganization : lawIssueOrganizationList) {
            HashMap item = new HashMap();
            item.put("objectId", lawIssueOrganization.getObjectId());
            item.put("name", lawIssueOrganization.getName());
            item.put("description", lawIssueOrganization.getDescription());
            items.add(item);
        }

        modelMap.put("total", total);
        modelMap.put("items", items);
    }

    //=================================== 属性方法 ========================================================


    public LawIssueOrganizationService getLawIssueOrganizationService() {

        return lawIssueOrganizationService;
    }

    public void setLawIssueOrganizationService(LawIssueOrganizationService lawIssueOrganizationService) {

        this.lawIssueOrganizationService = lawIssueOrganizationService;
    }
}
