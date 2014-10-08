package com.withub.service.system;

import com.withub.model.system.po.Role;
import com.withub.service.EntityService;

import java.util.List;

public interface RoleService extends EntityService {

    public List<Role> listRoleByScope(String scope) throws Exception;

    public Role getRoleByTag(String tag) throws Exception;
}
