package com.withub.service.std;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.service.EntityService;


public interface BlacklistService extends EntityService {

    public RecordsetInfo queryBlacklist(QueryInfo queryInfo) throws Exception;

    public void putToBlacklist(String userId, String description) throws Exception;

    public void removeFromBlacklist(String userId) throws Exception;
}