package com.withub.service.impl.system;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.common.util.Md5Util;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.exception.BaseBusinessException;
import com.withub.model.system.enumeration.AccountStatus;
import com.withub.model.system.enumeration.AccountType;
import com.withub.model.system.enumeration.PasswordChangeResult;
import com.withub.model.system.event.AccountEventArgs;
import com.withub.model.system.event.SystemEventPublisher;
import com.withub.model.system.po.Account;
import com.withub.model.system.po.PasswordHistory;
import com.withub.model.system.po.User;
import com.withub.service.EntityServiceImpl;
import com.withub.service.system.AccountService;
import com.withub.util.ConfigUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("accountService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class AccountServiceImpl extends EntityServiceImpl implements AccountService {

    //=========================== 接口实现 ================================================================

    public Account getAccountByName(final String accountName) throws Exception {

        String hql = "select o from " + Account.class.getName() + " o where o.objectStatus=1"
                + " and lower(o.name)=?";
        Account account = (Account) getByHql(hql, accountName.trim().toLowerCase());

        return account;
    }

    public List<PasswordHistory> listPasswordHistoryByAccountId(final String accountId) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(PasswordHistory.class);
        queryInfo.setTopRowCount(ConfigUtil.getSecurityConfigInfo().getPasswordRepeatHistory());
        List list = list(queryInfo);

        if (CollectionUtil.isEmpty(list)) {
            return null;
        }

        return (List<PasswordHistory>) list;
    }

    public String encryptPassword(final String salt, final String password) {

        String md5Password = Md5Util.getStringMD5(salt + password);
        return md5Password.toUpperCase();
    }

    public PasswordChangeResult changePassword(final String accountId, String accountName, final String oldPassword, final String newPassword) throws Exception {

        Account account = get(Account.class, accountId);

        if (StringUtil.isEmpty(accountName)) {
            accountName = account.getName();
        }

        String salt = account.getSalt();
        String encryptedPassword = encryptPassword(salt, oldPassword);

        if (!StringUtil.compareValue(encryptedPassword, account.getPassword())) {
            return PasswordChangeResult.OldPasswordError;
        }

        encryptedPassword = encryptPassword(salt, newPassword);
        List<PasswordHistory> passwordHistoryList = listPasswordHistoryByAccountId(accountId);
        if (CollectionUtil.isNotEmpty(passwordHistoryList)) {
            for (PasswordHistory passwordHistory : passwordHistoryList) {
                if (encryptedPassword.equals(passwordHistory.getPassword())) {
                    throw new BaseBusinessException("", String.format("新密码必须与最近 %s 次的历史密码不同.",
                            ConfigUtil.getSecurityConfigInfo().getPasswordRepeatHistory()));
                }
            }
        }

        account.setPassword(encryptedPassword);

        if (!account.getName().equalsIgnoreCase(accountName.trim())) {
            Account temp = getAccountByName(accountName.trim());
            if (temp != null) {
                throw new BaseBusinessException("", "帐号[" + accountName.trim() + "]已经被其他用户使用!");
            }
            account.setName(accountName.trim());
        }

        account.setCurrentUser(account.getUser());
        account.setPasswordTime(DateUtil.getCurrentTime());
        save(account);

        PasswordHistory passwordHistory = new PasswordHistory();
        passwordHistory.setAccountId(accountId);
        passwordHistory.setPasswordTime(account.getPasswordTime());
        passwordHistory.setPassword(encryptedPassword);
        passwordHistory.setCurrentUser(account.getUser());
        save(passwordHistory);

        return PasswordChangeResult.Success;
    }

    public void saveAccount(Account account) throws Exception {

        String password = account.getPassword();

        boolean passwordChanged = false;
        String encryptedPassword = "";

        account.setName(account.getName().trim());
        if (StringUtil.isEmpty(account.getObjectId())) {
            account.setObjectId(StringUtil.getUuid());
            account.setSalt(StringUtil.getUuid());
            encryptedPassword = encryptPassword(account.getSalt(), account.getPassword());
            account.setPassword(encryptedPassword);
            account.setAccountStatus(AccountStatus.Normal);
            account.setAccountType(AccountType.Normal);
            passwordChanged = true;
        } else {
            Account temp = get(Account.class, account.getObjectId());
            account.setAccountStatus(temp.getAccountStatus());
            account.setAccountType(temp.getAccountType());
            account.setSalt(temp.getSalt());
            if (StringUtil.isEmpty(account.getPassword())) {
                account.setPassword(temp.getPassword());
                account.setPasswordTime(temp.getPasswordTime());
            } else {
                encryptedPassword = encryptPassword(account.getSalt(), account.getPassword());
                account.setPassword(encryptedPassword);
                passwordChanged = true;

            }
        }
        if (passwordChanged) {
            account.setPasswordTime(DateUtil.getCurrentTime());
            PasswordHistory passwordHistory = new PasswordHistory();
            passwordHistory.setAccountId(account.getObjectId());
            passwordHistory.setPasswordTime(account.getPasswordTime());
            passwordHistory.setPassword(encryptedPassword);
            passwordHistory.setCurrentUser(account.getCurrentUser());
            save(passwordHistory);
        }

        save(account);

        if (passwordChanged) {
            // 抛出密码设置事件
            account = get(Account.class, account.getObjectId());
            AccountEventArgs accountEventArgs = new AccountEventArgs();
            accountEventArgs.setAccount(account);
            accountEventArgs.setPassword(password);
            SystemEventPublisher.publishPasswordSetEvent(this, accountEventArgs);
        }
    }

    @Override
    public void createRandomPassword(Account account) throws Exception {

        String encryptedPassword = "";
        StringBuilder randomPassword = new StringBuilder();

        int passwordLength = ConfigUtil.getSecurityConfigInfo().getPasswordLength();

        for (int i = 0; i < passwordLength; i++) {
            //  ASCII码 33 ～ 126
            int keyCode = (int) (Math.random() * 93 + 33);
            randomPassword.append(Character.toChars(keyCode));
        }

        encryptedPassword = encryptPassword(account.getSalt(), randomPassword.toString());
        account.setPassword(encryptedPassword);
        account.setPasswordTime(DateUtil.getCurrentTime());

        PasswordHistory passwordHistory = new PasswordHistory();
        passwordHistory.setAccountId(account.getObjectId());
        passwordHistory.setPasswordTime(account.getPasswordTime());
        passwordHistory.setPassword(encryptedPassword);
        passwordHistory.setCurrentUser(account.getCurrentUser());
        passwordHistory.setCreator(account.getCurrentUser());
        save(passwordHistory);

        save(account);

        // 抛出密码设置事件
        AccountEventArgs accountEventArgs = new AccountEventArgs();
        accountEventArgs.setAccount(account);
        accountEventArgs.setPassword(randomPassword.toString());
        SystemEventPublisher.publishPasswordSetEvent(this, accountEventArgs);

    }


    @Cacheable(value = "findAllUserCache")
    public List searchAllUser() throws Exception {

        String hql = "from " + User.class.getName();
        return listByHql(hql);
    }
}
