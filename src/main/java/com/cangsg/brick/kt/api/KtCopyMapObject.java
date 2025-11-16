package com.cangsg.brick.kt.api;

import java.util.Map;

public class KtCopyMapObject<T> {
    private Map<String, T> map;

    public Map<String, T> getMap() {
        return map;
    }

    public void setMap(Map<String, T> map) {
        this.map = map;
    }
}
