package com.withub.util;

import com.withub.common.util.CollectionUtil;
import com.withub.model.system.po.Account;
import com.withub.model.system.po.User;
import com.withub.service.EntityService;
import com.withub.service.security.WithubUserDetails;
import com.withub.spring.SpringContextUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

import java.util.List;

/**
 * Spring Security 工具类.
 */
public final class SpringSecurityUtil {

    /**
     * 获取当前用户的帐号ID
     *
     * @return String
     * TODO : 应该监控此方法,捕获主调方法
     */
    public static String getAccountId() {

        String accountId = "";

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            WithubUserDetails userDetails = (WithubUserDetails) authentication.getPrincipal();
            accountId = userDetails.getAccountId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accountId;
    }

    public static User getCurrentUser() throws Exception {

        EntityService entityService = (EntityService) SpringContextUtil.getInstance().getBean("entityService");
        Account account = entityService.get(Account.class, getAccountId());
        User user = null;
        if (account != null) {
            user = account.getUser();
        }
        return user;
    }

    public static SessionInformation getAccountSessionInformation(final String accountId) throws Exception {

        SessionInformation sessionInformation = null;
        SessionRegistry sessionRegistry = (SessionRegistry) SpringContextUtil.getInstance().getBean("sessionRegistry");
        List<Object> principalList = sessionRegistry.getAllPrincipals();
        if (CollectionUtil.isEmpty(principalList)) {
            return null;
        }
        for (Object principal : principalList) {
            WithubUserDetails withubUserDetails = (WithubUserDetails) principal;
            if (withubUserDetails.getAccountId().equals(accountId)) {
                sessionInformation = sessionRegistry.getAllSessions(principal, false).get(0);
                break;
            }
        }

        return sessionInformation;
    }
}
