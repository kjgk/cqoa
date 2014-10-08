package com.withub.service.impl.std;

import com.withub.model.std.po.LawIssueOrganization;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.CommonTextService;
import com.withub.service.std.FileService;
import com.withub.service.std.LawIssueOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("lawIssueOrganizationService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class LawIssueOrganizationServiceImpl extends EntityServiceImpl implements LawIssueOrganizationService {
//============================== 属性声明 =============================================================

    @Autowired
    private CommonTextService commonTextService;

    @Autowired
    private FileService fileService;

    //============================== 接口实现 =============================================================

    public LawIssueOrganization getRootLawIssueOrganization() throws Exception {

        return (LawIssueOrganization) getRootEntity(LawIssueOrganization.class);
    }

    public void addLawIssueOrganization(LawIssueOrganization lawIssueOrganization) throws Exception {

        save(lawIssueOrganization);

        commonTextService.saveContent(lawIssueOrganization, lawIssueOrganization.getContent());
        fileService.save(lawIssueOrganization, lawIssueOrganization.getAttachments());
    }

    public void updateLawIssueOrganization(LawIssueOrganization lawIssueOrganization) throws Exception {

        LawIssueOrganization old = (LawIssueOrganization) get(LawIssueOrganization.class, lawIssueOrganization.getObjectId());
        lawIssueOrganization.setCreator(old.getCreator());
        save(lawIssueOrganization);

        commonTextService.saveContent(lawIssueOrganization, lawIssueOrganization.getContent());

        fileService.save(lawIssueOrganization, lawIssueOrganization.getAttachments());
    }

    //============================== 属性方法 =============================================================

    public CommonTextService getCommonTextService() {

        return commonTextService;
    }

    public void setCommonTextService(CommonTextService commonTextService) {

        this.commonTextService = commonTextService;
    }

    public FileService getFileService() {

        return fileService;
    }

    public void setFileService(FileService fileService) {

        this.fileService = fileService;
    }
}
