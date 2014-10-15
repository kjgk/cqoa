package com.withub.service.impl.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Outgoing;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.OutgoingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("outgoingService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class OutgoingServiceImpl extends EntityServiceImpl implements OutgoingService {

    public Outgoing getOutgoing(String objectId) throws Exception {

        return get(Outgoing.class, objectId);
    }

    public RecordsetInfo<Outgoing> queryOutgoing(QueryInfo queryInfo) throws Exception {

        return query(queryInfo);
    }

    public void saveOutgoing(Outgoing outgoing) throws Exception {

        save(outgoing);
    }

    public void deleteOutgoing(String objectId) throws Exception {

        logicDelete(Outgoing.class, objectId);
    }

}
