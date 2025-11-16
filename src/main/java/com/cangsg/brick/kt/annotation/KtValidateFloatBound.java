package com.cangsg.brick.kt.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cangsg.brick.kt.entry.KtValidateHandler;
import com.cangsg.brick.kt.handler.KtValidateFloatBoundHandler;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@KtValidateHandler(handleBy = KtValidateFloatBoundHandler.class)
public @interface KtValidateFloatBound {
    /**
	 * 最大值, 默认0
	 * 
	 * @return 最小值
	 */
	public abstract float max() default 0;

	/**
	 * 最小, 默认0
	 * 
	 * @return 最小值
	 */
	public abstract float min() default 0;

	/**
	 * 验证失败信息
	 * 
	 * @return 验证失败信息
	 */
	public abstract String message() default "";
}
