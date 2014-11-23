package com.withub.service.impl.oa;

import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Miscellaneous;
import com.withub.model.system.po.Code;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.MiscellaneousService;
import com.withub.service.system.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("miscellaneousService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class MiscellaneousServiceImpl extends EntityServiceImpl implements MiscellaneousService {

    @Autowired
    private CodeService codeService;

    public Miscellaneous getMiscellaneous(String objectId) throws Exception {

        return get(Miscellaneous.class, objectId);
    }

    public RecordsetInfo<Miscellaneous> queryMiscellaneous(QueryInfo queryInfo) throws Exception {

        return query(queryInfo);
    }

    public void deleteMiscellaneous(String objectId) throws Exception {

        logicDelete(Miscellaneous.class, objectId);
    }

    public void submitMiscellaneous(Miscellaneous miscellaneous) throws Exception {

        if (StringUtil.isEmpty(miscellaneous.getObjectId())) {
            addMiscellaneous(miscellaneous);
        } else {
            updateMiscellaneous(miscellaneous);
        }
    }

    @Override
    public void addMiscellaneous(Miscellaneous miscellaneous) throws Exception {

        Code status = codeService.getCodeByTag("MiscellaneousStatus", "Create");
        miscellaneous.setStatus(status);
        miscellaneous.setProposer(miscellaneous.getCurrentUser());
        miscellaneous.setOrganization(miscellaneous.getCurrentUser().getOrganization());
        save(miscellaneous);
    }

    @Override
    public void updateMiscellaneous(Miscellaneous miscellaneous) throws Exception {

        miscellaneous.setProposer(miscellaneous.getCurrentUser());
        miscellaneous.setOrganization(miscellaneous.getCurrentUser().getOrganization());
        save(miscellaneous);
    }

}
