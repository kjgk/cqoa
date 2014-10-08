package com.withub.service.impl.security;

import com.withub.common.util.DateUtil;
import com.withub.model.system.enumeration.AccountStatus;
import com.withub.model.system.po.Account;
import com.withub.service.security.WithubUserDetails;
import com.withub.service.system.AccountService;
import com.withub.util.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("userDetails")
public class UserDetailsImpl implements WithubUserDetails {

    @Autowired
    private AccountService accountService;

    private Account account;

    public UserDetailsImpl() {

    }

    public UserDetailsImpl(Account account) {

        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        list.add(grantedAuthority);
        if (this.account.getUser().getAdministrator() == 1) {
            grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
            list.add(grantedAuthority);
        }

        return list;

    }

    @Override
    public String getPassword() {

        return this.account.getPassword();
    }

    @Override
    public String getUsername() {

        return this.account.getName();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return this.account.getAccountStatus() != AccountStatus.Locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }

    public String getAccountId() {

        return this.account.getObjectId();
    }

    public String getSalt() {

        return this.account.getSalt();
    }

    public boolean forceChangePassword() {

        if (!ConfigUtil.getSecurityConfigInfo().isForceChangePassword()) {
            return false;
        }

        if (!account.getLastEditor().getObjectId().equals(account.getUser().getObjectId())) {
            return true;
        }

        if (DateUtil.getDiffDays(account.getPasswordTime(), DateUtil.getCurrentTime()) >= ConfigUtil.getSecurityConfigInfo().getPasswordIntervalDays()) {
            return true;
        }

        return false;
    }

    public AccountService getAccountService() {

        return accountService;
    }

    public void setAccountService(AccountService accountService) {

        this.accountService = accountService;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserDetailsImpl that = (UserDetailsImpl) o;

        if (!this.account.getObjectId().equals(that.account.getObjectId())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {

        return this.account.getObjectId().hashCode();
    }
}
