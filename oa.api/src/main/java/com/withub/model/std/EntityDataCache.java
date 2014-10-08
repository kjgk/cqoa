package com.withub.model.std;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EntityDataCache {

    private static Map<String, List> dataMap = new HashMap<String, List>();

    private EntityDataCache() {

    }

    public static Map<String, List> getDataMap() {

        return dataMap;
    }

    public static void setDataMap(Map<String, List> dataMap) {

        EntityDataCache.dataMap = dataMap;
    }
}
