package com.cangsg.brick.kt.handler;

import java.util.Map;

import com.cangsg.brick.kt.annotation.KtValidateMapBound;
import com.cangsg.brick.kt.entry.KtValidateResult;
import com.cangsg.brick.kt.entry.KtValidator;

public final class KtValidateMapBoundHandler extends KtValidator<KtValidateMapBound, Map<?, ?>> {

    @Override
    public KtValidateResult<?> call(Map<?, ?> val, KtValidateMapBound annotation) {
        int currentArrayLength = 0;
        if (val != null) {
            currentArrayLength = val.size();
        }
        if (annotation.minCount() <= currentArrayLength && currentArrayLength <= annotation.maxCount()) {
            return null;
        }
        return KtValidateResult.build(annotation.message(), this.getFieldPath());
    }

}
