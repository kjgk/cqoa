package com.withub.service.impl.oa;

import com.withub.common.util.DateUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.CarUseUser;
import com.withub.model.oa.po.Outgoing;
import com.withub.model.oa.po.Outgoing;
import com.withub.model.oa.po.OutgoingUser;
import com.withub.model.system.po.Code;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.OutgoingService;
import com.withub.service.system.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;


@Service("outgoingService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class OutgoingServiceImpl extends EntityServiceImpl implements OutgoingService {

    @Autowired
    private CodeService codeService;

    public Outgoing getOutgoing(String objectId) throws Exception {

        return get(Outgoing.class, objectId);
    }

    public RecordsetInfo<Outgoing> queryOutgoing(QueryInfo queryInfo) throws Exception {

        return query(queryInfo);
    }

    public void deleteOutgoing(String objectId) throws Exception {

        logicDelete(Outgoing.class, objectId);
    }


    public void submitOutgoing(Outgoing outgoing) throws Exception {

        if (StringUtil.isEmpty(outgoing.getObjectId())) {
            addOutgoing(outgoing);
        } else {
            updateOutgoing(outgoing);
        }
    }

    @Override
    public void addOutgoing(Outgoing outgoing) throws Exception {

        Code status = codeService.getCodeByTag("OutgoingStatus", "Create");
        outgoing.setStatus(status);
        outgoing.setProposer(outgoing.getCurrentUser());
        Integer duration = (int) DateUtil.getDiffDays(outgoing.getBeginDate(), outgoing.getEndDate());
        outgoing.setDuration(duration);
        save(outgoing);

        for (OutgoingUser outgoingUser : outgoing.getOutgoingUserList()) {
            outgoingUser.setOutgoing(outgoing);
            save(outgoingUser);
        }
    }

    @Override
    public void updateOutgoing(Outgoing outgoing) throws Exception {

        outgoing.setProposer(outgoing.getCurrentUser());
        save(outgoing);

        executeHql("delete from OutgoingUser a where a.outgoing.objectId = ?", outgoing.getObjectId());

        for (OutgoingUser outgoingUser : outgoing.getOutgoingUserList()) {
            outgoingUser.setOutgoing(outgoing);
            save(outgoingUser);
        }
    }

}
