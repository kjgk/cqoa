package com.withub.web.util;

import com.withub.common.util.FileUtil;
import com.withub.common.util.StringUtil;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * HTTP 工具类
 */
public final class HttpUtil {

    /**
     * 判断是否 POST 请求
     *
     * @param request
     * @return boolean
     */
    public static boolean isPost(HttpServletRequest request) {

        return request.getMethod().equals("POST");
    }

    /**
     * 判断是否 GET 请求
     *
     * @param request
     * @return boolean
     */
    public static boolean isGet(HttpServletRequest request) {

        return request.getMethod().equals("GET");
    }

    /**
     * 获取远程主机IP
     *
     * @param request
     * @return ip
     */
    public static String getRemoteHost(HttpServletRequest request) {

        String clientIp = request.getHeader("x-forwarded-for");
        if (StringUtil.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtil.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtil.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtil.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (StringUtil.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
            // 本机IP6的情况下根据网卡取本机配置的IP
            if ("127.0.0.1".equals(clientIp) || clientIp.contains(":")) {
                try {
                    InetAddress inet = InetAddress.getLocalHost();
                    clientIp = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (clientIp != null && clientIp.length() > 15) {
            if (clientIp.indexOf(",") > 0) {
                clientIp = clientIp.substring(0, clientIp.indexOf(","));
            }
        }
        return clientIp;
    }

    /**
     * 获取远程主机端口
     *
     * @param request
     * @return Port
     */
    public static int getRemotePort(HttpServletRequest request) {

        return request.getRemotePort();
    }

    /**
     * 下载文件
     *
     * @param response
     * @param file        文件
     * @param displayName 下载时的显示名称
     * @throws Exception
     */
    public static void responseFile(HttpServletResponse response, File file, String displayName) throws Exception {

        response.setContentType("application/octet-stream;charset=UTF-8");

        if (StringUtil.isEmpty(displayName)) {
            displayName = FileUtil.getFileName(file);
        }

        String fileName = displayName + "." + FileUtil.getFileExtension(file);

        displayName = new String(fileName.getBytes(), "ISO8859-1");

        response.setHeader("Content-Disposition", "attachment; filename = " + displayName);
//        byte[] fileByteArray = FileUtil.getFileBytes(file);
        response.setHeader("Content-Length", FileUtil.getFileSize(file) + "");

        FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
    }
}
