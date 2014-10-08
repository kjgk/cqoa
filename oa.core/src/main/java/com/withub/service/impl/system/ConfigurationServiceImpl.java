package com.withub.service.impl.system;

import com.withub.common.util.ReflectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.system.config.*;
import com.withub.model.system.po.Configuration;
import com.withub.model.system.po.User;
import com.withub.service.EntityServiceImpl;
import com.withub.service.system.ConfigurationService;
import com.withub.util.ConfigUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("configurationService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class ConfigurationServiceImpl extends EntityServiceImpl implements ConfigurationService {

    //=================== 接口实现 =========================================================

    public void saveConfigurationInfo(ConfigurationInfo configurationInfo) throws Exception {

        SystemConfigInfo systemConfigInfo = configurationInfo.getSystemConfigInfo();
        SmtpConfigInfo smtpConfigInfo = configurationInfo.getSmtpConfigInfo();
        SecurityConfigInfo securityConfigInfo = configurationInfo.getSecurityConfigInfo();
        SmsConfigInfo smsConfigInfo = configurationInfo.getSmsConfigInfo();

        User currentUser = get(User.class, configurationInfo.getCurrentUserId());
        systemConfigInfo.setObjectId("SYSTEM");
        saveConfigInfo(currentUser, systemConfigInfo);
        smtpConfigInfo.setObjectId("SMTP");
        saveConfigInfo(currentUser, smtpConfigInfo);
        securityConfigInfo.setObjectId("SECURITY");
        saveConfigInfo(currentUser, securityConfigInfo);
        smsConfigInfo.setObjectId("SMS");
        saveConfigInfo(currentUser, smsConfigInfo);
    }

    public void saveConfigInfo(User currentUser, AbstractBaseConfigInfo configInfo) throws Exception {

        Configuration configuration;

        String value = ReflectionUtil.serializeObjectToString(configInfo);
        Configuration oldConfiguration = getConfiguration(configInfo.getClass().getName(), configInfo.getObjectId());
        if (oldConfiguration == null) {
            configuration = new Configuration();
            configuration.setObjectId(StringUtil.getUuid());
            configuration.setClassName(configInfo.getClass().getName());
            configuration.setRelatedObjectId(configInfo.getObjectId());
        } else {
            configuration = oldConfiguration;
        }
        configuration.setValue(value);
        configuration.setCurrentUser(currentUser);
        save(configuration);
        ConfigUtil.getInstance().refresh(configInfo);
    }

    public Configuration getConfiguration(final String className, final String relatedObjectId) throws Exception {

        String hql = "select o from " + Configuration.class.getName() + " o where o.objectStatus=1"
                + " and lower(o.className)='" + className.trim().toLowerCase() + "'"
                + " and o.relatedObjectId='" + relatedObjectId + "'";

        Configuration configuration = (Configuration) getByHql(hql);

        return configuration;

    }

    public AbstractBaseConfigInfo getConfigInfo(final String className, final String relatedObjectId) throws Exception {

        AbstractBaseConfigInfo configInfo = null;
        Configuration configuration = getConfiguration(className, relatedObjectId);

        if (configuration != null) {
            Object object = ReflectionUtil.deserializeObjectFromString(configuration.getValue());
            configInfo = (AbstractBaseConfigInfo) object;
        }

        return configInfo;
    }

}
