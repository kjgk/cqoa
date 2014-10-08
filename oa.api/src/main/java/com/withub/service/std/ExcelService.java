package com.withub.service.std;

import org.dom4j.Document;

import java.io.File;
import java.util.List;

public interface ExcelService {

    public void fillData(File excelTemplateFile, Document xmlDocument, List<String> columnHeaderList, List dataList) throws Exception;
}
