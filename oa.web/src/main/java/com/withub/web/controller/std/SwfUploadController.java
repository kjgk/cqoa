package com.withub.web.controller.std;

import com.withub.common.util.StringUtil;
import com.withub.model.system.config.SystemConfigInfo;
import com.withub.util.ConfigUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Controller
@RequestMapping(value = "/std")
public class SwfUploadController {

    @RequestMapping(value = "/swf/upload", method = RequestMethod.POST)
    public void swfUpload(ModelMap modelMap, @RequestParam(value = "attachment") CommonsMultipartFile attachment) throws Exception {

        String fileName = attachment.getFileItem().getName();
        String tempFileName = StringUtil.getUuid();
        SystemConfigInfo systemConfigInfo = ConfigUtil.getSystemConfigInfo();

        attachment.getFileItem().write(new File(systemConfigInfo.getTempDirectory() + "/" + tempFileName));

        modelMap.put("fileName", fileName);
        modelMap.put("tempFileName", tempFileName);
    }

    @RequestMapping(value = "/swf/getTempImage", method = RequestMethod.GET)
    public void getTempImage(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String tempFileName = request.getParameter("tempFileName");
        SystemConfigInfo systemConfigInfo = ConfigUtil.getSystemConfigInfo();
        File file = new File(systemConfigInfo.getTempDirectory() + "/" + tempFileName);
        InputStream inputStream = new FileInputStream(file);

        response.setHeader("Content-Length", file.length() + "");
        response.setContentType("image/jpg");
        FileCopyUtils.copy(inputStream, response.getOutputStream());
        inputStream.close();
        response.getOutputStream().flush();
        response.getOutputStream().close();

    }
}
