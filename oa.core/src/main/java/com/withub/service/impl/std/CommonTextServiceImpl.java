package com.withub.service.impl.std;

import com.withub.common.util.StringUtil;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.std.po.CommonText;
import com.withub.model.system.po.Entity;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.CommonTextService;
import com.withub.util.SpringSecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("commonTextService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class CommonTextServiceImpl extends EntityServiceImpl implements CommonTextService {

    public void saveContent(AbstractBaseEntity entityInstance, String content) throws Exception {

        saveContent(entityInstance, "Content", content);
    }

    public void saveContent(AbstractBaseEntity entityInstance, String property, String content) throws Exception {

        if (StringUtil.isEmpty(content)) {
            return;
        }

        String relatedObjectId = entityInstance.getObjectId();
        String hql = "select o from " + CommonText.class.getName() + " o"
                + " where o.relatedObjectId='" + relatedObjectId + "'"
                + " and lower(o.property)='" + property.toLowerCase() + "'";
        CommonText commonText = (CommonText) getByHql(hql);

        if (commonText == null) {
            commonText = new CommonText();
            commonText.setObjectId(StringUtil.getUuid());
        }
        Entity entity = getEntityByClassName(entityInstance.getClass().getName());
        commonText.setEntity(entity);
        commonText.setRelatedObjectId(relatedObjectId);
        commonText.setProperty(property);
        commonText.setContent(content);
        commonText.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        save(commonText);
    }

    public String getContent(AbstractBaseEntity entityInstance) throws Exception {

        String content = "";
        String relatedObjectId = entityInstance.getObjectId();
        CommonText commonText = (CommonText) getByPropertyValue(CommonText.class, "relatedObjectId", relatedObjectId);

        if (commonText != null) {
            content = commonText.getContent();
        }

        return content;
    }

    public String getContent(AbstractBaseEntity entityInstance, String property) throws Exception {

        String content = "";
        String relatedObjectId = entityInstance.getObjectId();
        String hql = "select o from " + CommonText.class.getName() + " o"
                + " where o.relatedObjectId='" + relatedObjectId + "'"
                + " and lower(o.property)='" + property.toLowerCase() + "'";
        CommonText commonText = (CommonText) getByHql(hql);

        if (commonText != null) {
            content = commonText.getContent();
        }

        return content;
    }

    public void deleteContent(AbstractBaseEntity entityInstance) throws Exception {

        String relatedObjectId = entityInstance.getObjectId();
        CommonText commonText = (CommonText) getByPropertyValue(CommonText.class, "relatedObjectId", relatedObjectId);
        if (commonText != null) {
            delete(commonText);
        }

    }

    public void deleteContent(AbstractBaseEntity entityInstance, String property) throws Exception {

        String relatedObjectId = entityInstance.getObjectId();
        String hql = "select o from " + CommonText.class.getName() + " o"
                + " where o.relatedObjectId='" + relatedObjectId + "'"
                + " and lower(o.property)='" + property.toLowerCase() + "'";
        CommonText commonText = (CommonText) getByHql(hql);
        if (commonText != null) {
            delete(commonText);
        }
    }
}