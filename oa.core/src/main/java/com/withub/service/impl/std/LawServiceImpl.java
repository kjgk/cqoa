package com.withub.service.impl.std;


import com.withub.model.std.po.Law;
import com.withub.model.std.po.LawCategory;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.CommonTextService;
import com.withub.service.std.FileService;
import com.withub.service.std.LawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("lawService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class LawServiceImpl extends EntityServiceImpl implements LawService {

    //============================== 属性声明 =============================================================

    @Autowired
    private CommonTextService commonTextService;

    @Autowired
    private FileService fileService;

    //============================== 接口实现 =============================================================

    public LawCategory getRootLawCategory() throws Exception {

        return (LawCategory) getRootEntity(LawCategory.class);
    }

    public void addLaw(Law law) throws Exception {

        save(law);

        commonTextService.saveContent(law, law.getContent());
        fileService.save(law, law.getAttachments());
    }

    public void updateLaw(Law law) throws Exception {

        Law old = (Law) get(Law.class, law.getObjectId());
        law.setCreator(old.getCreator());
        save(law);

        commonTextService.saveContent(law, law.getContent());

        fileService.save(law, law.getAttachments());
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
