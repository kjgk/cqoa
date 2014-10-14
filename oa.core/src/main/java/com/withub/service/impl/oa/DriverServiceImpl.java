package com.withub.service.impl.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Driver;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.DriverService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("driverService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class DriverServiceImpl extends EntityServiceImpl implements DriverService {

    public Driver getDriver(String objectId) throws Exception {

        return get(Driver.class, objectId);
    }

    public RecordsetInfo<Driver> queryDriver(QueryInfo queryInfo) throws Exception {

        return query(queryInfo);
    }

    public void saveDriver(Driver driver) throws Exception {

        save(driver);
    }

    public void deleteDriver(String objectId) throws Exception {

        logicDelete(Driver.class, objectId);
    }

}
