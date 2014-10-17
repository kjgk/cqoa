package com.withub.web.controller.workflow;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.*;
import com.withub.model.system.po.Organization;
import com.withub.model.system.po.User;
import com.withub.model.workflow.NextFlowNodeInfo;
import com.withub.model.workflow.enumeration.FlowNodeType;
import com.withub.model.workflow.enumeration.TaskStatus;
import com.withub.model.workflow.po.FlowNode;
import com.withub.model.workflow.po.Instance;
import com.withub.model.workflow.po.InstanceOrganization;
import com.withub.model.workflow.po.Task;
import com.withub.model.workflow.vo.ApproveInfo;
import com.withub.model.workflow.vo.InstanceTaskLog;
import com.withub.model.workflow.vo.TaskInfo;
import com.withub.service.workflow.WorkflowService;
import com.withub.web.common.BaseController;
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
@RequestMapping(value = "/workflow")
public class WorkflowController extends BaseController {

    //================================== 属性声明 =========================================================

    @Autowired
    private WorkflowService workflowService;

    //================================= Controller 方法 ==================================================

    @RequestMapping(value = "/task/queryCurrentUserTask", method = RequestMethod.GET)
    public void queryCurrentUserTask(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(TaskInfo.class);

        this.setPageInfoQueryCondition(request, queryInfo);

        this.setDateRangeQueryCondition(request, queryInfo, "taskCreateTime");

        this.setStringValueEqualsQueryCondition(request, queryInfo, "flowTypeId", "flowType");

        this.setStringValueEqualsQueryCondition(request, queryInfo, "taskStatus", "statusId");

        this.setStringValueEqualsQueryCondition(request, queryInfo, "taskStatusTag", "statusTag");

        this.setStringValueNotEqualsQueryCondition(queryInfo, "flowNodeType", "First");

        this.setDescOrderBy(queryInfo, "taskCreateTime");

        RecordsetInfo recordsetInfo = workflowService.queryCurrentUserTask(queryInfo);
        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (TaskInfo taskInfo : (List<TaskInfo>) list) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", taskInfo.getObjectId());
            item.put("instanceName", taskInfo.getInstanceName());
            item.put("flowType", taskInfo.getFlowTypeName());
            item.put("flowNodeType", taskInfo.getFlowNodeType());
            item.put("flowNodeName", taskInfo.getFlowNodeName());
            item.put("taskStatusName", taskInfo.getTaskStatusName());
            item.put("creatorName", taskInfo.getCreatorName());
            item.put("organizationName", taskInfo.getOrganizationName());
            item.put("result", taskInfo.getTaskHandleResultName());
            item.put("relatedObjectId", taskInfo.getRelatedObjectId());
            item.put("activity", taskInfo.getActivity());
            item.put("taskCreateTime", taskInfo.getTaskCreateTime().getTime());
            item.put("taskArriveTime", taskInfo.getTaskArriveTime().getTime());
            item.put("taskExpiration", taskInfo.getTaskExpiration());
            item.put("taskStatus", taskInfo.getTaskStatusTag());
            if (taskInfo.getTaskFinishTime() != null) {
                item.put("taskFinishTime", taskInfo.getTaskFinishTime().getTime());
            }
            item.put("taskLocked", taskInfo.getTaskLocked());
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/task/runningTask", method = RequestMethod.GET)
    public String runningTask(HttpServletRequest request) throws Exception {

        String pageSize = request.getParameter("pageSize");
        String pageIndex = request.getParameter("pageIndex");


        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(TaskInfo.class);

        this.setQueryInfoCondition(queryInfo, "taskStatusTag", TaskStatus.Running.name(), ExpressionOperation.Equals);

        queryInfo.setRecordsetSize(pageSize == null ? 6 : Integer.parseInt(pageSize));
        queryInfo.setRecordsetIndex(pageIndex == null ? 0 : Integer.parseInt(pageIndex));

        this.setDescOrderBy(queryInfo, "taskCreateTime");

        RecordsetInfo recordsetInfo = workflowService.queryCurrentUserTask(queryInfo);

        request.setAttribute("taskList", recordsetInfo.getEntityList());

        return "workflow/task/RunningTaskList";
    }

    @RequestMapping(value = "/instance/queryCurrentUserInstance", method = RequestMethod.GET)
    public void queryCurrentUserInstance(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Instance.class);

        this.setPageInfoQueryCondition(request, queryInfo);

        this.setDateRangeQueryCondition(request, queryInfo, "createTime");

        this.setStringValueEqualsQueryCondition(request, queryInfo, "flowType.objectId", "flowTypeId");

        this.setStringValueEqualsQueryCondition(request, queryInfo, "status.objectId", "statusId");

        this.setDescOrderBy(queryInfo, "createTime");

        RecordsetInfo recordsetInfo = workflowService.queryCurrentUserInstance(queryInfo);

        List list = recordsetInfo.getEntityList();

        List items = new ArrayList();
        for (Instance instance : (List<Instance>) list) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", instance.getObjectId());
            item.put("name", instance.getName());
            item.put("flowType", instance.getFlowType().getName());
            if (instance.getResult() != null) {
                item.put("result", instance.getResult().getName());
            }
            item.put("status", instance.getStatus().getName());
            item.put("createTime", instance.getCreateTime().getTime());
            if (instance.getFinishTime() != null) {
                item.put("finishTime", instance.getFinishTime().getTime());
            } else {
                String flowNode = "";
                String handler = "";
                for (InstanceOrganization instanceOrganization : instance.getOrganizationList()) {
                    if (instanceOrganization.getTask().getStatus().getCodeTag().equalsIgnoreCase(TaskStatus.Running.name())) {
                        handler += instanceOrganization.getTask().getHandler().getName() + ",";
                        if (flowNode == "") {
                            flowNode = instanceOrganization.getTask().getMasterTask().getFlowNode().getName();
                        }
                    }
                }
                item.put("flowNode", flowNode);
                item.put("handler", StringUtil.trim(handler, ","));
            }
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/instance/query", method = RequestMethod.GET)
    public void queryInstance(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Instance.class);

        this.setPageInfoQueryCondition(request, queryInfo);

        this.setDateRangeQueryCondition(request, queryInfo, "createTime");

        this.setStringValueEqualsQueryCondition(request, queryInfo, "flowType.objectId", "flowType");

        this.setStringValueEqualsQueryCondition(request, queryInfo, "status.objectId", "statusId");

        this.setStringValueEqualsQueryCondition(request, queryInfo, "status.codeTag", "statusTag");

        String organizationId = request.getParameter("organizationId");
        if (StringUtil.isNotEmpty(organizationId)) {
            Organization organization = workflowService.get(Organization.class, organizationId);
            this.setNodeLevelCodeQueryCondition(queryInfo, "organization.nodeLevelCode", organization);
        }

        this.setInputFieldQueryCondition(request, queryInfo, "creator.name", "user");

        this.setDescOrderBy(queryInfo, "createTime");

        RecordsetInfo recordsetInfo = workflowService.queryInstance(queryInfo);

        List list = recordsetInfo.getEntityList();

        List items = new ArrayList();
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        for (Instance instance : (List<Instance>) list) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", instance.getObjectId());
            item.put("name", instance.getName());
            item.put("flowType", instance.getFlowType().getName());
            item.put("organization", instance.getOrganization().getFullName());
            item.put("creator", instance.getCreator().getName());
            if (instance.getResult() != null) {
                item.put("result", instance.getResult().getName());
            }
            item.put("status", instance.getStatus().getName());
            item.put("createTime", instance.getCreateTime().getTime());
            if (instance.getFinishTime() != null) {
                item.put("finishTime", instance.getFinishTime().getTime());
            } else {
                if (instance.getCurrentFlowNode() != null) {
                    String flowNode = instance.getCurrentFlowNode().getName();
                    item.put("flowNode", flowNode);
                    String handler = "";
                    if (instance.getCurrentFlowNode().getFlowNodeType() == FlowNodeType.Modify) {
                        handler = instance.getCreator().getName();
                    } else {
                        for (InstanceOrganization instanceOrganization : instance.getOrganizationList()) {
                            if (instanceOrganization.getTask().getStatus().getCodeTag().equalsIgnoreCase(TaskStatus.Running.name())) {
                                handler += instanceOrganization.getTask().getHandler().getName() + ",";
                            }
                        }
                    }

                    item.put("handler", StringUtil.trim(handler, ","));
                }
            }
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/instance/listInstanceTaskLog", method = RequestMethod.GET)
    public void listInstanceTaskLog(HttpServletRequest request, ModelMap modelMap) throws Exception {

        String instanceId = request.getParameter("instanceId");
        if (StringUtil.isEmpty(instanceId)) {
            Instance instance;
            String taskId = request.getParameter("taskId");
            String relatedObjectId = request.getParameter("relatedObjectId");
            if (StringUtil.isNotEmpty(taskId)) {
                instance = workflowService.getInstanceByTaskId(taskId);
            } else {
                instance = workflowService.getInstanceByRelatedObjectId(relatedObjectId);
            }
            if (instance != null) {
                instanceId = instance.getObjectId();
            }
        }

        if (StringUtil.isEmpty(instanceId)) {
            return;
        }

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(InstanceTaskLog.class);

        this.setStringValueEqualsQueryCondition(queryInfo, "instanceId", instanceId);

        boolean onlyShowApproveLog = Boolean.parseBoolean(request.getParameter("onlyShowApproveLog"));
        if (onlyShowApproveLog) {
            this.setStringValueEqualsQueryCondition(queryInfo, "flowNodeType", FlowNodeType.AndSign.toString());
            {
                SqlExpressionConfig sqlExpressionConfig = new SqlExpressionConfig();
                sqlExpressionConfig.setPropertyName("taskHandleResult");
                sqlExpressionConfig.setPropertyValue(null);
                sqlExpressionConfig.setSqlExpressionOperation(ExpressionOperation.NotEquals);
                QueryConditionNode queryConditionNode = new QueryConditionNode();
                queryConditionNode.setSqlExpressionConfig(sqlExpressionConfig);
                queryInfo.getQueryConditionTree().getUserConditionNode().appendNode(queryConditionNode);
            }
        }

        if (onlyShowApproveLog) {
            this.setDescOrderBy(queryInfo, "taskFinishTime");
        } else {
            this.setAscOrderBy(queryInfo, "taskCreateTime");
        }

        List list = workflowService.list(queryInfo);

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (InstanceTaskLog instanceTaskLog : (List<InstanceTaskLog>) list) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", instanceTaskLog.getObjectId());
            item.put("taskId", instanceTaskLog.getTaskId());
            item.put("flowNodeName", instanceTaskLog.getFlowNodeName());
            item.put("result", instanceTaskLog.getTaskHandleResultName());
            item.put("opinion", instanceTaskLog.getOpinion());
            item.put("handler", instanceTaskLog.getActorName());
            if (instanceTaskLog.getTaskFinishTime() != null) {
                item.put("finishTime", instanceTaskLog.getTaskFinishTime().getTime());
            }
            items.add(item);
        }
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/instance/view", method = RequestMethod.GET)
    public void viewInstance(ModelMap modelMap, HttpServletRequest request) throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();

        Instance instance = null;
        String objectId = request.getParameter("objectId");
        String relatedObjectId = request.getParameter("relatedObjectId");
        String taskId = request.getParameter("taskId");
        if (StringUtil.isNotEmpty(objectId)) {
            instance = (Instance) workflowService.get(Instance.class, objectId);
        } else if (StringUtil.isNotEmpty(relatedObjectId)) {
            instance = workflowService.getInstanceByRelatedObjectId(relatedObjectId);
        } else if (StringUtil.isNotEmpty(taskId)) {
            instance = workflowService.getInstanceByTaskId(taskId);
        }

        model.put("objectId", instance.getObjectId());
        model.put("name", instance.getName());
        model.put("flowType", instance.getFlowType().getName());
        model.put("organization", instance.getOrganization().getName());
        model.put("url", instance.getFlowType().getUrl());
        model.put("relatedObjectId", instance.getRelatedObjectId());
        model.put("creator", instance.getCreator().getName());
        model.put("status", instance.getStatus().getName());
        model.put("createTime", DateUtil.getDateFormatString(instance.getCreateTime(), "yyyy-MM-dd HH:mm"));
        if (instance.getFinishTime() != null) {
            model.put("finishTime", DateUtil.getDateFormatString(instance.getFinishTime(), "yyyy-MM-dd HH:mm"));
        } else {
            List list = workflowService.listInstanceCurrentHandler(instance.getObjectId());
            String handler = "";
            if (CollectionUtil.isNotEmpty(list)) {
                for (User user : (List<User>) list) {
                    handler += user.getName() + ",";
                }
            }
            model.put("handler", StringUtil.trim(handler, ","));
        }

        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/instance/delete/{objectId}", method = RequestMethod.GET)
    public void deleteInstance(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        workflowService.deleteInstance(objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/instance/getByRelatedObjectId/{objectId}", method = RequestMethod.GET)
    public void getInstance(ModelMap modelMap, @PathVariable(value = "objectId") String relatedObjectId) throws Exception {

        Instance instance = workflowService.getInstanceByRelatedObjectId(relatedObjectId);
        modelMap.put("instanceId", instance.getObjectId());
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/task/getFlowNodeByTaskId/{taskId}", method = RequestMethod.GET)
    public void getFlowNodeByTaskId(ModelMap modelMap, @PathVariable(value = "taskId") String taskId) throws Exception {

        FlowNode flowNode = workflowService.getFlowNodeByTaskId(taskId);

        Map data = new HashMap();
        data.put("passAction", flowNode.getPassAction());
        data.put("returnAction", flowNode.getReturnAction());
        data.put("rejectAction", flowNode.getRejectAction());
        data.put("completeAction", flowNode.getCompleteAction());
        data.put("discardAction", flowNode.getDiscardAction());
        if (flowNode.getManualSelectHandler() != null && flowNode.getManualSelectHandler() == 1) {
            NextFlowNodeInfo nextFlowNodeInfo = workflowService.getNextFlowNodeInfo(taskId);
            if (nextFlowNodeInfo != null) {
                data.put("manualSelectHandler", nextFlowNodeInfo.getCurrentFlowNode().getManualSelectHandler());
                if (nextFlowNodeInfo.getNextFlowNode() != null) {
                    data.put("handlerFetchCount", nextFlowNodeInfo.getNextFlowNode().getHandlerFetchCount());
                    data.put("nextFlowNodeName", nextFlowNodeInfo.getNextFlowNode().getName());
                }
            }
        }
        putData(modelMap, data);
    }

    @RequestMapping(value = "/task/pass", method = RequestMethod.POST)
    public void passTask(ModelMap modelMap, @RequestBody ApproveInfo approveInfo) throws Exception {

        if (CollectionUtil.isEmpty(approveInfo.getApprovers())) {
            workflowService.passTask(approveInfo.getTaskId(), approveInfo.getOpinion());
        } else {
            List<User> approverList = new ArrayList();
            for (String approverId : approveInfo.getApprovers()) {
                User approver = workflowService.get(User.class, approverId);
                approverList.add(approver);
            }
            workflowService.passTask(approveInfo.getTaskId(), approveInfo.getOpinion(), approverList);
        }
    }

    @RequestMapping(value = "/task/reject", method = RequestMethod.POST)
    public void rejectTask(ModelMap modelMap, @RequestBody ApproveInfo approveInfo) throws Exception {

        workflowService.rejectTask(approveInfo.getTaskId(), approveInfo.getOpinion());
    }

    @RequestMapping(value = "/task/return", method = RequestMethod.POST)
    public void returnTask(ModelMap modelMap, @RequestBody ApproveInfo approveInfo) throws Exception {

        workflowService.returnTask(approveInfo.getTaskId(), approveInfo.getOpinion());
    }

    @RequestMapping(value = "/task/complete", method = RequestMethod.POST)
    public void completeTask(ModelMap modelMap, @RequestBody ApproveInfo approveInfo) throws Exception {

        workflowService.completeTask(approveInfo.getTaskId(), approveInfo.getOpinion());
    }
    //

    @RequestMapping(value = "/task/transmit/{objectId}", method = RequestMethod.POST)
    public void transmitTask(ModelMap modelMap, @PathVariable(value = "objectId") String objectId, HttpServletRequest request) throws Exception {

        String handler = request.getParameter("handler");
        workflowService.transmitTask(objectId, handler);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/task/rollback/{objectId}", method = RequestMethod.GET)
    public void rollbackTask(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        workflowService.rollbackTask(objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/task/getTaskHandleOpinion/{objectId}", method = RequestMethod.GET)
    public void getTaskHandleOpinion(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        Task task = workflowService.getTaskById(objectId);
        if (task.getTaskOpinion() != null) {
            modelMap.put("opinion", task.getTaskOpinion().getOpinion());
        }
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/task/getCurrentUserApproveTaskByRelatedObjectId/{relatedObjectId}", method = RequestMethod.GET)
    public void getCurrentUserApproveTaskByRelatedObjectId(ModelMap modelMap, @PathVariable(value = "relatedObjectId") String relatedObjectId) throws Exception {

        TaskInfo taskInfo = workflowService.getCurrentUserApproveTaskByRelatedObjectId(relatedObjectId);

        if (taskInfo == null) {
            modelMap.put("taskInfo", 0);
        } else {
            modelMap.put("taskInfo", 1);
            modelMap.put("taskId", taskInfo.getObjectId());
            modelMap.put("activity", taskInfo.getActivity());
            modelMap.put("relatedObjectId", relatedObjectId);
            modelMap.put("passAction", taskInfo.getPassAction());
            modelMap.put("returnAction", taskInfo.getReturnAction());
            modelMap.put("rejectAction", taskInfo.getRejectAction());
            modelMap.put("discardAction", taskInfo.getDiscardAction());
            modelMap.put("completesAction", taskInfo.getCompleteAction());
            modelMap.put("flowTypeName", taskInfo.getFlowTypeName());
            modelMap.put("instanceName", taskInfo.getInstanceName());
        }

        modelMap.put("success", true);
    }

}
