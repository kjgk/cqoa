package com.withub.service.std;

import com.withub.model.std.po.DocumentHistory;
import com.withub.service.EntityService;

public interface DocumentService extends EntityService {

    public void createDocument(DocumentHistory documentHistory) throws Exception;

    public void updateDocument(DocumentHistory documentHistory) throws Exception;

    public void createNewVersion(DocumentHistory documentHistory) throws Exception;

    public void deleteDocument(String id) throws Exception;

}