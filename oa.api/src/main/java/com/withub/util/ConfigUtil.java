package com.withub.util;

import com.withub.common.util.FileUtil;
import com.withub.model.system.config.*;
import com.withub.service.system.ConfigurationService;
import com.withub.spring.SpringContextUtil;

public final class ConfigUtil {

    //================================ 属性声明 ==========================================================

    private ConfigurationService configurationService = (ConfigurationService) SpringContextUtil.getInstance().getBean("configurationService");

    private static SmtpConfigInfo smtpConfigInfo;

    private static SmsConfigInfo smsConfigInfo;

    private static SystemConfigInfo systemConfigInfo;

    private static SecurityConfigInfo securityConfigInfo;

    private static ConfigUtil instance = null;

    public static ConfigUtil getInstance() {

        if (instance == null) {
            instance = new ConfigUtil();
        }
        return instance;
    }

    private ConfigUtil() {

    }

    //================================ 方法  =============================================================

    public void init() throws Exception {

        AbstractBaseConfigInfo abstractBaseConfigInfo = configurationService.getConfigInfo(SmtpConfigInfo.class.getName(), "SMTP");
        smtpConfigInfo = (SmtpConfigInfo) abstractBaseConfigInfo;

        abstractBaseConfigInfo = configurationService.getConfigInfo(SystemConfigInfo.class.getName(), "SYSTEM");
        systemConfigInfo = (SystemConfigInfo) abstractBaseConfigInfo;

        abstractBaseConfigInfo = configurationService.getConfigInfo(SecurityConfigInfo.class.getName(), "SECURITY");
        securityConfigInfo = (SecurityConfigInfo) abstractBaseConfigInfo;

        // 创建临时目录
        FileUtil.createDirectory(systemConfigInfo.getTempDirectory());
    }

    public void refresh(Object object) throws Exception {

        if (object instanceof SmtpConfigInfo) {
            smtpConfigInfo = (SmtpConfigInfo) object;
        } else if (object instanceof SystemConfigInfo) {
            systemConfigInfo = (SystemConfigInfo) object;
            FileUtil.createDirectory(systemConfigInfo.getTempDirectory());
        } else if (object instanceof SecurityConfigInfo) {
            securityConfigInfo = (SecurityConfigInfo) object;
        }
    }

    //================================ 属性方法 ==========================================================

    public static SmtpConfigInfo getSmtpConfigInfo() {

        return smtpConfigInfo;
    }

    public static SmsConfigInfo getSmsConfigInfo() {

        return smsConfigInfo;
    }

    public static SystemConfigInfo getSystemConfigInfo() {

        return systemConfigInfo;
    }

    public static SecurityConfigInfo getSecurityConfigInfo() {

        return securityConfigInfo;
    }
}

