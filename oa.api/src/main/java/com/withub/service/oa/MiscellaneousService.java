package com.withub.service.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Miscellaneous;

public interface MiscellaneousService {

    public Miscellaneous getMiscellaneous(String objectId) throws Exception;

    public RecordsetInfo<Miscellaneous> queryMiscellaneous(QueryInfo queryInfo) throws Exception;

    public void submitMiscellaneous(Miscellaneous miscellaneous) throws Exception;

    public void deleteMiscellaneous(String objectId) throws Exception;

    public void addMiscellaneous(Miscellaneous miscellaneous) throws Exception;

    public void updateMiscellaneous(Miscellaneous miscellaneous) throws Exception;

}
