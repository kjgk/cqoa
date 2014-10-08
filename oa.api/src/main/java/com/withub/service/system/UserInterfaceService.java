package com.withub.service.system;

import com.withub.model.system.po.UserInterfaceCategory;
import com.withub.model.system.po.UserInterfaceMenu;

import java.util.List;


public interface UserInterfaceService extends com.withub.service.EntityService {

    public UserInterfaceCategory getRootUserInterfaceCategory() throws Exception;

    public List<UserInterfaceMenu> listCurrentUserInterfaceDenyContextMenu(String userInterfaceTag, String objectId) throws Exception;

    public List<UserInterfaceMenu> listCurrentUserInterfaceDenyToolbarMenu(String userInterfaceTag) throws Exception;
}