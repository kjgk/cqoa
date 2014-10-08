package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.po.DocumentType;
import com.withub.model.std.po.EntityCacheConfig;
import com.withub.model.system.po.Entity;
import com.withub.service.std.EntityCacheService;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/std")
public class EntityCacheController extends BaseController {

    //======================= 属性声明 ===================================================

    @Autowired
    private EntityCacheService entityCacheService;

    //======================= Controller 方法 =============================================

    @RequestMapping(value = "/entityCacheConfig/query", method = RequestMethod.GET)
    public void queryEntityCacheConfig(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(EntityCacheConfig.class);

        this.setPageInfoQueryCondition(request, queryInfo);

        this.setDescOrderBy(queryInfo, "createTime");

        RecordsetInfo recordsetInfo = entityCacheService.query(queryInfo);

        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (EntityCacheConfig entityCacheConfig : (List<EntityCacheConfig>) list) {
            HashMap item = new HashMap();
            item.put("objectId", entityCacheConfig.getObjectId());
            item.put("name", entityCacheConfig.getName());
            item.put("entity", entityCacheConfig.getEntity().getName());
            item.put("cacheKey", entityCacheConfig.getCacheKey());
            item.put("timestampProperty", entityCacheConfig.getTimestampProperty());
            item.put("cacheCount", entityCacheConfig.getCacheCount());
            if (entityCacheConfig.getDocumentType() != null) {
                item.put("documentType", entityCacheConfig.getDocumentType().getName());
            }
            item.put("enable", entityCacheConfig.getEnable());
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/entityCacheConfig/create", method = RequestMethod.POST)
    public void createEntityCacheConfig(ModelMap modelMap, EntityCacheConfig entityCacheConfig) throws Exception {

        entityCacheService.save(entityCacheConfig);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entityCacheConfig/update", method = RequestMethod.POST)
    public void updateEntityCacheConfig(ModelMap modelMap, EntityCacheConfig entityCacheConfig) throws Exception {

        if (entityCacheConfig.getDocumentType() == null || StringUtil.isEmpty(entityCacheConfig.getDocumentType().getObjectId())) {
            entityCacheConfig.setDocumentType(null);
        }
        entityCacheService.save(entityCacheConfig);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entityCacheConfig/load/{objectId}", method = RequestMethod.GET)
    public void loadEntityCacheConfig(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        EntityCacheConfig entityCacheConfig = (EntityCacheConfig) entityCacheService.get(EntityCacheConfig.class, objectId);
        Map model = new HashMap();
        model.put("objectId", entityCacheConfig.getObjectId());
        model.put("name", entityCacheConfig.getName());
        model.put("enable", entityCacheConfig.getEnable());
        model.put("entity.objectId", Entity.class.getSimpleName() + "_" + entityCacheConfig.getEntity().getObjectId());
        model.put("cacheCount", entityCacheConfig.getCacheCount());
        model.put("cacheKey", entityCacheConfig.getCacheKey());
        model.put("timestampProperty", entityCacheConfig.getTimestampProperty());
        if (entityCacheConfig.getDocumentType() != null) {
            model.put("documentType.objectId", DocumentType.class.getSimpleName() + "_" + entityCacheConfig.getDocumentType().getObjectId());
        }
        model.put("addition", entityCacheConfig.getAddition());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entityCacheConfig/delete/{objectId}", method = RequestMethod.GET)
    public void deleteEntityCacheConfig(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        entityCacheService.logicDelete(EntityCacheConfig.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entityCacheConfig/refreshCache", method = RequestMethod.GET)
    public void refreshCache(ModelMap modelMap, HttpServletRequest request) throws Exception {

        entityCacheService.refreshCache();
        modelMap.put("success", true);
    }

    //======================= 属性方法 ===================================================

    public EntityCacheService getEntityCacheService() {

        return entityCacheService;
    }

    public void setEntityCacheService(EntityCacheService entityCacheService) {

        this.entityCacheService = entityCacheService;
    }
}
