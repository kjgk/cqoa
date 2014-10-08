package com.withub.model.system.event;

import com.withub.model.system.po.Organization;
import com.withub.model.system.po.User;
import com.withub.spring.SpringContextUtil;

/**
 * 定义系统事件发布器
 */
public final class SystemEventPublisher {

    /**
     * 发布异常事件
     *
     * @param source        发生源
     * @param exceptionInfo 异常对象
     */
    public static void publishExceptionEvent(Object source, ExceptionInfo exceptionInfo) {

        ExceptionEvent event = new ExceptionEvent(source, exceptionInfo);
        //SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    /**
     * 发布帐号设置密码事件
     *
     * @param source           发生源
     * @param accountEventArgs 帐号事件参数
     */
    public static void publishPasswordSetEvent(Object source, AccountEventArgs accountEventArgs) {

        PasswordSetEvent event = new PasswordSetEvent(source, accountEventArgs);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    /**
     * 发布帐号创建事件
     *
     * @param source           发生源
     * @param accountEventArgs 帐号事件参数
     */
    public static void publishAccountCreateEvent(Object source, AccountEventArgs accountEventArgs) {

        AccountCreateEvent event = new AccountCreateEvent(source, accountEventArgs);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    /**
     * 发布帐号更新事件
     *
     * @param source           发生源
     * @param accountEventArgs 帐号事件参数
     */
    public static void publishAccountModifyEvent(Object source, AccountEventArgs accountEventArgs) {

        AccountModifyEvent event = new AccountModifyEvent(source, accountEventArgs);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    /**
     * 发布用户归档事件
     *
     * @param source 发生源
     * @param user   用户
     */
    public static void publishUserArchiveEvent(Object source, User user) {

        UserArchiveEvent event = new UserArchiveEvent(source, user);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishUserSaveEvent(Object source, User user) {

        UserSaveEvent event = new UserSaveEvent(source, user);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishUserDeleteEvent(Object source, User user) {

        UserDeleteEvent event = new UserDeleteEvent(source, user);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishOrganizationCreateEvent(Object source, Organization organization) {

        OrganizationCreateEvent event = new OrganizationCreateEvent(source, organization);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishOrganizationUpdateEvent(Object source, Organization organization) {

        OrganizationUpdateEvent event = new OrganizationUpdateEvent(source, organization);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishOrganizationDeleteEvent(Object source, Organization organization) {

        OrganizationDeleteEvent event = new OrganizationDeleteEvent(source, organization);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishOrganizationMergeEvent(Object source, Organization sourceOrganization, Organization targetOrganization) {

        OrganizationMergEvent event = new OrganizationMergEvent(source, sourceOrganization, targetOrganization);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }

    public static void publishOrganizationMoveEvent(Object source, Organization sourceOrganization, Organization targetOrganization) {

        OrganizationMoveEvent event = new OrganizationMoveEvent(source, sourceOrganization, targetOrganization);
        SpringContextUtil.getInstance().getApplicationContext().publishEvent(event);
    }
}