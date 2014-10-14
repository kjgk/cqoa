package com.withub.model.oa.po;

import com.withub.model.entity.AbstractBaseEntity;

import javax.persistence.Table;
import java.util.Date;

@javax.persistence.Entity
@Table(name = "OA_CAR")
public class Car extends AbstractBaseEntity {

    //================================ 属性声明 ==========================================================

    private String name;

    //   CARTYPE

    private String license;

    private Integer capacity;

    private Date purchaseDate;

    private Integer status;

    private String description;

    //================================ 属性方法 ==========================================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}