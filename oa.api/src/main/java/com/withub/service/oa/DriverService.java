package com.withub.service.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Driver;
import com.withub.model.oa.po.MeetingRoom;

public interface DriverService {

    public Driver getDriver(String objectId) throws Exception;

    public RecordsetInfo<Driver> queryDriver(QueryInfo queryInfo) throws Exception;

    public void saveDriver(Driver driver) throws Exception;

    public void deleteDriver(String objectId) throws Exception;

}
