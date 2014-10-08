package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Entity;
import com.withub.model.system.po.User;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "STD_FILEDOWNLOAD")
public class FileDownload extends AbstractBaseEntity {

    //============================== 属性声明 =============================================================

    @OneToOne(targetEntity = com.withub.model.system.po.Entity.class)
    @JoinColumn(name = "EntityId")
    private com.withub.model.system.po.Entity entity;

    @OneToOne(targetEntity = FileInfo.class)
    @JoinColumn(name = "FileInfoId")
    private FileInfo fileInfo;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "UserId")
    private User user;

    private String clientIp;

    private Date downloadTime;

    //============================== 属性方法 =============================================================

    public Entity getEntity() {

        return entity;
    }

    public void setEntity(Entity entity) {

        this.entity = entity;
    }

    public FileInfo getFileInfo() {

        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {

        this.fileInfo = fileInfo;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }

    public String getClientIp() {

        return clientIp;
    }

    public void setClientIp(String clientIp) {

        this.clientIp = clientIp;
    }

    public Date getDownloadTime() {

        return downloadTime;
    }

    public void setDownloadTime(Date downloadTime) {

        this.downloadTime = downloadTime;
    }
}
