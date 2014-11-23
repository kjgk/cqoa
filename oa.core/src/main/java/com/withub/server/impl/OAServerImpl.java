package com.withub.server.impl;


import com.alibaba.fastjson.JSONObject;
import com.withub.common.util.CollectionUtil;
import com.withub.common.util.Md5Util;
import com.withub.model.oa.po.*;
import com.withub.model.system.po.*;
import com.withub.model.workflow.enumeration.TaskHandleResult;
import com.withub.model.workflow.po.Task;
import com.withub.model.workflow.vo.TaskFlowNodeInfo;
import com.withub.server.OAServer;
import com.withub.service.oa.*;
import com.withub.service.system.AccountService;
import com.withub.service.system.CodeService;
import com.withub.service.system.OrganizationService;
import com.withub.service.system.UserService;
import com.withub.service.workflow.TaskService;
import com.withub.service.workflow.WorkflowService;
import net.sf.json.JSONSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        return  null;
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

        return JSONSerializer.toJSON(list).toString();
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

        return JSONSerializer.toJSON(list).toString();
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

        return JSONSerializer.toJSON(list).toString();
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
}
