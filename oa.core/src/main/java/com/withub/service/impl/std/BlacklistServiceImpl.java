package com.withub.service.impl.std;

import com.withub.common.util.DateUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.event.BlacklistEventPublisher;
import com.withub.model.std.po.Blacklist;
import com.withub.model.system.enumeration.UserStatus;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.User;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.BlacklistService;
import com.withub.service.system.CodeService;
import com.withub.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("blacklistService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class BlacklistServiceImpl extends EntityServiceImpl implements BlacklistService {

    //===================== 属性声明 ===========================================================

    @Autowired
    private UserService userService;

    @Autowired
    private CodeService codeService;

    //===================== 接口实现 ===========================================================

    public RecordsetInfo queryBlacklist(QueryInfo queryInfo) throws Exception {

        RecordsetInfo recordsetInfo = query(queryInfo);

        return recordsetInfo;
    }

    public void putToBlacklist(String userId, String description) throws Exception {

        String hql = "select o from " + Blacklist.class.getName() + " o where o.objectStatus=1"
                + " and o.user.objectId=? and o.status=1";
        Blacklist blacklist = (Blacklist) getByHql(hql, userId);
        if (blacklist == null) {
            blacklist = new Blacklist();
        }

        User user = userService.getUserById(userId);
        blacklist.setUser(user);
        blacklist.setEnterTime(DateUtil.getCurrentTime());
        blacklist.setStatus(1);
        blacklist.setDescription(description);
        save(blacklist);

        Code status = codeService.getCodeByEnum(UserStatus.Blacklist);
        user.setStatus(status);
        save(user);

        BlacklistEventPublisher.publishBlacklistCreateEvent(this, blacklist);
    }

    public void removeFromBlacklist(String userId) throws Exception {

        String hql = "select o from " + Blacklist.class.getName() + " o where o.objectStatus=1"
                + " and o.user.objectId=? and o.status=1";
        Blacklist blacklist = (Blacklist) getByHql(hql, userId);
        blacklist.setStatus(0);
        save(blacklist);

        userService.activeUser(userId);

        BlacklistEventPublisher.publishBlacklistRemoveEvent(this, blacklist);
    }

    //===================== 属性方法 ===========================================================

    public UserService getUserService() {

        return userService;
    }

    public void setUserService(UserService userService) {

        this.userService = userService;
    }

    public CodeService getCodeService() {

        return codeService;
    }

    public void setCodeService(CodeService codeService) {

        this.codeService = codeService;
    }
}