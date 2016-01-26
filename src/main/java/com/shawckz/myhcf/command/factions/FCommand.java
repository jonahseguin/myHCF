/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.command.factions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 360 on 21/07/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FCommand {

    String name();

    String desc();

    String usage() default "";

    String[] aliases() default {};

    boolean playerOnly() default false;

    String perm() default "";

    boolean allowFlags() default false;

    String[] flags() default {};

    int minArgs() default 0;
}