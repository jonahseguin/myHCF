package com.shawckz.myhcf.util;

public class HCFException extends RuntimeException {

    public HCFException() {
    }

    public HCFException(String message) {
        super(message);
    }

    public HCFException(String message, Throwable cause) {
        super(message, cause);
    }

    public HCFException(Throwable cause) {
        super(cause);
    }

    public HCFException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
