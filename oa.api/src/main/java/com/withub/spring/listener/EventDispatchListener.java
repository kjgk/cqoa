package com.withub.spring.listener;

import com.withub.common.util.CollectionUtil;
import com.withub.common.util.ReflectionUtil;
import com.withub.common.util.StringUtil;
import com.withub.spring.SpringContextUtil;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.Iterator;
import java.util.Map;

public class EventDispatchListener implements ApplicationListener<ApplicationEvent> {

    public void onApplicationEvent(ApplicationEvent event) {

        EventDispatchConfigInfo eventDispatchConfigInfo;

        // 快速解决事件分发问题
        if (!event.getClass().getName().startsWith("com.withub")) {
            return;
        }

        // 系统启动时 SpringContextUtil 还没被实例化
        try {
            eventDispatchConfigInfo = (EventDispatchConfigInfo) SpringContextUtil.getInstance().getBean("eventDispatchConfigInfo");
        } catch (Exception e) {
            return;
        }

        // 如果没配置事件监听器则返回
        if (eventDispatchConfigInfo == null
                || CollectionUtil.isEmpty(eventDispatchConfigInfo.getListenerList())) {
            return;
        }

        // 将事件遍历分发到所有注册的事件监听器上
        for (String listener : eventDispatchConfigInfo.getListenerList()) {
            EventHandlerConfigInfo eventHandlerConfigInfo = (EventHandlerConfigInfo) SpringContextUtil.getInstance().getBean(listener);
            for (Iterator<Map.Entry<String, String>> iterator = eventHandlerConfigInfo.getEventHandlerMap().entrySet().iterator();
                 iterator.hasNext(); ) {

                Map.Entry<String, String> entry = iterator.next();
                if (event.getClass().getName().equalsIgnoreCase(entry.getKey().trim())) {
                    try {
                        /**
                         * 事件处理方法的配置格式为
                         * beanId.onEvent
                         */
                        String beanId = StringUtil.substring(entry.getValue(), 0, entry.getValue().indexOf("."));
                        Object obj = SpringContextUtil.getInstance().getBean(beanId);
                        String method = entry.getValue().substring(entry.getValue().indexOf(".") + 1);
                        ReflectionUtil.invokeMethod(obj, method, new Object[]{event});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
