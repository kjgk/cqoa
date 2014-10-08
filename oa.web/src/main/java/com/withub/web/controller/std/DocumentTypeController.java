package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.std.po.DocumentType;
import com.withub.model.std.po.DocumentTypeCategory;
import com.withub.service.std.DocumentTypeService;
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
public class DocumentTypeController {

    //======================= 属性声明 ===================================================

    @Autowired
    private DocumentTypeService documentTypeService;

    //======================= Controller 方法 =============================================

    @RequestMapping(value = "/documentType/loadManagerTree", method = RequestMethod.GET)
    public void loadManagerTree(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            DocumentTypeCategory root = documentTypeService.getRootDocumentTypeCategory();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setType(DocumentTypeCategory.class.getSimpleName());
            node.setText(root.getName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            String documentTypeCategoryId = nodeId.split("_")[1];
            DocumentTypeCategory documentTypeCategory = (DocumentTypeCategory) documentTypeService.get(DocumentTypeCategory.class, documentTypeCategoryId);
            if (CollectionUtil.isEmpty(documentTypeCategory.getChildList())) {
                return;
            }
            for (DocumentTypeCategory child : (documentTypeCategory.getChildList())) {
                TreeNode node = new TreeNode();
                node.setText(child.getName());
                node.setObjectId(child.getObjectId());
                node.setType(DocumentTypeCategory.class.getSimpleName());
                node.setLeaf(CollectionUtil.isEmpty(child.getChildList()));
                nodes.add(node);
            }
        }
    }

    @RequestMapping(value = "/documentType/loadTree", method = RequestMethod.GET)
    public void loadTree(@RequestParam(value = "node") String nodeId, @RequestParam(value = "depth") int depth
            , @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            DocumentTypeCategory root = documentTypeService.getRootDocumentTypeCategory();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setType(DocumentTypeCategory.class.getSimpleName());
            node.setText(root.getName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            String id = nodeId.split("_")[1];
            String type = nodeId.split("_")[0];
            if (StringUtil.compareValue(DocumentTypeCategory.class.getSimpleName(), type)) {
                DocumentTypeCategory category = (DocumentTypeCategory) documentTypeService.get(DocumentTypeCategory.class, id);
                if (CollectionUtil.isNotEmpty(category.getChildList())) {
                    for (DocumentTypeCategory child : category.getChildList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(child.getObjectId());
                        node.setText(child.getName());
                        node.setType(DocumentTypeCategory.class.getSimpleName());
                        node.setLeaf(depth == 1 && CollectionUtil.isEmpty(child.getChildList()));
                        nodes.add(node);
                    }
                }
                if (depth > 1 && CollectionUtil.isNotEmpty(category.getDocumentTypeList())) {
                    for (DocumentType documentType : category.getDocumentTypeList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(documentType.getObjectId());
                        node.setText(documentType.getName());
                        node.setType(DocumentType.class.getSimpleName());
                        node.setLeaf(true);
                        nodes.add(node);
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/documentType/loadTreePath", method = RequestMethod.GET)
    public void loadTreePath(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "node") ArrayList<String>
            nodes) throws Exception {

        String type = nodeId.split("_")[0];
        String id = nodeId.split("_")[1];

        String parentId = "";
        if (StringUtil.compareValue(DocumentTypeCategory.class.getSimpleName(), type)) {
            DocumentTypeCategory category = (DocumentTypeCategory) documentTypeService.get(DocumentTypeCategory.class, id);
            if (category.getNodeLevel() == 1) {
                return;
            }
            parentId = DocumentTypeCategory.class.getSimpleName() + "_" + category.getParent().getObjectId();
        }

        if (StringUtil.compareValue(DocumentType.class.getSimpleName(), type)) {
            DocumentType documentType = (DocumentType) documentTypeService.get(DocumentType.class, id);
            parentId = DocumentTypeCategory.class.getSimpleName() + "_" + documentType.getDocumentTypeCategory().getObjectId();
        }
        nodes.add(parentId);
        loadTreePath(parentId, nodes);
    }


    @RequestMapping(value = "/documentTypeCategory/create", method = RequestMethod.POST)
    public void createDocumentTypeCategory(ModelMap modelMap, DocumentTypeCategory documentTypeCategory) throws Exception {

        documentTypeService.save(documentTypeCategory);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/documentTypeCategory/update", method = RequestMethod.POST)
    public void updateDocumentTypeCategory(ModelMap modelMap, DocumentTypeCategory documentTypeCategory) throws Exception {

        documentTypeService.save(documentTypeCategory);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/documentTypeCategory/load/{objectId}", method = RequestMethod.GET)
    public void loadDocumentTypeCategory(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        DocumentTypeCategory documentTypeCategory = (DocumentTypeCategory) documentTypeService.get(DocumentTypeCategory.class, objectId);
        Map model = new HashMap();
        model.put("objectId", documentTypeCategory.getObjectId());
        model.put("name", documentTypeCategory.getName());
        model.put("nodeTag", documentTypeCategory.getNodeTag());
        model.put("code", documentTypeCategory.getCode());
        model.put("description", documentTypeCategory.getDescription());

        if (documentTypeCategory.getParent() != null) {
            model.put("parent.objectId", DocumentTypeCategory.class.getSimpleName() + "_" + documentTypeCategory.getParent().getObjectId());
        }
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/documentTypeCategory/delete/{objectId}", method = RequestMethod.GET)
    public void deleteDocumentTypeCategory(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        documentTypeService.logicDelete(DocumentTypeCategory.class, objectId);
        modelMap.put("success", true);
    }


    @RequestMapping(value = "/documentType/create", method = RequestMethod.POST)
    public void createDocumentType(ModelMap modelMap, DocumentType documentType) throws Exception {

        documentTypeService.save(documentType);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/documentType/update", method = RequestMethod.POST)
    public void updateDocumentType(ModelMap modelMap, DocumentType documentType) throws Exception {

        documentTypeService.save(documentType);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/documentType/load/{objectId}", method = RequestMethod.GET)
    public void loadDocumentType(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        DocumentType documentType = (DocumentType) documentTypeService.get(DocumentType.class, objectId);
        Map model = new HashMap();
        model.put("objectId", documentType.getObjectId());
        model.put("name", documentType.getName());
        model.put("code", documentType.getCode());
        model.put("documentTypeTag", documentType.getDocumentTypeTag());
        model.put("description", documentType.getDescription());
        model.put("documentTypeCategory.objectId", DocumentTypeCategory.class.getSimpleName() + "_" + documentType
                .getDocumentTypeCategory().getObjectId());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/documentType/delete/{objectId}", method = RequestMethod.GET)
    public void deleteDocumentType(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        documentTypeService.logicDelete(DocumentType.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/documentType/query", method = RequestMethod.GET)
    public void listDocumentType(@RequestParam("id") String id, @ModelAttribute("items") ArrayList<HashMap> items)
            throws Exception {

        DocumentTypeCategory documentTypeCategory = (DocumentTypeCategory) documentTypeService.get(DocumentTypeCategory.class, id);
        if (CollectionUtil.isEmpty(documentTypeCategory.getDocumentTypeList())) {
            return;
        }
        for (DocumentType documentType : documentTypeCategory.getDocumentTypeList()) {
            HashMap item = new HashMap();
            item.put("objectId", documentType.getObjectId());
            item.put("name", documentType.getName());
            item.put("documentTypeTag", documentType.getDocumentTypeTag());
            items.add(item);
        }
    }

    //======================= 属性方法 ===================================================


    public DocumentTypeService getDocumentTypeService() {

        return documentTypeService;
    }

    public void setDocumentTypeService(DocumentTypeService documentTypeService) {

        this.documentTypeService = documentTypeService;
    }
}
