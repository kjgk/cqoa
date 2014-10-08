package com.withub.service.system;

import com.withub.service.EntityService;


public interface MenuService extends EntityService {

    public void deleteMenu(String menuId) throws Exception;
}