package com.withub.web.controller.system;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.common.util.StringUtil;
import com.withub.common.util.VelocityUtil;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.entity.AbstractEntity;
import com.withub.model.entity.enumeration.EntityRowMoveType;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.system.po.*;
import com.withub.service.system.MetadataService;
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
@RequestMapping(value = "/system")
public class MetadataController extends BaseController {

    //=============================== 属性声明 ============================================================

    @Autowired
    private MetadataService metadataService;

    //=============================== Controller 方法 ====================================================

    @RequestMapping(value = "/metadata/loadManagerTree", method = RequestMethod.GET)
    public void loadManagerTree(@RequestParam(value = "node") String nodeId
            , @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            EntityCategory root = metadataService.getRootEntityCategory();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setType(EntityCategory.class.getSimpleName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            String id = nodeId.split("_")[1];
            String type = nodeId.split("_")[0];
            if (StringUtil.compareValue(EntityCategory.class.getSimpleName(), type)) {
                EntityCategory category = metadataService.get(EntityCategory.class, id);
                if (CollectionUtil.isNotEmpty(category.getChildList())) {
                    for (EntityCategory child : category.getChildList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(child.getObjectId());
                        node.setText(child.getName());
                        node.setType(EntityCategory.class.getSimpleName());
                        node.setLeaf(false);
                        nodes.add(node);
                    }
                }

                if (CollectionUtil.isNotEmpty(category.getEntityList())) {
                    for (Entity entity : category.getEntityList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(entity.getObjectId());
                        node.setText(entity.getName());
                        node.setType(Entity.class.getSimpleName());
                        node.getAttributes().put("entityName", entity.getEntityName());
                        node.setLeaf(true);
                        nodes.add(node);
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/metadata/loadTree", method = RequestMethod.GET)
    public void loadTree(@RequestParam(value = "node") String nodeId, @RequestParam(value = "depth") int depth,
                         @ModelAttribute(value = "nodes") ArrayList<TreeNode> nodes) throws Exception {

        if (StringUtil.compareValue(nodeId, TreeNode.ROOT)) {
            EntityCategory root = metadataService.getRootEntityCategory();
            TreeNode node = new TreeNode();
            node.setObjectId(root.getObjectId());
            node.setText(root.getName());
            node.setType(EntityCategory.class.getSimpleName());
            node.setLeaf(false);
            node.setExpanded(true);
            nodes.add(node);
        } else {
            String id = nodeId.split("_")[1];
            String type = nodeId.split("_")[0];
            if (StringUtil.compareValue(EntityCategory.class.getSimpleName(), type)) {
                EntityCategory category = metadataService.get(EntityCategory.class, id);
                if (CollectionUtil.isNotEmpty(category.getChildList())) {
                    for (EntityCategory child : category.getChildList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(child.getObjectId());
                        node.setText(child.getName());
                        node.setType(EntityCategory.class.getSimpleName());
                        node.setLeaf(depth == 1 && CollectionUtil.isEmpty(child.getChildList()));
                        nodes.add(node);
                    }
                }
                if (depth > 1 && CollectionUtil.isNotEmpty(category.getEntityList())) {
                    for (Entity entity : category.getEntityList()) {
                        TreeNode node = new TreeNode();
                        node.setObjectId(entity.getObjectId());
                        node.setText(entity.getName());
                        node.setType(Entity.class.getSimpleName());
                        node.setLeaf(true);
                        nodes.add(node);
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/metadata/loadTreePath", method = RequestMethod.GET)
    public void loadTreePath(@RequestParam(value = "node") String nodeId, @ModelAttribute(value = "node") ArrayList<String> nodes) throws Exception {

        String type = nodeId.split("_")[0];
        String id = nodeId.split("_")[1];

        String parentId = "";
        if (StringUtil.compareValue(EntityCategory.class.getSimpleName(), type)) {
            EntityCategory category = metadataService.get(EntityCategory.class, id);
            if (category.getNodeLevel() == 1) {
                return;
            }
            parentId = EntityCategory.class.getSimpleName() + "_" + category.getParent().getObjectId();
        }

        if (StringUtil.compareValue(Entity.class.getSimpleName(), type)) {
            Entity entity = metadataService.get(Entity.class, id);
            parentId = EntityCategory.class.getSimpleName() + "_" + entity.getEntityCategory().getObjectId();
        }
        nodes.add(parentId);
        loadTreePath(parentId, nodes);
    }

    @RequestMapping(value = "/entityCategory/create", method = RequestMethod.POST)
    public void createEntityCategory(ModelMap modelMap, EntityCategory entityCategory) throws Exception {

        metadataService.save(entityCategory);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entityCategory/update", method = RequestMethod.POST)
    public void updateEntityCategory(ModelMap modelMap, EntityCategory entityCategory) throws Exception {

        metadataService.save(entityCategory);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entityCategory/load/{objectId}", method = RequestMethod.GET)
    public void loadEntityCategory(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        EntityCategory category = metadataService.get(EntityCategory.class, objectId);
        Map model = new HashMap();
        model.put("objectId", category.getObjectId());
        model.put("name", category.getName());
        model.put("description", category.getDescription());
        model.put("orderNo", category.getOrderNo());
        model.put("parent.objectId", EntityCategory.class.getSimpleName() + "_" + category.getParent().getObjectId());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entityCategory/delete/{objectId}", method = RequestMethod.GET)
    public void deleteEntityCategory(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        metadataService.logicDelete(EntityCategory.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entity/create", method = RequestMethod.POST)
    public void createEntity(ModelMap modelMap, Entity entity) throws Exception {

        metadataService.save(entity);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entity/update", method = RequestMethod.POST)
    public void updateEntity(ModelMap modelMap, Entity entity) throws Exception {
        metadataService.save(entity);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entity/delete/{objectId}", method = RequestMethod.GET)
    public void deleteEntity(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        metadataService.logicDelete(Entity.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entity/load/{objectId}", method = RequestMethod.GET)
    public void loadEntity(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        Entity entity = metadataService.get(Entity.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", entity.getObjectId());
        model.put("name", entity.getName());
        model.put("entityCategory.objectId", EntityCategory.class.getSimpleName() + "_" + entity.getEntityCategory().getObjectId());
        model.put("allowAuthorization", entity.getAllowAuthorization());
        model.put("entityName", entity.getEntityName());
        model.put("tableName", entity.getTableName());
        model.put("entityType.objectId", entity.getEntityType().getObjectId());
        model.put("className", entity.getClassName());
        model.put("defaultOrderProperty", entity.getDefaultOrderProperty());
        model.put("defaultOrderType", entity.getDefaultOrderType());
        model.put("orderNoDependProperty", entity.getOrderNoDependProperty());
        model.put("identifierTemplate", entity.getIdentifierTemplate());

        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/metadata/listEntity", method = RequestMethod.GET)
    public void listEntity(@RequestParam("entity") String entityName, @RequestParam("order") String orderBy,
                           @ModelAttribute("items") ArrayList<HashMap> items) throws Exception {

        List<AbstractEntity> entityList = metadataService.listEntity(entityName, orderBy);

        for (AbstractEntity entity : entityList) {
            HashMap item = new HashMap();
            item.put("value", entity.getObjectId());
            item.put("label", metadataService.getNamePropertyValue(entity));
            items.add(item);
        }
    }

    @RequestMapping(value = "/metadata/setRecursionEntityRootNode", method = RequestMethod.POST)
    public void createRecursionEntityRootNode(ModelMap modelMap, @RequestParam("name") String name, @RequestParam("nodeTag") String nodeTag,
                                              @RequestParam("description") String description, @RequestParam("entityName") String entityName) throws Exception {

        metadataService.createRecursionEntityRootNode(name, nodeTag, description, entityName, SpringSecurityUtil.getCurrentUser());
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/metadata/resetEntityOrderNo", method = RequestMethod.POST)
    public void resetEntityOrderNo(@RequestParam("entity") String entityName, ModelMap modelMap) throws Exception {

        User currentUser = SpringSecurityUtil.getCurrentUser();
        metadataService.resetEntityOrderNo(currentUser, entityName);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/metadata/setEntityPinYin", method = RequestMethod.POST)
    public void setEntityPinYin(@RequestParam("entity") String entityName, ModelMap modelMap) throws Exception {

        metadataService.setEntityPinYin(entityName);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/metadata/orderEntity", method = RequestMethod.POST)
    public void orderEntity(@RequestParam("entity") String entityName
            , @RequestParam(value = "move", required = false) EntityRowMoveType entityRowMoveType
            , @RequestParam(value = "orderNo", required = false) Integer orderNo
            , @RequestParam("objectId") String objectId, ModelMap modelMap) throws Exception {

        User currentUser = SpringSecurityUtil.getCurrentUser();
        if (orderNo != null) {
            metadataService.setEntityOrderNo(currentUser, entityName, objectId, orderNo);
        } else {
            metadataService.moveEntityRow(currentUser, entityName, objectId, entityRowMoveType);
        }
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/metadata/searchEntity", method = RequestMethod.GET)
    public void searchEntity(HttpServletRequest request, @RequestParam("entity") String entityName,
                             ModelMap modelMap) throws Exception {

        String queryProperty = request.getParameter("queryProperty");
        String keyword = request.getParameter("keyword");
        String objectId = request.getParameter("objectId");
        String objectIds = request.getParameter("objectIds");

        List items = new ArrayList();
        if (StringUtil.isNotEmpty(keyword)) {
            List<AbstractEntity> entityList = metadataService.searchEntity(entityName, queryProperty, keyword);
            if (StringUtil.isEmpty(queryProperty)) {
                queryProperty = "name";
            }
            for (AbstractEntity entity : entityList) {
                HashMap item = new HashMap();
                item.put("value", entity.getObjectId());
                item.put("label", metadataService.getPropertyValue(entity, queryProperty));
                items.add(item);
            }
        } else if (StringUtil.isNotEmpty(objectId)) {
            Entity entity = (Entity) metadataService.getByPropertyValue(Entity.class, "entityName", entityName);
            AbstractEntity abstractEntity = metadataService.getEntityByClassName(entity.getClassName(), objectId);
            HashMap item = new HashMap();
            item.put("value", abstractEntity.getObjectId());
            item.put("label", metadataService.getPropertyValue(abstractEntity, queryProperty));
            items.add(item);
        } else if (StringUtil.isNotEmpty(objectIds)) {
            Entity entity = (Entity) metadataService.getByPropertyValue(Entity.class, "entityName", entityName);
            for (String id : objectIds.split(",")) {
                AbstractEntity abstractEntity = metadataService.getEntityByClassName(entity.getClassName(), id);
                HashMap item = new HashMap();
                item.put("value", abstractEntity.getObjectId());
                item.put("label", metadataService.getPropertyValue(abstractEntity, queryProperty));
                items.add(item);
            }
        }
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/metadata/checkPropertyValueExists", method = RequestMethod.GET)
    public void checkPropertyValueExists(@RequestParam("entity") String entityName, @RequestParam("property") String propertyName
            , @RequestParam("value") String propertyValue, ModelMap modelMap) throws Exception {

        boolean exists = metadataService.checkPropertyValueExists(entityName, propertyName, propertyValue);
        modelMap.put("data", exists);
    }


    @RequestMapping(value = "/property/query", method = RequestMethod.GET)
    public void listProperty(HttpServletRequest request, ModelMap modelMap) throws Exception {

        String id = request.getParameter("id");
        Entity entity = metadataService.get(Entity.class, id);
        List items = new ArrayList();
        if (entity == null || CollectionUtil.isEmpty(entity.getPropertyList())) {
            return;
        }

        for (Property property : entity.getPropertyList()) {
            if (property.getObjectStatus() == 1) {
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("objectId", property.getObjectId());
                item.put("entity.objectId", Entity.class.getSimpleName() + TreeNode.NODE_SPLITTER + property.getEntity().getObjectId());
                item.put("name", property.getName());
                item.put("propertyName", property.getPropertyName());
                item.put("columnName", property.getColumnName());
                item.put("propertyType", property.getPropertyType().getName());
                item.put("propertyDataType", property.getPropertyDataType().getName());
                item.put("dataLength", property.getDataLength());
                if (property.getPropertEntity() != null) {
                    item.put("propertEntity.objectId", Entity.class.getSimpleName() + TreeNode.NODE_SPLITTER + property.getPropertEntity().getObjectId());
                }
                if (property.getPropertCodeColumn() != null) {
                    item.put("propertCodeColumn.objectId", CodeColumn.class.getSimpleName() + TreeNode.NODE_SPLITTER + property.getPropertCodeColumn().getObjectId());
                }

                item.put("description", property.getDescription());

                items.add(item);
            }
        }
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/property/load/{objectId}", method = RequestMethod.GET)
    public void loadProperty(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        Property property = metadataService.get(Property.class, objectId);
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("objectId", property.getObjectId());
        item.put("entity.objectId", Entity.class.getSimpleName() + TreeNode.NODE_SPLITTER + property.getEntity().getObjectId());
        item.put("name", property.getName());
        item.put("propertyName", property.getPropertyName());
        item.put("columnName", property.getColumnName());
        item.put("language", property.getLanguage());
        item.put("propertyType.objectId", property.getPropertyType().getObjectId());
        item.put("propertyDataType.objectId", property.getPropertyDataType().getObjectId());
        item.put("fullTextSearchAlias", property.getFullTextSearchAlias());
        item.put("enableFullTextSearch", property.getEnableFullTextSearch());
        item.put("fullTextSearchField", property.getFullTextSearchField());
        item.put("dataLength", property.getDataLength());
        if (property.getPropertEntity() != null) {
            item.put("propertEntity.objectId", Entity.class.getSimpleName() + TreeNode.NODE_SPLITTER + property.getPropertEntity().getObjectId());
        }
        if (property.getPropertCodeColumn() != null) {
            item.put("propertCodeColumn.objectId", CodeColumn.class.getSimpleName() + TreeNode.NODE_SPLITTER + property.getPropertCodeColumn().getObjectId());
        }

        item.put("description", property.getDescription());

        modelMap.put("data", item);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/property/create", method = RequestMethod.POST)
    public void createProperty(ModelMap modelMap, Property property) throws Exception {

        metadataService.save(property);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/property/update", method = RequestMethod.POST)
    public void updateProperty(ModelMap modelMap, Property property) throws Exception {

        metadataService.save(property);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/property/delete/{objectId}", method = RequestMethod.GET)
    public void deleteProperty(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        metadataService.logicDelete(Property.class, objectId);
        modelMap.put("success", true);
    }


    @RequestMapping(value = "/dataDeleted/query", method = RequestMethod.GET)
    public void listDataDeleted(HttpServletRequest request, ModelMap modelMap) throws Exception {

        String entityId = request.getParameter("entityId");
        if (StringUtil.isEmpty(entityId)) {
            return;
        }

        Entity entity = metadataService.get(Entity.class, entityId);
        String identifierTemplate = entity.getIdentifierTemplate();
        AbstractEntity abstractEntity = (AbstractEntity) Class.forName(entity.getClassName()).newInstance();

        if (!(abstractEntity instanceof AbstractBaseEntity)) {
            return;
        }

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(abstractEntity.getClass());
        this.setPageInfoQueryCondition(request, queryInfo);
        this.setDateRangeQueryCondition(request, queryInfo, "lastUpdateTime");
        this.setDescOrderBy(queryInfo, "lastUpdateTime");

        RecordsetInfo recordsetInfo = metadataService.queryDeleted(queryInfo);

        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (AbstractBaseEntity abstractBaseEntity : (List<AbstractBaseEntity>) list) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", abstractBaseEntity.getObjectId());
            item.put("entityId", entityId);
            item.put("creator", abstractBaseEntity.getCreator() == null ? "-" : abstractBaseEntity.getCreator().getName());
            item.put("lastEditor", abstractBaseEntity.getLastEditor() == null ? "-" : abstractBaseEntity.getLastEditor().getName());
            item.put("deletedTime", DateUtil.getDateFormatString(abstractBaseEntity.getLastUpdateTime(), DateUtil.STANDARD_DATETIME_FORMAT));
            item.put("detail", StringUtil.isEmpty(identifierTemplate) ? "无标记模版" : VelocityUtil.getVelocityContent(identifierTemplate, entity.getEntityName(), abstractBaseEntity));

            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }


    @RequestMapping(value = "/dataDeleted/recover", method = RequestMethod.GET)
    public void dataDeletedRecover(@RequestParam("entityId") String entityId
            , @RequestParam("objectId") String objectId, ModelMap modelMap) throws Exception {

        Entity entity = metadataService.get(Entity.class, entityId);
        AbstractEntity abstractEntity = (AbstractEntity) Class.forName(entity.getClassName()).newInstance();
        AbstractBaseEntity abstractBaseEntity = (AbstractBaseEntity) metadataService.get(abstractEntity.getClass(), objectId);
        abstractBaseEntity.setObjectStatus(1);

        metadataService.save(abstractBaseEntity);
        modelMap.put("success", true);
    }

}
