package com.withub.service.impl.std;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.po.AppVersion;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.AppVersionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("appVersionService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class AppVersionServiceImpl extends EntityServiceImpl implements AppVersionService {

    public AppVersion getAppVersion(String objectId) throws Exception {

        return get(AppVersion.class, objectId);
    }

    public RecordsetInfo<AppVersion> queryAppVersion(QueryInfo queryInfo) throws Exception {

        return query(queryInfo);
    }

    public void saveAppVersion(AppVersion appVersion) throws Exception {

        save(appVersion);
    }

    public void deleteAppVersion(String objectId) throws Exception {

        logicDelete(AppVersion.class, objectId);
    }

}
