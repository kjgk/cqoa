package com.withub.common.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Dom4j 工具类.
 */
public final class Dom4jUtil {

    public static String getAttributeStringValue(final Element element, final String attributeName) {

        String value = "";

        if (element.attribute(attributeName) != null) {
            value = element.attribute(attributeName).getValue();
        }

        return value;
    }

    public static Integer getAttributeIntegerValue(final Element element, final String attributeName, final Integer... defaultValue) {

        Integer retValue = null;
        String stringValue = getAttributeStringValue(element, attributeName);

        try {
            retValue = Integer.parseInt(stringValue);
        } catch (Exception e) {
            if (CollectionUtil.isNotEmpty(defaultValue)) {
                retValue = defaultValue[0];
            }
        }

        return retValue;
    }

    public static Document parseXmlDocumentFromFile(final String xmlFile) {

        File file = new File(xmlFile);
        SAXReader saxReader = new SAXReader();

        Document document = null;
        try {
            document = saxReader.read(file);
        } catch (Exception e) {
            // do nothing
        }

        return document;
    }

    public static Document parseXmlDocument(final String xml) {

        Document document = null;

        try {
            document = DocumentHelper.parseText(xml);
        } catch (Exception e) {
            // do nothing
        }

        return document;
    }

    public static String formatXml(final String inputXml) throws Exception {

        String returnXml = null;

        SAXReader reader = new SAXReader();
        Document document = reader.read(new StringReader(inputXml));
        XMLWriter writer = null;
        if (document != null) {
            try {
                StringWriter stringWriter = new StringWriter();
                OutputFormat format = new OutputFormat(" ", true);
                writer = new XMLWriter(stringWriter, format);
                writer.write(document);
                writer.flush();
                returnXml = stringWriter.getBuffer().toString();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Exception e) {
                        // do nothing
                    }
                }
            }
        }
        return returnXml;
    }

}
