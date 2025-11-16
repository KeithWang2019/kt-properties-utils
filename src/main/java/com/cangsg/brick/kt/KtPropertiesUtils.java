package com.cangsg.brick.kt;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cangsg.brick.kt.api.KtListPropertiesChain;
import com.cangsg.brick.kt.api.KtMapPropertiesChain;
import com.cangsg.brick.kt.api.KtPropertiesChain;

public class KtPropertiesUtils {
    private KtPropertiesUtils() {
        throw new IllegalStateException("KtPropertiesUtils class");
    }

    public static <T, V> KtPropertiesChain<T, V> copyProperties(T source, V target) {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        return new KtPropertiesChain<>(source, target).mapType(sourceClass,
                targetClass);
    }

    public static <T, V> KtPropertiesChain<T, V> copyProperties(T source, Class<V> clazz) {
        try {
            V target = clazz.getDeclaredConstructor().newInstance();
            return new KtPropertiesChain<>(source, target).mapType(source.getClass(), clazz);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T, V> KtListPropertiesChain<T, V> copyProperties(List<T> source, List<V> target) {
        return new KtListPropertiesChain<T, V>(source, target);
    }

    public static <T, V> KtListPropertiesChain<T, V> copyProperties(List<T> source, Class<V> clazz) {
        List<V> list = new ArrayList<V>();
        if (source.isEmpty()) {
            return (new KtListPropertiesChain<T, V>(source, list));
        }
        return (new KtListPropertiesChain<T, V>(source, list)).mapType(source.getFirst().getClass(), clazz);
    }

    public static <T, V> KtMapPropertiesChain<T, V> copyProperties(Map<String, T> source, Map<String, V> target) {
        return new KtMapPropertiesChain<T, V>(source, target);
    }

    public static <T, V> KtMapPropertiesChain<T, V> copyProperties(Map<String, T> source, Class<V> clazz) {
        Map<String, V> map = new HashMap<String, V>();
        if (source.isEmpty()) {
            return (new KtMapPropertiesChain<T, V>(source, map));
        }
        return (new KtMapPropertiesChain<T, V>(source, map)).mapType(source.values().toArray()[0].getClass(), clazz);
    }
}
