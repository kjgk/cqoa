package com.withub.service.system;


import com.withub.model.system.config.AbstractBaseConfigInfo;
import com.withub.model.system.config.ConfigurationInfo;
import com.withub.model.system.po.Configuration;
import com.withub.model.system.po.User;
import com.withub.service.EntityService;

public interface ConfigurationService extends EntityService {

    public void saveConfigurationInfo(ConfigurationInfo configurationInfo) throws Exception;

    public void saveConfigInfo(User currentUser, AbstractBaseConfigInfo configInfo) throws Exception;

    public Configuration getConfiguration(final String className, final String relatedObjectId) throws Exception;

    public AbstractBaseConfigInfo getConfigInfo(final String className, final String relatedObjectId) throws Exception;
}
