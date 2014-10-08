package com.withub.common.util;


import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Logger;

public class JdbcUtil {
    public static Logger logger = Logger.getLogger(JdbcUtil.class.getName());
    private static Properties p = new Properties();

    static {
        try {
            p.load(JdbcUtil.class.getResourceAsStream("/config/jdbc-config.properties"));
        } catch (Exception e) {

        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            String dbDriver = JdbcUtil.getValue("default.driverClass", "");
            Class.forName(dbDriver).newInstance();
            String dbUrl = JdbcUtil.getValue("default.jdbcUrl", "");
            conn = java.sql.DriverManager.getConnection(dbUrl, JdbcUtil.getValue("default.user", ""), JdbcUtil.getValue("default.password", ""));
        } catch (Exception e) {
            logger.info("数据库connection连接失败：" + e.getMessage());
        }
        return conn;
    }

    /**
     * 取得配置值
     *
     * @param key
     * @param
     * @return
     */
    private static String getValue(String key, String def) {
        if (p.containsKey(key))
            return p.getProperty(key);
        return def;
    }
}
