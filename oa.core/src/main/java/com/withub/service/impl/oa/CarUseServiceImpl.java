package com.withub.service.impl.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.CarUse;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.CarUseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("carUseUseService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class CarUseServiceImpl extends EntityServiceImpl implements CarUseService {

    public CarUse getCarUse(String objectId) throws Exception {

        return get(CarUse.class, objectId);
    }

    public RecordsetInfo<CarUse> queryCarUse(QueryInfo queryInfo) throws Exception {

        return query(queryInfo);
    }

    public void saveCarUse(CarUse carUse) throws Exception {

        save(carUse);
    }

    public void deleteCarUse(String objectId) throws Exception {

        logicDelete(CarUse.class, objectId);
    }

}
