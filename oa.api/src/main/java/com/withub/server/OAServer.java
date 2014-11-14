package com.withub.server;


import com.withub.model.oa.po.*;
import com.withub.model.workflow.vo.TaskFlowNodeInfo;

import java.util.List;

public interface OAServer {

    public String getTaskFlowNodeInfo(String currentUserId, String taskId) throws Exception;

    public void commitTask(String currentUserId, String taskId, String result, String opinion, List<String> nextHandlerList) throws Exception;

    public void submitMiscellaneous(Miscellaneous miscellaneous) throws Exception;

    public void submitCarUse(CarUse carUse) throws Exception;

    public void submitLeave(Leave leave) throws Exception;

    public void submitOutgoing(Outgoing outgoing) throws Exception;

    public void submitTraining(Training training) throws Exception;
}
