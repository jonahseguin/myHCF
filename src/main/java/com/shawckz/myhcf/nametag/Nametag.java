/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.nametag;


/**
 * Created by 360 on 23/06/2015.
 */
public class Nametag {

    private String name;
    private String prefix;
    private String suffix;
    private boolean canSeeFriendlyInvisibles;
    private boolean allowFriendlyFire;

    public Nametag(String name, String prefix, String suffix) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.canSeeFriendlyInvisibles = false;
        this.allowFriendlyFire = true;
    }

    public Nametag(String name, String prefix, String suffix, boolean canSeeFriendlyInvisibles, boolean allowFriendlyFire) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.canSeeFriendlyInvisibles = canSeeFriendlyInvisibles;
        this.allowFriendlyFire = allowFriendlyFire;
    }

    public boolean isCanSeeFriendlyInvisibles() {
        return canSeeFriendlyInvisibles;
    }

    public void setCanSeeFriendlyInvisibles(boolean canSeeFriendlyInvisibles) {
        this.canSeeFriendlyInvisibles = canSeeFriendlyInvisibles;
    }

    public boolean isAllowFriendlyFire() {
        return allowFriendlyFire;
    }

    public void setAllowFriendlyFire(boolean allowFriendlyFire) {
        this.allowFriendlyFire = allowFriendlyFire;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
