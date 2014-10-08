package com.withub.model.system.enumeration;

/**
 * 定义密码修改结果.
 */
public enum PasswordChangeResult {

    /**
     * 成功
     */
	Success,

    /**
     * 不允许修改
     */
    NotAllow,

    /**
     * 旧密码错误
     */
    OldPasswordError,
}
