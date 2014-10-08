package com.withub.common.util;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


/**
 * 文件压缩工具类.
 */
public final class CompressUtil {

    private static CompressUtil instance = new CompressUtil();

    private static final int BUFFEREDSIZE = 1024;

    private CompressUtil() {

    }

    public static CompressUtil getInstance() {
        return instance;
    }

    public synchronized void zip(final String inputFilename, final String zipFilename) throws Exception {

        zip(new File(inputFilename), zipFilename);
    }

    public synchronized void zip(final File inputFile, final String zipFilename) throws Exception {

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilename));
        try {
            zip(inputFile, out, "");
        } catch (Exception e) {
            throw e;
        } finally {
            out.close();
        }
    }


    public synchronized void unzip(final String zipFilename, final String outputDirectory) throws IOException {
        File outFile = new File(outputDirectory);
        if (!outFile.exists()) {
            outFile.mkdirs();
        }

        ZipFile zipFile = new ZipFile(zipFilename);
        Enumeration en = zipFile.entries();
        ZipEntry zipEntry = null;
        while (en.hasMoreElements()) {
            zipEntry = (ZipEntry) en.nextElement();
            if (zipEntry.isDirectory()) {
                // mkdir directory
                String dirName = zipEntry.getName();
                dirName = dirName.substring(0, dirName.length() - 1);

                File f = new File(outFile.getPath() + File.separator + dirName);
                f.mkdirs();

            } else {
                // unzip file
                File f = new File(outFile.getPath() + File.separator
                        + zipEntry.getName());
                f.createNewFile();
                InputStream in = zipFile.getInputStream(zipEntry);
                FileOutputStream out = new FileOutputStream(f);
                try {
                    int c;
                    byte[] by = new byte[BUFFEREDSIZE];
                    while ((c = in.read(by)) != -1) {
                        out.write(by, 0, c);
                    }
                    // out.flush();
                } catch (IOException e) {
                    throw e;
                } finally {
                    out.close();
                    in.close();
                }
            }
        }
    }

    private synchronized void zip(final File inputFile, ZipOutputStream out, String base) throws Exception {

        if (inputFile.isDirectory()) {
            File[] inputFiles = inputFile.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < inputFiles.length; i++) {
                zip(inputFiles[i], out, base + inputFiles[i].getName());
            }
        } else {
            if (base.length() > 0) {
                out.putNextEntry(new ZipEntry(base));
            } else {
                out.putNextEntry(new ZipEntry(inputFile.getName()));
            }

            FileInputStream in = new FileInputStream(inputFile);
            try {
                int c;
                byte[] by = new byte[BUFFEREDSIZE];
                while ((c = in.read(by)) != -1) {
                    out.write(by, 0, c);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                in.close();
            }
        }
    }
}
