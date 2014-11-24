package com.withub.server.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.withub.common.util.CollectionUtil;
import com.withub.common.util.Md5Util;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.ExpressionOperation;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.*;
import com.withub.model.system.po.*;
import com.withub.model.workflow.enumeration.FlowNodeType;
import com.withub.model.workflow.enumeration.TaskHandleResult;
import com.withub.model.workflow.po.FlowType;
import com.withub.model.workflow.po.Instance;
import com.withub.model.workflow.po.Task;
import com.withub.model.workflow.vo.InstanceTaskLog;
import com.withub.model.workflow.vo.TaskFlowNodeInfo;
import com.withub.model.workflow.vo.TaskInfo;
import com.withub.server.OAServer;
import com.withub.service.oa.*;
import com.withub.service.system.AccountService;
import com.withub.service.system.CodeService;
import com.withub.service.system.OrganizationService;
import com.withub.service.system.UserService;
import com.withub.service.workflow.TaskService;
import com.withub.service.workflow.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service("oaServer")
@Transactional
public class OAServerImpl implements OAServer {

    @Autowired
    private TaskService taskService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private MiscellaneousService miscellaneousService;

    @Autowired
    private OutgoingService outgoingService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private CarUseService carUseService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private OrganizationService organizationService;

    public User login(String username, String password) throws Exception {

        Account account = accountService.getAccountByName(username);
        String encryptedPassword = Md5Util.getStringMD5(account.getSalt() + password);
        boolean isValidate = !encryptedPassword.equalsIgnoreCase(account.getPassword());
        if (isValidate) {
            User user = new User();
            user.setName(account.getUser().getName());
            user.setObjectId(account.getUser().getObjectId());
            return user;
        }
        return null;
    }

    public RecordsetInfo queryTask(String currentUserId, String flowTypeTag, String taskStatusTag, Integer page, Integer pageSize) throws Exception {

        FlowType flowType = (FlowType) taskService.getByPropertyValue(FlowType.class, "flowTypeTag", flowTypeTag);
        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(TaskInfo.class);
        queryInfo.addCondition("flowTypeId", flowType.getObjectId(), ExpressionOperation.Equals);
        queryInfo.addCondition("taskStatusTag", taskStatusTag, ExpressionOperation.Equals);
        queryInfo.addCondition("handler", currentUserId, ExpressionOperation.Equals);
        queryInfo.setRecordsetIndex((page - 1) * pageSize);
        queryInfo.setRecordsetSize(pageSize);
        queryInfo.setDescOrderBy("taskCreateTime");

        RecordsetInfo recordsetInfo = taskService.query(queryInfo);
        if (recordsetInfo.getEntityList() == null) {
            recordsetInfo.setEntityList(new ArrayList());
        }
        return recordsetInfo;
    }

    public Map queryInstance(String currentUserId, String flowTypeTag, String complete, Integer page, Integer pageSize) throws Exception {

        String sql = " from wf_instance a\n" +
                "  , (\n" +
                "      select\n" +
                "        c.instanceid,\n" +
                "        max(a.finishtime) finishtime\n" +
                "      from wf_task a, wf_mastertask b, wf_subinstance c, wf_flowtype d, wf_flownode e\n" +
                "      where a.mastertaskid = b.objectid and b.subinstanceid = c.objectid \n" +
                "            and b.flownodeid = e.objectid and d.objectid = e.flowtypeid\n" +
                "        and a.owner = ?\n" +
                "        and d.flowtypetag = ?\n" +
                "        and a.result is not null\n" +
                "      group by c.instanceid\n" +
                "    ) b, vw_taskinfo c, sys_user d\n" +
                "where a.objectid = b.instanceid and .a.objectid = c.instanceid and d.objectid = c.handler\n" +
                "   and c.taskstatustag = 'Running'";
        if (StringUtil.isNotEmpty(complete) && !StringUtil.compareValue("0", complete)) {
            sql += " and a.result = '69F248C7-30CC-4723-A100-3DECD577FCDD'";
        }

        List list = workflowService.listBySql("select a.objectid instanceId, a.name instanceName" +
                ", a.relatedobjectid relatedObjectId, b.finishtime finishTime, c.flownodename flowNodeName, d.name hander " +
                sql + " order by b.finishtime desc limit ?, ?"
                , currentUserId, flowTypeTag, (page - 1) * pageSize, pageSize);
        Integer count = Integer.parseInt(workflowService.listBySql("select count(1) " + sql, currentUserId, flowTypeTag).get(0).toString());

        List result = new ArrayList();
        for (Object[] objects : (List<Object[]>) list) {
            Map item = new HashMap();
            item.put("instanceId", objects[0]);
            item.put("instanceName", objects[1]);
            item.put("relatedObjectId", objects[2]);
            item.put("finishTime", ((Date) objects[3]).getTime());
            item.put("flowNodeName", objects[4]);
            item.put("hander", objects[5]);
            result.add(item);
        }

        Map returnMap = new HashMap();
        returnMap.put("count", count.toString());
        returnMap.put("result", JSON.toJSON(result).toString());
        return returnMap;
    }

    public String getTaskFlowNodeInfo(String currentUserId, String taskId) throws Exception {

        TaskFlowNodeInfo taskFlowNodeInfo = workflowService.getTaskFlowNode(taskId);

        List<User> userList = taskFlowNodeInfo.getHandlerList();
        taskFlowNodeInfo.setHandlerList(null);

        JSONObject data = (JSONObject) JSONObject.toJSON(taskFlowNodeInfo);
        if (CollectionUtil.isNotEmpty(userList)) {
            List list = new ArrayList();
            for (User user : userList) {
                Map item = new HashMap();
                item.put("objectId", user.getObjectId());
                item.put("name", user.getName());
                list.add(item);
            }
            data.put("handlerList", list);
        }
        return data.toString();
    }

    public String getInstance(String instanceId) throws Exception {

        Instance instance = taskService.get(Instance.class, instanceId);

        Map data = new HashMap();
        data.put("name", instance.getName());
        data.put("status", instance.getStatus().getName());
        data.put("flowType", instance.getFlowType().getName());
        data.put("createTime", instance.getCreateTime().getTime());
        data.put("creator", instance.getCreator().getName());
        data.put("organization", instance.getOrganization().getName());
        if (instance.getFinishTime() != null) {
            data.put("finishTime", instance.getFinishTime().getTime());
            List list = workflowService.listInstanceCurrentHandler(instance.getObjectId());
            String handler = "";
            if (CollectionUtil.isNotEmpty(list)) {
                for (User user : (List<User>) list) {
                    handler += user.getName() + ",";
                }
            }
            data.put("handler", StringUtil.trim(handler, ","));
            data.put("flowNode", instance.getCurrentFlowNode().getName());
        } else {
            data.put("handler", "");
            data.put("flowNode", "");
        }

        return JSON.toJSON(data).toString();
    }

    public String getInstanceTaskLog(String instanceId) throws Exception {
        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(InstanceTaskLog.class);
        queryInfo.addCondition("instanceId", instanceId, ExpressionOperation.Equals);
        queryInfo.addCondition("flowNodeType", FlowNodeType.AndSign.toString(), ExpressionOperation.Equals);
        queryInfo.addCondition("taskHandleResult", null, ExpressionOperation.NotEquals);
        queryInfo.setDescOrderBy("taskFinishTime");
        List list = workflowService.list(queryInfo);
        return JSON.toJSON(list).toString();
    }

    public void commitTask(String currentUserId, String taskId, String result, String opinion, List<String> nextHandlerList) throws Exception {

        Task task = taskService.getTaskById(taskId);
        TaskHandleResult taskHandleResult = TaskHandleResult.valueOf(result);
        List<User> handlerList = new ArrayList();
        if (CollectionUtil.isNotEmpty(nextHandlerList)) {
            for (String userId : nextHandlerList) {
                handlerList.add(taskService.get(User.class, userId));
            }
        }
        User currentUser = taskService.get(User.class, currentUserId);

        taskService.commit(currentUser, task, taskHandleResult, opinion, handlerList, null);
    }

    public String getOrganizationList() throws Exception {

        List list = new ArrayList();
        Organization root = organizationService.getRootOrganization();

        for (Organization child : root.getChildList()) {
            Map item = new HashMap();
            item.put("objectId", child.getObjectId());
            item.put("name", child.getName());
            list.add(item);
        }

        return JSON.toJSON(list).toString();
    }

    public String getUserList(String organizationId) throws Exception {

        List list = new ArrayList();
        List<User> userList = userService.listByOrganizationId(organizationId);
        for (User user : userList) {
            Map item = new HashMap();
            item.put("objectId", user.getObjectId());
            item.put("name", user.getName());
            list.add(item);
        }

        return JSON.toJSON(list).toString();
    }

    public String getCodeList(String codeColumnTag) throws Exception {

        List list = new ArrayList();
        CodeColumn codeColumn = codeService.getCodeColumnByTag(codeColumnTag);
        for (Code code : codeColumn.getCodeList()) {
            Map item = new HashMap();
            item.put("objectId", code.getObjectId());
            item.put("name", code.getName());
            list.add(item);
        }

        return JSON.toJSON(list).toString();
    }

    @Override
    public void submitMiscellaneous(Miscellaneous miscellaneous) throws Exception {

        miscellaneous.setCurrentUser(userService.get(User.class, miscellaneous.getCurrentUser().getObjectId()));

        miscellaneousService.submitMiscellaneous(miscellaneous);
    }

    @Override
    public void submitCarUse(CarUse carUse) throws Exception {

        carUse.setCurrentUser(userService.get(User.class, carUse.getCurrentUser().getObjectId()));

        carUseService.submitCarUse(carUse);
    }

    @Override
    public void submitLeave(Leave leave) throws Exception {


        leave.setCurrentUser(userService.get(User.class, leave.getCurrentUser().getObjectId()));

        leaveService.submitLeave(leave);
    }

    @Override
    public void submitOutgoing(Outgoing outgoing) throws Exception {

        outgoing.setCurrentUser(userService.get(User.class, outgoing.getCurrentUser().getObjectId()));

        outgoingService.submitOutgoing(outgoing);
    }

    @Override
    public void submitTraining(Training training) throws Exception {

        training.setCurrentUser(userService.get(User.class, training.getCurrentUser().getObjectId()));

        trainingService.submitTraining(training);
    }

    @Override
    public String getMiscellaneous(String objectId) throws Exception {

        Miscellaneous miscellaneous = miscellaneousService.get(Miscellaneous.class, objectId);
        Map result = new HashMap();
        result.put("objectId", miscellaneous.getObjectId());
        result.put("description", miscellaneous.getDescription());
        result.put("proposerName", miscellaneous.getProposer().getName());
        result.put("organizationName", miscellaneous.getOrganization().getName());
        return JSON.toJSON(result).toString();
    }

    @Override
    public String getCarUse(String objectId) throws Exception {

        CarUse carUse = carUseService.get(CarUse.class, objectId);
        Map result = new HashMap();
        result.put("objectId", carUse.getObjectId());
        result.put("description", carUse.getDescription());
        result.put("proposerName", carUse.getProposer().getName());
        result.put("organizationName", carUse.getOrganization().getName());
        result.put("beginTime", carUse.getBeginTime().getTime());
        result.put("endTime", carUse.getEndTime().getTime());
        result.put("region", carUse.getRegion());
        result.put("address", carUse.getAddress());
        result.put("createTime", carUse.getCreateTime().getTime());
        List users = new ArrayList();
        for (CarUseUser carUseUser : carUse.getCarUseUserList()) {
            Map item = new HashMap();
            item.put("objectId", carUseUser.getUser().getObjectId());
            item.put("name", carUseUser.getUser().getName());
            users.add(item);
        }
        result.put("users", users);
        return JSON.toJSON(result).toString();
    }

    @Override
    public String getLeave(String objectId) throws Exception {

        Leave leave = leaveService.get(Leave.class, objectId);
        Map result = new HashMap();
        result.put("objectId", leave.getObjectId());
        result.put("description", leave.getDescription());
        result.put("proposerName", leave.getProposer().getName());
        result.put("organizationName", leave.getOrganization().getName());
        result.put("beginDate", leave.getBeginDate().getTime());
        result.put("endDate", leave.getEndDate().getTime());
        result.put("createTime", leave.getCreateTime().getTime());
        return JSON.toJSON(result).toString();
    }

    @Override
    public String getOutgoing(String objectId) throws Exception {

        Outgoing outgoing = outgoingService.get(Outgoing.class, objectId);
        Map result = new HashMap();
        result.put("objectId", outgoing.getObjectId());
        result.put("description", outgoing.getDescription());
        result.put("proposerName", outgoing.getProposer().getName());
        result.put("organizationName", outgoing.getOrganization().getName());
        result.put("beginDate", outgoing.getBeginDate().getTime());
        result.put("endDate", outgoing.getEndDate().getTime());
        result.put("localCity", outgoing.getLocalCity());
        result.put("destination", outgoing.getDestination());
        result.put("driverRoute", outgoing.getDriveRoute());
        result.put("transportation", outgoing.getTransportation().getObjectId());
        result.put("transportationName", outgoing.getTransportation().getName());
        result.put("requiredCar", outgoing.getRequiredCar());
        result.put("createTime", outgoing.getCreateTime().getTime());
        List users = new ArrayList();
        for (OutgoingUser outgoingUser : outgoing.getOutgoingUserList()) {
            Map item = new HashMap();
            item.put("objectId", outgoingUser.getUser().getObjectId());
            item.put("name", outgoingUser.getUser().getName());
            users.add(item);
        }
        result.put("users", users);
        return JSON.toJSON(result).toString();
    }

    @Override
    public String getTraining(String objectId) throws Exception {

        Training training = trainingService.get(Training.class, objectId);
        Map result = new HashMap();
        result.put("objectId", training.getObjectId());
        result.put("proposerName", training.getProposer().getName());
        result.put("organizationName", training.getOrganization().getName());
        result.put("beginDate", training.getBeginDate().getTime());
        result.put("endDate", training.getEndDate().getTime());
        result.put("address", training.getAddress());
        result.put("content", training.getContent());
        result.put("target", training.getTarget());
        result.put("peopleCount", training.getPeopleCount());
        result.put("publicity", training.getPublicity());
        return JSON.toJSON(result).toString();
    }
}
