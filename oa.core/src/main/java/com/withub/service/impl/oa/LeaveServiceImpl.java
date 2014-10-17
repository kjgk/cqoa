package com.withub.service.impl.oa;

import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Leave;
import com.withub.model.system.po.Code;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.LeaveService;
import com.withub.service.system.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("leaveService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class LeaveServiceImpl extends EntityServiceImpl implements LeaveService {

    @Autowired
    private CodeService codeService;

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


    public void submitLeave(Leave leave) throws Exception {

        if (StringUtil.isEmpty(leave.getObjectId())) {
            addLeave(leave);
        } else {
            updateLeave(leave);
        }
    }

    @Override
    public void addLeave(Leave leave) throws Exception {

        Code status = codeService.getCodeByTag("LeaveStatus", "Create");
        leave.setStatus(status);
        leave.setProposer(leave.getCurrentUser());
        save(leave);
    }

    @Override
    public void updateLeave(Leave leave) throws Exception {

        save(leave);
    }

}
