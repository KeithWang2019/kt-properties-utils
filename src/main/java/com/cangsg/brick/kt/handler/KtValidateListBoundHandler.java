package com.cangsg.brick.kt.handler;

import java.util.List;

import com.cangsg.brick.kt.annotation.KtValidateListBound;
import com.cangsg.brick.kt.entry.KtValidateResult;
import com.cangsg.brick.kt.entry.KtValidator;

public final class KtValidateListBoundHandler extends KtValidator<KtValidateListBound, List<?>> {

    @Override
    public KtValidateResult<?> call(List<?> val, KtValidateListBound annotation) {
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
