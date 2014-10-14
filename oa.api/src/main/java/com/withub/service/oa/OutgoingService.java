package com.withub.service.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Outgoing;

public interface OutgoingService {

    public Outgoing getOutgoing(String objectId) throws Exception;

    public RecordsetInfo<Outgoing> queryOutgoing(QueryInfo queryInfo) throws Exception;

    public void saveOutgoing(Outgoing outgoing) throws Exception;

    public void deleteOutgoing(String objectId) throws Exception;

}
