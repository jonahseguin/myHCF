/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.auth;

public enum XAuthEvent {

    AUTHORIZE_RESULT("auth:result"),
    AUTHORIZE_REQUEST("auth:request")

    ;

    private final String name;

    XAuthEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}