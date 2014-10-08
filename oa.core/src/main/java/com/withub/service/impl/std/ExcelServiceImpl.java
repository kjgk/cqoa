package com.withub.service.impl.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.Dom4jUtil;
import com.withub.common.util.ReflectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.ExcelService;
import com.withub.spring.SpringContextUtil;
import com.withub.util.EntityUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("excelService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class ExcelServiceImpl extends EntityServiceImpl implements ExcelService {

    public void fillData(File excelTemplateFile, Document xmlDocument, List<String> columnHeaderList, List dataList) throws Exception {

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            // 打开 Excel 文件
            inputStream = new FileInputStream(excelTemplateFile);
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(inputStream);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

            Element rootElement = xmlDocument.getRootElement();

            // 填充列标题
            Node node = rootElement.selectSingleNode("./columnHeader/dynamicColumnIndex");
            int dynamicColumnIndex = -1;
            if (node != null && StringUtil.isNotEmpty(node.getText())) {
                dynamicColumnIndex = Integer.parseInt(node.getText());
            }

            if (dynamicColumnIndex > -1 && CollectionUtil.isNotEmpty(columnHeaderList)) {
                int rowIndex = Integer.parseInt(rootElement.selectSingleNode("./columnHeader/rowIndex").getText());
                HSSFRow hssfRow = hssfSheet.getRow(rowIndex);
                for (int i = 0; i < columnHeaderList.size(); i++) {
                    HSSFCell hssfCell = hssfRow.createCell(dynamicColumnIndex + i);
                    hssfCell.setCellValue(columnHeaderList.get(i));
                }
            }

            // 获取数据开始填充的起始行索引
            int rowIndex = 0;
            try {
                rowIndex = Integer.parseInt(rootElement.selectSingleNode("startRowIndex").getText());
            } catch (Exception e) {
                // do nothing
            }

            // 遍历数据列表
            int recordNo = 0;
            for (Object data : dataList) {
                HSSFRow hssfRow = hssfSheet.createRow(rowIndex++);

                // 获取单元格列表
                List<Element> cellElementList = rootElement.selectNodes("./row/cellList/cell");
                for (Element element : cellElementList) {
                    int cellIndex = Integer.parseInt(Dom4jUtil.getAttributeStringValue(element, "index"));
                    HSSFCell hssfCell = hssfRow.createCell(cellIndex);

                    // 获取data 对象上的属性名
                    String propertyName = Dom4jUtil.getAttributeStringValue(element, "name");

                    // 序号
                    if (propertyName.equalsIgnoreCase("#recordNo")) {
                        setCellValue(hssfCell, ++recordNo, element);
                        continue;
                    }

                    // 支持自定义方法,实现动态列填充
                    String method = Dom4jUtil.getAttributeStringValue(element, "method");
                    if (StringUtil.isNotEmpty(method)) {
                        String args = Dom4jUtil.getAttributeStringValue(element, "args");
                        String[] argArray = args.split(",");
                        String beanId = StringUtil.substring(method, 0, method.indexOf("."));
                        Object object = SpringContextUtil.getInstance().getBean(beanId);
                        String methodName = method.substring(method.indexOf(".") + 1);
                        Object[] objectArgs = new Object[argArray.length];
                        for (int i = 0; i < argArray.length; i++) {
                            Object argValue = getPropertyValue(data, argArray[i]);
                            if (argValue == null) {
                                objectArgs[i] = "";
                            } else {
                                objectArgs[i] = argValue;
                            }
                        }
                        Object value = ReflectionUtil.invokeMethod(object, methodName, objectArgs);
                        int nextCellIndex = 0;
                        for (String objectValue : (List<String>) value) {
                            hssfCell = hssfRow.createCell(cellIndex + nextCellIndex++);
                            if (objectValue != null) {
                                hssfCell.setCellValue(objectValue);
                            }
                        }
                        continue;
                    }

                    Object value;

                    int dotIndex = propertyName.indexOf(".");

                    // 简单的直接的属性
                    if (dotIndex < 0) {
                        value = EntityUtil.getPropertyValue(data, propertyName);
                        setCellValue(hssfCell, value, element);
                        continue;
                    }
                    String firstPropertyName = propertyName.substring(0, dotIndex);
                    String restPropertyName = propertyName.substring(dotIndex + 1);

                    // 快速判断是否是列表类型
                    if (firstPropertyName.endsWith("List")) {
                        List list = (List) EntityUtil.getPropertyValue(data, firstPropertyName);
                        if (CollectionUtil.isEmpty(list)) {
                            // do nothing
                        }
                        String values = "";
                        for (Object obj : list) {
                            Object tempValue = EntityUtil.getPropertyValue(obj, restPropertyName);
                            if (tempValue != null) {
                                values += tempValue.toString() + ",";
                            }
                        }
                        values = StringUtil.trimEnd(values, ",");
                        setCellValue(hssfCell, values, element);
                        continue;
                    }

                    value = EntityUtil.getPropertyValue(data, propertyName);
                    // 设置单元格值
                    setCellValue(hssfCell, value, element);
                }
            }

            fileOutputStream = new FileOutputStream(excelTemplateFile);
            hssfWorkbook.write(fileOutputStream);
            poifsFileSystem.writeFilesystem(fileOutputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    private void setCellValue(HSSFCell hssfCell, Object value, Element element) {

        // 设置编码保证中文显示
        //hssfCell.setEncoding(HSSFCell.ENCODING_UTF_16);

        if (value == null) {
            hssfCell.setCellValue("");
            return;
        }

        String formatString = Dom4jUtil.getAttributeStringValue(element, "format");

        if (value instanceof Date) {
            if (StringUtil.isEmpty(formatString)) {
                formatString = "yyyy-MM-dd";
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);
            hssfCell.setCellValue(dateFormat.format(value));
        } else if (value instanceof Integer) {
            String dataType = Dom4jUtil.getAttributeStringValue(element, "dataType");
            if (dataType.equals("boolean")) {
                String booleanMode = Dom4jUtil.getAttributeStringValue(element, "booleanMode");
                if (booleanMode.equals("0")) {
                    hssfCell.setCellValue(((Integer) value) == 1 ? "否" : "是");
                } else {
                    hssfCell.setCellValue(((Integer) value) == 1 ? "是" : "否");
                }
            } else {
                hssfCell.setCellValue((Integer) value);
            }
        } else if (value instanceof Double) {
            hssfCell.setCellValue((Double) value);
        } else if (value instanceof String) {
            hssfCell.setCellValue((String) value);
        } else {
            hssfCell.setCellValue(value.toString());
        }
    }

}
