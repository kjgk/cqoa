package com.withub.web.tag;


import com.withub.model.entity.AbstractBaseEntity;
import com.withub.service.std.CommonTextService;
import com.withub.spring.SpringContextUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class CommonTextTag extends TagSupport {

    private AbstractBaseEntity entity;

    public int doEndTag() throws JspException {

        CommonTextService commonTextService = (CommonTextService) SpringContextUtil.getInstance().getBean("commonTextService");

        try {
            JspWriter out = pageContext.getOut();
            out.print(commonTextService.getContent(entity));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TagSupport.EVAL_PAGE;
    }

    public AbstractBaseEntity getEntity() {

        return entity;
    }

    public void setEntity(AbstractBaseEntity entity) {

        this.entity = entity;
    }
}
