package com.withub.web.controller.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.oa.po.UserGroup;
import com.withub.service.oa.UserGroupService;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Controller
@RequestMapping(value = "/oa")
public class UserGroupController extends BaseController {

    @Autowired
    private UserGroupService userGroupService;

    @RequestMapping(value = "/userGroup", method = RequestMethod.GET)
    public void queryUserGroup(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(UserGroup.class);
        setPageInfoQueryCondition(request, queryInfo);
//        setDescOrderBy(queryInfo, "createTime");

        putRecordsetInfo(modelMap, userGroupService.queryUserGroup(queryInfo));
    }

    @RequestMapping(value = "/userGroup/{objectId}", method = RequestMethod.GET)
    public void getUserGroup(@PathVariable("objectId") String objectId, ModelMap modelMap) throws Exception {

        UserGroup userGroup = userGroupService.getUserGroup(objectId);
        putData(modelMap, userGroup);
    }

    @RequestMapping(value = "/userGroup", method = RequestMethod.POST)
    public void createUserGroup(@RequestBody UserGroup userGroup) throws Exception {

        userGroupService.addUserGroup(userGroup);
    }

    @RequestMapping(value = "/userGroup", method = RequestMethod.PUT)
    public void updateUserGroup(@RequestBody UserGroup userGroup) throws Exception {

        userGroupService.updateUserGroup(userGroup);
    }


    @RequestMapping(value = "/userGroup/{objectId}", method = RequestMethod.DELETE)
    public void deleteUserGroup(@PathVariable("objectId") String objectId) throws Exception {

        userGroupService.deleteUserGroup(objectId);
    }

}
