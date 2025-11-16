package com.cangsg.brick.kt.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cangsg.brick.kt.entry.KtValidateHandler;
import com.cangsg.brick.kt.handler.KtValidateMapBoundHandler;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@KtValidateHandler(handleBy = KtValidateMapBoundHandler.class)
public @interface KtValidateMapBound {
	/**
	 * Map最大数量，默认50
	 * 
	 * @return Map最大数量
	 */
	public abstract int maxCount() default 50;

	/**
	 * Map最小数量, 默认0
	 * 
	 * @return Map最小数量
	 */
	public abstract int minCount() default 0;

	/**
	 * 验证失败信息
	 * 
	 * @return 验证失败信息
	 */
	public abstract String message() default "";
}
