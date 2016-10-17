package com.xieyu.ecar.injector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by cheng on 13-10-27.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface V {
	public int vid() default -1;
	public String click() default "";
	public String itemClick() default "";
	public String itemLongClick() default "";
}
