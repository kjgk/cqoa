package com.withub.web.controller.system;

import com.withub.model.system.config.*;
import com.withub.service.system.ConfigurationService;
import com.withub.util.SpringSecurityUtil;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/system")
public class ConfigurationController extends BaseController {

    //============================== 属性声明 =============================================================

    @Autowired
    private ConfigurationService configurationService;

    //============================= Controller 方法 ======================================================

    @RequestMapping(value = "/configuration/save", method = RequestMethod.POST)
    public void saveConfiguration(ModelMap modelMap, ConfigurationInfo configurationInfo) throws Exception {

        configurationInfo.setCurrentUserId(SpringSecurityUtil.getCurrentUser().getObjectId());
        configurationService.saveConfigurationInfo(configurationInfo);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/configuration/load", method = RequestMethod.GET)
    public void loadConfiguration(ModelMap modelMap) throws Exception {

        SystemConfigInfo systemConfigInfo = (SystemConfigInfo) configurationService.getConfigInfo(SystemConfigInfo.class.getName(), "SYSTEM");
        SmtpConfigInfo smtpConfigInfo = (SmtpConfigInfo) configurationService.getConfigInfo(SmtpConfigInfo.class.getName(), "SMTP");
        SecurityConfigInfo securityConfigInfo = (SecurityConfigInfo) configurationService.getConfigInfo(SecurityConfigInfo.class.getName(), "SECURITY");
        SmsConfigInfo smsConfigInfo = (SmsConfigInfo) configurationService.getConfigInfo(SmsConfigInfo.class.getName(), "SMS");

        Map<String, Object> data = new HashMap<String, Object>();

        // systemConfigInfo
        if (systemConfigInfo != null) {
            data.put("systemConfigInfo.url", systemConfigInfo.getUrl());
            data.put("systemConfigInfo.systemEnabledYear", systemConfigInfo.getSystemEnabledYear());
            data.put("systemConfigInfo.databaseType", systemConfigInfo.getDatabaseType());
            data.put("systemConfigInfo.tempDirectory", systemConfigInfo.getTempDirectory());
            data.put("systemConfigInfo.logFileDirectory", systemConfigInfo.getLogFileDirectory());
            data.put("systemConfigInfo.logFileKeepDays", systemConfigInfo.getLogFileKeepDays());
            data.put("systemConfigInfo.ipSegments", systemConfigInfo.getIpSegments());
        }

        // smtpConfigInfo
        if (smtpConfigInfo != null) {
            data.put("smtpConfigInfo.host", smtpConfigInfo.getHost());
            data.put("smtpConfigInfo.port", smtpConfigInfo.getPort());
            data.put("smtpConfigInfo.username", smtpConfigInfo.getUsername());
            data.put("smtpConfigInfo.password", smtpConfigInfo.getPassword());
            data.put("smtpConfigInfo.mailAddress", smtpConfigInfo.getMailAddress());
            data.put("smtpConfigInfo.displayName", smtpConfigInfo.getDisplayName());
            data.put("smtpConfigInfo.returnAddress", smtpConfigInfo.getReturnAddress());
            data.put("smtpConfigInfo.protocolType", smtpConfigInfo.getProtocolType());
            data.put("smtpConfigInfo.sendEmailUserName", smtpConfigInfo.getSendEmailUserName());
            data.put("smtpConfigInfo.certificateRequired", smtpConfigInfo.getCertificateRequired() ? 1 : 0);
        }

        // securityConfigInfo
        if (securityConfigInfo != null) {
            data.put("securityConfigInfo.requiredLoginValidateCode", securityConfigInfo.isRequiredLoginValidateCode() ? 1 : 0);
            data.put("securityConfigInfo.allowChangeAccount", securityConfigInfo.isAllowChangeAccount() ? 1 : 0);
            data.put("securityConfigInfo.forceChangePassword", securityConfigInfo.isForceChangePassword() ? 1 : 0);
            data.put("securityConfigInfo.passwordIntervalDays", securityConfigInfo.getPasswordIntervalDays());
            data.put("securityConfigInfo.passwordLength", securityConfigInfo.getPasswordLength());
            data.put("securityConfigInfo.passwordStrength", securityConfigInfo.getPasswordStrength());
            data.put("securityConfigInfo.passwordRepeatHistory", securityConfigInfo.getPasswordRepeatHistory());
        }

        // smsConfigInfo
        if (smsConfigInfo != null) {
            data.put("smsConfigInfo.enableSms", smsConfigInfo.isEnableSms() ? 1 : 0);
            data.put("smsConfigInfo.port", smsConfigInfo.getPort());
            data.put("smsConfigInfo.baudrate", smsConfigInfo.getBaudrate());
            data.put("smsConfigInfo.manufacturer", smsConfigInfo.getManufacturer());
            data.put("smsConfigInfo.model", smsConfigInfo.getModel());
            data.put("smsConfigInfo.protocol", smsConfigInfo.getProtocol());
        }

        modelMap.put("data", data);
        modelMap.put("success", true);
    }
}
