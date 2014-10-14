package com.withub.service.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Leave;

public interface LeaveService {

    public Leave getLeave(String objectId) throws Exception;

    public RecordsetInfo<Leave> queryLeave(QueryInfo queryInfo) throws Exception;

    public void saveLeave(Leave leave) throws Exception;

    public void deleteLeave(String objectId) throws Exception;

}
