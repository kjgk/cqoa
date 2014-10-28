package com.withub.service.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.CarUse;
import com.withub.model.oa.po.CarUseInfo;
import com.withub.service.EntityService;

public interface CarUseService extends EntityService {

    public CarUse getCarUse(String objectId) throws Exception;

    public RecordsetInfo<CarUse> queryCarUse(QueryInfo queryInfo) throws Exception;

    public void deleteCarUse(String objectId) throws Exception;

    public void submitCarUse(CarUse carUse) throws Exception;

    public void addCarUse(CarUse carUse) throws Exception;

    public void updateCarUse(CarUse carUse) throws Exception;

    public void addCarUseInfo(CarUseInfo carUseInfo) throws Exception;

}
