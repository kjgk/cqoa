package com.withub.service.impl.security;

import com.withub.model.system.po.Account;
import com.withub.service.system.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {

        UserDetails userDetails = null;

        try {
            Account account = accountService.getAccountByName(accountName);
            if (account != null) {
                userDetails = new UserDetailsImpl(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userDetails;
    }

    public AccountService getAccountService() {

        return accountService;
    }

    public void setAccountService(AccountService accountService) {

        this.accountService = accountService;
    }
}
