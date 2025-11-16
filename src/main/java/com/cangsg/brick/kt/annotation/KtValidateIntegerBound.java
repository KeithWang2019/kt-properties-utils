package com.cangsg.brick.kt.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cangsg.brick.kt.entry.KtValidateHandler;
import com.cangsg.brick.kt.handler.KtValidateIntegerBoundHandler;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@KtValidateHandler(handleBy = KtValidateIntegerBoundHandler.class)
public @interface KtValidateIntegerBound {
	/**
	 * 最大数，默认50
	 * 
	 * @return 最大数
	 */
	public abstract int max() default 50;

	/**
	 * 最小数, 默认0
	 * 
	 * @return 最小数
	 */
	public abstract int min() default 0;

	/**
	 * 验证失败信息
	 * 
	 * @return 验证失败信息
	 */
	public abstract String message() default "";
}
