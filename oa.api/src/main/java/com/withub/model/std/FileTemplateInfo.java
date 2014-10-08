package com.withub.model.std;

import com.withub.model.std.po.FileInfo;
import org.dom4j.Document;

import java.io.File;

public final class FileTemplateInfo {

    private File file;

    private String fileName;

    private Document xmlDocument;

    private FileInfo fileInfo;

    public File getFile() {

        return file;
    }

    public void setFile(File file) {

        this.file = file;
    }

    public String getFileName() {

        return fileName;
    }

    public void setFileName(String fileName) {

        this.fileName = fileName;
    }

    public Document getXmlDocument() {

        return xmlDocument;
    }

    public void setXmlDocument(Document xmlDocument) {

        this.xmlDocument = xmlDocument;
    }

    public FileInfo getFileInfo() {

        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {

        this.fileInfo = fileInfo;
    }
}
