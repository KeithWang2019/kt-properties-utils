package com.cangsg.brick.kt.api;

import java.util.List;

public class KtListPropertiesChain<T, V> {
    private KtPropertiesChain<KtCopyListObject<T>, KtCopyListObject<V>> chain;

    public KtListPropertiesChain(List<T> source, List<V> target) {
        KtCopyListObject<T> sourCopyObject = new KtCopyListObject<>();
        sourCopyObject.setList(source);
        KtCopyListObject<V> targetCopyObject = new KtCopyListObject<>();
        targetCopyObject.setList(target);

        this.chain = new KtPropertiesChain<KtCopyListObject<T>, KtCopyListObject<V>>(sourCopyObject, targetCopyObject);
    }

    public KtListPropertiesChain<T, V> mapType(Class<?> sourceType, Class<?> targetType) {
        this.chain.mapType(sourceType, targetType);
        return this;
    }

    public KtListPropertiesChain<T, V> mapValue(KtMapValueHandler valueHandler) {
        this.chain.mapValue(valueHandler);
        return this;
    }

    public List<V> build() {
        KtCopyListObject<V> copyObject = this.chain.build();

        return copyObject.getList();
    }
}
