package com.withub.service.std;


import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.std.FileUploadInfo;
import com.withub.model.std.po.FileConfig;
import com.withub.model.std.po.FileInfo;
import com.withub.service.EntityService;

import java.io.File;
import java.util.List;

public interface FileService extends EntityService {

    public void save(AbstractBaseEntity entity, List<File> fileList, List<String> fileNameList) throws Exception;

    public void save(AbstractBaseEntity entity, File file, String fileName, String fileDescription, Integer... orderNo) throws Exception;

    public void save(AbstractBaseEntity entity, List<FileUploadInfo> fileUploads) throws Exception;

    public void save(AbstractBaseEntity entity, String uploadFileName, String fileName, Integer... orderNo) throws Exception;

    public void save(AbstractBaseEntity entity, FileUploadInfo fileUploadInfo) throws Exception;

    public FileInfo getFileInfoById(String fileInfoId) throws Exception;

    public List<FileInfo> listFileInfo(AbstractBaseEntity entity) throws Exception;

    public List<FileInfo> listFileInfo(String relatedObjectId) throws Exception;

    public FileInfo getFileInfoByRelatedObjectId(String relatedObjectId) throws Exception;

    public byte[] getFileByteArray(String fileInfoId) throws Exception;

    public byte[] getFileByteArray(FileInfo fileInfo) throws Exception;

    public void deleteFileInfo(String fileInfoId) throws Exception;

    public void logFileDownload(FileInfo fileInfo, String clientIp) throws Exception;

    public FileConfig getFileConfigByFileInfo(FileInfo fileInfo) throws Exception;

}
