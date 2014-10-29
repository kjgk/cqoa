package com.withub.web.controller.system;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.enumeration.OrderByType;
import com.withub.model.entity.query.ExpressionOperation;
import com.withub.model.entity.query.OrderByProperty;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.system.po.Organization;
import com.withub.model.system.po.Role;
import com.withub.service.system.CodeService;
import com.withub.service.system.OrganizationService;
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
@RequestMapping(value = "/system")
public class OrganizationController extends BaseController {

    //================================ 属性声明 ===========================================================

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CodeService codeService;

    //================================ Controller 方法 ===================================================

    @RequestMapping(value = "/organization/loadManagerTree", method = RequestMethod.GET)
    public void loadManagerTree(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            Organization root = organizationService.getRootEntity(Organization.class);
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setType(Organization.class.getSimpleName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
            return;
        }

        String id = TreeNode.splitNode(nodeId, 1);
        Organization organization = organizationService.get(Organization.class, id);
        if (CollectionUtil.isEmpty(organization.getChildList())) {
            return;
        }

        for (Organization child : (organization.getChildList())) {
            TreeNode node = new TreeNode();
            node.setObjectId(child.getObjectId());
            node.setText(child.getName());
            node.setType(Organization.class.getSimpleName());
            node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
            node.setOrderNo(child.getOrderNo());
            node.getAttributes().put("organizationTag", child.getNodeTag());
            node.getAttributes().put("organizationTypeId", child.getOrganizationType().getObjectId());
            nodes.add(node);
        }
    }

    @RequestMapping(value = "/organization/loadTree", method = RequestMethod.GET)
    public void loadTree(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "nodes")
    ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            Organization root = organizationService.getRootEntity(Organization.class);
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            Organization organization = organizationService.get(Organization.class, nodeId);
            if (CollectionUtil.isEmpty(organization.getChildList())) {
                return;
            }
            for (Organization child : (organization.getChildList())) {
                TreeNode node = new TreeNode();
                node.setObjectId(child.getObjectId());
                node.setText(child.getName());
                node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
                node.setOrderNo(child.getOrderNo());
                nodes.add(node);
            }
        }
    }

    @RequestMapping(value = "/organization/loadTreePath", method = RequestMethod.GET)
    public void loadTreePath(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "node") ArrayList<String> nodes) throws Exception {

        Organization organization = organizationService.get(Organization.class, nodeId);
        if (organization.getNodeLevel() == 1) {
            return;
        } else {
            nodes.add(organization.getParent().getObjectId());
            loadTreePath(organization.getParent().getObjectId(), nodes);
        }
    }

    @RequestMapping(value = "/organization/create", method = RequestMethod.POST)
    public void createOrganization(ModelMap modelMap, Organization organization) throws Exception {
        organizationService.addOrganization(organization);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/organization/update", method = RequestMethod.POST)
    public void updateOrganization(ModelMap modelMap, Organization organization) throws Exception {

        organizationService.updateOrganization(organization);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/organization/load/{objectId}", method = RequestMethod.GET)
    public void loadOrganization(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        Organization organization = organizationService.get(Organization.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", organization.getObjectId());
        model.put("name", organization.getName());
        model.put("code", organization.getCode());
        model.put("nodeTag", organization.getNodeTag());
        model.put("contact", organization.getContact());
        model.put("phone", organization.getPhone());
        model.put("description", organization.getDescription());
        if (organization.getParent() != null) {
            model.put("parent.objectId", organization.getParent().getObjectId());
        }
        if (organization.getOrganizationType() != null) {
            model.put("organizationType.objectId", organization.getOrganizationType().getObjectId());
            model.put("organizationType.codeTag", organization.getOrganizationType().getCodeTag());
        }

        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/organization/loadByTag/{nodeTag}", method = RequestMethod.GET)
    public void loadByTag(ModelMap modelMap, @PathVariable("nodeTag") String nodeTag) throws Exception {

        Organization organization = (Organization) organizationService.getByPropertyValue(Organization.class, "nodeTag", nodeTag);
        if (CollectionUtil.isNotEmpty(organization.getChildList())) {
            List list = new ArrayList();
            for (Organization child : organization.getChildList()) {
                Map item = new HashMap();
                item.put("objectId", child.getObjectId());
                item.put("name", child.getName());
                list.add(item);
            }
            modelMap.put("childList", list);
        }
        modelMap.put("objectId", organization.getObjectId());
        modelMap.put("name", organization.getName());

    }

    @RequestMapping(value = "/organization/delete/{objectId}", method = RequestMethod.GET)
    public void deleteOrganization(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        organizationService.logicDelete(Organization.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/organization/query", method = RequestMethod.GET)
    public void queryOrganization(HttpServletRequest request, ModelMap modelMap) throws Exception {

        String id = request.getParameter("id");
        List<Organization> organizationList;
        int total = 0;
        List items = new ArrayList();
        if (StringUtil.isNotEmpty(id)) {
            Organization organization = organizationService.get(Organization.class, id);
            organizationList = organization.getChildList();
        } else {
            String name = request.getParameter("name");
            String organizationType = request.getParameter("organizationType");

            QueryInfo queryInfo = new QueryInfo();
            queryInfo.setTargetEntity(Organization.class);

            if (StringUtil.isNotEmpty(name)) {
                this.setQueryInfoCondition(queryInfo, "name", name, ExpressionOperation.MatchMiddle);
            }
            if (StringUtil.isNotEmpty(organizationType)) {
                this.setQueryInfoCondition(queryInfo, "organizationType.codeTag", organizationType, ExpressionOperation.Equals);
                this.setQueryInfoCondition(queryInfo, "nodeLevel", 3, ExpressionOperation.Equals);
            }

            this.setPageInfoQueryCondition(request, queryInfo);
            this.setAscOrderBy(queryInfo, "orderNo");

            RecordsetInfo recordsetInfo = organizationService.query(queryInfo);
            organizationList = recordsetInfo.getEntityList();
            total = recordsetInfo.getTotalRecordCount();
        }

        if (CollectionUtil.isEmpty(organizationList)) {
            return;
        }

        for (Organization organization : organizationList) {
            HashMap item = new HashMap();
            item.put("objectId", organization.getObjectId());
            item.put("name", organization.getName());
            item.put("code", organization.getCode());
            item.put("organizationType", organization.getOrganizationType().getName());
            item.put("organizationTag", organization.getNodeTag());
            item.put("parentOrganization", organization.getParent().getName());
            item.put("contact", organization.getContact());
            item.put("phone", organization.getPhone());

            items.add(item);
        }

        modelMap.put("total", total);
        modelMap.put("items", items);
    }


    @RequestMapping(value = "/organization/list", method = RequestMethod.GET)
    public void listOrganization(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Organization.class);

        setAscOrderBy(queryInfo, "nodeLevelCode");

        List list = organizationService.list(queryInfo);

        List items = new ArrayList();
        for (Organization organization : (List<Organization>) list) {
            Map item = new HashMap();
            item.put("value", organization.getObjectId());
            item.put("label", organization.getName());
            item.put("nodeLevel", organization.getNodeLevel());
            items.add(item);
        }

        modelMap.put("items", items);
    }
}
