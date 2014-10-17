package com.withub.service.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Leave;
import com.withub.service.EntityService;

public interface LeaveService extends EntityService {

    public Leave getLeave(String objectId) throws Exception;

    public RecordsetInfo<Leave> queryLeave(QueryInfo queryInfo) throws Exception;

    public void deleteLeave(String objectId) throws Exception;

    public void submitLeave(Leave leave) throws Exception;

    public void addLeave(Leave leave) throws Exception;

    public void updateLeave(Leave leave) throws Exception;
    
}
