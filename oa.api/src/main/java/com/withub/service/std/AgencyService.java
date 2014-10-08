package com.withub.service.std;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.po.Agency;
import com.withub.model.system.po.User;
import com.withub.service.EntityService;

import java.util.List;

public interface AgencyService extends EntityService {

    public RecordsetInfo queryAgency(QueryInfo queryInfo) throws Exception;

    /**
     * 添加代理
     *
     * @param agency 代理
     */
    public void add(Agency agency) throws Exception;

    /**
     * 结束代理
     *
     * @param agency 代理
     */
    public void finish(Agency agency) throws Exception;

    /**
     * 获取委托人的直接代理
     *
     * @param owner 委托人
     * @return Agency
     */
    public Agency getCurrentAgency(User owner) throws Exception;

    /**
     * 获取委托人的代理链
     *
     * @param owner 委托人
     * @return List<Agency>
     */
    public List<Agency> listAgencyChain(User owner) throws Exception;

    /**
     * 获取委托人的当前代理人
     *
     * @param owner 委托人
     * @return User
     */
    public User getAgent(User owner) throws Exception;

    public void getOwner(User agent, List<User> ownerList) throws Exception;

    /**
     * 判断代理人和委托人是否循环
     *
     * @param owner 委托人
     * @param agent 代理人
     * @return boolean
     */
    public boolean isCirculateAgent(User owner, User agent) throws Exception;
}
