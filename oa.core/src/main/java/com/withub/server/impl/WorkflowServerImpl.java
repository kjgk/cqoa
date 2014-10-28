package com.withub.server.impl;


import com.withub.common.util.CollectionUtil;
import com.withub.model.system.po.User;
import com.withub.model.workflow.enumeration.TaskHandleResult;
import com.withub.model.workflow.po.Task;
import com.withub.server.WorkflowServer;
import com.withub.service.workflow.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("workflowServer")
@Transactional
public class WorkflowServerImpl implements WorkflowServer {

    @Autowired
    private TaskService taskService;

    public void commitTask(String currentUserId, String taskId, String result, String opinion, List<String> nextHandlerList) throws Exception {

        Task task = taskService.getTaskById(taskId);
        TaskHandleResult taskHandleResult = TaskHandleResult.valueOf(result);
        List<User> handlerList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(nextHandlerList)) {
            for (String userId : nextHandlerList) {
                handlerList.add(taskService.get(User.class, userId));
            }
        }
        User currentUser = taskService.get(User.class, currentUserId);

        taskService.commit(currentUser, task, taskHandleResult, opinion, handlerList, null);


    }
}
