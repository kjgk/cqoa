package com.withub.model.security.enumeration;

/**
 * 定义登录验证结果
 * 注意: 定义的次序请按照登录验证的逻辑次序
 */
public enum LoginValidateResult {

    /**
     * 验证码错误
     */
    ValidateCodeError,

    /**
     * 非法的客户端身份
     */
    illegalClientIdentityCode,

    /**
     * 帐号不存在
     */
    AccountNotExist,

    /**
     * 没有登录权限
     */
    NoLoginRight,

    /**
     * 帐号被锁住
     */
    AccountLocked,

    /**
     * 帐号禁用
     */
    AccountDisabled,

    /**
     * 帐号已经登录
     */
    AccountAlreadyLogin,

    /**
     * 密码错误
     */
    PasswordError,

    /**
     * 密码已经过期
     */
    PasswordExpired,

    /**
     * 密码即将过期
     */
    PasswordWillExpired,

    /**
     * 成功
     */
    Success,
}
