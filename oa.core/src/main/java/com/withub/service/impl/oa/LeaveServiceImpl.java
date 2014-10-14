package com.withub.service.impl.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Leave;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.LeaveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("leaveService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class LeaveServiceImpl extends EntityServiceImpl implements LeaveService {

    public Leave getLeave(String objectId) throws Exception {

        return get(Leave.class, objectId);
    }

    public RecordsetInfo<Leave> queryLeave(QueryInfo queryInfo) throws Exception {

        return query(queryInfo);
    }

    public void saveLeave(Leave leave) throws Exception {

        save(leave);
    }

    public void deleteLeave(String objectId) throws Exception {

        logicDelete(Leave.class, objectId);
    }

}
