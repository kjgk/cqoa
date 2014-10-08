package com.withub.web.controller.std;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.DateUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.query.QueryInfo;
import com.withub.model.entity.query.RecordsetInfo;
import com.withub.model.std.enumeration.BulletinStatus;
import com.withub.model.std.po.Bulletin;
import com.withub.model.system.po.Organization;
import com.withub.service.std.BulletinService;
import com.withub.service.std.CommonTextService;
import com.withub.service.system.CodeService;
import com.withub.service.system.PermissionService;
import com.withub.util.SpringSecurityUtil;
import com.withub.web.common.BaseController;
import com.withub.web.common.HtmlEditorUtil;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/std")
public class BulletinController extends BaseController {

    //================================ 属性声明 ===========================================================

    @Autowired
    private BulletinService bulletinService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private CommonTextService commonTextService;

    @Autowired
    private PermissionService permissionService;

    //=============================== Controller 方法 ====================================================

    @InitBinder
    public void initBinder(WebDataBinder binder) {

        super.initBinder(binder);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping(value = "/bulletin/query", method = RequestMethod.GET)
    public void queryBulletin(HttpServletRequest request, ModelMap modelMap) throws Exception {

        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setTargetEntity(Bulletin.class);
        this.setPageInfoQueryCondition(request, queryInfo);

        String organizationId = request.getParameter("organizationId");
        if (StringUtil.isNotEmpty(organizationId)) {
            Organization organization = (Organization) bulletinService.get(Organization.class, organizationId);
            this.setNodeLevelCodeQueryCondition(queryInfo, "issueOrganization.nodeLevelCode", organization);
        }

        this.setStringValueEqualsQueryCondition(request, queryInfo, "bulletinType.objectId", "bulletinType");
        this.setStringValueEqualsQueryCondition(request, queryInfo, "bulletinType.codeTag", "bulletinTypeTag");
        this.setInputFieldQueryCondition(request, queryInfo, "title");
        String status = request.getParameter("status");
        if (status.equals(BulletinStatus.Draft.toString())) {
            this.setStringValueEqualsQueryCondition(request, queryInfo, "status.codeTag", "status");
        } else {
            this.setDateRangeQueryCondition(request, queryInfo, "issueTime");
        }
        String issuer = request.getParameter("issuer");
        if (StringUtil.isNotEmpty(issuer) && issuer.equalsIgnoreCase("currentUser")) {
            this.setStringValueEqualsQueryCondition(queryInfo, "issuer.objectId", SpringSecurityUtil.getCurrentUser().getObjectId());
        }

        if (status.equals(BulletinStatus.Draft.toString())) {
            this.setDescOrderBy(queryInfo, "createTime");
        } else {
            this.setDescOrderBy(queryInfo, "issueTime");
        }

        RecordsetInfo recordsetInfo = bulletinService.query(queryInfo);

        List list = recordsetInfo.getEntityList();

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        List items = new ArrayList();
        for (Bulletin bulletin : (List<Bulletin>) list) {
            HashMap item = new HashMap();
            item.put("objectId", bulletin.getObjectId());
            item.put("title", bulletin.getTitle());
            item.put("organization", bulletin.getOrganization().getFullName());
            item.put("issueOrganization", bulletin.getIssueOrganization().getFullName());
            item.put("issuer", bulletin.getIssuer().getName());
            if (bulletin.getIssueTime() != null) {
                item.put("issueTime", bulletin.getIssueTime().getTime());
            }
            item.put("bulletinType", bulletin.getBulletinType().getName());
            item.put("effectiveTime", bulletin.getEffectiveTime().getTime());

            items.add(item);
        }

        modelMap.put("total", recordsetInfo.getTotalRecordCount());
        modelMap.put("items", items);
    }

    @RequestMapping(value = "/bulletin/create", method = RequestMethod.POST)
    public void createBulletin(Bulletin bulletin, ModelMap modelMap) throws Exception {

        String content = HtmlEditorUtil.convertStandardization(bulletin.getContent());
        bulletin.setContent(content);
        bulletinService.addBulletin(bulletin);
        modelMap.put("objectId", bulletin.getObjectId());
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/bulletin/update", method = RequestMethod.POST)
    public void updateBulletin(Bulletin bulletin, ModelMap modelMap) throws Exception {

        String content = HtmlEditorUtil.convertStandardization(bulletin.getContent());
        bulletin.setContent(content);
        bulletinService.updateBulletin(bulletin);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/bulletin/issue", method = RequestMethod.POST)
    public void issueBulletin(Bulletin bulletin, ModelMap modelMap) throws Exception {

        String content = HtmlEditorUtil.convertStandardization(bulletin.getContent());
        bulletin.setContent(content);
        bulletinService.issueBulletin(bulletin);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/bulletin/issue/{objectId}", method = RequestMethod.GET)
    public void issueBulletin(@PathVariable(value = "objectId") String objectId, ModelMap modelMap) throws Exception {

        bulletinService.issueBulletin(objectId);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/bulletin/load/{objectId}", method = RequestMethod.GET)
    public void loadBulletin(@PathVariable(value = "objectId") String objectId, ModelMap modelMap) throws Exception {

        Bulletin bulletin = (Bulletin) bulletinService.get(Bulletin.class, objectId);
        HashMap model = new HashMap();

        model.put("objectId", bulletin.getObjectId());
        model.put("title", bulletin.getTitle());
        model.put("organization.objectId", bulletin.getOrganization().getObjectId());

        model.put("bulletinType.objectId", bulletin.getBulletinType().getObjectId());
        if (bulletin.getBulletinLevel() != null) {
            model.put("bulletinLevel.objectId", bulletin.getBulletinLevel().getObjectId());
        }
        if (bulletin.getEffectiveTime() != null) {
            model.put("effectiveTime",  DateUtil.getDateFormatString(bulletin.getEffectiveTime(), "yyyy-MM-dd HH:mm"));
        }
        model.put("content", commonTextService.getContent(bulletin));

        setAttachementInfo(model, bulletin.getAttachmentList());

        modelMap.put("data", model);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/bulletin/delete/{objectId}", method = RequestMethod.GET)
    public void deleteBulletin(@PathVariable(value = "objectId") String objectId, ModelMap modelMap) throws Exception {

        Bulletin bulletin = (Bulletin) bulletinService.get(Bulletin.class, objectId);
        bulletinService.logicDelete(bulletin);
        modelMap.put("success", true);
    }

    @RequestMapping(value = "/bulletin/view", method = RequestMethod.GET)
    public String viewBulletin(String objectId, HttpServletRequest request) throws Exception {

        Bulletin bulletin = bulletinService.get(Bulletin.class, objectId);
        bulletin.setContent(commonTextService.getContent(bulletin));
        request.setAttribute("bulletin", bulletin);
        return "std/bulletin/BulletinView";
    }

    //================================= 属性方法 ==========================================================

    public BulletinService getBulletinService() {

        return bulletinService;
    }

    public void setBulletinService(BulletinService bulletinService) {

        this.bulletinService = bulletinService;
    }

    public CodeService getCodeService() {

        return codeService;
    }

    public void setCodeService(CodeService codeService) {

        this.codeService = codeService;
    }

    public CommonTextService getCommonTextService() {

        return commonTextService;
    }

    public void setCommonTextService(CommonTextService commonTextService) {

        this.commonTextService = commonTextService;
    }

    public PermissionService getPermissionService() {

        return permissionService;
    }

    public void setPermissionService(PermissionService permissionService) {

        this.permissionService = permissionService;
    }
}
