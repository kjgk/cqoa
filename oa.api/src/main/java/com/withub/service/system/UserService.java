package com.withub.service.system;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.system.po.User;
import com.withub.model.system.po.UserPhoto;
import com.withub.service.EntityService;

import java.util.List;

public interface UserService extends EntityService {

    public RecordsetInfo queryUser(QueryInfo queryInfo) throws Exception;

    public void addUser(User user) throws Exception;

    public void addUser(User user, boolean event) throws Exception;

    public void updateUser(User user) throws Exception;

    public void archiveUser(String userId) throws Exception;

    public void activeUser(String userId) throws Exception;

    public void setUserAdministrator(String userId) throws Exception;

    public void cancelUserAdministrator(String userId) throws Exception;

    public void deleteUser(String userId) throws Exception;

    public User getUserById(String userId) throws Exception;

    public User getUserByCode(String code) throws Exception;

    public User getSystemUser() throws Exception;

    public UserPhoto getPhotoByUserId(String userId) throws Exception;

    public List<User> searchUser(String keyword) throws Exception;

    public List<User> listByRoleTag(String roleTag) throws Exception;
}