package com.withub.service.system;

import com.withub.model.entity.AbstractEntity;
import com.withub.model.system.po.EntityCategory;
import com.withub.model.system.po.User;
import com.withub.service.EntityService;

import java.util.List;


public interface MetadataService extends EntityService {

    public EntityCategory getRootEntityCategory() throws Exception;

    public List<AbstractEntity> listEntity(String entityName, String orderBy) throws Exception;

    public List<AbstractEntity> searchEntity(String entityName, String queryProperty, String keyword) throws Exception;

    public void setEntityPinYin(final String entityName) throws Exception;

    public void createRecursionEntityRootNode(String name, String nodeTag, String description, String entityName, User currentUser) throws Exception;
}