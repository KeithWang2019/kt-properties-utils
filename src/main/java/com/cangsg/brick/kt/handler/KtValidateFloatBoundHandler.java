package com.cangsg.brick.kt.handler;

import com.cangsg.brick.kt.annotation.KtValidateFloatBound;
import com.cangsg.brick.kt.entry.KtValidateResult;
import com.cangsg.brick.kt.entry.KtValidator;

public final class KtValidateFloatBoundHandler extends KtValidator<KtValidateFloatBound, Float> {

    @Override
    public KtValidateResult<?> call(Float val, KtValidateFloatBound annotation) {
        if (val == null) {
            return null;
        }

        float floatValue = (float) val;

        if (annotation.min() <= floatValue && floatValue <= annotation.max()) {
            return null;
        }
        return KtValidateResult.build(annotation.message(), this.getFieldPath());
    }

}
