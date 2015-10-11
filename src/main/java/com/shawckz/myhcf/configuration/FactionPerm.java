package com.shawckz.myhcf.configuration;

public enum FactionPerm {

    BYPASS_BORDER("myhcf.bypassborder")
    ;

    private final String perm;

    FactionPerm(String perm) {
        this.perm = perm;
    }

    public String getPerm() {
        return perm;
    }
}
