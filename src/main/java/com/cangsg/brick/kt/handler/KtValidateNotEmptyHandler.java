package com.cangsg.brick.kt.handler;

import com.cangsg.brick.kt.annotation.KtValidateNotEmpty;
import com.cangsg.brick.kt.entry.KtValidateResult;
import com.cangsg.brick.kt.entry.KtValidator;

public final class KtValidateNotEmptyHandler extends KtValidator<KtValidateNotEmpty, Object> {

    @Override
    public KtValidateResult<?> call(Object val, KtValidateNotEmpty annotation) {
        if (val instanceof String) {
            String valString = (String) val;
            if (valString != null && !valString.isEmpty()) {
                return null;
            }
        } else {
            if (val != null) {
                return null;
            }
        }
        
        return KtValidateResult.build(annotation.message(), this.getFieldPath());
    }

}