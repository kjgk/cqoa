package com.withub.web.controller.oa;

import com.withub.model.entity.query.ExpressionOperation;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.oa.po.Miscellaneous;
import com.withub.service.oa.MiscellaneousService;
import com.withub.util.SpringSecurityUtil;
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
public class MiscellaneousController extends BaseController {

    @Autowired
    private MiscellaneousService miscellaneousService;

    @RequestMapping(value = "/miscellaneous", method = RequestMethod.GET)
    public void queryMiscellaneous(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Miscellaneous.class);
        setPageInfoQueryCondition(request, queryInfo);

        setQueryInfoCondition(queryInfo, "creator.objectId", SpringSecurityUtil.getCurrentUser().getObjectId() , ExpressionOperation.Equals);

        setDescOrderBy(queryInfo, "createTime");

        putRecordsetInfo(modelMap, miscellaneousService.queryMiscellaneous(queryInfo));
    }

    @RequestMapping(value = "/miscellaneous/{objectId}", method = RequestMethod.GET)
    public void getMiscellaneous(@PathVariable("objectId") String objectId, ModelMap modelMap) throws Exception {

        Miscellaneous miscellaneous = miscellaneousService.getMiscellaneous(objectId);
        putData(modelMap, miscellaneous);
    }

    @RequestMapping(value = "/miscellaneous", method = RequestMethod.POST)
    public void createMiscellaneous(@RequestBody Miscellaneous miscellaneous) throws Exception {

        miscellaneousService.submitMiscellaneous(miscellaneous, miscellaneous.getApprover());
    }

    @RequestMapping(value = "/miscellaneous", method = RequestMethod.PUT)
    public void updateMiscellaneous(@RequestBody Miscellaneous miscellaneous) throws Exception {

        miscellaneousService.submitMiscellaneous(miscellaneous);
    }


    @RequestMapping(value = "/miscellaneous/{objectId}", method = RequestMethod.DELETE)
    public void deleteMiscellaneous(@PathVariable("objectId") String objectId) throws Exception {

        miscellaneousService.deleteMiscellaneous(objectId);
    }

}
