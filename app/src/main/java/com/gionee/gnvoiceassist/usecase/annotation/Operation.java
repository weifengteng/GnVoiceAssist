package com.gionee.gnvoiceassist.usecase.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注进行实际操作的方法
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface Operation {
    String value() default "";
}
