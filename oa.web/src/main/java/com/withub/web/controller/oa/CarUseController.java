package com.withub.web.controller.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.oa.po.CarUse;
import com.withub.service.oa.CarUseService;
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
public class CarUseController extends BaseController {

    @Autowired
    private CarUseService carUseService;

    @RequestMapping(value = "/carUse", method = RequestMethod.GET)
    public void queryCarUse(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(CarUse.class);
        setPageInfoQueryCondition(request, queryInfo);

        putRecordsetInfo(modelMap, carUseService.queryCarUse(queryInfo));
    }

    @RequestMapping(value = "/carUse/{objectId}", method = RequestMethod.GET)
    public void getCarUse(@PathVariable("objectId") String objectId, ModelMap modelMap) throws Exception {

        CarUse carUse = carUseService.getCarUse(objectId);
        putData(modelMap, carUse);
    }

    @RequestMapping(value = "/carUse", method = RequestMethod.POST)
    public void createCarUse(@RequestBody CarUse carUse) throws Exception {

        carUseService.saveCarUse(carUse);
    }

    @RequestMapping(value = "/carUse", method = RequestMethod.PUT)
    public void updateCarUse(@RequestBody CarUse carUse) throws Exception {

        carUseService.saveCarUse(carUse);
    }


    @RequestMapping(value = "/carUse/{objectId}", method = RequestMethod.DELETE)
    public void deleteCarUse(@PathVariable("objectId") String objectId) throws Exception {

        carUseService.deleteCarUse(objectId);
    }

}