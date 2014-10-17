package com.withub.model.system.po;


import com.withub.model.entity.AbstractBaseEntity;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@javax.persistence.Entity
@Table(name = "SYS_USER")
public class User extends AbstractBaseEntity {

    //=============================== 属性声明 ============================================================

    @OneToOne(targetEntity = Account.class, mappedBy = "user")
    @JsonIgnore
    private Account account;

    private String code;

    private String name;

    private String pinYin;

    private Integer administrator = 0;

    @OneToOne(targetEntity = Organization.class)
    @JoinColumn(name = "OrganizationId")
    private Organization organization;

    @OneToOne(targetEntity = Role.class)
    @JoinColumn(name = "RoleId")
    private Role role;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "Sex")
    private Code sex;

    private Date birthday;

    private String idCard;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "Nation")
    private Code nation;

    private String email;

    private String mobile;

    private Date employmentDate;

    @OneToOne(targetEntity = Code.class)
    @JoinColumn(name = "Status")
    private Code status;

    @OneToMany(targetEntity = UserOrganizationRole.class, mappedBy = "user", fetch = FetchType.LAZY)
    @OrderBy(value = "orderNo")
    @JsonIgnore
    private List<UserOrganizationRole> organizationRoleList;

    //=============================== 属性方法 ============================================================

    public Account getAccount() {

        return account;
    }

    public void setAccount(Account account) {

        this.account = account;
    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getPinYin() {

        return pinYin;
    }

    public void setPinYin(String pinYin) {

        this.pinYin = pinYin;
    }

    public Integer getAdministrator() {

        return administrator;
    }

    public void setAdministrator(Integer administrator) {

        this.administrator = administrator;
    }

    public Organization getOrganization() {

        return organization;
    }

    public void setOrganization(Organization organization) {

        this.organization = organization;
    }

    public Role getRole() {

        return role;
    }

    public void setRole(Role role) {

        this.role = role;
    }

    public Code getSex() {

        return sex;
    }

    public void setSex(Code sex) {

        this.sex = sex;
    }

    public Date getBirthday() {

        return birthday;
    }

    public void setBirthday(Date birthday) {

        this.birthday = birthday;
    }

    public String getIdCard() {

        return idCard;
    }

    public void setIdCard(String idCard) {

        this.idCard = idCard;
    }

    public Code getNation() {

        return nation;
    }

    public void setNation(Code nation) {

        this.nation = nation;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getMobile() {

        return mobile;
    }

    public void setMobile(String mobile) {

        this.mobile = mobile;
    }

    public Date getEmploymentDate() {

        return employmentDate;
    }

    public void setEmploymentDate(Date employmentDate) {

        this.employmentDate = employmentDate;
    }

    public Code getStatus() {

        return status;
    }

    public void setStatus(Code status) {

        this.status = status;
    }

    public List<UserOrganizationRole> getOrganizationRoleList() {

        return organizationRoleList;
    }

    public void setOrganizationRoleList(List<UserOrganizationRole> organizationRoleList) {

        this.organizationRoleList = organizationRoleList;
    }
}