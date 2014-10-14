package com.withub.web.controller.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.oa.po.Car;
import com.withub.service.oa.CarService;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(value = "/oa")
public class CarController extends BaseController {

    @Autowired
    private CarService carService;

    @RequestMapping(value = "/car", method = RequestMethod.GET)
    public void queryCar(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Car.class);
        setPageInfoQueryCondition(request, queryInfo);

        putRecordsetInfo(modelMap, carService.queryCar(queryInfo));
    }

    @RequestMapping(value = "/car/{objectId}", method = RequestMethod.GET)
    public void getCar(@PathVariable("objectId") String objectId, ModelMap modelMap) throws Exception {

        Car car = carService.getCar(objectId);
        putData(modelMap, car);
    }

    @RequestMapping(value = "/car", method = RequestMethod.POST)
    public void createCar(@RequestBody Car car) throws Exception {

        carService.saveCar(car);
    }

    @RequestMapping(value = "/car", method = RequestMethod.PUT)
    public void updateCar(@RequestBody Car car) throws Exception {

        carService.saveCar(car);
    }


    @RequestMapping(value = "/car/{objectId}", method = RequestMethod.DELETE)
    public void deleteCar(@PathVariable("objectId") String objectId) throws Exception {

        carService.deleteCar(objectId);
    }

}
