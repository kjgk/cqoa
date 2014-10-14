package com.withub.web.controller.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.oa.po.Leave;
import com.withub.service.oa.LeaveService;
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
public class LeaveController extends BaseController {

    @Autowired
    private LeaveService leaveService;

    @RequestMapping(value = "/leave", method = RequestMethod.GET)
    public void queryLeave(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Leave.class);
        setPageInfoQueryCondition(request, queryInfo);

        putRecordsetInfo(modelMap, leaveService.queryLeave(queryInfo));
    }

    @RequestMapping(value = "/leave/{objectId}", method = RequestMethod.GET)
    public void getLeave(@PathVariable("objectId") String objectId, ModelMap modelMap) throws Exception {

        Leave leave = leaveService.getLeave(objectId);
        putData(modelMap, leave);
    }

    @RequestMapping(value = "/leave", method = RequestMethod.POST)
    public void createLeave(@RequestBody Leave leave) throws Exception {

        leaveService.saveLeave(leave);
    }

    @RequestMapping(value = "/leave", method = RequestMethod.PUT)
    public void updateLeave(@RequestBody Leave leave) throws Exception {

        leaveService.saveLeave(leave);
    }


    @RequestMapping(value = "/leave/{objectId}", method = RequestMethod.DELETE)
    public void deleteLeave(@PathVariable("objectId") String objectId) throws Exception {

        leaveService.deleteLeave(objectId);
    }

}
