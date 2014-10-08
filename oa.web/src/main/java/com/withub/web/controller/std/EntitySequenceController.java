package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.po.EntitySequence;
import com.withub.model.std.po.EntitySequenceRegulation;
import com.withub.model.system.po.EntityCategory;
import com.withub.service.std.EntitySequenceService;
import com.withub.util.SpringSecurityUtil;
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
public class EntitySequenceController extends BaseController {

    //======================= 属性声明 ===================================================

    @Autowired
    private EntitySequenceService entitySequenceService;

    //======================= Controller 方法 =============================================

    @RequestMapping(value = "/entitySequence/query", method = RequestMethod.GET)
    public void listEntitySequence(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(EntitySequence.class);
        this.setPageInfoQueryCondition(request, queryInfo);
        this.setAscOrderBy(queryInfo, "orderNo");

        String entityCategoryId = request.getParameter("entityCategoryId");
        if (StringUtil.isNotEmpty(entityCategoryId)) {
            EntityCategory entityCategory = entitySequenceService.get(EntityCategory.class, entityCategoryId);
            this.setNodeLevelCodeQueryCondition(queryInfo, "entity.entityCategory.nodeLevelCode", entityCategory);
        }

        RecordsetInfo recordsetInfo = entitySequenceService.query(queryInfo);

        List list = recordsetInfo.getEntityList();
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (EntitySequence entitySequence : (List<EntitySequence>) list) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", entitySequence.getObjectId());
            item.put("name", entitySequence.getName());
            item.put("entity", entitySequence.getEntity().getName());
            item.put("sequenceProperty", entitySequence.getSequenceProperty());
            item.put("fixedLength", entitySequence.getFixedLength());
            item.put("circleSequenceByYear", entitySequence.getCircleSequenceByYear());
            item.put("yearProperty", entitySequence.getYearProperty());
            item.put("description", entitySequence.getDescription());

            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/entitySequence/create", method = RequestMethod.POST)
    public void createEntitySequence(ModelMap modelMap, EntitySequence entitySequence) throws Exception {

        entitySequence.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        entitySequenceService.createEntitySequence(entitySequence);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entitySequence/update", method = RequestMethod.POST)
    public void updateEntitySequence(ModelMap modelMap, EntitySequence entitySequence) throws Exception {

        entitySequence.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        entitySequenceService.updateEntitySequence(entitySequence);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entitySequence/load/{objectId}", method = RequestMethod.GET)
    public void loadEntitySequence(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        EntitySequence entitySequence = entitySequenceService.get(EntitySequence.class, objectId);
        List tempList = entitySequenceService.listByPropertyValue(EntitySequenceRegulation.class, "entitySequence.objectId", objectId);
        List<Map> regulationList = new ArrayList<Map>();
        if (CollectionUtil.isNotEmpty(tempList)) {

            for (EntitySequenceRegulation entitySequenceRegulation : (List<EntitySequenceRegulation>) tempList) {
                Map<String,String> regulation = new HashMap<String, String>();
                regulation.put("objectId", entitySequenceRegulation.getObjectId());
                regulation.put("name", entitySequenceRegulation.getName());
                regulation.put("regulationExpression", entitySequenceRegulation.getRegulationExpression());
                regulationList.add(regulation);
            }
        }


        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", entitySequence.getObjectId());
        model.put("name", entitySequence.getName());
        model.put("entity.objectId", entitySequence.getEntity().getObjectId());
        model.put("sequenceProperty", entitySequence.getSequenceProperty());
        model.put("fixedLength", entitySequence.getFixedLength());
        model.put("circleSequenceByYear", entitySequence.getCircleSequenceByYear());
        model.put("yearProperty", entitySequence.getYearProperty());
        model.put("description", entitySequence.getDescription());

        modelMap.put("data", model);
        modelMap.put("regulationList", regulationList);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/entitySequence/delete/{objectId}", method = RequestMethod.GET)
    public void deleteEntitySequence(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        entitySequenceService.logicDelete(EntitySequence.class, objectId);
        modelMap.put("success", true);
    }
    //======================= 属性方法 ===================================================

    public EntitySequenceService getEntitySequenceService() {

        return entitySequenceService;
    }

    public void setEntitySequenceService(EntitySequenceService entitySequenceService) {

        this.entitySequenceService = entitySequenceService;
    }
}
