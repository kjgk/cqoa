package com.withub.service.system;


import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.security.event.LoginEvent;
import com.withub.model.security.event.LogoutEvent;
import com.withub.service.EntityService;

public interface SecurityService extends EntityService {
    public void kickoutAccount(String accountId, String loginType) throws Exception;

    public RecordsetInfo querySecurityLog(QueryInfo queryInfo) throws Exception;

    public RecordsetInfo queryOnlineUser(QueryInfo queryInfo) throws Exception;

    public void onLoginEvent(LoginEvent loginEvent) throws Exception;

    public void onLogoutEvent(LogoutEvent logoutEvent) throws Exception;

    public void clearOnlineUserAndUpdateSecurityLog() throws Exception;
}
