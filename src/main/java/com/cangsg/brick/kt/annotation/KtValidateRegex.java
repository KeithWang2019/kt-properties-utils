package com.cangsg.brick.kt.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cangsg.brick.kt.entry.KtValidateHandler;
import com.cangsg.brick.kt.handler.KtValidateRegexHandler;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@KtValidateHandler(handleBy = KtValidateRegexHandler.class)
public @interface KtValidateRegex {
    /**
     * 正则表达式
     * 
     * @return 正则表达式
     */
    public abstract java.lang.String regex();

    /**
     * 验证失败信息
     * 
     * @return 验证失败信息
     */
    public abstract String message() default "";
}
