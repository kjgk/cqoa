package com.withub.service.impl.std;

import com.withub.common.util.DateUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.std.enumeration.BulletinStatus;
import com.withub.model.std.po.Bulletin;
import com.withub.model.system.po.User;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.*;
import com.withub.service.system.CodeService;
import com.withub.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("bulletinService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class BulletinServiceImpl extends EntityServiceImpl implements BulletinService {

    //=============================== 属性声明 ============================================================

    @Autowired
    private CodeService codeService;

    @Autowired
    private FileService fileService;

    @Autowired
    private CommonTextService commonTextService;

    @Autowired
    private SystemEventService systemEventService;

    @Autowired
    private EntityCacheService entityCacheService;

    //================================ 接口实现 ===========================================================

    public void addBulletin(Bulletin bulletin) throws Exception {

        User user = bulletin.getCurrentUser();
        bulletin.setStatus(codeService.getCodeByEnum(BulletinStatus.Draft));
        bulletin.setIssuer(user);
        bulletin.setIssueOrganization(user.getOrganization());
        save(bulletin);

        commonTextService.saveContent(bulletin, bulletin.getContent());
        fileService.save(bulletin, bulletin.getAttachments());
    }

    public void updateBulletin(Bulletin bulletin) throws Exception {

        Bulletin old = (Bulletin) get(Bulletin.class, bulletin.getObjectId());
        bulletin.setIssuer(old.getIssuer());
        bulletin.setIssueTime(old.getIssueTime());
        bulletin.setIssueOrganization(old.getIssueOrganization());
        bulletin.setStatus(old.getStatus());

        save(bulletin);

        commonTextService.saveContent(bulletin, bulletin.getContent());

        fileService.save(bulletin, bulletin.getAttachments());
    }

    public void issueBulletin(String bulletinId) throws Exception {

        Bulletin bulletin = (Bulletin) get(Bulletin.class, bulletinId);
        bulletin.setStatus(codeService.getCodeByEnum(BulletinStatus.Issue));
        bulletin.setIssueTime(DateUtil.getCurrentTime());
        bulletin.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        save(bulletin);
    }

    public void issueBulletin(Bulletin bulletin) throws Exception {

        if (StringUtil.isEmpty(bulletin.getObjectId())) {
            addBulletin(bulletin);
        } else {
            updateBulletin(bulletin);
        }
        issueBulletin(bulletin.getObjectId());
    }

    //================================== 属性方法 =========================================================

    public CodeService getCodeService() {

        return codeService;
    }

    public void setCodeService(CodeService codeService) {

        this.codeService = codeService;
    }

    public FileService getFileService() {

        return fileService;
    }

    public void setFileService(FileService fileService) {

        this.fileService = fileService;
    }

    public CommonTextService getCommonTextService() {

        return commonTextService;
    }

    public void setCommonTextService(CommonTextService commonTextService) {

        this.commonTextService = commonTextService;
    }

    public SystemEventService getSystemEventService() {

        return systemEventService;
    }

    public void setSystemEventService(SystemEventService systemEventService) {

        this.systemEventService = systemEventService;
    }

    public EntityCacheService getEntityCacheService() {

        return entityCacheService;
    }

    public void setEntityCacheService(EntityCacheService entityCacheService) {

        this.entityCacheService = entityCacheService;
    }
}