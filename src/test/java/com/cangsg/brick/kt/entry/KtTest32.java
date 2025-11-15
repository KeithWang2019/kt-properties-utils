package com.cangsg.brick.kt.entry;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KtTest32<T> {
    private List<T> items;
}
