package com.withub.service.std;


import com.withub.model.entity.AbstractBaseEntity;
import com.withub.service.EntityService;

public interface CommonTextService extends EntityService {

    public void saveContent(AbstractBaseEntity entityInstance, String content) throws Exception;

    public void saveContent(AbstractBaseEntity entityInstance, String property, String content) throws Exception;

    public String getContent(AbstractBaseEntity entityInstance) throws Exception;

    public String getContent(AbstractBaseEntity entityInstance, String property) throws Exception;

    public void deleteContent(AbstractBaseEntity entityInstance) throws Exception;

    public void deleteContent(AbstractBaseEntity entityInstance, String property) throws Exception;
}