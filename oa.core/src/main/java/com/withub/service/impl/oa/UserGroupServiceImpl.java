package com.withub.service.impl.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.UserGroup;
import com.withub.model.oa.po.UserGroupDetail;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.UserGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("userGroupService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class UserGroupServiceImpl extends EntityServiceImpl implements UserGroupService {

    public UserGroup getUserGroup(String objectId) throws Exception {

        return get(UserGroup.class, objectId);
    }

    public RecordsetInfo<UserGroup> queryUserGroup(QueryInfo queryInfo) throws Exception {

        return query(queryInfo);
    }

    public void deleteUserGroup(String objectId) throws Exception {

        logicDelete(UserGroup.class, objectId);
    }

    @Override
    public void addUserGroup(UserGroup userGroup) throws Exception {

        save(userGroup);

        for (UserGroupDetail userGroupDetail : userGroup.getUserGroupDetailList()) {
            userGroupDetail.setUserGroup(userGroup);
            save(userGroupDetail);
        }
    }

    @Override
    public void updateUserGroup(UserGroup userGroup) throws Exception {

        save(userGroup);

        executeHql("delete from UserGroupDetail a where a.userGroup.objectId = ?", userGroup.getObjectId());

        for (UserGroupDetail userGroupDetail : userGroup.getUserGroupDetailList()) {
            userGroupDetail.setUserGroup(userGroup);
            save(userGroupDetail);
        }
    }

}
