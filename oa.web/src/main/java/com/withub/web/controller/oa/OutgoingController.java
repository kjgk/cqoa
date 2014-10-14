package com.withub.web.controller.oa;

import com.withub.model.entity.query.QueryInfo;
import com.withub.model.oa.po.Outgoing;
import com.withub.service.oa.OutgoingService;
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
public class OutgoingController extends BaseController {

    @Autowired
    private OutgoingService outgoingService;

    @RequestMapping(value = "/outgoing", method = RequestMethod.GET)
    public void queryOutgoing(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Outgoing.class);
        setPageInfoQueryCondition(request, queryInfo);

        putRecordsetInfo(modelMap, outgoingService.queryOutgoing(queryInfo));
    }

    @RequestMapping(value = "/outgoing/{objectId}", method = RequestMethod.GET)
    public void getOutgoing(@PathVariable("objectId") String objectId, ModelMap modelMap) throws Exception {

        Outgoing outgoing = outgoingService.getOutgoing(objectId);
        putData(modelMap, outgoing);
    }

    @RequestMapping(value = "/outgoing", method = RequestMethod.POST)
    public void createOutgoing(@RequestBody Outgoing outgoing) throws Exception {

        outgoingService.saveOutgoing(outgoing);
    }

    @RequestMapping(value = "/outgoing", method = RequestMethod.PUT)
    public void updateOutgoing(@RequestBody Outgoing outgoing) throws Exception {

        outgoingService.saveOutgoing(outgoing);
    }


    @RequestMapping(value = "/outgoing/{objectId}", method = RequestMethod.DELETE)
    public void deleteOutgoing(@PathVariable("objectId") String objectId) throws Exception {

        outgoingService.deleteOutgoing(objectId);
    }

}
