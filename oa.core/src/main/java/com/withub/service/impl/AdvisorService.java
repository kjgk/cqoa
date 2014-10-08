package com.withub.service.impl;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.ReflectionUtil;
import com.withub.model.entity.event.EntityAddEvent;
import com.withub.model.entity.event.EntityDeleteEvent;
import com.withub.model.entity.event.EntityLogicDeleteEvent;
import com.withub.model.entity.event.EntityUpdateEvent;
import com.withub.model.entity.query.QueryInfo;
import com.withub.service.EntityService;
import com.withub.service.std.SystemEventService;
import com.withub.service.system.PermissionService;
import com.withub.service.system.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Service
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class AdvisorService {

    //======================== 属性声明 ==================================================================

    @Autowired
    private EntityService entityService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private SystemEventService systemEventService;

    @Autowired
    private UserService userService;

    //======================== 方法实现 ==================================================================

    //@AfterThrowing(pointcut = "execution(* com.withub.service.impl..*.*(..))", throwing = "e")
    public void catchException(JoinPoint joinPoint, Exception e) {

        // 忽略代理方法
        if (joinPoint.getTarget().getClass().getName().startsWith("$Proxy")) {
            return;
        }

        String message = "方法[" + joinPoint.getSignature() + "] " + e.toString();

        /*// 发布异常事件
        ExceptionInfo exceptionInfo = new ExceptionInfo();
        excptionInfo.setClazz(joinPoint.getTarget().getClass());
        exceptionInfo.setSignature(joinPoint.getSignature());
        exceptionInfo.setException(e);
        try {
            exceptionInfo.setUserId(SpringSecurityUtil.getCurrentUser().getObjectId());
        } catch (Exception ex) {
            // do nothing
        }
        exceptionInfo.setHappenTime(DateUtil.getCurrentTime());

        SystemEventPublisher.publishExceptionEvent(this, exceptionInfo);*/
    }

    @AfterReturning("execution(* com.withub.spring.listener.EventDispatchListener.*(..))")
    public void catchEvent(JoinPoint joinPoint) throws Exception {

        /*System.out.println(joinPoint.getTarget().getClass().getName());
        System.out.println(joinPoint.getSignature().getName());
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            System.out.println(joinPoint.getArgs()[i].toString());
        }*/
        if (joinPoint.getSignature().getName().equals("onApplicationEvent")
                && joinPoint.getArgs().length > 0) {
            ApplicationEvent event = (ApplicationEvent) joinPoint.getArgs()[0];
            if (!event.getClass().getName().startsWith("com.withub")) {
                return;
            }
            if (event instanceof EntityAddEvent
                    || event instanceof EntityUpdateEvent
                    || event instanceof EntityDeleteEvent
                    || event instanceof EntityLogicDeleteEvent) {
                return;
            }

            System.out.println("获取的事件名称:" + event.getClass().getName());
            systemEventService.onEvent(event);
        }
    }

    public void doAfterReturning(JoinPoint joinPoint, Object returnValue) throws Exception {

        /* System.out.println(jp.getTarget().getClass().getName());
                System.out.println(jp.getSignature().getName());
                //jp.getSignature().getClass().getTypeParameters();

                System.out.println(jp.getSignature().getDeclaringTypeName());
        */

        ReflectiveMethodInvocation rmi;

        // 强制访问 methodInvocation 属性
        rmi = (ReflectiveMethodInvocation) ReflectionUtil.getFieldValue(joinPoint, "methodInvocation");
        System.out.println(rmi.getMethod().getName());
        for (Object o : rmi.getArguments()) {
            System.out.println(o.toString());
        }
    }

    @Before("execution(* com.withub.service.impl..*.query*(..)) || execution(* com.withub.service.impl..*.listAll*(..))")
    public void addQueryPermissionFilter(JoinPoint joinPoint) throws Exception {

        if (CollectionUtil.isEmpty(joinPoint.getArgs())) {
            return;
        }

        QueryInfo queryInfo = (QueryInfo) joinPoint.getArgs()[0];
        queryInfo.setServiceMethod(joinPoint.getSignature().getName());

        permissionService.addQueryPermissionCondition(queryInfo);
    }

    /* @Before("execution(* com.withub.service.impl..*.add*(..)) || execution(* com.withub.service.impl..*.update*(..)) || execution(* com.withub.service.impl..*.submit*(..))")
    public void checkPermission(JoinPoint joinPoint) throws Exception {

        Permission permission = (Permission) entityService.getByPropertyValue(Permission.class, "serviceMethod", joinPoint.getSignature().getName());

        // 方法 未注册则不做限制
        if (permission == null) {
            return;
        }

        AbstractBaseEntity entity = null;
        if (joinPoint.getArgs().length > 0) {
            entity = (AbstractBaseEntity) joinPoint.getArgs()[0];
        }

        if (!permissionService.hasPermission(permission, entity)) {
            throw new BaseBusinessException("", "没有权限!");
        }
    }*/


    //======================== 属性方法 ==================================================================

    public EntityService getEntityService() {

        return entityService;
    }

    public void setEntityService(EntityService entityService) {

        this.entityService = entityService;
    }

    public PermissionService getPermissionService() {

        return permissionService;
    }

    public void setPermissionService(PermissionService permissionService) {

        this.permissionService = permissionService;
    }

    public SystemEventService getSystemEventService() {

        return systemEventService;
    }

    public void setSystemEventService(SystemEventService systemEventService) {

        this.systemEventService = systemEventService;
    }

    public UserService getUserService() {

        return userService;
    }

    public void setUserService(UserService userService) {

        this.userService = userService;
    }
}
