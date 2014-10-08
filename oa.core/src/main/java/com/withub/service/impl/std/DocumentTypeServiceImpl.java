package com.withub.service.impl.std;

import com.withub.model.std.po.DocumentType;
import com.withub.model.std.po.DocumentTypeCategory;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.DocumentTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("documentTypeService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class DocumentTypeServiceImpl extends EntityServiceImpl implements DocumentTypeService {

    public DocumentTypeCategory getRootDocumentTypeCategory() throws Exception {

        return (DocumentTypeCategory) getRootEntity(DocumentTypeCategory.class);
    }

    public List<DocumentType> listDocumentTypeByCategoryTag(String documentTypeCategoryTag) throws Exception {

        String hql = "select o from " + DocumentType.class.getName() + " o where o.objectStatus=1"
                + " and o.documentTypeCategory.documentTypeCategoryTag=?"
                + " order by o.orderNo";
        List list = listByHql(hql, documentTypeCategoryTag);

        return (List<DocumentType>) list;
    }
}
