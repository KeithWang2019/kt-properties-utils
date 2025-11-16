package com.cangsg.brick.kt.entry;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KtTest22<T> {
    private Map<String, T> mp1;
}
