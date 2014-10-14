package com.withub.service.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.CarUse;

public interface CarUseService {

    public CarUse getCarUse(String objectId) throws Exception;

    public RecordsetInfo<CarUse> queryCarUse(QueryInfo queryInfo) throws Exception;

    public void saveCarUse(CarUse carUse) throws Exception;

    public void deleteCarUse(String objectId) throws Exception;

}
