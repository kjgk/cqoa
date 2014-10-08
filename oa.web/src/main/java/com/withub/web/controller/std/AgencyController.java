package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.po.Agency;
import com.withub.service.std.AgencyService;
import com.withub.util.SpringSecurityUtil;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/std")
public class AgencyController extends BaseController {

    //======================= 属性声明 ===================================================

    @Autowired
    private AgencyService agencyService;

    //======================= Controller 方法 =============================================
    @InitBinder
    public void initBinder(WebDataBinder binder) {

        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateFormat.setLenient(true);
            binder.registerCustomEditor(Date.class, "startTime", new CustomDateEditor(dateFormat, true));
            binder.registerCustomEditor(Date.class, "endTime", new CustomDateEditor(dateFormat, true));
        }
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(true);
            binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        }

    }

    @RequestMapping(value = "/agency/query", method = RequestMethod.GET)
    public void queryAgency(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Agency.class);

        this.setPageInfoQueryCondition(request, queryInfo);

        this.setDateRangeQueryCondition(request, queryInfo, "startTime");

        this.setDescOrderBy(queryInfo, "startTime");

        RecordsetInfo recordsetInfo = agencyService.queryAgency(queryInfo);

        List list = recordsetInfo.getEntityList();
        List items = new ArrayList();
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        for (Agency agency : (List<Agency>) list) {
            Map item = new HashMap();
            item.put("objectId", agency.getObjectId());
            item.put("owner", agency.getOwner().getName());
            item.put("agent", agency.getAgent().getName());
            item.put("startTime", agency.getStartTime().getTime());
            if (agency.getEndTime() != null) {
                item.put("endTime", agency.getEndTime().getTime());
            }
            item.put("status", agency.getStatus().getName());
            item.put("statusTag", agency.getStatus().getCodeTag());
            item.put("creator", agency.getCreator().getName());
            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/agency/create", method = RequestMethod.POST)
    public void createAgency(ModelMap modelMap, Agency agency) throws Exception {

        agency.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        agencyService.add(agency);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/agency/finish/{objectId}", method = RequestMethod.GET)
    public void finish(ModelMap modelMap, @PathVariable(value = "objectId") String objectId) throws Exception {

        Agency agency = agencyService.get(Agency.class, objectId);
        agency.setCurrentUser(SpringSecurityUtil.getCurrentUser());
        agencyService.finish(agency);
        modelMap.put("success", true);
    }

    //======================= 属性方法 ===================================================

    public AgencyService getAgencyService() {

        return agencyService;
    }

    public void setAgencyService(AgencyService agencyService) {

        this.agencyService = agencyService;
    }
}
