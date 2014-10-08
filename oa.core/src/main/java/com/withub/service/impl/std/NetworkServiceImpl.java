package com.withub.service.impl.std;


import com.withub.service.std.NetworkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Service("networkService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class NetworkServiceImpl implements NetworkService {

    public void copyFile(File file, String targetServer, String username, String password, String domain, String targetDirectory, String fileName) throws Exception {

        /*// 如果目标机器在域中，需要配置smb.client
        Config.setProperty("jcifs.smb.client.username", username);
        Config.setProperty("jcifs.smb.client.password", password);
        //Config.setProperty("jcifs.smb.client.domain", domain);

        OutputStream outputStream = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            SmbFile smbFile = new SmbFile("smb://" + targetServer + "/" + targetDirectory + "/" + fileName);
            SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(smbFile);
            outputStream = new BufferedOutputStream(smbFileOutputStream);
            byte byteArray[] = new byte[5 * 1024];
            int len;
            while ((len = fileInputStream.read(byteArray)) != -1) {
                outputStream.write(byteArray, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            fileInputStream.close();
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }*/
    }

    public File copySmbFileToTempDirectory(String smbFileName, String server, String username, String password, String domain, String fileName) throws Exception {

        /*// 如果目标机器在域中，需要配置smb.client
        Config.setProperty("jcifs.smb.client.username", username);
        Config.setProperty("jcifs.smb.client.password", password);
        //Config.setProperty("jcifs.smb.client.domain", domain);

        OutputStream outputStream = null;
        SmbFileInputStream smbFileInputStream = null;
        File file = null;
        try {
            SmbFile smbFile = new SmbFile("smb://" + server + "/" + smbFileName);
            smbFileInputStream = new SmbFileInputStream(smbFile);
            file = new File(ConfigUtil.getSystemConfigInfo().getTempDirectory() + "\\" + fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            outputStream = new BufferedOutputStream(fileOutputStream);
            byte byteArray[] = new byte[5 * 1024];
            int len;
            while ((len = smbFileInputStream.read(byteArray)) != -1) {
                outputStream.write(byteArray, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            smbFileInputStream.close();
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
        return file;*/
        return null;
    }

    public void deleteFile(String targetServer, String username, String password, String domain, String fileName) throws Exception {

       /* Config.setProperty("jcifs.smb.client.username", username);
        Config.setProperty("jcifs.smb.client.password", password);
        //Config.setProperty("jcifs.smb.client.domain", domain);

        try {
            SmbFile smbFile = new SmbFile("smb://" + targetServer + "/" + fileName);
            smbFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
        }*/
    }

    public void deleteDatabaseBackupFile() throws Exception {

        /*Config.setProperty("jcifs.smb.client.username", ConfigUtil.getSystemConfigInfo().getDatabaseServerUserName());
        Config.setProperty("jcifs.smb.client.password", ConfigUtil.getSystemConfigInfo().getDatabaseServerPassword());

        try {
            SmbFile smbFile = new SmbFile("smb://" + ConfigUtil.getSystemConfigInfo().getDatabaseServer() + "/" + ConfigUtil.getSystemConfigInfo().getDatabaseBackupDirectory() + "/");
            SmbFile[] fileArray = smbFile.listFiles();

            if (fileArray == null || fileArray.length == 0) {
                return;
            }

            for (SmbFile file : fileArray) {
                long diff = new Date().getTime() - file.lastModified();
                long days = diff / (1000 * 60 * 60 * 24);
                if (days >= ConfigUtil.getSystemConfigInfo().getDatabaseBackupFileKeepDays()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
        }*/
    }
}
