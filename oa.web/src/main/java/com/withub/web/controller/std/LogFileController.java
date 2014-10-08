package com.withub.web.controller.std;

import com.withub.util.ConfigUtil;
import com.withub.web.common.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/std")
public class LogFileController extends BaseController {


    @RequestMapping(value = "/logFile/query", method = RequestMethod.GET)
    public void queryLogDownloadFileName(HttpServletRequest request, ModelMap modelMap) throws Exception {
        File file = new File(ConfigUtil.getSystemConfigInfo().getLogFileDirectory());
        File[] files = file.listFiles();
        List fileList = Arrays.asList(files);
        List items = new ArrayList();
        for (int i = 0; i < fileList.size(); i++) {
            HashMap item = new HashMap();
            File fileName = (File) fileList.get(i);
            long time = fileName.lastModified();
            FileInputStream fileInputStream = new FileInputStream(fileName);
            DecimalFormat decimalFormat = new DecimalFormat("#");
            String fileSize = decimalFormat.format((double) fileInputStream.available() / 1024);
            item.put("fileName", fileName.getName());
            item.put("logFileSize", fileSize);
            item.put("logFileCreateTime", time);
            items.add(item);
        }
        modelMap.put("total", items.size());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/logFile/Download", method = RequestMethod.GET)
    public void downloadLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = request.getParameter("fileName");
        File file = new File(ConfigUtil.getSystemConfigInfo().getLogFileDirectory() + "/" + fileName);
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
