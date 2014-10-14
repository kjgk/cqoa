package com.withub.web.controller.oa;

import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.ExpressionOperation;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.oa.po.Driver;
import com.withub.service.oa.DriverService;
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
public class DriverController extends BaseController {

    @Autowired
    private DriverService driverService;

    @RequestMapping(value = "/driver", method = RequestMethod.GET)
    public void queryDriver(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Driver.class);
        setPageInfoQueryCondition(request, queryInfo);

        String name = request.getParameter("name");
        if (StringUtil.isNotEmpty(name)) {
            setQueryInfoCondition(queryInfo, "name", name, ExpressionOperation.MatchMiddle);
        }

        putRecordsetInfo(modelMap, driverService.queryDriver(queryInfo));
    }

    @RequestMapping(value = "/driver/{objectId}", method = RequestMethod.GET)
    public void getDriver(@PathVariable("objectId") String objectId, ModelMap modelMap) throws Exception {

        Driver driver = driverService.getDriver(objectId);
        putData(modelMap, driver);
    }

    @RequestMapping(value = "/driver", method = RequestMethod.POST)
    public void createDriver(@RequestBody Driver driver) throws Exception {

        driverService.saveDriver(driver);
    }

    @RequestMapping(value = "/driver", method = RequestMethod.PUT)
    public void updateDriver(@RequestBody Driver driver) throws Exception {

        driverService.saveDriver(driver);
    }


    @RequestMapping(value = "/driver/{objectId}", method = RequestMethod.DELETE)
    public void deleteDriver(@PathVariable("objectId") String objectId) throws Exception {

        driverService.deleteDriver(objectId);
    }

}
