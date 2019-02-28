package com.ht.common.annotations.dto;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryColumn {
	QueryType value();
	String column() default "";
}
