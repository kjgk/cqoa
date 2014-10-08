package com.withub.model.system.po;

import com.withub.model.entity.AbstractRecursionEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_ORGANIZATION")
public class Organization extends AbstractRecursionEntity {

    //================================= 属性声明 =========================================================

    private String code;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "organizationTypeId")
    private Code organizationType;

    private String address;

    private String contact;

    private String phone;

    @ManyToOne(targetEntity = Organization.class)
    @JoinColumn(name = "parentId")
    private Organization parent;

    @OneToMany(targetEntity = Organization.class, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    @Where(clause = "objectStatus = 1")
    private List<Organization> childList = new ArrayList<Organization>();

    @OneToMany(targetEntity = UserOrganizationRole.class, mappedBy = "organization", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo asc")
    private List<UserOrganizationRole> roleUserList = new ArrayList<UserOrganizationRole>();

    //================================= 属性方法 =========================================================

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public Code getOrganizationType() {

        return organizationType;
    }

    public void setOrganizationType(Code organizationType) {

        this.organizationType = organizationType;
    }

    public String getContact() {

        return contact;
    }

    public void setContact(String contact) {

        this.contact = contact;
    }

    public String getPhone() {

        return phone;
    }

    public void setPhone(String phone) {

        this.phone = phone;
    }

    public Organization getParent() {

        return parent;
    }

    public void setParent(Organization parent) {

        this.parent = parent;
    }

    public List<Organization> getChildList() {

        return childList;
    }

    public void setChildList(List<Organization> childList) {

        this.childList = childList;
    }

    public List<UserOrganizationRole> getRoleUserList() {

        return roleUserList;
    }

    public void setRoleUserList(List<UserOrganizationRole> roleUserList) {

        this.roleUserList = roleUserList;
    }
}
