package com.nttdata.mock.mms.api.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {

	public enum Level {
		DEBUG, INFO, WARN, ERROR
	}

	public String[] exclusions() default {};

	public boolean logParameters() default true;

	public Level level() default Level.INFO;

	boolean skipResult() default false;

	boolean skipArgs() default false;
}