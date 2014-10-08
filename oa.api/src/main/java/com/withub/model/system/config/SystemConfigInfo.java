package com.withub.model.system.config;

/**
 * 系统配置信息
 */
public final class SystemConfigInfo extends AbstractBaseConfigInfo {

    private static final long serialVersionUID = 1L;

    //================================ 属性声明 ===========================================================

    private String url;

    /**
     * 系统启用年份
     */
    private int systemEnabledYear;

    /**
     * 临时文件路径
     */
    private String tempDirectory;

    /**
     * 日志文件路径
     */
    private String logFileDirectory;

    /**
     * 日志文件保留天数
     */
    private int logFileKeepDays;

    /**
     * IP 段
     */
    private String ipSegments;

    /**
     * 数据库
     */
    private String databaseType;

    private String databaseServer;

    private String databaseServerUserName;

    private String databaseServerPassword;

    private String databaseBackupDirectory;

    private Integer databaseBackupFileKeepDays;

    //================================ 属性方法 ===========================================================

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public int getSystemEnabledYear() {

        return systemEnabledYear;
    }

    public void setSystemEnabledYear(int systemEnabledYear) {

        this.systemEnabledYear = systemEnabledYear;
    }

    public String getTempDirectory() {

        return tempDirectory;
    }

    public void setTempDirectory(String tempDirectory) {

        this.tempDirectory = tempDirectory;
    }

    public String getLogFileDirectory() {

        return logFileDirectory;
    }

    public void setLogFileDirectory(String logFileDirectory) {

        this.logFileDirectory = logFileDirectory;
    }

    public int getLogFileKeepDays() {

        return logFileKeepDays;
    }

    public void setLogFileKeepDays(int logFileKeepDays) {

        this.logFileKeepDays = logFileKeepDays;
    }

    public String getIpSegments() {

        return ipSegments;
    }

    public void setIpSegments(String ipSegments) {

        this.ipSegments = ipSegments;
    }

    public String getDatabaseType() {

        return databaseType;
    }

    public void setDatabaseType(String databaseType) {

        this.databaseType = databaseType;
    }

    public String getDatabaseServer() {

        return databaseServer;
    }

    public void setDatabaseServer(String databaseServer) {

        this.databaseServer = databaseServer;
    }

    public String getDatabaseServerUserName() {

        return databaseServerUserName;
    }

    public void setDatabaseServerUserName(String databaseServerUserName) {

        this.databaseServerUserName = databaseServerUserName;
    }

    public String getDatabaseServerPassword() {

        return databaseServerPassword;
    }

    public void setDatabaseServerPassword(String databaseServerPassword) {

        this.databaseServerPassword = databaseServerPassword;
    }

    public String getDatabaseBackupDirectory() {

        return databaseBackupDirectory;
    }

    public void setDatabaseBackupDirectory(String databaseBackupDirectory) {

        this.databaseBackupDirectory = databaseBackupDirectory;
    }

    public Integer getDatabaseBackupFileKeepDays() {

        return databaseBackupFileKeepDays;
    }

    public void setDatabaseBackupFileKeepDays(Integer databaseBackupFileKeepDays) {

        this.databaseBackupFileKeepDays = databaseBackupFileKeepDays;
    }
}
