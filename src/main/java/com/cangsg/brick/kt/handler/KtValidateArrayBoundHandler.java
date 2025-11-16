package com.cangsg.brick.kt.handler;

import java.lang.reflect.Array;

import com.cangsg.brick.kt.annotation.KtValidateArrayBound;
import com.cangsg.brick.kt.entry.KtValidateResult;
import com.cangsg.brick.kt.entry.KtValidator;

public final class KtValidateArrayBoundHandler extends KtValidator<KtValidateArrayBound, Object> {
    @Override
    public KtValidateResult<?> call(Object arr, KtValidateArrayBound annotation) {
        int currentArrayLength = Array.getLength(arr);

        if (annotation.minCount() <= currentArrayLength && currentArrayLength <= annotation.maxCount()) {
            return null;
        }

        return KtValidateResult.build(annotation.message(), this.getFieldPath());
    }
}
