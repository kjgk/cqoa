package com.withub.service.std;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.FileUploadInfo;
import com.withub.model.std.po.AppVersion;
import com.withub.model.system.po.User;
import com.withub.service.EntityService;

public interface AppVersionService extends EntityService {

    public AppVersion getAppVersion(String objectId) throws Exception;

    public RecordsetInfo<AppVersion> queryAppVersion(QueryInfo queryInfo) throws Exception;

    public void saveAppVersion(AppVersion appVersion) throws Exception;

    public void deleteAppVersion(String objectId) throws Exception;

    public void enableAppVersion(String objectId, User currentUser) throws Exception;

    public void disableAppVersion(String objectId, User currentUser) throws Exception;

    public AppVersion getEnabledAppVersion() throws Exception;

}
