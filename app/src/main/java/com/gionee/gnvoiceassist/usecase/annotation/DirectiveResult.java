package com.gionee.gnvoiceassist.usecase.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注处理普通Directive回复的方法
 */

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
@Inherited
public @interface DirectiveResult {
    String value() default "";
}
