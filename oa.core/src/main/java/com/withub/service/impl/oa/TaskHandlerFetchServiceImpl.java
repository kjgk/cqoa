package com.withub.service.impl.oa;

import com.withub.model.system.po.User;
import com.withub.model.workflow.WFArguments;
import com.withub.model.workflow.enumeration.FlowNodeType;
import com.withub.model.workflow.po.MasterTask;
import com.withub.model.workflow.po.SubInstance;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.TaskHandlerFetchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("taskHandlerFetchService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class TaskHandlerFetchServiceImpl extends EntityServiceImpl implements TaskHandlerFetchService {

    public List<User> fetchOrganizationManager() throws Exception {

        String hql = "select o from " + User.class.getName() + " o where 1=1 and o.objectStatus=1"
                + " and o.role.roleTag in (?, ?)" +
                " order by orderNo";

        List<User> handlerList = (List) listByHql(hql, "Manager", "DeputyManager");
        return handlerList;
    }

    public List<User> fetchOrganizationManager(WFArguments wfArguments) throws Exception {

        return fetchOrganizationManager(wfArguments.getInstance().getCreator().getOrganization().getCode());
    }


    public List<User> fetchFirstTaskHander(WFArguments wfArguments) throws Exception {

        List<User> userList = new ArrayList<>();
        List<SubInstance> subInstanceList = wfArguments.getInstance().getSubInstanceList();
        for (MasterTask masterTask : subInstanceList.get(subInstanceList.size() - 1).getMasterTaskList()) {
            if (masterTask.getFlowNode().getFlowNodeType() == FlowNodeType.AndSign) {
                userList.add(masterTask.getTaskList().get(0).getHandler());
                break;
            }
        }
        return userList;
    }


    public List<User> fetchOrganizationManager(String organizationCode) throws Exception {

        String hql = "select o from " + User.class.getName() + " o where 1=1 and o.objectStatus=1"
                + " and o.organization.code=? and o.role.roleTag in (?, ?)" +
                " order by orderNo";

        List<User> handlerList = (List) listByHql(hql
                , organizationCode, "Manager", "DeputyManager");
        return handlerList;
    }

    public List<User> fetchBoss() throws Exception {

        String hql = "select o from " + User.class.getName() + " o where 1=1 and o.objectStatus=1"
                + " and o.role.roleTag = ?" +
                " order by orderNo";

        List<User> handlerList = (List) listByHql(hql, "Boss");
        return handlerList;
    }

    public List<User> fetchLeader() throws Exception {

        String hql = "select o from " + User.class.getName() + " o where 1=1 and o.objectStatus=1"
                + " and o.organization.code = ?" +
                " order by orderNo";

        List<User> handlerList = (List) listByHql(hql, "100000000015000001");
        return handlerList;
    }
}
