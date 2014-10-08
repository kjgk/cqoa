package com.withub.service.impl.std;


import com.withub.common.util.*;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.exception.BaseBusinessException;
import com.withub.model.std.FileUploadInfo;
import com.withub.model.std.po.FileConfig;
import com.withub.model.std.po.FileContent;
import com.withub.model.std.po.FileDownload;
import com.withub.model.std.po.FileInfo;
import com.withub.model.system.config.SystemConfigInfo;
import com.withub.model.system.po.Entity;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.FileService;
import com.withub.service.std.NetworkService;
import com.withub.util.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Service("fileService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class FileServiceImpl extends EntityServiceImpl implements FileService {

    //============================ 属性声明 ===============================================================

    @Autowired
    private NetworkService networkService;

    private static final Integer STORAGETYPE_FILESYSTEM = 1;

    private static final Integer STORAGETYPE_DATABASE = 2;

    //============================= 接口实现 ==============================================================

    public void save(AbstractBaseEntity entity, List<File> fileList, List<String> fileNameList) throws Exception {

        for (int i = 0; i < fileList.size(); i++) {
            save(entity, fileList.get(i), fileNameList.get(i), "", i + 1);
        }
    }

    public void save(AbstractBaseEntity entity, File file, String fileName, String fileDescription, Integer... orderNo) throws Exception {

        //  获取文件存放配置信息
        Entity targetEntity = (Entity) getByPropertyValue(Entity.class, "className", entity.getClass().getName());
        FileConfig fileConfig = (FileConfig) getByPropertyValue(FileConfig.class, "entity.objectId", targetEntity.getObjectId());

        if (fileConfig == null) {
            // 获取默认配置
            targetEntity = (Entity) getByPropertyValue(Entity.class, "className", Entity.class.getName());
            String hql = "select o from " + FileConfig.class.getName() + " o where o.objectStatus=1"
                    + " and entity.objectId=? and o.storageType=?";
            fileConfig = (FileConfig) getByHql(hql, targetEntity.getObjectId(), STORAGETYPE_FILESYSTEM);
        }

        if (fileConfig == null) {
            throw new BaseBusinessException("", "[" + entity.getClass().getName() + "]未设置附件存放位置!");
        }

        if (fileConfig.getCount() == 1) {
            // 删除原来的文件信息和附件
            List<FileInfo> oldFileInfoList = listFileInfo(entity);
            if (CollectionUtil.isNotEmpty(oldFileInfoList)) {
                for (FileInfo fileInfo : oldFileInfoList) {
                    if (fileConfig.getServer().getLocalhost() == 1) {
                        String oldFileName = fileConfig.getServerPath() + "\\" + fileConfig.getEntity().getEntityName()
                                + "\\" + fileInfo.getPath() + "\\" + fileInfo.getFileName() + "." + fileInfo.getFileExtension();
                        File oldFile = new File(oldFileName);
                        try {
                            oldFile.delete();
                        } catch (Exception e) {
                            // do nothing
                        }
                    } else {
                        // TODO:网络删除
                    }
                }
                delete(oldFileInfoList);
            }
        }

        // 为避免将所有文件存放在一个路径下,根据实体名和年月动态生成子目录
        String yearMonthPath = DateUtil.getDateFormatString(DateUtil.getCurrentTime(), "yyyyMM");

        // 使用 GUID 文件系统名
        String guidFileName = StringUtil.getUuid();
        String extension = FileUtil.getFileExtension(fileName);

        // 保存文件信息
        FileInfo fileInfo = new FileInfo();
        fileInfo.setObjectId(StringUtil.getUuid());
        fileInfo.setEntity(targetEntity);
        fileInfo.setRelatedObjectId(entity.getObjectId());
        if (fileConfig.getStorageType().equals(STORAGETYPE_FILESYSTEM)) {
            fileInfo.setPath(yearMonthPath);
            fileInfo.setFileName(guidFileName);
        }
        fileInfo.setName(fileName);
        fileInfo.setDescription(fileDescription);
        fileInfo.setFileExtension(extension);
        fileInfo.setFileSize(FileUtil.getFileSize(file));
        fileInfo.setFileMd5(Md5Util.getFileMd5(file));
        if (CollectionUtil.isNotEmpty(orderNo)) {
            fileInfo.setOrderNo(orderNo[0]);
        } else {
            fileInfo.setOrderNo(1);
        }
        save(fileInfo);

        // 保存到数据库
        if (fileConfig.getStorageType().equals(STORAGETYPE_DATABASE)) {
            byte[] fileByteArray = FileUtil.getFileBytes(file);
            FileContent fileContent = new FileContent();
            fileContent.setFileInfoId(fileInfo.getObjectId());
            fileContent.setContent(fileByteArray);
            save(fileContent);
        }

        // 存放到文件系统
        if (fileConfig.getStorageType().equals(STORAGETYPE_FILESYSTEM)) {
            String targetPath = fileConfig.getServerPath() + "\\" + entity.getClass().getSimpleName()
                    + "\\" + yearMonthPath;
            if (fileConfig.getServer().getLocalhost() == 1) {
                FileUtil.copyFile(file, targetPath + "\\" + guidFileName + "." + extension);
            } else {
                // TODO 网络复制
            }
        }
    }

    public void save(AbstractBaseEntity entity, String uploadFileName, String fileName, Integer... orderNo) throws Exception {

        SystemConfigInfo systemConfigInfo = ConfigUtil.getSystemConfigInfo();
        File file = new File(systemConfigInfo.getTempDirectory() + "/" + uploadFileName);
        save(entity, file, fileName, "", orderNo);
    }

    public void save(AbstractBaseEntity entity, FileUploadInfo fileUpload) throws Exception {

        if (fileUpload == null) {
            return;
        }
        SystemConfigInfo systemConfigInfo = ConfigUtil.getSystemConfigInfo();
        if (StringUtil.isNotEmpty(fileUpload.getFileId())) {
            if (fileUpload.getFileDelete() != null && fileUpload.getFileDelete() == 1) {
                deleteFileInfo(fileUpload.getFileId());
            } else {
                FileInfo fileInfo = get(FileInfo.class, fileUpload.getFileId());
                fileInfo.setDescription(fileUpload.getFileDescription());
                save(fileInfo);
            }
        } else {
            if (fileUpload.getFileOrderNo() == null) {
                fileUpload.setFileOrderNo(1);
            }
            File file = new File(systemConfigInfo.getTempDirectory() + "/" + fileUpload.getTempFileName());
            save(entity, file, fileUpload.getFileName(), fileUpload.getFileDescription(), fileUpload.getFileOrderNo());
        }
    }

    public void save(AbstractBaseEntity entity, List<FileUploadInfo> fileUploads) throws Exception {

        if (CollectionUtil.isEmpty(fileUploads)) {
            return;
        }
        for (int i = 0; i < fileUploads.size(); i++) {
            save(entity, fileUploads.get(i));
        }
    }

    public List<FileInfo> listFileInfo(String relatedObjectId) throws Exception {

        String hql = "select o from " + FileInfo.class.getName() + " o"
                + " where o.objectStatus=1 and o.relatedObjectId=? order by createTime";
        List list = listByHql(hql, relatedObjectId);

        return (List<FileInfo>) list;
    }

    public FileInfo getFileInfoByRelatedObjectId(String relatedObjectId) throws Exception {

        FileInfo fileInfo = null;
        List<FileInfo> list = listFileInfo(relatedObjectId);
        if (CollectionUtil.isNotEmpty(list)) {
            fileInfo = list.get(0);
        }
        return fileInfo;
    }

    public FileInfo getFileInfoById(String fileInfoId) throws Exception {

        FileInfo fileInfo = get(FileInfo.class, fileInfoId);
        return fileInfo;
    }

    public List<FileInfo> listFileInfo(AbstractBaseEntity entity) throws Exception {

        return listFileInfo(entity.getObjectId());
    }

    public byte[] getFileByteArray(String fileInfoId) throws Exception {

        FileInfo fileInfo = get(FileInfo.class, fileInfoId);
        byte[] fileByteArray = getFileByteArray(fileInfo);
        return fileByteArray;
    }

    public byte[] getFileByteArray(FileInfo fileInfo) throws Exception {

        byte[] fileByteArray;
        FileConfig fileConfig = (FileConfig) getByPropertyValue(FileConfig.class, "entity.objectId", fileInfo.getEntity().getObjectId());

        File file = null;

        if (fileConfig.getStorageType() == STORAGETYPE_FILESYSTEM) {
            if (fileConfig.getServer().getLocalhost() == 1) {
                String fileName = fileConfig.getServerPath() + "\\" + fileConfig.getEntity().getEntityName()
                        + "\\" + fileInfo.getPath() + "\\" + fileInfo.getFileName() + "." + fileInfo.getFileExtension();
                file = new File(fileName);
            } else {
                // TODO: 网络复制
            }

            fileByteArray = FileUtil.getFileBytes(file);
        } else {
            FileContent fileContent = (FileContent) getByPropertyValue(FileContent.class, "fileInfoId", fileInfo.getObjectId());
            fileByteArray = fileContent.getContent();
        }

        return fileByteArray;
    }

    public void deleteFileInfo(String fileInfoId) throws Exception {

        FileInfo fileInfo = get(FileInfo.class, fileInfoId);
        logicDelete(fileInfo);
    }

    public void logFileDownload(FileInfo fileInfo, String clientIp) throws Exception {

        FileConfig fileConfig = getFileConfigByFileInfo(fileInfo);
        if (fileConfig.getTraceDownload() == 0) {
            return;
        }
        FileDownload fileDownload = new FileDownload();
        fileDownload.setFileInfo(fileInfo);
        fileDownload.setEntity(fileInfo.getEntity());
        fileDownload.setUser(fileInfo.getCurrentUser());
        fileDownload.setDownloadTime(DateUtil.getCurrentTime());
        fileDownload.setClientIp(clientIp);
        save(fileDownload);
    }

    public FileConfig getFileConfigByFileInfo(FileInfo fileInfo) throws Exception {

        String hql = "select o from " + FileConfig.class.getName() + " o where o.objectStatus=1"
                + " and o.entity.objectId=?";
        List list = listByHql(hql, fileInfo.getEntity().getObjectId());
        return (FileConfig) list.get(0);
    }

    //=========================== 属性方法 ================================================================

    public NetworkService getNetworkService() {

        return networkService;
    }

    public void setNetworkService(NetworkService networkService) {

        this.networkService = networkService;
    }
}
