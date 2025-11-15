package com.cangsg.brick.kt.entry;

import java.util.Map;

import lombok.Data;

@Data
public class KtTest12 {
    private int i1;
    private int i2;
    private String name;
    private KtTest12Item item;

    private Map<String, Object> map1;
}
