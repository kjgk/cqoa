package com.withub.common.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 各种格式的编码加码工具类.
 */
public final class EncodeUtil {

    /**
     * 默认编码
     */
    public static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Hex编码.
     */
    public static String hexEncode(final byte[] input) {
        return Hex.encodeHexString(input);
    }

    /**
     * Hex解码.
     */
    public static byte[] hexDecode(final String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            throw new IllegalStateException("Hex Decoder exception", e);
        }
    }

    /**
     * Base64编码.
     */
    public static String base64Encode(final byte[] input) {
        return Base64.encodeBase64String(input);
    }

    /**
     * Base64解码.
     */
    public static String decodeBase64(final String base64String) {

        byte[] byteArray = Base64.decodeBase64(base64String);
        String str = new String(byteArray);
        return str;
    }

    public static String base64Encode(final String input) {

        byte[] bytes = input.getBytes();
        return Base64.encodeBase64String(bytes);
    }

    /**
     * Base64编码, URL安全(对URL不支持的字符如+,/,=转为其他字符, 见RFC3548).
     */
    public static String base64UrlEncode(final byte[] input) {
        return Base64.encodeBase64URLSafeString(input);
    }

    /**
     * Base64解码.
     */
    public static byte[] base64Decode(final String input) {
        return Base64.decodeBase64(input);
    }

    /**
     * URL 编码, Encode默认为UTF-8.
     */
    public static String urlEncode(final String input) {
        return urlEncode(input, DEFAULT_ENCODING);
    }

    /**
     * URL 编码.
     */
    public static String urlEncode(final String input, final String encoding) {
        try {
            return URLEncoder.encode(input, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unsupported Encoding Exception", e);
        }
    }

    /**
     * URL 解码, Encode默认为UTF-8.
     */
    public static String urlDecode(final String input) {
        return urlDecode(input, DEFAULT_ENCODING);
    }

    /**
     * URL 解码.
     */
    public static String urlDecode(String input, String encoding) {
        try {
            return URLDecoder.decode(input, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unsupported Encoding Exception", e);
        }
    }

    /**
     * Html转码.
     */
    public static String htmlEscape(final String html) {
        return StringEscapeUtils.escapeHtml(html);
    }

    /**
     * Html 反转码.
     */
    public static String htmlUnescape(final String htmlEscaped) {
        return StringEscapeUtils.unescapeHtml(htmlEscaped);
    }

    /**
     * Xml转码.
     */
    public static String xmlEscape(final String xml) {
        return StringEscapeUtils.escapeXml(xml);
    }

    /**
     * Xml 反转码.
     */
    public static String xtmlUnescape(final String xmlEscaped) {
        return StringEscapeUtils.unescapeXml(xmlEscaped);
    }

}
