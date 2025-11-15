package com.cangsg.brick.kt.api;

import java.util.Map;

public class KtMapPropertiesChain<T, V> {
    private KtPropertiesChain<KtCopyMapObject<T>, KtCopyMapObject<V>> chain;

    public KtMapPropertiesChain(Map<String, T> source, Map<String, V> target) {
        KtCopyMapObject<T> sourCopyObject = new KtCopyMapObject<>();
        sourCopyObject.setMap(source);
        KtCopyMapObject<V> targetCopyObject = new KtCopyMapObject<>();
        targetCopyObject.setMap(target);

        this.chain = new KtPropertiesChain<KtCopyMapObject<T>, KtCopyMapObject<V>>(sourCopyObject, targetCopyObject);
    }

    public KtMapPropertiesChain<T, V> mapType(Class<?> sourceType, Class<?> targetType) {
        this.chain.mapType(sourceType, targetType);
        return this;
    }

    public KtMapPropertiesChain<T, V> mapValue(KtMapValueHandler valueHandler) {
        this.chain.mapValue(valueHandler);
        return this;
    }

    public Map<String, V> build() {
        KtCopyMapObject<V> copyObject = this.chain.build();

        return copyObject.getMap();
    }
}
