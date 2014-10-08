package com.withub.common.util;

/**
 * 版本工具类.
 */
public final class VersionUtil {

    /**
     * 获取初始版本号
     *
     * @return String
     */
    public static String getInitialVersion() {
        return "1.0";
    }

    /**
     * 递增版本号
     *
     * @param version 版本
     * @return String
     */
    public static String increaseVersion(String version) {

        int i = version.indexOf(".");
        int major = Integer.parseInt(version.substring(0, i));
        int minor = Integer.parseInt(StringUtil.substring(version, i + 1));
        if (minor == 9) {
            major += 1;
            minor = 0;
        } else {
            minor += 1;
        }
        String v = major + "." + minor;
        return v;
    }
}
