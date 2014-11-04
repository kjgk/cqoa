package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.ExpressionOperation;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.std.po.AppVersion;
import com.withub.model.std.po.FileInfo;
import com.withub.service.std.AppVersionService;
import com.withub.service.std.FileService;
import com.withub.web.common.BaseController;
import com.withub.web.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/std")
public class AppVersionController extends BaseController {

    @Autowired
    private AppVersionService appVersionService;

    @Autowired
    private FileService fileService;

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

    @RequestMapping(value = "/appVersion/current", method = RequestMethod.GET)
    public void currentAppVersion(ModelMap modelMap) throws Exception {

        AppVersion appVersion = appVersionService.getEnabledAppVersion();

        List fileList = fileService.listFileInfo(appVersion.getObjectId());
        Map data = new HashMap();
        data.put("appVersion", appVersion);
        data.put("fileList", fileList);
        putData(modelMap, data);
    }


    @RequestMapping(value = "/appVersion/download/{objectId}/{name}", method = RequestMethod.GET)
    public void downloadAppVersion(HttpServletRequest request, HttpServletResponse response
            , @PathVariable(value = "objectId") String objectId, @PathVariable(value = "name") String name) throws Exception {

        String fileInfoId = objectId;
        FileInfo fileInfo = fileService.get(FileInfo.class, fileInfoId);

        byte[] fileByteArray = fileService.getFileByteArray(fileInfoId);
        if (CollectionUtil.isEmpty(fileByteArray)) {
            return;
        }

        InputStream contentStream = null;
        try {
            response.setContentType("application/vnd.android.package-archive");
            contentStream = new ByteArrayInputStream(fileByteArray);
            FileCopyUtils.copy(contentStream, response.getOutputStream());
            String clientIp = HttpUtil.getRemoteHost(request);
            fileService.logFileDownload(fileInfo, clientIp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (contentStream != null) {
                contentStream.close();
            }
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }

}
