package com.withub.server;


import com.withub.model.oa.po.*;
import com.withub.model.system.po.User;

import java.util.List;

public interface OAServer {

    public User login(String username, String password) throws Exception;

    public String getTaskFlowNodeInfo(String currentUserId, String taskId) throws Exception;

    public void commitTask(String currentUserId, String taskId, String result, String opinion, List<String> nextHandlerList) throws Exception;

    public void submitMiscellaneous(Miscellaneous miscellaneous) throws Exception;

    public void submitCarUse(CarUse carUse) throws Exception;

    public void submitLeave(Leave leave) throws Exception;

    public void submitOutgoing(Outgoing outgoing) throws Exception;

    public void submitTraining(Training training) throws Exception;

    public String getOrganizationList() throws Exception;

    public String getUserList(String organizationId) throws Exception;

    public String getCodeList(String codeColumnTag) throws Exception;

}
