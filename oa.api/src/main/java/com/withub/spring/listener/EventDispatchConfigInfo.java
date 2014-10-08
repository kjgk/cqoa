package com.withub.spring.listener;


import java.util.ArrayList;
import java.util.List;

/**
 * 事件分发配置信息
 * 当有事件发生时,会遍历分发到所有定义的事件监听器上
 * 参考 spring-eventdispatch-config.xml
 */
public final class EventDispatchConfigInfo {

    private List<String> listenerList = new ArrayList<String>();

    public List<String> getListenerList() {

        return listenerList;
    }

    public void setListenerList(List<String> listenerList) {

        this.listenerList = listenerList;
    }

}
