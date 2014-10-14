package com.withub.service.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Miscellaneous;

public interface MiscellaneousService {

    public Miscellaneous getMiscellaneous(String objectId) throws Exception;

    public RecordsetInfo<Miscellaneous> queryMiscellaneous(QueryInfo queryInfo) throws Exception;

    public void saveMiscellaneous(Miscellaneous miscellaneous) throws Exception;

    public void deleteMiscellaneous(String objectId) throws Exception;
}
