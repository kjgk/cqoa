package com.withub.service.impl.system;


import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.security.LoginInfo;
import com.withub.model.security.event.LoginEvent;
import com.withub.model.security.event.LogoutEvent;
import com.withub.model.system.po.Account;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.OnlineUser;
import com.withub.model.system.po.SecurityLog;
import com.withub.service.EntityServiceImpl;
import com.withub.service.system.AccountService;
import com.withub.service.system.CodeService;
import com.withub.service.system.SecurityService;
import com.withub.spring.SpringContextUtil;
import com.withub.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service("securityService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class SecurityServiceImpl extends EntityServiceImpl implements SecurityService {

    //================================ 属性声明 ===========================================================

    @Autowired
    private AccountService accountService;

    @Autowired
    private CodeService codeService;

    //================================ 接口实现 ===========================================================

    public RecordsetInfo querySecurityLog(QueryInfo queryInfo) throws Exception {

        RecordsetInfo recordsetInfo = query(queryInfo);

        return recordsetInfo;
    }

    public RecordsetInfo queryOnlineUser(QueryInfo queryInfo) throws Exception {

        RecordsetInfo recordsetInfo = query(queryInfo);

        return recordsetInfo;
    }

    public void clearOnlineUserAndUpdateSecurityLog() throws Exception {

        String hql = "select o from " + OnlineUser.class.getName() + " o order by o.loginTime desc";
        List onlineUserlist = listByHql(hql);
        if (CollectionUtil.isEmpty(onlineUserlist)) {
            return;
        }
        Code loginType = codeService.getCodeByTag("LogoutType", "ServerStop");
        for (OnlineUser onlineUser : (List<OnlineUser>) onlineUserlist) {
            SecurityLog securityLog = (SecurityLog) getByPropertyValue(SecurityLog.class, "onlineUserId", onlineUser.getObjectId());
            securityLog.setLogoutType(loginType);
            update(securityLog);
        }
        delete(onlineUserlist);
    }

    public void kickoutAccount(String accountId, String loginType) throws Exception {

        SessionRegistry sessionRegistry = (SessionRegistry) SpringContextUtil.getInstance().getBean("sessionRegistry");
        SessionInformation sessionInformation = SpringSecurityUtil.getAccountSessionInformation(accountId);
        sessionInformation.expireNow();
        sessionRegistry.removeSessionInformation(sessionInformation.getSessionId());
    }

    public void onLoginEvent(LoginEvent loginEvent) throws Exception {

        LoginInfo loginInfo = loginEvent.getLoginInfo();
        SecurityLog securityLog = new SecurityLog();

        securityLog.setObjectId(StringUtil.getUuid());
        Account account = accountService.getAccountByName(loginInfo.getAccountName());
        securityLog.setUser(account.getUser());
        Code loginType = codeService.getCodeByTag("LoginType", loginInfo.getLoginType());
        securityLog.setLoginType(loginType);
        securityLog.setClientIdentityCode(loginInfo.getClientIdentityCode());
        Date currentTime = DateUtil.getCurrentTime();
        securityLog.setLoginTime(currentTime);

        Date loginOutTime = DateUtil.convertStringToDate("1900-01-01", DateUtil.STANDARD_DATE_FORMAT);
        securityLog.setLogoutTime(loginOutTime);

        OnlineUser onlineUser = new OnlineUser();
        String objectId = StringUtil.getUuid();
        onlineUser.setObjectId(objectId);
        onlineUser.setUser(account.getUser());
        onlineUser.setLoginType(loginType);
        onlineUser.setClientIdentityCode(loginInfo.getClientIdentityCode());
        onlineUser.setLoginTime(currentTime);
        onlineUser.setAccount(account);
        securityLog.setOnlineUserId(objectId);
        try {
            String[] ipSegment = loginInfo.getClientIdentityCode().split("[.]");
            long ip256Value = Integer.parseInt(ipSegment[0]) * 256 * 256 * 256 + Integer.parseInt(ipSegment[1]) * 256 * 256
                    + Integer.parseInt(ipSegment[2]) * 256 + Integer.parseInt(ipSegment[3]);
            securityLog.setIp256Value(ip256Value);
            onlineUser.setIp256Value(ip256Value);
        } catch (Exception e) {
            // do nothing
        }

        save(securityLog);
        save(onlineUser);
    }

    public void onLogoutEvent(LogoutEvent logoutEvent) throws Exception {

        String accountId = logoutEvent.getAccountId();

        String hql = "select o from " + SecurityLog.class.getName() + " o"
                + " where o.user.account.objectId='" + accountId + "'"
                + " order by o.loginTime desc";

        List list = listByHql(hql);
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        SecurityLog securityLog = (SecurityLog) list.get(0);

        // 记录退出时间
        if (securityLog != null && securityLog.getLogoutTime() == null) {
            securityLog.setLogoutTime(DateUtil.getCurrentTime());
            save(securityLog);
        }
    }

    //================================ 属性方法 ===========================================================

    public AccountService getAccountService() {

        return accountService;
    }

    public void setAccountService(AccountService accountService) {

        this.accountService = accountService;
    }

    public CodeService getCodeService() {

        return codeService;
    }

    public void setCodeService(CodeService codeService) {

        this.codeService = codeService;
    }


}
