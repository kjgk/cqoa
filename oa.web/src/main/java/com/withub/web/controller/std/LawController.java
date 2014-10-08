package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.po.Law;
import com.withub.model.std.po.LawCategory;
import com.withub.service.std.CommonTextService;
import com.withub.service.std.LawService;
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
public class LawController extends BaseController {

    //================================= 属性声明 ==========================================================

    @Autowired
    private LawService lawService;

    @Autowired
    private CommonTextService commonTextService;

    //================================= Controller 方法 ==================================================

    @RequestMapping(value = "/law/loadManagerTree", method = RequestMethod.GET)
    public void loadManagerTree(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            LawCategory root = lawService.getRootLawCategory();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setLeaf(false);
            node.setType(LawCategory.class.getSimpleName());
            node.setExpanded(true);
            nodes.add(node);
            return;
        }

        String lawCategoryId = nodeId.split("_")[1];
        LawCategory lawCategory = (LawCategory) lawService.get(LawCategory.class, lawCategoryId);
        if (CollectionUtil.isEmpty(lawCategory.getChildList())) {
            return;
        }

        for (LawCategory child : (lawCategory.getChildList())) {
            TreeNode node = new TreeNode();
            node.setObjectId(child.getObjectId());
            node.setText(child.getName());
            node.setType(LawCategory.class.getSimpleName());
            node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
            node.setOrderNo(child.getOrderNo());
            nodes.add(node);
        }
    }

    @RequestMapping(value = "/law/loadTree", method = RequestMethod.GET)
    public void loadTree(@RequestParam(value = "node") String nodeId
            , @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            LawCategory root = lawService.getRootLawCategory();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setType(LawCategory.class.getSimpleName());
            node.setText(root.getName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            String id = nodeId.split("_")[1];
            String type = nodeId.split("_")[0];
            if (StringUtil.compareValue(LawCategory.class.getSimpleName(), type)) {
                LawCategory category = (LawCategory) lawService.get(LawCategory.class, id);
                if (CollectionUtil.isNotEmpty(category.getChildList())) {
                    for (LawCategory child : category.getChildList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(child.getObjectId());
                        node.setText(child.getName());
                        node.setType(LawCategory.class.getSimpleName());
                        node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
                        nodes.add(node);
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/law/loadTreePath", method = RequestMethod.GET)
    public void loadTreePath(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "node") ArrayList<String>
            nodes) throws Exception {

        String type = nodeId.split("_")[0];
        String id = nodeId.split("_")[1];

        String parentId = "";
        if (StringUtil.compareValue(LawCategory.class.getSimpleName(), type)) {
            LawCategory category = (LawCategory) lawService.get(LawCategory.class, id);
            if (category.getNodeLevel() == 1) {
                return;
            }
            parentId = LawCategory.class.getSimpleName() + "_" + category.getParent().getObjectId();
        }

        if (StringUtil.compareValue(Law.class.getSimpleName(), type)) {
            Law law = (Law) lawService.get(Law.class, id);
            parentId = LawCategory.class.getSimpleName() + "_" + law.getLawCategory().getObjectId();
        }
        nodes.add(parentId);
        loadTreePath(parentId, nodes);
    }


    @RequestMapping(value = "/lawCategory/create", method = RequestMethod.POST)
    public void createLawCategory(ModelMap modelMap, LawCategory lawCategory) throws Exception {

        lawCategory.setCurrentUser(SpringSecurityUtil.getCurrentUser());

        lawService.save(lawCategory);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/lawCategory/update", method = RequestMethod.POST)
    public void updateLawCategory(ModelMap modelMap, LawCategory lawCategory) throws Exception {

        lawService.save(lawCategory);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/lawCategory/load/{objectId}", method = RequestMethod.GET)
    public void loadLawCategory(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        LawCategory lawCategory = (LawCategory) lawService.get(LawCategory.class, objectId);
        Map model = new HashMap();
        model.put("objectId", lawCategory.getObjectId());
        model.put("name", lawCategory.getName());
        model.put("code", lawCategory.getCode());
        model.put("description", lawCategory.getDescription());
        model.put("orderNo", lawCategory.getOrderNo());
        if (lawCategory.getParent() != null) {
            model.put("parent.objectId", LawCategory.class.getSimpleName() + "_" + lawCategory.getParent().getObjectId());
        }
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/lawCategory/delete/{objectId}", method = RequestMethod.GET)
    public void deleteLawCategory(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        lawService.logicDelete(LawCategory.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/law/create", method = RequestMethod.POST)
    public void createLaw(ModelMap modelMap, Law law) throws Exception {

        lawService.addLaw(law);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/law/update", method = RequestMethod.POST)
    public void updateLaw(ModelMap modelMap, Law law) throws Exception {

        lawService.updateLaw(law);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/law/load/{objectId}", method = RequestMethod.GET)
    public void loadLaw(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        Law law = (Law) lawService.get(Law.class, objectId);
        Map model = new HashMap();
        model.put("objectId", law.getObjectId());
        model.put("name", law.getName());
        model.put("issueOrganization", law.getIssueOrganization());
        model.put("issueDate", DateUtil.getDateFormatString(law.getIssueDate(), "yyyy-MM-dd"));

        model.put("law.lawCategory.objectId", LawCategory.class.getSimpleName() + "_" + law.getLawCategory().getObjectId());

        model.put("content", commonTextService.getContent(law));

        setAttachementInfo(model, law.getAttachmentList());

        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/law/delete/{objectId}", method = RequestMethod.GET)
    public void deleteLaw(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        lawService.logicDelete(Law.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/law/query", method = RequestMethod.GET)
    public void listLaw(@RequestParam("id") String id, @ModelAttribute("items") ArrayList<HashMap> items) throws Exception {

        LawCategory lawCategory = (LawCategory) lawService.get(LawCategory.class, id);
        if (CollectionUtil.isEmpty(lawCategory.getLawList())) {
            return;
        }
        for (Law law : lawCategory.getLawList()) {
            HashMap item = new HashMap();
            item.put("objectId", law.getObjectId());
            item.put("name", law.getName());
            item.put("lawCategoryId", law.getLawCategory().getObjectId());
            items.add(item);
        }
    }

    @RequestMapping(value = "/law/view/{objectId}", method = RequestMethod.GET)
    public String viewLaw(@PathVariable(value = "objectId") String objectId, HttpServletRequest reqeust) throws Exception {

        Law law = (Law) lawService.get(Law.class, objectId);
        reqeust.setAttribute("law", law);
        return "std/law/LawView";
    }

    @RequestMapping(value = "/law/queryLaw", method = RequestMethod.GET)
    public void queryLaw(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Law.class);
        this.setPageInfoQueryCondition(request, queryInfo);

        String lawCategoryId = request.getParameter("lawCategoryId");
        if (StringUtil.isNotEmpty(lawCategoryId)) {
            LawCategory lawCategory = (LawCategory) lawService.get(LawCategory.class, lawCategoryId);
            this.setNodeLevelCodeQueryCondition(queryInfo, "lawCategory.nodeLevelCode", lawCategory);
        }

        this.setInputFieldQueryCondition(request, queryInfo, "name");

        this.setDescOrderBy(queryInfo, "issueDate");

        RecordsetInfo recordsetInfo = lawService.query(queryInfo);

        List list = recordsetInfo.getEntityList();
        List items = new ArrayList();
        if (CollectionUtil.isNotEmpty(list)) {
            for (Law law : (List<Law>) list) {
                Map item = new HashMap();
                item.put("objectId", law.getObjectId());
                item.put("name", law.getName());
                item.put("issueOrganization", law.getIssueOrganization());
                if (law.getIssueDate() != null) {
                    item.put("issueDate", law.getIssueDate().getTime());
                }

                items.add(item);
            }
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    //=================================== 属性方法 ========================================================

    public LawService getLawService() {

        return lawService;
    }

    public void setLawService(LawService lawService) {

        this.lawService = lawService;
    }

    public CommonTextService getCommonTextService() {

        return commonTextService;
    }

    public void setCommonTextService(CommonTextService commonTextService) {

        this.commonTextService = commonTextService;
    }
}
