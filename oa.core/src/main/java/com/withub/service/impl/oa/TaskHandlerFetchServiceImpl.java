package com.withub.service.impl.oa;

import com.withub.model.system.po.Organization;
import com.withub.model.system.po.User;
import com.withub.model.workflow.WFArguments;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.TaskHandlerFetchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("taskHandlerFetchService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class TaskHandlerFetchServiceImpl extends EntityServiceImpl implements TaskHandlerFetchService {

    public List<User> fetchOrganizationManager(WFArguments wfArguments) throws Exception {

        Organization organization = wfArguments.getInstance().getCreator().getOrganization();

        String hql = "select o from " + User.class.getName() + " o where 1=1 and o.objectStatus=1"
                + " and o.organization.objectId=? and o.role.roleTag=?";

        List<User> handlerList = (List) listByHql(hql, organization.getObjectId(), "Manager");

        return handlerList;
    }

}
