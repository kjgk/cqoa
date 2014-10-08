package com.withub.service.impl.system;

import com.withub.common.util.StringUtil;
import com.withub.model.system.po.Organization;
import com.withub.service.system.OrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("organizationService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class OrganizationServiceImpl extends com.withub.service.EntityServiceImpl implements OrganizationService {

    public Organization getRootOrganization() throws Exception {

        return getRootEntity(Organization.class);
    }

    public void addOrganization(Organization organization) throws Exception {

        Organization parent = get(Organization.class, organization.getParent().getObjectId());
        String fullName = organization.getName();
        if (parent != null) {
            Organization temp = parent;
            while (temp.getNodeLevel() > 2) {
                if (StringUtil.isEmpty(fullName)) {
                    fullName = temp.getName();
                } else {
                    fullName = temp.getName() + fullName;
                }
                temp = temp.getParent();
            }
        }
        organization.setFullName(fullName);
        save(organization);
    }

    public void updateOrganization(Organization organization) throws Exception {

        Organization parent = get(Organization.class, organization.getParent().getObjectId());
        organization.setParent(parent);

        String fullName = organization.getName();
        if (parent != null) {
            Organization temp = parent;
            while (temp.getNodeLevel() > 2) {
                if (StringUtil.isEmpty(fullName)) {
                    fullName = temp.getName();
                } else {
                    fullName = temp.getName() + fullName;
                }
                temp = temp.getParent();
            }
        }
        organization.setFullName(fullName);
        save(organization);

        String hql = "select o from " + Organization.class.getName() + " o where o.nodeLevelCode like '" + organization.getNodeLevelCode() + "%'";
        List list = listByHql(hql);
        for (Organization child : (List<Organization>) list) {
            if (!StringUtil.compareValue(child.getObjectId(), organization.getObjectId())) {
                Organization temp = child;
                fullName = "";
                while (temp.getNodeLevel() > 2) {
                    fullName = temp.getName() + fullName;
                    temp = temp.getParent();
                }
                child.setFullName(fullName);
                child.setCurrentUser(organization.getCurrentUser());
                save(child);
            }
        }
    }

}