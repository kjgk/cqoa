package com.withub.service.impl.security;

import com.withub.common.util.StringUtil;
import com.withub.model.security.LoginInfo;
import com.withub.model.security.LoginResult;
import com.withub.model.security.enumeration.LoginValidateResult;
import com.withub.model.system.enumeration.AccountStatus;
import com.withub.model.system.po.Account;
import com.withub.service.security.LoginService;
import com.withub.service.security.SecurityEventPublisher;
import com.withub.service.system.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("loginService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class LoginServiceImpl implements LoginService {

    //================================ 属性声明 ===========================================================

    @Autowired
    private AccountService accountService;

    //================================ 接口实现 ===========================================================

    public LoginResult validate(LoginInfo loginInfo) throws Exception {

        LoginResult loginResult = new LoginResult();

        // TODO: 校验验证码
        Account account = accountService.getAccountByName(loginInfo.getAccountName());

        // 判断帐号是否存在
        if (account == null) {
            loginResult.setValidateResult(LoginValidateResult.AccountNotExist);
            return loginResult;
        }

        // 判断帐号的当前状态
        if (account.getAccountStatus() == AccountStatus.Locked) {
            loginResult.setValidateResult(LoginValidateResult.AccountLocked);
            return loginResult;
        }

        if (account.getAccountStatus() == AccountStatus.Disabled) {
            loginResult.setValidateResult(LoginValidateResult.AccountDisabled);
            return loginResult;
        }

        // 校验密码
        String salt = account.getSalt();
        String encryptPassword = accountService.encryptPassword(salt, loginInfo.getPassword());

        if (!StringUtil.compareValue(encryptPassword, account.getPassword())) {
            loginResult.setValidateResult(LoginValidateResult.PasswordError);
            return loginResult;
        }

        // 验证通过
        loginResult.setValidateResult(LoginValidateResult.Success);
        loginResult.setAccount(account);

        // 发布登录事件
        SecurityEventPublisher.publishLoginEvent(this, loginInfo);

        return loginResult;
    }

    public void logout() throws Exception {
    }


    //================================ 属性方法 ===========================================================

    public AccountService getAccountService() {

        return accountService;
    }

    public void setAccountService(AccountService accountService) {

        this.accountService = accountService;
    }
}
