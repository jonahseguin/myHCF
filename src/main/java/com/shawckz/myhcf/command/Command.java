package com.shawckz.myhcf.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 360 on 21/07/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {

    String name();

    String desc();

    String usage() default "";

    String[] aliases() default {};

    boolean playerOnly() default true;

    String perm() default "";

    boolean allowFlags() default false;

    String[] flags() default {};

    int minArgs() default 0;
}