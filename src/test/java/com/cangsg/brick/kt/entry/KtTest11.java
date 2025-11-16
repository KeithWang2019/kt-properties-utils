package com.cangsg.brick.kt.entry;

import java.util.Map;

import lombok.Data;

@Data
public class KtTest11 {
    private int i1;
    private int i2;
    private String name;
    private KtTest11Item item;

    private Map<String, String> map1;
}
