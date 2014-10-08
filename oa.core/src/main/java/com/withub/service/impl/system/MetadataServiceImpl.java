package com.withub.service.impl.system;

import com.withub.common.util.AlphabetUtil;
import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.AbstractEntity;
import com.withub.model.entity.AbstractRecursionEntity;
import com.withub.model.entity.enumeration.EntityType;
import com.withub.model.exception.BaseBusinessException;
import com.withub.model.system.po.Entity;
import com.withub.model.system.po.EntityCategory;
import com.withub.model.system.po.User;
import com.withub.service.EntityServiceImpl;
import com.withub.service.system.MetadataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("metadataService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class MetadataServiceImpl extends EntityServiceImpl implements MetadataService {

    public EntityCategory getRootEntityCategory() throws Exception {

        return (EntityCategory) getRootEntity(EntityCategory.class);
    }

    public List<AbstractEntity> listEntity(String entityName, String orderBy) throws Exception {

        String hql = "from " + entityName + " where objectStatus = 1";
        if (StringUtil.isNotEmpty(orderBy)) {
            hql += " order by " + orderBy;
        }
        return listByHql(hql);
    }

    public List<AbstractEntity> searchEntity(String entityName, String queryProperty, String keyword) throws Exception {

        String hql;
        if (queryProperty.equals("name")) {
            hql = "from " + entityName + " where ( name like ? or pinYin like ? ) and objectStatus = 1 order by name";
            return listByHql(hql, "%" + keyword + "%", keyword + "%");
        } else {
            hql = "from " + entityName + " where " + queryProperty + " like ? and objectStatus = 1 order by " + queryProperty;
            return listByHql(hql, "%" + keyword + "%");
        }
    }

    public void setEntityPinYin(final String entityName) throws Exception {

        Entity entity = (Entity) getByPropertyValue(Entity.class, "entityName", entityName);
        if (!hasPinYinProperty(entity.getClassName())) {
            throw new BaseBusinessException("", "当前实体未定义拼音属性！");
        }

        String hql = "select o from " + entity.getClassName() + " o order by o.createTime";
        List<AbstractEntity> list = listByHql(hql);
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        int start = 0;
        int limit = 20;
        int total = list.size();
        int times = total % limit == 0 ? total / limit : total / limit + 1;
        for (int i = 0; i < times; i++) {
            List<AbstractEntity> list1 = listByHql(start, limit, hql);
            for (AbstractEntity entityInstance : list1) {
                String pinYin = AlphabetUtil.getAlphabetList(getNamePropertyValue(entityInstance));
                setPropertyValue(entityInstance, "pinYin", pinYin);
                save(entityInstance);
            }
            start += limit;
        }
    }

    public void createRecursionEntityRootNode(String name, String nodeTag, String description, String entityName, User currentUser) throws Exception {

        Entity entity = (Entity) getByPropertyValue(Entity.class, "entityName", entityName);
        EntityType entityType = EntityType.valueOf(entity.getEntityType().getCodeTag());

        if (entityType != EntityType.Recursion) {
            throw new BaseBusinessException("", "非递归实体不能添加根节点！");
        }

        AbstractRecursionEntity abstractRecursionEntity = getRootEntity(entityName);
        if (abstractRecursionEntity != null) {
            throw new BaseBusinessException("", "根节点已存在！");
        }
        abstractRecursionEntity = (AbstractRecursionEntity) Class.forName(entity.getClassName()).newInstance();
        abstractRecursionEntity.setName(name);
        abstractRecursionEntity.setNodeTag(nodeTag);
        abstractRecursionEntity.setFullName(name);
        abstractRecursionEntity.setCurrentUser(currentUser);
        abstractRecursionEntity.setDescription(description);
        save(abstractRecursionEntity);
    }
}