package com.withub.model.std.po;

import com.withub.model.entity.AbstractEntity;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "STD_CUSTOMPAGEITEM")
public class CustomPageItem extends AbstractEntity {

    private String name;

    private Integer width;

    private Integer height;

    @Column(name = "positiontop")
    private Integer top;

    @Column(name = "positionleft")
    private Integer left;

    private String page;

    private String pageData;

    @ManyToOne(targetEntity = CustomPage.class)
    @JoinColumn(name = "customPageId")
    @JsonIgnore
    private CustomPage customPage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public String getPageData() {
        return pageData;
    }

    public void setPageData(String pageData) {
        this.pageData = pageData;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public CustomPage getCustomPage() {
        return customPage;
    }

    public void setCustomPage(CustomPage customPage) {
        this.customPage = customPage;
    }
}
