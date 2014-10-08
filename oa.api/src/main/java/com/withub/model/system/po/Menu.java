package com.withub.model.system.po;

import com.withub.model.entity.AbstractRecursionEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_MENU")
public class Menu extends AbstractRecursionEntity {

    //================================ 属性声明 ===========================================================

    private String subMenuMethod;

    private Integer permission;

    private String url;

    private String image;

    private Integer expand;

    private Integer openMode;

    private Integer visible;

    private Integer requiredLogin;

    @ManyToOne(targetEntity = Menu.class)
    @JoinColumn(name = "parentId")
    private Menu parent;

    @OneToMany(targetEntity = Menu.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<Menu> childList = new ArrayList<Menu>();

    //=============================== 属性方法 ============================================================

    public String getSubMenuMethod() {

        return subMenuMethod;
    }

    public void setSubMenuMethod(String subMenuMethod) {

        this.subMenuMethod = subMenuMethod;
    }

    public Integer getPermission() {

        return permission;
    }

    public void setPermission(Integer permission) {

        this.permission = permission;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {

        this.image = image;
    }

    public Integer getExpand() {

        return expand;
    }

    public void setExpand(Integer expand) {

        this.expand = expand;
    }

    public Integer getOpenMode() {

        return openMode;
    }

    public void setOpenMode(Integer openMode) {

        this.openMode = openMode;
    }

    public Integer getVisible() {

        return visible;
    }

    public void setVisible(Integer visible) {

        this.visible = visible;
    }

    public Integer getRequiredLogin() {

        return requiredLogin;
    }

    public void setRequiredLogin(Integer requiredLogin) {

        this.requiredLogin = requiredLogin;
    }

    public Menu getParent() {

        return parent;
    }

    public void setParent(Menu parent) {

        this.parent = parent;
    }

    public List<Menu> getChildList() {

        return childList;
    }

    public void setChildList(List<Menu> childList) {

        this.childList = childList;
    }
}