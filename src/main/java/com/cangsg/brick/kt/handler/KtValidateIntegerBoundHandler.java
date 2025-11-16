package com.cangsg.brick.kt.handler;

import com.cangsg.brick.kt.annotation.KtValidateIntegerBound;
import com.cangsg.brick.kt.entry.KtValidateResult;
import com.cangsg.brick.kt.entry.KtValidator;

public final class KtValidateIntegerBoundHandler extends KtValidator<KtValidateIntegerBound, Integer> {

    @Override
    public KtValidateResult<?> call(Integer val, KtValidateIntegerBound annotation) {
        if (val == null) {
            return null;
        }
        if (annotation.min() <= val && val <= annotation.max()) {
            return null;
        }

        return KtValidateResult.build(annotation.message(), this.getFieldPath());
    }

}
