package com.withub.service.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.UserGroup;
import com.withub.service.EntityService;


public interface UserGroupService extends EntityService {

    public UserGroup getUserGroup(String objectId) throws Exception;

    public UserGroup getUserGroupByTag(String tag) throws Exception;

    public RecordsetInfo<UserGroup> queryUserGroup(QueryInfo queryInfo) throws Exception;

    public void deleteUserGroup(String objectId) throws Exception;

    public void addUserGroup(UserGroup userGroup) throws Exception;

    public void updateUserGroup(UserGroup userGroup) throws Exception;

}
