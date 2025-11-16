package com.cangsg.brick.kt.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cangsg.brick.kt.entry.KtValidateHandler;
import com.cangsg.brick.kt.handler.KtValidateAfterDateHandler;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@KtValidateHandler(handleBy = KtValidateAfterDateHandler.class)
public @interface KtValidateAfterDate {
    /**
     * 验证失败信息
     * 
     * @return 验证失败信息
     */
    public abstract String message() default "";

    /**
     * 同层属性名
     * 
     * @return 属性名
     */
    public abstract String propertyName() default "";
}