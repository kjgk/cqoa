package com.withub.service.system;

import com.withub.model.system.enumeration.PasswordChangeResult;
import com.withub.model.system.po.Account;
import com.withub.model.system.po.PasswordHistory;
import com.withub.service.EntityService;

import java.util.List;


public interface AccountService extends EntityService {

    public Account getAccountByName(final String accountName) throws Exception;

    public List<PasswordHistory> listPasswordHistoryByAccountId(final String accountId) throws Exception;

    public String encryptPassword(final String salt, final String password);

    public PasswordChangeResult changePassword(final String accountId, final String accountName, final String oldPassword, final String newPassword) throws Exception;

    public void saveAccount(Account account) throws Exception;

    public void createRandomPassword(Account account) throws Exception;

    public List searchAllUser() throws Exception;
}
