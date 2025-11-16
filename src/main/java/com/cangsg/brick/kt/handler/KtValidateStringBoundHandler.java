package com.cangsg.brick.kt.handler;

import com.cangsg.brick.kt.annotation.KtValidateStringBound;
import com.cangsg.brick.kt.entry.KtValidateResult;
import com.cangsg.brick.kt.entry.KtValidator;

public final class KtValidateStringBoundHandler extends KtValidator<KtValidateStringBound, String> {

    @Override
    public KtValidateResult<?> call(String val, KtValidateStringBound annotation) throws RuntimeException {
        int currentLength = 0;
        if (val == null || val.isEmpty()) {
            return null;
        }
        currentLength = val.length();

        if (annotation.minLength() <= currentLength && currentLength <= annotation.maxLength()) {
            return null;
        }
        return KtValidateResult.build(annotation.message(), this.getFieldPath());
    }

}
