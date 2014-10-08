package com.withub.model.std.po;

import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.std.FileUploadInfo;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "STD_CUSTOMPAGE")
public class CustomPage extends AbstractBaseEntity {

    //=================== 属性声明 ============================================

    private String name;

    private String code;

    @ManyToOne(targetEntity = CustomPageCategory.class)
    @JoinColumn(name = "CUSTOMPAGECATEGORYID")
    private CustomPageCategory customPageCategory;

    @OneToMany(targetEntity = CustomPageItem.class, mappedBy = "customPage", fetch = FetchType.LAZY)
    private List<CustomPageItem> customPageItemList;

    private String description;

    private Integer orderNo;

    @Transient
    private FileUploadInfo backgroundImageInfo;

    //=================== 属性方法 ============================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CustomPageCategory getCustomPageCategory() {
        return customPageCategory;
    }

    public void setCustomPageCategory(CustomPageCategory customPageCategory) {
        this.customPageCategory = customPageCategory;
    }

    public List<CustomPageItem> getCustomPageItemList() {
        return customPageItemList;
    }

    public void setCustomPageItemList(List<CustomPageItem> customPageItemList) {
        this.customPageItemList = customPageItemList;
    }

    public FileUploadInfo getBackgroundImageInfo() {
        return backgroundImageInfo;
    }

    public void setBackgroundImageInfo(FileUploadInfo backgroundImageInfo) {
        this.backgroundImageInfo = backgroundImageInfo;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
