package com.withub.web.servlet;

import com.withub.service.system.SecurityService;
import com.withub.spring.SpringContextUtil;
import com.withub.util.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;

public class SystemInitServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(SystemInitServlet.class);

    public SystemInitServlet() {

        super();
        try {
            logger.debug("SystemInitServlet start.");

            logger.debug("开始初始化配置信息.");
            ConfigUtil.getInstance().init();

            logger.info("开始初始化缓存.");
            //EntityCacheService entityCacheService = (EntityCacheService) SpringContextUtil.getInstance().getBean("entityCacheService");
            //entityCacheService.refreshCache();

            logger.info("开始清空在线用户并更新登录日志");
            SecurityService securityService = (SecurityService) SpringContextUtil.getInstance().getBean("securityService");
            securityService.clearOnlineUserAndUpdateSecurityLog();

            logger.debug("SystemInitServlet finish.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
