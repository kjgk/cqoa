package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.po.FileConfig;
import com.withub.model.std.po.FileInfo;
import com.withub.model.system.po.Entity;
import com.withub.service.std.FileService;
import com.withub.web.common.BaseController;
import com.withub.web.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/std")
public class FileController extends BaseController {

    //================================== 属性声明 =========================================================

    @Autowired
    private FileService fileService;

    //================================== Controller 方法 =================================================

    @RequestMapping(value = "/fileConfig/create", method = RequestMethod.POST)
    public void createFileConfig(ModelMap modelMap, FileConfig fileConfig) throws Exception {

        fileService.save(fileConfig);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/fileConfig/update", method = RequestMethod.POST)
    public void updateFileConfig(ModelMap modelMap, FileConfig fileConfig) throws Exception {

        fileService.save(fileConfig);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/fileConfig/load/{objectId}", method = RequestMethod.GET)
    public void loadFileConfig(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        FileConfig fileConfig = (FileConfig) fileService.get(FileConfig.class, objectId);
        Map model = new HashMap();
        model.put("objectId", fileConfig.getObjectId());
        model.put("entity.objectId", Entity.class.getSimpleName() + "_" + fileConfig.getEntity().getObjectId());
        if (fileConfig.getServer() != null) {
            model.put("server.objectId", fileConfig.getServer().getObjectId());
        }
        model.put("storageType", fileConfig.getStorageType());
        model.put("serverPath", fileConfig.getServerPath());
        model.put("count", fileConfig.getCount());
        model.put("traceDownload", fileConfig.getTraceDownload() == 1);
        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/fileConfig/delete/{objectId}", method = RequestMethod.GET)
    public void deleteFileConfig(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        fileService.logicDelete(FileConfig.class, objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/fileConfig/query", method = RequestMethod.GET)
    public void listFileConfig(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(FileConfig.class);

        this.setPageInfoQueryCondition(request, queryInfo);
        this.setInputFieldQueryCondition(request, queryInfo, "name");

        this.setAscOrderBy(queryInfo, "orderNo");

        RecordsetInfo recordsetInfo = fileService.query(queryInfo);

        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (FileConfig fileConfig : (List<FileConfig>) list) {
            HashMap item = new HashMap();
            item.put("objectId", fileConfig.getObjectId());
            item.put("entityName", fileConfig.getEntity().getName());
            if (fileConfig.getServer() != null) {
                item.put("serverName", fileConfig.getServer().getName());
            }
            item.put("serverPath", fileConfig.getServerPath());
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/file/download", method = RequestMethod.GET)
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String fileInfoId = request.getParameter("fileInfoId");
        FileInfo fileInfo = (FileInfo) fileService.get(FileInfo.class, fileInfoId);

        byte[] fileByteArray = fileService.getFileByteArray(fileInfoId);
        if (CollectionUtil.isEmpty(fileByteArray)) {
            return;
        }

        InputStream contentStream = null;
        try {
            response.setHeader("Content-Length", fileInfo.getFileSize() + "");
            response.setHeader("Content-Disposition", "filename=" + new String(fileInfo.getName().getBytes("GBK"), "ISO-8859-1"));
            response.setContentType("application/octet-stream");
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

    @RequestMapping(value = "/file/picture", method = RequestMethod.GET)
    public void picture(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String fileInfoId = request.getParameter("fileInfoId");
        FileInfo fileInfo = fileService.getFileInfoById(fileInfoId);

        if (fileInfo == null) {
            return;
        }

        String imageStr = "jpg;jpeg;png;gif;bmp;tip;";
        if (imageStr.indexOf(fileInfo.getFileExtension()) == -1) {
            return;
        }

        byte[] fileByteArray = fileService.getFileByteArray(fileInfoId);
        if (CollectionUtil.isEmpty(fileByteArray)) {
            return;
        }

        response.setHeader("Content-Length", fileInfo.getFileSize() + "");
        response.setHeader("Content-Disposition", "filename=" + new String(fileInfo.getName().getBytes("GBK"), "ISO-8859-1"));
        response.setContentType("image/jpg");
        InputStream contentStream = new ByteArrayInputStream(fileByteArray);
        FileCopyUtils.copy(contentStream, response.getOutputStream());
        contentStream.close();
        response.getOutputStream().flush();
        response.getOutputStream().close();

    }

    //============================== 属性方法 =============================================================

    public FileService getFileService() {

        return fileService;
    }

    public void setFileService(FileService fileService) {

        this.fileService = fileService;
    }
}
