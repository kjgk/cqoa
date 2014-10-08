package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Entity;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "STD_FILECONFIG")
public class FileConfig extends AbstractBaseEntity {

    //============================== 属性声明 =============================================================

    @OneToOne(targetEntity = com.withub.model.system.po.Entity.class)
    @JoinColumn(name = "EntityId")
    private com.withub.model.system.po.Entity entity;

    @OneToOne(targetEntity = Server.class)
    @JoinColumn(name = "ServerId")
    private Server server;

    private Integer storageType;

    @OneToOne(targetEntity = com.withub.model.system.po.Entity.class)
    @JoinColumn(name = "FileEntityId")
    private com.withub.model.system.po.Entity fileEntity;

    private String serverPath;

    private Integer count = 0;

    private Integer fileSize = 0;

    private Integer overwrite = 0;

    private Integer traceDownload = 0;

    private Integer orderNo;

    //============================== 属性方法 =============================================================

    public Entity getEntity() {

        return entity;
    }

    public void setEntity(Entity entity) {

        this.entity = entity;
    }

    public Server getServer() {

        return server;
    }

    public void setServer(Server server) {

        this.server = server;
    }

    public Integer getStorageType() {

        return storageType;
    }

    public void setStorageType(Integer storageType) {

        this.storageType = storageType;
    }

    public Entity getFileEntity() {

        return fileEntity;
    }

    public void setFileEntity(Entity fileEntity) {

        this.fileEntity = fileEntity;
    }

    public String getServerPath() {

        return serverPath;
    }

    public void setServerPath(String serverPath) {

        this.serverPath = serverPath;
    }

    public Integer getCount() {

        return count;
    }

    public void setCount(Integer count) {

        this.count = count;
    }

    public Integer getFileSize() {

        return fileSize;
    }

    public void setFileSize(Integer fileSize) {

        this.fileSize = fileSize;
    }

    public Integer getOverwrite() {

        return overwrite;
    }

    public void setOverwrite(Integer overwrite) {

        this.overwrite = overwrite;
    }

    public Integer getTraceDownload() {

        return traceDownload;
    }

    public void setTraceDownload(Integer traceDownload) {

        this.traceDownload = traceDownload;
    }

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }
}
