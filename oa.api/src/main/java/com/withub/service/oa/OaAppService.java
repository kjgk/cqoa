package com.withub.service.oa;


import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.*;
import com.withub.model.system.po.User;

import java.util.List;
import java.util.Map;

public interface OaAppService {

    public User login(String username, String password) throws Exception;

    public RecordsetInfo queryTask(String currentUserId, String flowTypeTag, String taskStatusTag, Integer page, Integer pageSize) throws Exception;

    public Map queryInstance(String currentUserId, String flowTypeTag, String complete, Integer page, Integer pageSize) throws Exception;

    public String getTaskFlowNodeInfo(String currentUserId, String taskId) throws Exception;

    public String getInstance(String instanceId) throws Exception;

    public String getInstanceTaskLog(String instanceId) throws Exception;

    public void commitTask(String currentUserId, String taskId, String result, String opinion, List<String> nextHandlerList) throws Exception;

    public void submitMiscellaneous(Miscellaneous miscellaneous) throws Exception;

    public void submitCarUse(CarUse carUse) throws Exception;

    public void submitLeave(Leave leave) throws Exception;

    public void submitOutgoing(Outgoing outgoing) throws Exception;

    public void submitTraining(Training training) throws Exception;

    public String getMiscellaneous(String objectId) throws Exception;

    public String getCarUse(String objectId) throws Exception;

    public String getLeave(String objectId) throws Exception;

    public String getOutgoing(String objectId) throws Exception;

    public String getTraining(String objectId) throws Exception;

    public String getOrganizationList() throws Exception;

    public String getUserList(String organizationId) throws Exception;

    public String getCodeList(String codeColumnTag) throws Exception;

}
