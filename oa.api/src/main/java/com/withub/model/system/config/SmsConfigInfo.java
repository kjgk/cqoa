package com.withub.model.system.config;

/**
 * SMS 配置信息
 */
public final class SmsConfigInfo extends AbstractBaseConfigInfo {

    private static final long serialVersionUID = 1L;

    //================= 属性声明 ==============================================

    /**
     * 启用SMS
     */
    private boolean enableSms;

    /**
     * COM端口
     */
    private String port = "COM1";

    /**
     * 比特率,默认为9600
     */
    private Integer baudrate = 9600;

    /**
     * 制造商
     */
    private String manufacturer;

    /**
     * 型号
     */
    private String model;

    /**
     * 协议,默认为 protocol
     */
    private String protocol = "PDU";

    //================== 属性 方法 ===========================================

    public static long getSerialVersionUID() {

        return serialVersionUID;
    }

    public boolean isEnableSms() {

        return enableSms;
    }

    public void setEnableSms(boolean enableSms) {

        this.enableSms = enableSms;
    }

    public String getPort() {

        return port;
    }

    public void setPort(String port) {

        this.port = port;
    }

    public Integer getBaudrate() {

        return baudrate;
    }

    public void setBaudrate(Integer baudrate) {

        this.baudrate = baudrate;
    }

    public String getManufacturer() {

        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {

        this.manufacturer = manufacturer;
    }

    public String getModel() {

        return model;
    }

    public void setModel(String model) {

        this.model = model;
    }

    public String getProtocol() {

        return protocol;
    }

    public void setProtocol(String protocol) {

        this.protocol = protocol;
    }
}
