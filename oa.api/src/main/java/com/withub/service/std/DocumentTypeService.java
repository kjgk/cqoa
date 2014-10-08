package com.withub.service.std;

import com.withub.model.std.po.DocumentType;
import com.withub.model.std.po.DocumentTypeCategory;
import com.withub.service.EntityService;

import java.util.List;


public interface DocumentTypeService extends EntityService {

    public DocumentTypeCategory getRootDocumentTypeCategory() throws Exception;

    List<DocumentType> listDocumentTypeByCategoryTag(String documentTypeCategoryTag) throws Exception;
}