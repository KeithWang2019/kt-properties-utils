package com.cangsg.brick.kt.handler;

import com.cangsg.brick.kt.annotation.KtValidateOptions;
import com.cangsg.brick.kt.entry.KtValidateResult;
import com.cangsg.brick.kt.entry.KtValidator;

public final class KtValidateOptionsHandler extends KtValidator<KtValidateOptions, String> {

    @Override
    public KtValidateResult<?> call(String val, KtValidateOptions annotation) {
        if (val == null) {
            return null;
        }
        for (String option : annotation.options()) {
            if (option.equals(val)) {
                return null;
            }
        }
        return KtValidateResult.build(annotation.message(), this.getFieldPath());
    }

}
