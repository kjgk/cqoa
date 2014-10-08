package com.withub.common.util;


import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Properties;

public class VelocityUtil {

    public static String getVelocityContent(String template, String key, Object value) throws Exception {
        //配置
        Properties properties = new Properties();
        //设置输入输出编码类型。
        properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        //这里加载类路径里的模板而不是文件系统路径里的模板
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        VelocityEngine velocityEngine = new VelocityEngine(properties);
        VelocityContext context = new VelocityContext();
        context.put(key, value);
        StringWriter writer = new StringWriter();
        //转换输出
        velocityEngine.evaluate(context, writer, "", template);
        return writer.toString();
    }
}
