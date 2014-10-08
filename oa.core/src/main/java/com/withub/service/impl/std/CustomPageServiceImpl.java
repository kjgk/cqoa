package com.withub.service.impl.std;

import com.withub.model.std.po.CustomPage;
import com.withub.model.std.po.CustomPageItem;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.CustomPageService;
import com.withub.service.std.FileService;
import com.withub.service.system.CodeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("customPageService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class CustomPageServiceImpl extends EntityServiceImpl implements CustomPageService {

    //=================== 属性声明 =========================================================

    @Autowired
    private CodeService codeService;


    @Autowired
    private FileService fileService;


    //=================== 接口实现 =========================================================

    @Override
    public void saveCustomPage(CustomPage customPage) throws Exception {

        CustomPage temp = get(CustomPage.class, customPage.getObjectId());
        if (CollectionUtils.isNotEmpty(temp.getCustomPageItemList())) {
            for (CustomPageItem customPageItem : temp.getCustomPageItemList()) {
                delete(customPageItem);
            }
        }

        if (CollectionUtils.isNotEmpty(customPage.getCustomPageItemList())) {
            for (CustomPageItem customPageItem : customPage.getCustomPageItemList()) {
                customPageItem.setCustomPage(customPage);
                save(customPageItem);
            }
        }

        if (customPage.getBackgroundImageInfo() != null) {
            fileService.save(temp, customPage.getBackgroundImageInfo());
        }
    }


    //==================== 属性方法 ====================================================

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
}
