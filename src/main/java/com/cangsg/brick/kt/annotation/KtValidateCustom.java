package com.cangsg.brick.kt.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cangsg.brick.kt.entry.KtValidateHandler;
import com.cangsg.brick.kt.entry.KtValidator;
import com.cangsg.brick.kt.handler.KtValidateCustomHandler;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@KtValidateHandler(handleBy = KtValidateCustomHandler.class)
public @interface KtValidateCustom {
    @SuppressWarnings("rawtypes")
	Class<? extends KtValidator> handleBy();

    /**
	 * 验证失败信息
	 * 
	 * @return 验证失败信息
	 */
	public abstract String message() default "";
}
