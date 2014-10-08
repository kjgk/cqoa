package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.ExpressionOperation;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.vo.*;
import com.withub.service.std.SmsService;
import com.withub.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/std")
public class SmsController extends BaseController {

    //======================  属性声明 ==================================================================

    @Autowired
    private SmsService smsService;

    //======================  Controller方法 ============================================================

    @RequestMapping(value = "/sms/send", method = RequestMethod.POST)
    public void sendSms(HttpServletRequest request, ModelMap modelMap) throws Exception {

        String mobile = request.getParameter("mobile");
        String message = request.getParameter("message");
        smsService.sendSms(mobile, message);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/sms/query", method = RequestMethod.GET)
    public void querySms(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(ViewSmsServerOut.class);
        this.setPageInfoQueryCondition(request, queryInfo);
        this.setDateRangeQueryCondition(request, queryInfo, "create_date");
        this.setInputFieldQueryCondition(request, queryInfo, "status");
        String name = request.getParameter("name");
        String sendDate = request.getParameter("sendDate");
        if (StringUtil.isNotEmpty(name)) {
            this.setQueryInfoCondition(queryInfo, "name", name, ExpressionOperation.MatchMiddle);
        }
        if (StringUtil.isNotEmpty(sendDate)) {
            this.setQueryInfoCondition(queryInfo, "sent_date", sendDate, ExpressionOperation.NotEquals);
        }
        this.setAscOrderBy(queryInfo, "create_date");

        RecordsetInfo recordsetInfo = smsService.query(queryInfo);

        List list = recordsetInfo.getEntityList();
        List items = new ArrayList();
        if (CollectionUtil.isNotEmpty(list)) {
            for (ViewSmsServerOut viewSmsServerOut : (List<ViewSmsServerOut>) list) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("objectId", viewSmsServerOut.getObjectId());
                item.put("createDate", viewSmsServerOut.getCreate_date());
                item.put("recipient", viewSmsServerOut.getRecipient());
//                item.put("errors", viewSmsServerOut.getErrors());
                item.put("name", viewSmsServerOut.getName());
                item.put("originator", viewSmsServerOut.getOriginator());
//                item.put("refNo", viewSmsServerOut.getRef_no());
//                item.put("sendDate", viewSmsServerOut.getSent_date());
                item.put("statusReport", viewSmsServerOut.getStatus_report());
                item.put("text", viewSmsServerOut.getText());

                items.add(item);
            }
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/sentSms/query", method = RequestMethod.GET)
    public void querySentSms(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(ViewSmsServerOutSent.class);
        this.setPageInfoQueryCondition(request, queryInfo);
        this.setDateRangeQueryCondition(request, queryInfo, "create_date");
        this.setInputFieldQueryCondition(request, queryInfo, "status");
        String name = request.getParameter("name");
        String sendDate = request.getParameter("sendDate");
        if (StringUtil.isNotEmpty(name)) {
            this.setQueryInfoCondition(queryInfo, "name", name, ExpressionOperation.MatchMiddle);
        }
        if (StringUtil.isNotEmpty(sendDate)) {
            this.setQueryInfoCondition(queryInfo, "sent_date", sendDate, ExpressionOperation.NotEquals);
        }
        this.setAscOrderBy(queryInfo, "create_date");

        RecordsetInfo recordsetInfo = smsService.query(queryInfo);

        List list = recordsetInfo.getEntityList();
        List items = new ArrayList();
        if (CollectionUtil.isNotEmpty(list)) {
            for (ViewSmsServerOutSent viewSmsServerOutSent : (List<ViewSmsServerOutSent>) list) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("objectId", viewSmsServerOutSent.getObjectId());
                item.put("createDate", viewSmsServerOutSent.getCreate_date());
                item.put("recipient", viewSmsServerOutSent.getRecipient());
                item.put("name", viewSmsServerOutSent.getName());
                item.put("originator", viewSmsServerOutSent.getOriginator());
                item.put("sendDate", viewSmsServerOutSent.getSent_date());
                item.put("statusReport", viewSmsServerOutSent.getStatus_report());
                item.put("text", viewSmsServerOutSent.getText());

                items.add(item);
            }
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/unsentSms/query", method = RequestMethod.GET)
    public void queryUnsentSms(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(ViewSmsServerOutUnsent.class);
        this.setPageInfoQueryCondition(request, queryInfo);
        this.setDateRangeQueryCondition(request, queryInfo, "create_date");
        String name = request.getParameter("name");
        if (StringUtil.isNotEmpty(name)) {
            this.setQueryInfoCondition(queryInfo, "name", name, ExpressionOperation.MatchMiddle);
        }
        this.setAscOrderBy(queryInfo, "create_date");

        RecordsetInfo recordsetInfo = smsService.query(queryInfo);

        List list = recordsetInfo.getEntityList();
        List items = new ArrayList();
        if (CollectionUtil.isNotEmpty(list)) {
            for (ViewSmsServerOutUnsent viewSmsServerOutUnsent : (List<ViewSmsServerOutUnsent>) list) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("objectId", viewSmsServerOutUnsent.getObjectId());
                item.put("createDate", viewSmsServerOutUnsent.getCreate_date());
                item.put("recipient", viewSmsServerOutUnsent.getRecipient());
                item.put("name", viewSmsServerOutUnsent.getName());
                item.put("originator", viewSmsServerOutUnsent.getOriginator());
                item.put("statusReport", viewSmsServerOutUnsent.getStatus_report());
                item.put("text", viewSmsServerOutUnsent.getText());
                items.add(item);
            }
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/failedSms/query", method = RequestMethod.GET)
    public void queryFailedSms(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(ViewSmsServerOutFailed.class);
        this.setPageInfoQueryCondition(request, queryInfo);
        this.setDateRangeQueryCondition(request, queryInfo, "create_date");
        this.setInputFieldQueryCondition(request, queryInfo, "status");
        String name = request.getParameter("name");
        String sendDate = request.getParameter("sendDate");
        if (StringUtil.isNotEmpty(name)) {
            this.setQueryInfoCondition(queryInfo, "name", name, ExpressionOperation.MatchMiddle);
        }
        if (StringUtil.isNotEmpty(sendDate)) {
            this.setQueryInfoCondition(queryInfo, "sent_date", sendDate, ExpressionOperation.NotEquals);
        }
        this.setAscOrderBy(queryInfo, "create_date");

        RecordsetInfo recordsetInfo = smsService.query(queryInfo);

        List list = recordsetInfo.getEntityList();
        List items = new ArrayList();
        if (CollectionUtil.isNotEmpty(list)) {
            for (ViewSmsServerOutFailed viewSmsServerOutFailed : (List<ViewSmsServerOutFailed>) list) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("objectId", viewSmsServerOutFailed.getObjectId());
                item.put("createDate", viewSmsServerOutFailed.getCreate_date());
                item.put("recipient", viewSmsServerOutFailed.getRecipient());
                item.put("errors", viewSmsServerOutFailed.getErrors());
                item.put("name", viewSmsServerOutFailed.getName());
                item.put("originator", viewSmsServerOutFailed.getOriginator());
                item.put("refNo", viewSmsServerOutFailed.getRef_no());
                item.put("sendDate", viewSmsServerOutFailed.getSent_date());
                item.put("statusReport", viewSmsServerOutFailed.getStatus_report());
                item.put("text", viewSmsServerOutFailed.getText());

                items.add(item);
            }
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/queuedSms/query", method = RequestMethod.GET)
    public void queryQueuedSms(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(ViewSmsServerOutQueued.class);
        this.setPageInfoQueryCondition(request, queryInfo);
        this.setDateRangeQueryCondition(request, queryInfo, "create_date");
        this.setInputFieldQueryCondition(request, queryInfo, "status");
        String name = request.getParameter("name");
        String sendDate = request.getParameter("sendDate");
        if (StringUtil.isNotEmpty(name)) {
            this.setQueryInfoCondition(queryInfo, "name", name, ExpressionOperation.MatchMiddle);
        }
        if (StringUtil.isNotEmpty(sendDate)) {
            this.setQueryInfoCondition(queryInfo, "sent_date", sendDate, ExpressionOperation.NotEquals);
        }
        this.setAscOrderBy(queryInfo, "create_date");

        RecordsetInfo recordsetInfo = smsService.query(queryInfo);

        List list = recordsetInfo.getEntityList();
        List items = new ArrayList();
        if (CollectionUtil.isNotEmpty(list)) {
            for (ViewSmsServerOutQueued viewSmsServerOutQueued : (List<ViewSmsServerOutQueued>) list) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("objectId", viewSmsServerOutQueued.getObjectId());
                item.put("createDate", viewSmsServerOutQueued.getCreate_date());
                item.put("recipient", viewSmsServerOutQueued.getRecipient());
                item.put("errors", viewSmsServerOutQueued.getErrors());
                item.put("name", viewSmsServerOutQueued.getName());
                item.put("originator", viewSmsServerOutQueued.getOriginator());
                item.put("refNo", viewSmsServerOutQueued.getRef_no());
                item.put("sendDate", viewSmsServerOutQueued.getSent_date());
                item.put("statusReport", viewSmsServerOutQueued.getStatus_report());
                item.put("text", viewSmsServerOutQueued.getText());

                items.add(item);
            }
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    //======================  属性方法 ==================================================================

    public SmsService getSmsService() {

        return smsService;
    }

    public void setSmsService(SmsService smsService) {

        this.smsService = smsService;
    }
}
