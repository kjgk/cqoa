package com.withub.service.impl.workflow;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.ReflectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.model.entity.AbstractBaseEntity;
import com.withub.model.exception.BaseBusinessException;
import com.withub.model.system.po.User;
import com.withub.model.system.po.UserOrganizationRole;
import com.withub.model.workflow.WFArguments;
import com.withub.model.workflow.enumeration.TaskHandleResult;
import com.withub.model.workflow.po.*;
import com.withub.service.EntityServiceImpl;
import com.withub.service.workflow.FlowTypeService;
import com.withub.service.workflow.InstanceService;
import com.withub.service.workflow.WFRegulationService;
import com.withub.spring.SpringContextUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("wfRegulationService")
@Transactional(rollbackForClassName = {"Exception", "BaseBusinessException"})
public class WFRegulationServiceImpl extends EntityServiceImpl implements WFRegulationService {

    //================================= 属性声明 =========================================================

    @Autowired
    private FlowTypeService flowTypeService;

    @Autowired
    private InstanceService instanceService;

    //================================= 接口实现 =========================================================

    public List<User> parseTaskHandler(Instance instance, FlowNode flowNode) throws Exception {

        List<User> handlerList = new ArrayList<User>();

        if (instance.getSubInstanceList() != null && instance.getSubInstanceList().size() > 1) {
            SubInstance previousSubInstance = instance.getSubInstanceList().get(instance.getSubInstanceList().size() - 1);
            for (MasterTask masterTask : previousSubInstance.getMasterTaskList()) {
                if (masterTask.getFlowNode().getObjectId().equals(flowNode.getObjectId())) {
                    for (Task task : masterTask.getTaskList()) {
                        handlerList.add(task.getHandler());
                    }
                    break;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(handlerList)) {
            return handlerList;
        }

        AbstractBaseEntity relatedEntity = instanceService.getRelatedEntityInstance(instance);

        if (StringUtil.isNotEmpty(flowNode.getHandlerServiceMethod())) {
            String handlerServiceMethod = flowNode.getHandlerServiceMethod();
            WFArguments wfArguments = new WFArguments();
            wfArguments.setInstance(instance);
            wfArguments.setFlowNode(flowNode);
            String beanId = StringUtil.substring(handlerServiceMethod, 0, handlerServiceMethod.indexOf("."));
            String method = handlerServiceMethod.substring(handlerServiceMethod.indexOf(".") + 1, handlerServiceMethod.indexOf("("));
            String[] arguments = handlerServiceMethod.substring(handlerServiceMethod.indexOf("(") + 1, handlerServiceMethod.indexOf(")")).split(",");
            Object object = SpringContextUtil.getInstance().getBean(beanId);
            Object[] args = new Object[arguments.length];

            for (int i = 0; i < arguments.length; i++) {
                String argument = arguments[i];
                argument = StringUtils.trim(argument);
                if ("${wfArguments}".equals(argument)) {
                    args[i] = wfArguments;
                } else if (argument.startsWith("\"")) {
                    args[i] = argument.substring(1, argument.lastIndexOf("\""));
                } else {
                    args[i] = argument;
                }
            }
            Object value = ReflectionUtil.invokeMethod(object, method, args);
            handlerList = (List<User>) value;
        } else if (StringUtil.isNotEmpty(flowNode.getHandlerOnFlowNode())) {
            String hql = "select o from " + Task.class.getName() + " o where o.masterTask.subInstance.instance.objectId=?"
                    + " and o.masterTask.flowNode.flowNodeTag=?";
            List list = listByHql(hql, instance.getObjectId(), flowNode.getHandlerOnFlowNode());
            if (CollectionUtil.isNotEmpty(list)) {
                for (Task task : (List<Task>) list) {
                    handlerList.add(task.getHandler());
                }
            }
        } else if (StringUtil.isNotEmpty(flowNode.getUserPropertyOnEntity())) {
            Object userValue = getPropertyValue(relatedEntity, flowNode.getUserPropertyOnEntity());
            if (userValue != null) {
                if (flowNode.getUserPropertyOnEntity().endsWith("List")) {
                    List list = (List) userValue;
                    for (User user : (List<User>) list) {
                        handlerList.add(user);
                    }
                } else {
                    handlerList.add((User) userValue);
                }
            }
        } else if (StringUtil.isNotEmpty(flowNode.getOrganizationId()) && StringUtil.isNotEmpty(flowNode.getRoleId())) {
            String hql = "select o from " + UserOrganizationRole.class.getName() + " o"
                    + " where o.organization.objectId=? and o.role.objectId=?";
            List userOrganizationRoleList = listByHql(hql, flowNode.getOrganizationId(), flowNode.getRoleId());
            if (CollectionUtil.isNotEmpty(userOrganizationRoleList)) {
                for (UserOrganizationRole userOrganizationRole : (List<UserOrganizationRole>) userOrganizationRoleList) {
                    handlerList.add(userOrganizationRole.getUser());
                }
            }
        } /*else if (StringUtil.isNotEmpty(flowNode.getOrganizationProperty()) && StringUtil.isNotEmpty(flowNode.getRoleId())) {
            Organization organization = (Organization) getPropertyValue(relatedEntity, flowNode.getOrganizationProperty());
            String hql = "select o from " + UserOrganizationRole.class.getName() + " o"
                    + " where o.organization.objectId=? and o.role.objectId=?";
            List userOrganizationRoleList = listByHql(hql, organization.getObjectId(), flowNode.getRoleId());
            if (CollectionUtil.isNotEmpty(userOrganizationRoleList)) {
                for (UserOrganizationRole userOrganizationRole : (List<UserOrganizationRole>) userOrganizationRoleList) {
                    handlerList.add(userOrganizationRole.getUser());
                }
            }
        }*/ else if (StringUtil.isNotEmpty(flowNode.getOrganizationId())) {
            String hql = "select o from " + User.class.getName() + " o" + " where o.organization.objectId=?";
            List userList = listByHql(hql, flowNode.getOrganizationId());
            if (CollectionUtil.isNotEmpty(userList)) {
                for (User user : (List<User>) userList) {
                    handlerList.add(user);
                }
            }
        } else if (StringUtil.isNotEmpty(flowNode.getRoleId())) {
            String hql = "select o from " + User.class.getName() + " o" + " where o.role.objectId=?";
            List userList = listByHql(hql, flowNode.getRoleId());
            if (CollectionUtil.isNotEmpty(userList)) {
                for (User user : (List<User>) userList) {
                    handlerList.add(user);
                }
            }
        }

        if (CollectionUtil.isEmpty(handlerList)) {
            throw new BaseBusinessException("", "未找到流程节点[" + flowNode.getName() + "]上的任务执行人! 请联系系统管理员!");
        }

        if (handlerList.size() == 1 || flowNode.getHandlerFetchCount() == 0 || flowNode.getHandlerFetchCount() >= handlerList.size()) {
            return handlerList;
        }

        if (handlerList.contains(instance.getCreator())) {
            handlerList.remove(instance.getCreator());
            if (handlerList.size() == 1 || flowNode.getHandlerFetchCount() >= handlerList.size()) {
                return handlerList;
            }
        }

        /*List<User> userList = new ArrayList();
        if (flowNode.getHandlerFetchType() == HandlerFetchType.Random) {
            List<Integer> indexList = RandomUtil.getUniqueRandom(0, handlerList.size() - 1, flowNode.getHandlerFetchCount());
            for (Integer index : indexList) {
                userList.add(handlerList.get(index));
            }
        } else if (flowNode.getHandlerFetchType() == HandlerFetchType.IdleMost) {
            String userIds = quoteSqlQueryStringValue(handlerList, "objectId");
            String sql = "select handler,count(*) from wf_task where status='398DB916-92B9-43C8-B1C3-356AB1875495' and handler in (" + userIds + ") group by handler order by count(*) asc";
            List list = listBySql(sql);
            if (CollectionUtil.isEmpty(list)) {
                List<Integer> indexList = RandomUtil.getUniqueRandom(0, handlerList.size() - 1, flowNode.getHandlerFetchCount());
                for (Integer index : indexList) {
                    userList.add(handlerList.get(index));
                }
            } else {
                String userId = ((Object[]) list.get(0))[0].toString();
                User user = get(User.class, userId);
                userList.add(user);
            }
        } else if (flowNode.getHandlerFetchType() == HandlerFetchType.TaskLeast) {
            String userIds = quoteSqlQueryStringValue(handlerList, "objectId");
            String sql = "select handler,count(*) from vw_taskinfo"
                    + " where flowTypeId='" + instance.getFlowType().getObjectId() + "'"
                    + " and taskstatus='398DB916-92B9-43C8-B1C3-356AB1875495' and handler in (" + userIds + ")"
                    + " group by handler order by count(*) asc";
            List list = listBySql(sql);
            if (CollectionUtil.isEmpty(list)) {
                List<Integer> indexList = RandomUtil.getUniqueRandom(0, handlerList.size() - 1, flowNode.getHandlerFetchCount());
                for (Integer index : indexList) {
                    userList.add(handlerList.get(index));
                }
            } else {
                String userId = ((Object[]) list.get(0))[0].toString();
                User user = get(User.class, userId);
                userList.add(user);
            }
        }

        if (CollectionUtil.isEmpty(userList)) {
            throw new BaseBusinessException("", "未找到流程节点[" + flowNode.getName() + "]上的任务执行人! 请联系系统管理员!");
        }*/

        return handlerList;
    }

    public FlowNodeRoute parseFlowNodeRoute(Instance instance, FlowNode flowNode, TaskHandleResult taskHandleResult) throws Exception {

        List<FlowNodeRoute> flowNodeRouteList = flowNode.getFlowNodeRouteList();

        if (flowNodeRouteList.size() == 1) {
            return flowNodeRouteList.get(0);
        }

        ExpressionParser parser = new SpelExpressionParser();
        for (FlowNodeRoute flowNodeRoute : flowNodeRouteList) {
            Ramus ramus = flowNodeRoute.getRamus();
            if (ramus.getStartWorkflow() == 0) {
                continue;
            }
            if (CollectionUtil.isEmpty(ramus.getRegulationList())) {
                throw new BaseBusinessException("", "分支[" + ramus.getName() + "]没有定义业务规则表达式!");
            }
            String expression = ramus.getRegulationList().get(0).getExpression();
            if (StringUtil.isEmpty(expression)) {
                throw new BaseBusinessException("", "无法解析分支[" + ramus.getName() + "]上的业务规则表达式!");
            }

            if (expression.contains("${TaskHandleResult}")) {
                expression = expression.replace("${TaskHandleResult}", "\"" + taskHandleResult.toString() + "\"");
            }
            String[] phraseArray = StringUtils.substringsBetween(expression, "{#", "#}");
            if (CollectionUtil.isNotEmpty(phraseArray)) {
                for (String pharse : phraseArray) {
                    String className = instance.getFlowType().getEntity().getClassName();
                    AbstractBaseEntity entity = (AbstractBaseEntity) getEntityByClassName(className, instance.getRelatedObjectId());
                    Object objectValue = getPropertyValue(entity, pharse);
                    String value = "";
                    if (objectValue != null) {
                        value = objectValue.toString();
                    }
                    expression = expression.replace("{#" + pharse + "#}", value);
                }
            }
            expression = expression.replace("#", "\"");
            boolean expressionValue;
            try {
                expressionValue = parser.parseExpression(expression).getValue(Boolean.class);
            } catch (Exception e) {
                System.out.println(expression);
                e.printStackTrace();
                throw new BaseBusinessException("", "无法解析流程节点分支[" + ramus.getName() + "]上定义的业务规则!");
            }
            if (expressionValue) {
                return flowNodeRoute;
            }
        }

        throw new BaseBusinessException("", "未找到节点[" + flowNode.getName() + "]下一个流程节点!");

    }

    //================================= 属性方法 ==========================================================

    public FlowTypeService getFlowTypeService() {

        return flowTypeService;
    }

    public void setFlowTypeService(FlowTypeService flowTypeService) {

        this.flowTypeService = flowTypeService;
    }

    public InstanceService getInstanceService() {

        return instanceService;
    }

    public void setInstanceService(InstanceService instanceService) {

        this.instanceService = instanceService;
    }
}
