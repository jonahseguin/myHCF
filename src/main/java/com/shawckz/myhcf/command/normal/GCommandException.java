/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.command.normal;

/**
 * Created by Jonah Seguin on 12/13/2015.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class GCommandException extends RuntimeException {

    public GCommandException() {
    }

    public GCommandException(String message) {
        super(message);
    }

    public GCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public GCommandException(Throwable cause) {
        super(cause);
    }

    public GCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
