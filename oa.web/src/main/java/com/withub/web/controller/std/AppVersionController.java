package com.withub.web.controller.std;

import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.ExpressionOperation;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.std.po.AppVersion;
import com.withub.service.std.AppVersionService;
import com.withub.spring.SpringContextUtil;
import com.withub.util.SpringSecurityUtil;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;


@Controller
@RequestMapping(value = "/std")
public class AppVersionController extends BaseController {

    @Autowired
    private AppVersionService appVersionService;

    @RequestMapping(value = "/appVersion", method = RequestMethod.GET)
    public void queryAppVersion(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(AppVersion.class);
        setPageInfoQueryCondition(request, queryInfo);
        setDescOrderBy(queryInfo, "createTime");

        String version = request.getParameter("version");
        if (StringUtil.isNotEmpty(version)) {
            setQueryInfoCondition(queryInfo, "version", version, ExpressionOperation.MatchMiddle);
        }

        putRecordsetInfo(modelMap, appVersionService.queryAppVersion(queryInfo));
    }

    @RequestMapping(value = "/appVersion/{objectId}", method = RequestMethod.GET)
    public void getAppVersion(@PathVariable("objectId") String objectId, ModelMap modelMap) throws Exception {

        AppVersion appVersion = appVersionService.getAppVersion(objectId);
        putData(modelMap, appVersion);
    }

    @RequestMapping(value = "/appVersion", method = RequestMethod.POST)
    public void createAppVersion(@RequestBody AppVersion appVersion) throws Exception {

        appVersionService.saveAppVersion(appVersion);
    }

    @RequestMapping(value = "/appVersion", method = RequestMethod.PUT)
    public void updateAppVersion(@RequestBody AppVersion appVersion) throws Exception {

        appVersionService.saveAppVersion(appVersion);
    }


    @RequestMapping(value = "/appVersion/{objectId}", method = RequestMethod.DELETE)
    public void deleteAppVersion(@PathVariable("objectId") String objectId) throws Exception {

        appVersionService.deleteAppVersion(objectId);
    }

    @RequestMapping(value = "/appVersion/enable/{objectId}", method = RequestMethod.POST)
    public void enableAppVersion(@PathVariable("objectId") String objectId) throws Exception {

        appVersionService.enableAppVersion(objectId,SpringSecurityUtil.getCurrentUser());
    }

    @RequestMapping(value = "/appVersion/disable/{objectId}", method = RequestMethod.POST)
    public void disableAppVersion(@PathVariable("objectId") String objectId) throws Exception {

        appVersionService.disableAppVersion(objectId,SpringSecurityUtil.getCurrentUser());
    }

}
