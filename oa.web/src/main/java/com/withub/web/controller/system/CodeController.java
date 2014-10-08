package com.withub.web.controller.system;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.CodeColumn;
import com.withub.model.system.po.CodeColumnCategory;
import com.withub.service.system.CodeService;
import com.withub.web.common.ext.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/system")
public class CodeController {

    //======================= 属性声明 ===================================================

    @Autowired
    private CodeService codeService;

    //======================= Controller 方法 =============================================

    @RequestMapping(value = "/code/loadManagerTree", method = RequestMethod.GET)
    public void loadManagerTree(@RequestParam(value = "node") String nodeId
            , @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            CodeColumnCategory root = codeService.getRootCodeColumnCategory();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setType(CodeColumnCategory.class.getSimpleName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            String id = nodeId.split("_")[1];
            String type = nodeId.split("_")[0];
            if (StringUtil.compareValue(CodeColumnCategory.class.getSimpleName(), type)) {
                CodeColumnCategory category = codeService.get(CodeColumnCategory.class, id);
                if (CollectionUtil.isNotEmpty(category.getChildList())) {
                    for (CodeColumnCategory child : category.getChildList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(child.getObjectId());
                        node.setText(child.getName());
                        node.setType(CodeColumnCategory.class.getSimpleName());
                        node.setLeaf(false);
                        nodes.add(node);
                    }
                }
                if (CollectionUtil.isNotEmpty(category.getCodeColumnList())) {
                    for (CodeColumn codeColumn : category.getCodeColumnList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(codeColumn.getObjectId());
                        node.setText(codeColumn.getName());
                        node.setType(CodeColumn.class.getSimpleName());
                        node.setLeaf(true);
                        nodes.add(node);
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/code/loadTree", method = RequestMethod.GET)
    public void loadTree(@RequestParam(value = "node") String nodeId, @RequestParam(value = "depth") int depth,
                         @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            CodeColumnCategory root = codeService.getRootCodeColumnCategory();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setType(CodeColumnCategory.class.getSimpleName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            String id = nodeId.split("_")[1];
            String type = nodeId.split("_")[0];
            if (StringUtil.compareValue(CodeColumnCategory.class.getSimpleName(), type)) {
                CodeColumnCategory category = codeService.get(CodeColumnCategory.class, id);
                if (CollectionUtil.isNotEmpty(category.getChildList())) {
                    for (CodeColumnCategory child : category.getChildList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(child.getObjectId());
                        node.setText(child.getName());
                        node.setType(CodeColumnCategory.class.getSimpleName());
                        node.setLeaf(depth == 1 && CollectionUtil.isEmpty(child.getChildList()));
                        nodes.add(node);
                    }
                }
                if (depth > 1 && CollectionUtil.isNotEmpty(category.getCodeColumnList())) {
                    for (CodeColumn codeColumn : category.getCodeColumnList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(codeColumn.getObjectId());
                        node.setText(codeColumn.getName());
                        node.setType(CodeColumn.class.getSimpleName());
                        node.setLeaf(false);
                        nodes.add(node);
                    }
                }
            }

            if (StringUtil.compareValue(CodeColumn.class.getSimpleName(), type)) {
                CodeColumn codeColumn = codeService.get(CodeColumn.class, id);
                if (depth > 2 && CollectionUtil.isNotEmpty(codeColumn.getCodeList())) {
                    for (Code code : codeColumn.getCodeList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(code.getObjectId());
                        node.setText(code.getName());
                        node.setType(Code.class.getSimpleName());
                        node.setLeaf(true);
                        nodes.add(node);
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/code/loadTreePath", method = RequestMethod.GET)
    public void loadTreePath(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "node") ArrayList<String>
            nodes) throws Exception {

        String type = nodeId.split("_")[0];
        String id = nodeId.split("_")[1];

        String parentId = "";
        if (StringUtil.compareValue(CodeColumnCategory.class.getSimpleName(), type)) {
            CodeColumnCategory category = codeService.get(CodeColumnCategory.class, id);
            if (category.getNodeLevel() == 1) {
                return;
            }
            parentId = CodeColumnCategory.class.getSimpleName() + "_" + category.getParent().getObjectId();
        }

        if (StringUtil.compareValue(CodeColumn.class.getSimpleName(), type)) {
            CodeColumn codeColumn = codeService.get(CodeColumn.class, id);
            parentId = CodeColumnCategory.class.getSimpleName() + "_" + codeColumn.getCodeColumnCategory().getObjectId();
        }

        if (StringUtil.compareValue(Code.class.getSimpleName(), type)) {
            Code code = codeService.get(Code.class, id);
            parentId = CodeColumn.class.getSimpleName() + "_" + code.getCodeColumn().getObjectId();
        }
        nodes.add(parentId);
        loadTreePath(parentId, nodes);
    }

    @RequestMapping(value = "/codeColumnCategory/create", method = RequestMethod.POST)
    public void createCodeColumnCategory(ModelMap modelMap, @ModelAttribute(value = "exclude") CodeColumnCategory
            codeColumnCategory) throws Exception {

        codeService.save(codeColumnCategory);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/codeColumnCategory/update", method = RequestMethod.POST)
    public void updateCodeColumnCategory(ModelMap modelMap, CodeColumnCategory codeColumnCategory) throws Exception {

        codeService.save(codeColumnCategory);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/codeColumnCategory/load/{objectId}", method = RequestMethod.GET)
    public void loadCodeColumnCategory(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        CodeColumnCategory category = codeService.get(CodeColumnCategory.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", category.getObjectId());
        model.put("name", category.getName());
        model.put("description", category.getDescription());
        model.put("orderNo", category.getOrderNo());
        model.put("parent.objectId", CodeColumnCategory.class.getSimpleName() + "_" + category.getParent().getObjectId());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/codeColumnCategory/delete/{objectId}", method = RequestMethod.GET)
    public void deleteCodeColumnCategory(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        codeService.logicDelete(CodeColumnCategory.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/codeColumn/create", method = RequestMethod.POST)
    public void createCodeColumn(ModelMap modelMap, CodeColumn codeColumn) throws Exception {

        codeService.save(codeColumn);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/codeColumn/update", method = RequestMethod.POST)
    public void updateCodeColumn(ModelMap modelMap, CodeColumn codeColumn) throws Exception {

        codeService.save(codeColumn);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/codeColumn/load/{objectId}", method = RequestMethod.GET)
    public void loadCodeColumn(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        CodeColumn codeColumn = codeService.get(CodeColumn.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", codeColumn.getObjectId());
        model.put("name", codeColumn.getName());
        model.put("codeColumnTag", codeColumn.getCodeColumnTag());
        model.put("selectMode", codeColumn.getSelectMode());
        model.put("statusMode", codeColumn.getStatusMode());
        model.put("displayRegulation", codeColumn.getDisplayRegulation());
        model.put("description", codeColumn.getDescription());
        model.put("codeColumnCategory.objectId", CodeColumnCategory.class.getSimpleName() + "_" + codeColumn.getCodeColumnCategory().getObjectId());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/codeColumn/delete/{objectId}", method = RequestMethod.GET)
    public void deleteCodeColumn(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws
            Exception {

        codeService.logicDelete(CodeColumn.class, objectId);
        modelMap.put("success", true);
    }


    @RequestMapping(value = "/code/create", method = RequestMethod.POST)
    public void createCode(ModelMap modelMap, Code code) throws Exception {

        codeService.save(code);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/code/update", method = RequestMethod.POST)
    public void updateCode(ModelMap modelMap, Code code) throws Exception {

        codeService.save(code);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/code/load/{objectId}", method = RequestMethod.GET)
    public void loadCode(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        Code code = codeService.get(Code.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", code.getObjectId());
        model.put("name", code.getName());
        model.put("codeTag", code.getCodeTag());
        model.put("defaultValue", code.getDefaultValue());
        model.put("statusValue", code.getStatusValue());
        model.put("statusValue", code.getStatusValue());
        model.put("description", code.getDescription());
        model.put("orderNo", code.getOrderNo());
        model.put("codeColumn.objectId", code.getCodeColumn().getObjectId());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/code/delete/{objectId}", method = RequestMethod.GET)
    public void deleteCode(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        codeService.logicDelete(Code.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/code/query", method = RequestMethod.GET)
    public void list(@RequestParam("id") String id, @ModelAttribute("items") ArrayList<HashMap> items) throws Exception {

        CodeColumn codeColumn = codeService.get(CodeColumn.class, id);
        for (Code code : codeColumn.getCodeList()) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", code.getObjectId());
            item.put("name", code.getName());
            item.put("codeTag", code.getCodeTag());
            item.put("defaultValue", code.getDefaultValue());
            items.add(item);
        }
    }

    @RequestMapping(value = "/code/listCodeByCodeColumn", method = RequestMethod.GET)
    public void listCodeByCodeColumn(@RequestParam("codeColumnTag") String codeColumnTag,
                                     @ModelAttribute("items") ArrayList<HashMap> items) throws Exception {

        CodeColumn codeColumn = codeService.getCodeColumnByTag(codeColumnTag);
        if (codeColumn == null) {
            return;
        }
        for (Code code : codeColumn.getCodeList()) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("value", code.getObjectId());
            item.put("label", parseDisplayText(code));
            item.put("codeTag", code.getCodeTag());
            item.put("checked", code.getDefaultValue() == 1);
            items.add(item);
        }
    }

    private String parseDisplayText(Code code) throws Exception {

        String displayRegulation = code.getCodeColumn().getDisplayRegulation();

        if (StringUtil.isEmpty(displayRegulation)) {
            return code.getName();
        }

        String codeTag = code.getCodeTag() == null ? "" : code.getCodeTag();
        String descripiton = code.getDescription() == null ? "" : code.getDescription();

        String text = displayRegulation.replace("{name}", code.getName())
                .replace("{codeTag}", codeTag)
                .replace("{descripiton}", descripiton);

        return text;
    }
}
