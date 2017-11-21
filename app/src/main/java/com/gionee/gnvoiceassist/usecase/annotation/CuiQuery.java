package com.gionee.gnvoiceassist.usecase.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注发起自定义多轮交互的方法
 */

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface CuiQuery {
    String value() default "";
}