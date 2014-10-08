package com.withub.service.security;

import com.withub.model.security.LoginInfo;
import com.withub.model.security.LoginResult;

public interface LoginService {

    /**
     * 登录验证
     *
     * @param loginInfo 登录信息
     * @return LoginResult
     */
    public LoginResult validate(LoginInfo loginInfo) throws Exception;

    /**
     * 退出
     */
    public void logout() throws Exception;
}
