package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "STD_APPVERSION")
public class AppVersion extends AbstractBaseEntity {

    //================================ 属性声明 ==========================================================

    // 平台：android or ios
    private String platform;

    private String version;

    private String description;

    private String attachmentAddress;

    // 状态：1代表启用，0代表未启用
    private Integer status;

    //================================ 属性方法 ==========================================================

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachmentAddress() {
        return attachmentAddress;
    }

    public void setAttachmentAddress(String attachmentAddress) {
        this.attachmentAddress = attachmentAddress;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}