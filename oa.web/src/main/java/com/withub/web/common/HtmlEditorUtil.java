package com.withub.web.common;


import com.withub.common.util.StringUtil;

public final class HtmlEditorUtil {

    public static String convertStandardization(String content) {

        if (StringUtil.isEmpty(content)) {
            return content;
        }

//        content = content.substring(1);
        String text = content.replaceAll("<STRONG>", "<B>").replaceAll("</STRONG>", "</B>")
                .replaceAll("<strong>", "<B>").replaceAll("</strong>", "</B>")
                .replaceAll("<EM>", "<I>").replaceAll("</EM>", "</I>")
                .replaceAll("<em>", "<I>").replaceAll("</em>", "</I>");

        return text;
    }

}
