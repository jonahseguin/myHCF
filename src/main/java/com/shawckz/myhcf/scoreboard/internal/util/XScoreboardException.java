package com.shawckz.myhcf.scoreboard.internal.util;

public class XScoreboardException extends RuntimeException {

    public XScoreboardException() {
    }

    public XScoreboardException(String message) {
        super(message);
    }

    public XScoreboardException(String message, Throwable cause) {
        super(message, cause);
    }

    public XScoreboardException(Throwable cause) {
        super(cause);
    }

    public XScoreboardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
