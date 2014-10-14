package com.withub.service.impl.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Miscellaneous;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.MiscellaneousService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("miscellaneousService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class MiscellaneousServiceImpl extends EntityServiceImpl implements MiscellaneousService {

    public Miscellaneous getMiscellaneous(String objectId) throws Exception {

        return get(Miscellaneous.class, objectId);
    }

    public RecordsetInfo<Miscellaneous> queryMiscellaneous(QueryInfo queryInfo) throws Exception {

        return query(queryInfo);
    }

    public void saveMiscellaneous(Miscellaneous miscellaneous) throws Exception {

        save(miscellaneous);
    }

    public void deleteMiscellaneous(String objectId) throws Exception {

        logicDelete(Miscellaneous.class, objectId);
    }

}
