/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.myscoreboard.util;

/**
 * Created by jonahseguin on 2016-07-22.
 */
public class MyScoreboardException extends RuntimeException{

    public MyScoreboardException(String message) {
        super(message);
    }


    public MyScoreboardException(String message, Throwable cause) {
        super(message, cause);
    }

}
