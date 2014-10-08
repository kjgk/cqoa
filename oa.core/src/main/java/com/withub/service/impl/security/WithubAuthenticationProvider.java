package com.withub.service.impl.security;

import com.withub.model.security.LoginInfo;
import com.withub.service.security.SecurityEventPublisher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


public class WithubAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Authentication result = super.authenticate(authentication);

        if (result.isAuthenticated()) {
            SecurityEventPublisher.publishLoginEvent(this, (LoginInfo) authentication.getDetails());
        }

        return result;
    }
}
