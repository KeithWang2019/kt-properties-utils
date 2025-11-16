package com.cangsg.brick.kt.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import com.cangsg.brick.kt.annotation.KtValidateAfterDate;
import com.cangsg.brick.kt.entry.KtValidateResult;
import com.cangsg.brick.kt.entry.KtValidator;

public final class KtValidateAfterDateHandler extends KtValidator<KtValidateAfterDate, Object> {

    @Override
    public KtValidateResult<?> call(Object val, KtValidateAfterDate annotation) {
        if (val instanceof Date) {
            Date afterdDate = (Date) val;
            Date beforeDate = getPreconditionValue(0, annotation.propertyName());
            if (beforeDate.before(afterdDate)) {
                return null;
            }
        } else if (val instanceof Calendar) {
            Calendar afterdDate = (Calendar) val;
            Calendar beforeDate = getPreconditionValue(0, annotation.propertyName());
            if (beforeDate.before(afterdDate)) {
                return null;
            }
        } else if (val instanceof LocalDate){
            LocalDate afterdDate = (LocalDate) val;
            LocalDate beforeDate = getPreconditionValue(0, annotation.propertyName());
            if (beforeDate.isBefore(afterdDate)) {
                return null;
            }
        } else if (val instanceof LocalDateTime){
            LocalDateTime afterdDate = (LocalDateTime) val;
            LocalDateTime beforeDate = getPreconditionValue(0, annotation.propertyName());
            if (beforeDate.isBefore(afterdDate)) {
                return null;
            }
        }
        return KtValidateResult.build(annotation.message(), this.getFieldPath());
    }

}
