package com.withub.service.impl.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Car;
import com.withub.service.EntityServiceImpl;
import com.withub.service.oa.CarService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("carService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class CarServiceImpl extends EntityServiceImpl implements CarService {

    public Car getCar(String objectId) throws Exception {

        return get(Car.class, objectId);
    }

    public RecordsetInfo<Car> queryCar(QueryInfo queryInfo) throws Exception {

        return query(queryInfo);
    }

    public void saveCar(Car car) throws Exception {

        save(car);
    }

    public void deleteCar(String objectId) throws Exception {

        logicDelete(Car.class, objectId);
    }

}
