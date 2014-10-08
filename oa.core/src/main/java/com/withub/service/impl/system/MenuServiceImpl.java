package com.withub.service.impl.system;

import com.withub.model.system.po.AuthorizationClusterMenu;
import com.withub.model.system.po.Menu;
import com.withub.model.system.po.Permission;
import com.withub.service.EntityServiceImpl;
import com.withub.service.system.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("menuService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class MenuServiceImpl extends EntityServiceImpl implements MenuService {

    //=================== 接口实现 =========================================================

    public void deleteMenu(String menuId) throws Exception {

        Menu menu = get(Menu.class, menuId);
        logicDelete(menu);

        String hql = "delete from " + AuthorizationClusterMenu.class.getName() + " o"
                + " where o.menu.objectId='" + menu.getObjectId() + "'";
        executeHql(hql);

        hql = "update " + Permission.class.getName() + " o set o.menu.objectId = null"
                + " where o.menu.objectId='" + menuId + "'";
        executeHql(hql);

    }
}