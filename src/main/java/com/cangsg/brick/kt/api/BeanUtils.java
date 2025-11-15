package com.cangsg.brick.kt.api;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class BeanUtils {
    static final ConcurrentMap<Class<?>, Map<String, PropertyDescriptor>> strongClassCache = new ConcurrentHashMap<>(
            64);

    private static Map<String, PropertyDescriptor> resolveAllPropertyDescriptors(Class<?> clazz) {
        Map<String, PropertyDescriptor> propertyMap = new HashMap<>();
        try {
            // 获取 JavaBean 信息（会解析类的所有属性，包含继承自父类的属性）
            // 第二个参数：忽略 Object 类的属性（避免解析 getClass() 等方法）
            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();

            // 遍历属性描述符，按属性名存入映射
            for (PropertyDescriptor descriptor : descriptors) {
                propertyMap.put(descriptor.getName(), descriptor);
            }
        } catch (IntrospectionException e) {
            // 解析失败（如类不是 JavaBean，无公共 getter/setter 等），打印日志并返回空映射
            System.err.printf("解析类 [%s] 的 PropertyDescriptor 失败：%s%n", clazz.getName(), e.getMessage());
        }
        return propertyMap;
    }

    public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String propertyName) {
        if (clazz == null || propertyName == null || propertyName.isBlank()) {
            throw new IllegalArgumentException("Class 和属性名不能为空");
        }

        // 1. 从缓存获取该类的属性描述符映射，若不存在则初始化并缓存
        Map<String, PropertyDescriptor> propertyMap = strongClassCache.computeIfAbsent(clazz,
                BeanUtils::resolveAllPropertyDescriptors);

        // 2. 返回指定属性的描述符（不存在则返回 null）
        return propertyMap.get(propertyName);
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class 不能为空");
        }

        // 从缓存获取该类的属性映射，不存在则初始化
        Map<String, PropertyDescriptor> propertyMap = strongClassCache.computeIfAbsent(clazz,
                BeanUtils::resolveAllPropertyDescriptors);
        // 转换为数组返回（保持解析时的顺序，与 Introspector 解析结果一致）
        return propertyMap.values().toArray(new PropertyDescriptor[0]);
    }

}
