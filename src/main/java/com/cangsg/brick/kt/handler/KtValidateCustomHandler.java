package com.cangsg.brick.kt.handler;

import java.lang.reflect.InvocationTargetException;

import com.cangsg.brick.kt.annotation.KtValidateCustom;
import com.cangsg.brick.kt.entry.KtValidateResult;
import com.cangsg.brick.kt.entry.KtValidator;

public final class KtValidateCustomHandler extends KtValidator<KtValidateCustom, Object> {
    protected KtValidator validator;

    @Override
    public final boolean init(KtValidateCustom annotation) {
        try {
            validator = annotation.handleBy().getDeclaredConstructor().newInstance();
            validator.discover(fieldName, depath, depthTreeNode);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("自定义验证处理程序异常", e);
        }
        return true;
    }

    @Override
    public final KtValidateResult<?> call(Object val, KtValidateCustom annotation) throws RuntimeException {
        return validator.call(val, annotation);
    }

}
