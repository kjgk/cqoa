package com.withub.service.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.oa.po.Car;

public interface CarService {

    public Car getCar(String objectId) throws Exception;

    public RecordsetInfo<Car> queryCar(QueryInfo queryInfo) throws Exception;

    public void saveCar(Car car) throws Exception;

    public void deleteCar(String objectId) throws Exception;

}
