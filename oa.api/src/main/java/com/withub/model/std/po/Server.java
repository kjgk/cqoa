package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.system.po.Code;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "STD_SERVER")
public class Server extends AbstractBaseEntity {

    //======================== 属性声明 ==============================

    private String name;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "OperatingSystem")
    private Code operatingSystem;

    private String version;

    private String ip;

    private String domain;

    private String username;

    private String password;

    private Integer localhost;

    private String description;

    private Integer orderNo;

    //======================== 属性方法 ================================


    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Code getOperatingSystem() {

        return operatingSystem;
    }

    public void setOperatingSystem(Code operatingSystem) {

        this.operatingSystem = operatingSystem;
    }

    public String getVersion() {

        return version;
    }

    public void setVersion(String version) {

        this.version = version;
    }

    public String getIp() {

        return ip;
    }

    public void setIp(String ip) {

        this.ip = ip;
    }

    public String getDomain() {

        return domain;
    }

    public void setDomain(String domain) {

        this.domain = domain;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public Integer getLocalhost() {

        return localhost;
    }

    public void setLocalhost(Integer localhost) {

        this.localhost = localhost;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public Integer getOrderNo() {

        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {

        this.orderNo = orderNo;
    }
}
