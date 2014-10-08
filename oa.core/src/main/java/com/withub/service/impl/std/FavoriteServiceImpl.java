package com.withub.service.impl.std;

import com.withub.common.util.StringUtil;
import com.withub.model.std.po.Favorite;
import com.withub.model.system.po.Entity;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.FavoriteService;
import com.withub.service.system.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("favoriteService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class FavoriteServiceImpl extends EntityServiceImpl implements FavoriteService {

    //============== 属性声明 ==================================

    @Autowired
    private CodeService codeService;

    //============== 接口实现 ==================================

    public void add(Favorite favorite, String objectType) throws Exception {

        favorite.setObjectId(StringUtil.getUuid());
        Entity entity = getEntityByClassName(objectType);
        favorite.setEntity(entity);
        save(favorite);
    }

    public void update(Favorite favorite) throws Exception {

        Favorite old = get(Favorite.class, favorite.getObjectId());
        favorite.setEntity(old.getEntity());
        save(favorite);
    }

    //============== 属性方法 =================================================

    public CodeService getCodeService() {

        return codeService;
    }

    public void setCodeService(CodeService codeService) {

        this.codeService = codeService;
    }
}
