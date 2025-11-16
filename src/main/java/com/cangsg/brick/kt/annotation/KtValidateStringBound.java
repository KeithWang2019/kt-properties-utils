package com.cangsg.brick.kt.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cangsg.brick.kt.entry.KtValidateHandler;
import com.cangsg.brick.kt.handler.KtValidateStringBoundHandler;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@KtValidateHandler(handleBy = KtValidateStringBoundHandler.class)
public @interface KtValidateStringBound {
    /**
     * 字符串最大长度，默认50
     * 
     * @return 字符串最大长度
     */
    public abstract int maxLength() default 50;

    /**
     * 字符串最小长度, 默认0
     * 
     * @return 字符串最小长度
     */
    public abstract int minLength() default 0;

    /**
     * 验证失败信息
     * 
     * @return 验证失败信息
     */
    public abstract String message() default "";
}
