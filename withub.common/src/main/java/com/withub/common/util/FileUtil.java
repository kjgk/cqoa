package com.withub.common.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * 文件工具类.
 */
public final class FileUtil {

    //================================= 变量声明 ==========================================================

    /**
     * 文件后缀名分隔符
     */
    public static final String EXTENSION_SEPARATOR = ".";

    private static final String UNIX_SEPARATOR = "/";

    private static final String WINDOWS_SEPARATOR = "\\";

    //================================= 目录操作方法 =======================================================

    /**
     * 根据文件获取操作系统路径符
     *
     * @param file 文件
     * @return String
     */
    public static String getSystemSeparator(final File file) {

        String separator = WINDOWS_SEPARATOR;
        if (file.getName().indexOf(UNIX_SEPARATOR) > -1) {
            separator = UNIX_SEPARATOR;
        }
        return separator;
    }

    /**
     * 判断目录是否存在
     *
     * @param directoryFullName 目录全名
     * @return boolean
     */
    public static boolean directoryExists(final String directoryFullName) {

        File file = new File(directoryFullName);

        return file.exists() && file.isDirectory();
    }

    /**
     * 清空目录
     *
     * @param directory 目录对象
     * @throws java.io.IOException IO异常
     */
    public static void clearDirectory(final File directory) throws IOException {

        FileUtils.cleanDirectory(directory);
    }

    /**
     * 清空目录
     *
     * @param directoryFullName 目录全名
     * @throws java.io.IOException IO异常
     */
    public static void clearDirectory(final String directoryFullName) throws IOException {

        File fileObject = new File(directoryFullName);
        clearDirectory(fileObject);
    }

    /**
     * 删除目录
     *
     * @param directory 目录对象
     * @throws java.io.IOException IO异常
     */
    public static void deleteDirectory(final File directory) throws IOException {

        FileUtils.deleteDirectory(directory);
    }

    /**
     * 删除目录
     *
     * @param directoryFullName 目录全名
     * @throws java.io.IOException IO异常
     */
    public static void deleteDirectory(final String directoryFullName) throws IOException {

        File fileObject = new File(directoryFullName);
        deleteDirectory(fileObject);
    }

    /**
     * 创建目录
     *
     * @param directoryFullName 目录全名
     * @return boolean
     */
    public static boolean createDirectory(final String directoryFullName) {

        if (directoryExists(directoryFullName)) {
            return true;
        }

        StringTokenizer stringTokenizer;
        String pathSeparator = directoryFullName.indexOf("\\") > 0 ? "\\" : "/";
        stringTokenizer = new StringTokenizer(directoryFullName, pathSeparator);

        String path1 = "";
        String path2 = path1;
        while (stringTokenizer.hasMoreTokens()) {
            path1 = stringTokenizer.nextToken() + pathSeparator;
            path2 += path1;
            File dir = new File(path2);
            if (!dir.exists()) {
                dir.mkdir();
            }
        }
        return true;
    }

    //================================ 文件操作方法 =======================================================

    /**
     * 判断文件是否存在
     *
     * @param fileFullName 文件全名
     * @return boolean
     */
    public static boolean fileExists(final String fileFullName) {

        File file = new File(fileFullName);

        return file.exists() && file.isFile();
    }

    /**
     * 在指定的目录下创建一个随机文件
     *
     * @param directoryFullName 目录全名
     * @param extension         后缀名
     * @return File             文件
     * @throws java.io.IOException IO异常
     */
    public File createRandomFile(final String directoryFullName, final String extension) throws IOException {

        File directory = new File(directoryFullName);
        if (!directoryExists(directoryFullName)) {
            FileUtils.forceMkdir(directory);
        }

        File file = File.createTempFile(StringUtil.getUuid(), extension, directory);

        return file;
    }

    /**
     * 将字节数组写到文件中
     *
     * @param bytes             字节数组
     * @param directoryFullName 目录全名
     * @param extension         后缀名
     * @return File             文件
     * @throws java.io.IOException IO异常
     */
    public File createRandomFileFormByteArray(final byte[] bytes, final String directoryFullName, final String extension) throws IOException {

        File file = createRandomFile(directoryFullName, extension);
        FileCopyUtils.copy(bytes, file);
        return file;
    }

    /**
     * 获取文件的字节数据
     *
     * @param file 文件
     * @return byte[] 字节数组
     * @throws java.io.IOException IO异常
     */
    public static byte[] getFileBytes(final File file) throws IOException {

        byte[] bytes = null;
        if (file.exists()) {
            bytes = FileCopyUtils.copyToByteArray(file);
        }
        return bytes;
    }

    /**
     * 获取文件的字节数据
     *
     * @param fileFullName 文件全名
     * @return byte[] 字节数组
     * @throws java.io.IOException IO异常
     */
    public static byte[] getFileBytes(final String fileFullName) throws IOException {

        File file = new File(fileFullName);
        byte[] bytes = FileCopyUtils.copyToByteArray(file);
        return bytes;
    }

    /**
     * 复制文件
     *
     * @param file             源文件
     * @param destFileFullName 目标文件全名
     * @throws java.io.IOException IO异常
     */
    public static void copyFile(final File file, final String destFileFullName) throws IOException {

        File destFile = new File(destFileFullName);

        // FileUtils 已经实现目录的动态创建
        FileUtils.copyFile(file, destFile);
    }

    /**
     * 复制文件
     *
     * @param srcFileFullName  源文件全名
     * @param destFileFullName 目标文件全名
     * @throws java.io.IOException IO异常
     */
    public static void copyFile(final String srcFileFullName, final String destFileFullName) throws IOException {

        File srcFile = new File(srcFileFullName);
        File destFile = new File(destFileFullName);
        FileUtils.copyFile(srcFile, destFile);
    }

    /**
     * 获取文件的大小
     *
     * @param file 文件
     * @return long
     */
    public static long getFileSize(final File file) {

        return file.length();
    }

    /**
     * 将字节数组保存为一个文件
     *
     * @param byteArray 字节数组
     * @param fileName  文件名
     * @return File
     */
    public static File getFileFromBytes(final byte[] byteArray, final String fileName) throws Exception {

        File file = new File(fileName);
        FileUtils.writeByteArrayToFile(file, byteArray);

        return file;
    }

    //============================= 文件名操作方法 ========================================================

    /**
     * 获取文件后缀名
     *
     * @param filename 文件名
     * @return String 文件后缀名
     */
    public static String getFileExtension(final String filename) {

        String fileExtension = FilenameUtils.getExtension(filename);

        return fileExtension;
    }

    /**
     * 获取文件后缀名
     *
     * @param file 文件
     * @return String 文件后缀名
     */
    public static String getFileExtension(final File file) {

        String fileExtension = FilenameUtils.getExtension(file.getName());

        return fileExtension;
    }

    /**
     * 获取文件名
     *
     * @param fileFullName 文件全名
     * @return String 文件名
     */
    public static String getFileName(final String fileFullName) {

        String fileName = FilenameUtils.getName(fileFullName);

        return fileName;
    }

    /**
     * 获取文件名
     *
     * @param file 文件
     * @return String 文件名
     */
    public static String getFileName(final File file) {

        String fileFullName = file.getName();
        String fileName = getFileName(fileFullName);

        return fileName;
    }

    /**
     * 获取随机文件名
     *
     * @param extension 文件后缀名
     * @return String 文件名
     */
    public static String getRandomFileName(final String extension) {

        String fileName = StringUtil.getUuid() + EXTENSION_SEPARATOR + extension;

        return fileName;
    }


    /**
     * 获取文件的路径,带目录分割符号
     *
     * @param file 文件
     * @return String
     */
    public static String getFilePath(final String file) {

        String filePath = FilenameUtils.getPath(file);

        return filePath;
    }

}