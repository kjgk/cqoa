package com.withub.web.controller.workflow;

import com.withub.common.util.CollectionUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.workflow.po.TaskOpinionTemplate;
import com.withub.service.workflow.TaskOpinionTemplateService;
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
@RequestMapping(value = "/workflow")
public class TaskOpinionTemplateController extends BaseController {

    //================================== 属性声明 =========================================================

    @Autowired
    private TaskOpinionTemplateService taskOpinionTemplateService;

    //================================= Controller 方法 ==================================================

    @RequestMapping(value = "/taskOpinionTemplate/create", method = RequestMethod.POST)
    public void createTaskOpinionTemplate(ModelMap modelMap, TaskOpinionTemplate taskOpinionTemplate) throws Exception {

        taskOpinionTemplate.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        taskOpinionTemplateService.save(taskOpinionTemplate);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/taskOpinionTemplate/update", method = RequestMethod.POST)
    public void updateTaskOpinionTemplate(ModelMap modelMap, TaskOpinionTemplate taskOpinionTemplate) throws Exception {

        taskOpinionTemplate.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        taskOpinionTemplateService.save(taskOpinionTemplate);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/taskOpinionTemplate/load/{objectId}", method = RequestMethod.GET)
    public void loadTaskOpinionTemplate(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        TaskOpinionTemplate taskOpinionTemplate = taskOpinionTemplateService.get(TaskOpinionTemplate.class, objectId);
        HashMap model = new HashMap();
        model.put("objectId", taskOpinionTemplate.getObjectId());
        model.put("opinion", taskOpinionTemplate.getOpinion());
        model.put("user.objectId", taskOpinionTemplate.getUser().getObjectId());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/taskOpinionTemplate/delete/{objectId}", method = RequestMethod.GET)
    public void deleteTaskOpinionTemplate(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        taskOpinionTemplateService.logicDelete(TaskOpinionTemplate.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/taskOpinionTemplate/query", method = RequestMethod.GET)
    public void queryTaskOpinionTemplate(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(TaskOpinionTemplate.class);

        this.setPageInfoQueryCondition(request, queryInfo);
        this.setInputFieldQueryCondition(request, queryInfo, "opinion");
        this.setStringValueEqualsQueryCondition(queryInfo,"user.objectId",SpringSecurityUtil.getCurrentUser().getObjectId());
        this.setAscOrderBy(queryInfo, "orderNo");

        RecordsetInfo recordsetInfo = taskOpinionTemplateService.query(queryInfo);

        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (TaskOpinionTemplate taskOpinionTemplate : (List<TaskOpinionTemplate>) list) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", taskOpinionTemplate.getObjectId());
            item.put("opinion", taskOpinionTemplate.getOpinion());
            item.put("createTime", taskOpinionTemplate.getCreateTime());
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }
}
