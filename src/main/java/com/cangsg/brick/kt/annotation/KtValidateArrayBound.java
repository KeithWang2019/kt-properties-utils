package com.cangsg.brick.kt.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cangsg.brick.kt.entry.KtValidateHandler;
import com.cangsg.brick.kt.handler.KtValidateArrayBoundHandler;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@KtValidateHandler(handleBy = KtValidateArrayBoundHandler.class)
public @interface KtValidateArrayBound {
	/**
	 * 数组最大数量，默认50
	 * 
	 * @return 数组最大数量
	 */
	public abstract int maxCount() default 50;

	/**
	 * 数组最小数量, 默认0
	 * 
	 * @return 数组最小数量
	 */
	public abstract int minCount() default 0;

	/**
	 * 验证失败信息
	 * 
	 * @return 验证失败信息
	 */
	public abstract String message() default "";
}
