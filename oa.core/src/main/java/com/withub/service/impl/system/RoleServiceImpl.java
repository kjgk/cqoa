package com.withub.service.impl.system;

import com.withub.model.system.po.Role;
import com.withub.service.EntityServiceImpl;
import com.withub.service.system.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("roleService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class RoleServiceImpl extends EntityServiceImpl implements RoleService {

    public List<Role> listRoleByScope(String scope) throws Exception {

        String hql = "select o from " + Role.class.getName() + " o where o.objectStatus=1"
                + " and o.roleScope.codeTag='" + scope + "'"
                + " order by o.orderNo";
        List roleList = null;
        try {
            roleList = listByHql(hql);
        } catch (Exception e) {

        }

        return (List<Role>) roleList;
    }

    public Role getRoleByTag(String tag) throws Exception {

        Role role = (Role) getByPropertyValue(Role.class, "roleTag", tag);
        return role;
    }
}
