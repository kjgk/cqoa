package com.withub.service.impl.system;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.exception.BaseBusinessException;
import com.withub.model.system.po.UserInterface;
import com.withub.model.system.po.UserInterfaceCategory;
import com.withub.model.system.po.UserInterfaceMenu;
import com.withub.service.system.PermissionService;
import com.withub.service.system.UserInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("userInterfaceService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class UserInterfaceServiceImpl extends com.withub.service.EntityServiceImpl implements UserInterfaceService {

    //=================== 属性声明 =========================================================

    @Autowired
    private PermissionService permissionService;

    //=================== 接口实现 =========================================================

    public UserInterfaceCategory getRootUserInterfaceCategory() throws Exception {

        return (UserInterfaceCategory) getRootEntity(UserInterfaceCategory.class);
    }

    public List<UserInterfaceMenu> listCurrentUserInterfaceDenyContextMenu(String userInterfaceTag, String objectId) throws Exception {

        return listCurrentUserInterfaceDenyMenu(userInterfaceTag, objectId);
    }

    public List<UserInterfaceMenu> listCurrentUserInterfaceDenyToolbarMenu(String userInterfaceTag) throws Exception {

        return listCurrentUserInterfaceDenyMenu(userInterfaceTag, "");
    }

    private List<UserInterfaceMenu> listCurrentUserInterfaceDenyMenu(String userInterfaceTag, String objectId) throws Exception {

        List<UserInterfaceMenu> denyMenuList = new ArrayList<UserInterfaceMenu>();
        /*if (getCurrentUser().getAdministrator() == 1) {
            return denyMenuList;
        }*/

        String hql = "select o from " + UserInterface.class.getName() + " o"
                + " where lower(o.userInterfaceTag)='" + userInterfaceTag.toLowerCase() + "'"
                + " and o.objectStatus=1";
        List list = listByHql(hql);
        if (CollectionUtil.isEmpty(list)) {
            throw new BaseBusinessException("", "找不到用户接口标识[" + userInterfaceTag + "]");
        }

        UserInterface userInterface = (UserInterface) list.get(0);

        // 判断工具栏菜单权限
        if (StringUtil.isEmpty(objectId)) {
            if (CollectionUtil.isNotEmpty(userInterface.getToolbarMenuList())) {
                for (UserInterfaceMenu menu : userInterface.getToolbarMenuList()) {
                    if (!permissionService.hasPermission(menu.getPermission(), null)) {
                        denyMenuList.add(menu);
                    }
                }
            }
            return denyMenuList;
        }


        // 判断上下文菜单权限
        List<UserInterfaceMenu> contextMenuList = userInterface.getContextMenuList();
        if (CollectionUtil.isEmpty(contextMenuList)) {
            return denyMenuList;
        }

        Class clazz = Class.forName(contextMenuList.get(0).getPermission().getEntity().getClassName());
        AbstractBaseEntity entityInstance = (AbstractBaseEntity) get(clazz, objectId);
        for (UserInterfaceMenu menu : contextMenuList) {
            if (!permissionService.hasPermission(menu.getPermission(), entityInstance)) {
                denyMenuList.add(menu);
            }
        }

        return denyMenuList;
    }

    //==================== 属性方法 ====================================================

    public PermissionService getPermissionService() {

        return permissionService;
    }

    public void setPermissionService(PermissionService permissionService) {

        this.permissionService = permissionService;
    }
}