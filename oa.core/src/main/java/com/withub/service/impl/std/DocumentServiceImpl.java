package com.withub.service.impl.std;

import com.withub.common.util.AlphabetUtil;
import com.withub.common.util.VersionUtil;
import com.withub.model.std.po.Document;
import com.withub.model.std.po.DocumentHistory;
import com.withub.model.system.po.User;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.DocumentService;
import com.withub.service.std.FileService;
import com.withub.service.system.CodeService;
import com.withub.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service("documentService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class DocumentServiceImpl extends EntityServiceImpl implements DocumentService {

    @Autowired
    private FileService fileService;

    @Autowired
    private CodeService codeService;

    public void createDocument(DocumentHistory documentHistory) throws Exception {

        User currentUser = documentHistory.getCurrentUser();
        Document document = documentHistory.getDocument();
        document.setObjectId(null);
        document.setFirstHistory(documentHistory);
        document.setLastHistory(documentHistory);
        document.setOrganization(currentUser.getOrganization());
        document.setHistoryCount(1);
        save(document);

        documentHistory.setPinYin(AlphabetUtil.getAlphabetList(documentHistory.getName()));
        documentHistory.setCreator(currentUser);
        documentHistory.setVersion(VersionUtil.getInitialVersion());
        if (documentHistory.getWriteDate() == null) {
            documentHistory.setWriteDate(new Date());
        }
        save(documentHistory);

        // 保存附件
        fileService.save(documentHistory, documentHistory.getAttachmentInfo());
    }

    public void updateDocument(DocumentHistory documentHistory) throws Exception {

        DocumentHistory tempHistory = (DocumentHistory) get(DocumentHistory.class, documentHistory.getObjectId());
        Document tempDocument = tempHistory.getDocument();

        tempDocument.setDocumentType(documentHistory.getDocument().getDocumentType());
        save(tempDocument);

        documentHistory.setDocument(tempDocument);
        documentHistory.setPinYin(AlphabetUtil.getAlphabetList(documentHistory.getName()));
        documentHistory.setCreator(tempHistory.getCreator());
        documentHistory.setVersion(tempHistory.getVersion());
        documentHistory.setStatus(tempHistory.getStatus());
        documentHistory.setSecrecyLevel(tempHistory.getSecrecyLevel());
        save(documentHistory);

        // 保存附件
        fileService.save(documentHistory, documentHistory.getAttachmentInfo());
    }

    /**
     * 上传新版本
     *
     * @param documentHistory
     * @throws Exception
     */
    public void createNewVersion(DocumentHistory documentHistory) throws Exception {

        Document document = (Document) get(Document.class, documentHistory.getDocument().getObjectId());

        documentHistory.setPinYin(AlphabetUtil.getAlphabetList(documentHistory.getName()));
        documentHistory.setCreator(SpringSecurityUtil.getCurrentUser());
        documentHistory.setVersion(getNextVersion(document));
        if (documentHistory.getWriteDate() == null) {
            documentHistory.setWriteDate(new Date());
        }
        save(documentHistory);

        document.setDocumentType(documentHistory.getDocument().getDocumentType());
        document.setLastHistory(documentHistory);
        document.setHistoryCount(document.getHistoryCount() + 1);
        save(document);

        // 保存附件
        fileService.save(documentHistory, documentHistory.getAttachmentInfo());
    }

    public void deleteDocument(String id) throws Exception {

        logicDelete(Document.class, id);
    }

    public FileService getFileService() {

        return fileService;
    }

    public void setFileService(FileService fileService) {

        this.fileService = fileService;
    }

    public CodeService getCodeService() {

        return codeService;
    }

    public void setCodeService(CodeService codeService) {

        this.codeService = codeService;
    }
}
