package com.withub.service.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Outgoing;
import com.withub.model.system.po.User;
import com.withub.service.EntityService;

public interface OutgoingService extends EntityService {

    public Outgoing getOutgoing(String objectId) throws Exception;

    public RecordsetInfo<Outgoing> queryOutgoing(QueryInfo queryInfo) throws Exception;

    public void deleteOutgoing(String objectId) throws Exception;

    public void submitOutgoing(Outgoing outgoing, User approver) throws Exception;

    public void submitOutgoing(Outgoing outgoing) throws Exception;

    public void addOutgoing(Outgoing outgoing) throws Exception;

    public void updateOutgoing(Outgoing outgoing) throws Exception;    

    
}
