package com.withub.spring.listener;

import java.util.HashMap;
import java.util.Map;

public final class EventHandlerConfigInfo {

    private Map<String, String> eventHandlerMap = new HashMap<String, String>();

    public Map<String, String> getEventHandlerMap() {

        return eventHandlerMap;
    }

    public void setEventHandlerMap(Map<String, String> eventHandlerMap) {

        this.eventHandlerMap = eventHandlerMap;
    }
}
