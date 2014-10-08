package com.withub.web.controller.system;

import com.withub.model.system.config.SystemConfigInfo;
import com.withub.service.system.ConfigurationService;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

@Controller
@RequestMapping(value = "/system/common")
public class CommonController extends BaseController {

    //=============================== 属性声明 ===========================================================

    @Autowired
    private ConfigurationService configurationService;

    //============================== Controller 方法 =====================================================


    @RequestMapping(value = "/downloadTempFile", method = RequestMethod.GET)
    public void downloadTempFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String tempFileName = request.getParameter("tempFileName");
        String fileName = request.getParameter("fileName");

        SystemConfigInfo systemConfigInfo = (SystemConfigInfo) configurationService.getConfigInfo(SystemConfigInfo.class.getName(), "SYSTEM");

        File file = new File(systemConfigInfo.getTempDirectory() + "/" + tempFileName);
        FileInputStream fileInputStream = new FileInputStream(file);

        response.setHeader("Content-Length", file.length() + "");
        response.setHeader("Content-Disposition", "filename=" + new String(fileName.getBytes("GBK"), "ISO-8859-1"));
        response.setContentType("application/octet-stream");

        FileCopyUtils.copy(fileInputStream, response.getOutputStream());

        fileInputStream.close();
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

}
