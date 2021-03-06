package com.withub.web.controller.workflow;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.*;
import com.withub.model.system.po.Organization;
import com.withub.model.system.po.User;
import com.withub.model.workflow.enumeration.FlowNodeType;
import com.withub.model.workflow.enumeration.TaskStatus;
import com.withub.model.workflow.po.Instance;
import com.withub.model.workflow.po.InstanceOrganization;
import com.withub.model.workflow.po.Task;
import com.withub.model.workflow.vo.ApproveInfo;
import com.withub.model.workflow.vo.InstanceTaskLog;
import com.withub.model.workflow.vo.TaskFlowNodeInfo;
import com.withub.model.workflow.vo.TaskInfo;
import com.withub.service.oa.TaskHandlerFetchService;
import com.withub.service.workflow.WorkflowService;
import com.withub.util.SpringSecurityUtil;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "/workflow")
public class WorkflowController extends BaseController {

    //================================== 属性声明 =========================================================

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private TaskHandlerFetchService taskHandlerFetchService;

    //================================= Controller 方法 ==================================================

    @RequestMapping(value = "/task/queryCurrentUserTask", method = RequestMethod.GET)
    public void queryCurrentUserTask(HttpServletRequest request, Date date, ModelMap modelMap) throws Exception {

        String flowTypeId = request.getParameter("flowTypeId");
        String statusTag = request.getParameter("statusTag");
        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(TaskInfo.class);

        if (StringUtil.isNotEmpty(flowTypeId)) {
            this.setQueryInfoCondition(queryInfo, "flowTypeId", flowTypeId, ExpressionOperation.Equals);
        }
        if (StringUtil.isNotEmpty(statusTag)) {
            this.setQueryInfoCondition(queryInfo, "taskStatusTag", statusTag, ExpressionOperation.Equals);
        }
        this.setQueryInfoCondition(queryInfo, "flowNodeType", "First", ExpressionOperation.NotEquals);
        if (date != null) {
            this.setQueryInfoCondition(queryInfo, "taskCreateTime", date, ExpressionOperation.LessThanOrEquals);
            this.setQueryInfoCondition(queryInfo, "taskFinishTime", date, ExpressionOperation.GreaterThanOrEquals);
        }
        this.setPageInfoQueryCondition(request, queryInfo);
        if (StringUtil.compareValue(statusTag, "Finish")) {
            this.setDescOrderBy(queryInfo, "taskFinishTime");
        } else {
            this.setDescOrderBy(queryInfo, "taskCreateTime");
        }

        RecordsetInfo recordsetInfo = workflowService.queryCurrentUserTask(queryInfo);
        List list = recordsetInfo.getEntityList();

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", list);
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
    public void queryCurrentUserInstance(HttpServletRequest request, Date date, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Instance.class);

        this.setPageInfoQueryCondition(request, queryInfo);

        this.setStringValueEqualsQueryCondition(request, queryInfo, "flowType.objectId", "flowType");

        this.setStringValueEqualsQueryCondition(request, queryInfo, "status.objectId", "status");

        if (date != null) {
            List dateList = new ArrayList();
            dateList.add(date);
            dateList.add(DateUtil.getEndDate(date));
            this.setQueryInfoCondition(queryInfo, "createTime", dateList, ExpressionOperation.Between);
        }

        this.setDescOrderBy(queryInfo, "createTime");

        RecordsetInfo recordsetInfo = workflowService.queryCurrentUserInstance(queryInfo);

        List list = recordsetInfo.getEntityList();

        List items = new ArrayList();
        if (CollectionUtil.isNotEmpty(list)) {
            for (Instance instance : (List<Instance>) list) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("objectId", instance.getObjectId());
                item.put("name", instance.getName());
                item.put("flowType", instance.getFlowType().getName());
                if (instance.getResult() != null) {
                    item.put("result", instance.getResult().getName());
                }
                item.put("status", instance.getStatus());
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
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/instance/query", method = RequestMethod.GET)
    public void queryInstance(HttpServletRequest request, Date date, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Instance.class);

        this.setPageInfoQueryCondition(request, queryInfo);

        this.setStringValueEqualsQueryCondition(request, queryInfo, "flowType.objectId", "flowType");

        this.setStringValueEqualsQueryCondition(request, queryInfo, "status.objectId", "status");

        this.setStringValueEqualsQueryCondition(request, queryInfo, "status.codeTag", "statusTag");

        String organizationId = request.getParameter("organizationId");
        if (StringUtil.isNotEmpty(organizationId)) {
            Organization organization = workflowService.get(Organization.class, organizationId);
            this.setNodeLevelCodeQueryCondition(queryInfo, "organization.nodeLevelCode", organization);
        }

        this.setInputFieldQueryCondition(request, queryInfo, "creator.name", "creator");

        if (date != null) {
            List dateList = new ArrayList();
            dateList.add(date);
            dateList.add(DateUtil.getEndDate(date));
            this.setQueryInfoCondition(queryInfo, "createTime", dateList, ExpressionOperation.Between);
        }

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
            item.put("status", instance.getStatus());
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
            this.setAscOrderBy(queryInfo, "taskFinishTime");
        } else {
            this.setAscOrderBy(queryInfo, "taskTimeMillis");
        }

        List list = workflowService.list(queryInfo);
        modelMap.put("items", list);
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
        model.put("createTime", instance.getCreateTime());
        if (instance.getFinishTime() != null) {
            model.put("finishTime", instance.getFinishTime());
        } else {
            List list = workflowService.listInstanceCurrentHandler(instance.getObjectId());
            String handler = "";
            if (CollectionUtil.isNotEmpty(list)) {
                for (User user : (List<User>) list) {
                    handler += user.getName() + ",";
                }
            }
            model.put("handler", StringUtil.trim(handler, ","));
            model.put("flowNode", instance.getCurrentFlowNode().getName());
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

    @RequestMapping(value = "/task/getTaskFlowNode/{taskId}", method = RequestMethod.GET)
    public void getTaskFlowNode(ModelMap modelMap, @PathVariable(value = "taskId") String taskId) throws Exception {

        TaskFlowNodeInfo taskFlowNodeInfo = workflowService.getTaskFlowNode(taskId);
        putData(modelMap, taskFlowNodeInfo);
    }

    @RequestMapping(value = "/task/pass", method = RequestMethod.POST)
    public void passTask(ModelMap modelMap, @RequestBody ApproveInfo approveInfo) throws Exception {

        if (CollectionUtil.isEmpty(approveInfo.getApprovers())) {
            workflowService.passTask(SpringSecurityUtil.getCurrentUser(), approveInfo.getTaskId(), approveInfo.getContextList(), approveInfo.getOpinion());
        } else {
            List<User> approverList = new ArrayList();
            for (String approverId : approveInfo.getApprovers()) {
                User approver = workflowService.get(User.class, approverId);
                approverList.add(approver);
            }
            workflowService.passTask(SpringSecurityUtil.getCurrentUser(), approveInfo.getTaskId(), approveInfo.getContextList(), approveInfo.getOpinion(), approverList);
        }
    }

    @RequestMapping(value = "/task/reject", method = RequestMethod.POST)
    public void rejectTask(ModelMap modelMap, @RequestBody ApproveInfo approveInfo) throws Exception {

        workflowService.rejectTask(SpringSecurityUtil.getCurrentUser(), approveInfo.getTaskId(), approveInfo.getContextList(), approveInfo.getOpinion());
    }

    @RequestMapping(value = "/task/return", method = RequestMethod.POST)
    public void returnTask(ModelMap modelMap, @RequestBody ApproveInfo approveInfo) throws Exception {

        workflowService.returnTask(SpringSecurityUtil.getCurrentUser(), approveInfo.getTaskId(), approveInfo.getContextList(), approveInfo.getOpinion());
    }

    @RequestMapping(value = "/task/complete", method = RequestMethod.POST)
    public void completeTask(ModelMap modelMap, @RequestBody ApproveInfo approveInfo) throws Exception {

        workflowService.completeTask(SpringSecurityUtil.getCurrentUser(), approveInfo.getTaskId(), approveInfo.getContextList(), approveInfo.getOpinion());
    }
    //

    @RequestMapping(value = "/task/transmit/{objectId}", method = RequestMethod.POST)
    public void transmitTask(ModelMap modelMap, @PathVariable(value = "objectId") String objectId, @RequestBody Map data) throws Exception {

        workflowService.transmitTask(SpringSecurityUtil.getCurrentUser(), objectId, data.get("handler").toString());
    }

    @RequestMapping(value = "/task/rollback/{objectId}", method = RequestMethod.POST)
    public void rollbackTask(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        workflowService.rollbackTask(SpringSecurityUtil.getCurrentUser(), objectId);
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

    @RequestMapping(value = "/task/fetchHander/organizationManager", method = RequestMethod.GET)
    public void fetchOrganizationManager(ModelMap modelMap, String organizationCode) throws Exception {

        List<User> userList;
        if (StringUtil.compareValue("all", organizationCode)) {
            userList = taskHandlerFetchService.fetchOrganizationManager();
        } else {
            if (StringUtil.isEmpty(organizationCode)) {
                organizationCode = SpringSecurityUtil.getCurrentUser().getOrganization().getCode();
            }
            userList = taskHandlerFetchService.fetchOrganizationManager(organizationCode);
        }
        putData(modelMap, userList);
    }

    @RequestMapping(value = "/task/fetchHander/leader", method = RequestMethod.GET)
    public void fetchLeader(ModelMap modelMap) throws Exception {

        List<User> userList = taskHandlerFetchService.fetchLeader();
        putData(modelMap, userList);
    }

    @RequestMapping(value = "/task/fetchHander/boss", method = RequestMethod.GET)
    public void fetchBoss(ModelMap modelMap) throws Exception {

        List<User> userList = taskHandlerFetchService.fetchBoss();
        putData(modelMap, userList);
    }
}
