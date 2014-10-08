package com.withub.web.controller.system;

import com.withub.common.util.CollectionUtil;
import com.withub.model.entity.enumeration.OrderByType;
import com.withub.model.entity.query.OrderByProperty;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.system.po.Role;
import com.withub.service.system.RoleService;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/system")
public class RoleController extends BaseController {

    //======================= 属性声明 ===================================================

    @Autowired
    private RoleService roleService;

    //======================= Controller 方法 =============================================

    @RequestMapping(value = "/role/create", method = RequestMethod.POST)
    public void createRole(ModelMap modelMap, Role role) throws Exception {

        roleService.save(role);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    public void updateRole(ModelMap modelMap, Role role) throws Exception {

        roleService.save(role);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/role/load/{objectId}", method = RequestMethod.GET)
    public void loadRole(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        Role role = roleService.get(Role.class, objectId);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("objectId", role.getObjectId());
        model.put("name", role.getName());
        model.put("roleTag", role.getRoleTag());
        model.put("description", role.getDescription());
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/role/delete/{objectId}", method = RequestMethod.GET)
    public void deleteRole(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        roleService.logicDelete(Role.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/role/query", method = RequestMethod.GET)
    public void queryRole(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Role.class);

        this.setPageInfoQueryCondition(request, queryInfo);
        this.setInputFieldQueryCondition(request, queryInfo, "name");

        this.setAscOrderBy(queryInfo, "orderNo");

        RecordsetInfo recordsetInfo = roleService.query(queryInfo);

        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (Role role : (List<Role>) list) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("objectId", role.getObjectId());
            item.put("name", role.getName());
            item.put("roleTag", role.getRoleTag());
            item.put("description", role.getDescription());
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/role/list", method = RequestMethod.GET)
    public void listRole(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Role.class);
        OrderByProperty orderByProperty = new OrderByProperty();
        orderByProperty.setPropertyName("orderNo");
        orderByProperty.setOrderByType(OrderByType.Asc);
        queryInfo.getOrderByPropertyList().add(orderByProperty);

        List list = roleService.list(queryInfo);

        List items = new ArrayList();
        for (Role role : (List<Role>) list) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("value", role.getObjectId());
            item.put("label", role.getName());
            items.add(item);
        }

        modelMap.put("items", items);
    }

    //======================= 属性方法 ===================================================

    public RoleService getRoleService() {

        return roleService;
    }

    public void setRoleService(RoleService roleService) {

        this.roleService = roleService;
    }
}
