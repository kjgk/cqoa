package com.withub.service.impl.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.exception.BaseBusinessException;
import com.withub.model.std.enumeration.AgencyStatus;
import com.withub.model.std.event.agencyevent.AgencyEventArgs;
import com.withub.model.std.event.agencyevent.AgencyEventPublisher;
import com.withub.model.std.po.Agency;
import com.withub.model.system.po.Code;
import com.withub.model.system.po.User;
import com.withub.service.EntityServiceImpl;
import com.withub.service.std.AgencyService;
import com.withub.service.system.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("agencyService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class AgencyServiceImpl extends EntityServiceImpl implements AgencyService {

    //=================== 属性声明 =========================================================

    @Autowired
    private CodeService codeService;

    //=================== 接口实现 =========================================================

    public RecordsetInfo queryAgency(QueryInfo queryInfo) throws Exception {

        RecordsetInfo recordsetInfo = query(queryInfo);

        return recordsetInfo;
    }

    /**
     * 添加代理
     *
     * @param agency 代理
     */
    public void add(Agency agency) throws Exception {

        // 判断是否有代理
        String hql = "select o from " + Agency.class.getName() + " o where o.objectStatus=1"
                + " and o.owner.objectId=? and o.status.codeTag=?";
        Agency old = (Agency) getByHql(hql, agency.getOwner().getObjectId(), AgencyStatus.Running.name());
        if (old != null) {
            throw new BaseBusinessException("", "委托人已经设置代理人:[ " + old.getAgent().getName() + " ].");
        }

        // 判断是否会形成循环代理
        if (isCirculateAgent(agency.getAgent(), agency.getOwner())) {
            List<Agency> list = listAgencyChain(agency.getAgent());
            String chain = "";
            for (Agency temp : list) {
                chain += temp.getOwner().getName() + "->";
            }
            User user = (User) get(User.class, agency.getOwner().getObjectId());
            chain += user.getName();
            throw new BaseBusinessException("", "代理设置出现循环:[ " + chain + " ].");
        }

        agency.setObjectId(StringUtil.getUuid());
        // agency.setStartTime(DateUtil.getCurrentTime());
        Code agencyRunningStatus = codeService.getCodeByEnum(AgencyStatus.Running);
        agency.setStatus(agencyRunningStatus);
        save(agency);

        // 发布代理创建事件
        AgencyEventArgs agencyEventArgs = new AgencyEventArgs();
        agencyEventArgs.setAgency(agency);
        AgencyEventPublisher.publishAgencyCreatedEvent(this, agencyEventArgs);
    }

    /**
     * 结束代理
     *
     * @param agency 代理
     */
    public void finish(Agency agency) throws Exception {

        Code agencyFinishStatus = codeService.getCodeByEnum(AgencyStatus.Finish);
        agency.setStatus(agencyFinishStatus);
        agency.setEndTime(DateUtil.getCurrentTime());
        save(agency);

        // 发布代理结束事件
        AgencyEventArgs agencyEventArgs = new AgencyEventArgs();
        agencyEventArgs.setAgency(agency);
        AgencyEventPublisher.publishAgencyFinishedEvent(this, agencyEventArgs);
    }

    /**
     * 获取委托人的直接代理
     *
     * @param owner 委托人
     * @return Agency
     */
    public Agency getCurrentAgency(User owner) throws Exception {

        String hql = "select o from " + Agency.class.getName() + " o where o.objectStatus=1"
                + " and o.owner.objectId=? and o.status.codeTag=?";
        Agency agency = (Agency) getByHql(hql, owner.getObjectId(), AgencyStatus.Running.name());

        return agency;
    }

    /**
     * 获取委托人的代理链
     *
     * @param owner 委托人
     * @return List<Agency>
     */
    public List<Agency> listAgencyChain(User owner) throws Exception {

        List<Agency> agencyChain = new ArrayList<Agency>();
        Agency agency = getCurrentAgency(owner);

        if (agency == null) {
            return null;
        }

        while (agency != null) {
            agencyChain.add(agency);
            agency = getCurrentAgency(agency.getAgent());
        }

        return agencyChain;
    }

    /**
     * 获取委托人的当前代理人
     *
     * @param owner 委托人
     * @return User
     */
    public User getAgent(User owner) throws Exception {

        User agent = null;
        Agency agency = getCurrentAgency(owner);
        while (agency != null) {
            agent = agency.getAgent();
            agency = getCurrentAgency(agent);
        }
        return agent;
    }

    public void getOwner(User agent, List<User> ownerList) throws Exception {

        String hql = "select o from " + Agency.class.getName() + " o where o.objectStatus=1"
                + " and o.agent.objectId=? and o.status.codeTag=?";
        List list = listByHql(hql, agent.getObjectId(), AgencyStatus.Running.name());

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        for (Agency agency : (List<Agency>) list) {
            ownerList.add(agency.getOwner());
            getOwner(agency.getOwner(), ownerList);
        }
    }


    /**
     * 判断代理人和委托人是否循环
     *
     * @param owner 委托人
     * @param agent 代理人
     * @return boolean
     */
    public boolean isCirculateAgent(User owner, User agent) throws Exception {

        List<Agency> agencyChain = listAgencyChain(owner);

        if (CollectionUtil.isEmpty(agencyChain)) {
            return false;
        }

        for (Agency o : agencyChain) {
            if (StringUtil.compareValue(o.getOwner().getObjectId(), owner.getObjectId())) {
                return true;
            }
        }

        return false;
    }

    //==================== 属性方法 ====================================================

    public CodeService getCodeService() {

        return codeService;
    }

    public void setCodeService(CodeService codeService) {

        this.codeService = codeService;
    }
}
