package com.cangsg.brick.kt.entry;

import java.util.List;

public final class KtValidateResult<T> {
    private String errorMsg;
    private List<Object> fieldPath;

    public KtValidateResult(String errorMsg, List<Object> fieldPath) {
        this.errorMsg = errorMsg;
        this.setFieldPath(fieldPath);
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public static KtValidateResult<?> build(String errorMsg, List<Object> fieldPath) {
        return new KtValidateResult<>(errorMsg, fieldPath);
    }

    public List<Object> getFieldPath() {
        return fieldPath;
    }

    public void setFieldPath(List<Object> fieldPath) {
        this.fieldPath = fieldPath;
    }

    public boolean hasError() {
        if (errorMsg != null && !errorMsg.isEmpty()) {
            return true;
        }
        return false;
    }

}
