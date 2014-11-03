package com.withub.server.impl;


import com.withub.common.util.CollectionUtil;
import com.withub.model.oa.po.*;
import com.withub.model.system.po.User;
import com.withub.model.workflow.enumeration.TaskHandleResult;
import com.withub.model.workflow.po.Task;
import com.withub.server.OAServer;
import com.withub.service.oa.*;
import com.withub.service.system.UserService;
import com.withub.service.workflow.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("oaServer")
@Transactional
public class OAServerImpl implements OAServer {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

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
