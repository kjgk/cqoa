package com.withub.service.std;

import java.io.File;

public interface NetworkService {

    public void copyFile(File file, String targetServer, String username, String password, String targetDirectory, String domain, String fileName) throws Exception;

    public File copySmbFileToTempDirectory(String smbFileName, String server, String username, String password, String domain, String fileName) throws Exception;

    public void deleteFile(String targetServer, String username, String password, String domain, String fileName) throws Exception;

    public void deleteDatabaseBackupFile() throws Exception;
}
