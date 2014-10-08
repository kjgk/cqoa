package com.withub.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 以全局静态变量保存 Spring ApplicationContext
 * 可在任意代码中取出 ApplicationContext.
 * 配置方式为:
 * <bean id="SpringContextUtil" class="com.withub.spring.SpringContextUtil"/>
 */
public final class SpringContextUtil implements ApplicationContextAware {

    private static SpringContextUtil instance;

    private static ApplicationContext applicationContext = null;

    private SpringContextUtil() {

    }

    /**
     * 单例模式
     *
     * @return SpringContextUtil
     */
    public static SpringContextUtil getInstance() {

        if (instance == null) {
            instance = new SpringContextUtil();
        }

        return instance;
    }

    /**
     * 获取 Bean 对象
     *
     * @param beanId Bean 对象的ID
     * @return Object
     */
    public Object getBean(final String beanId) {

        Object obj = applicationContext.getBean(beanId);
        return obj;
    }

    public ApplicationContext getApplicationContext() {

        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext ac) {

        applicationContext = ac;
    }

}
