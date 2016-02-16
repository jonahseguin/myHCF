package com.shawckz.myhcf.player.cache;


/*
 * Copyright (c) 2015 Jonah Seguin (Shawckz).  All rights reserved.  You may not modify, decompile, distribute or use any code/text contained in this document(plugin) without explicit signed permission from Jonah Seguin.
 */


import com.shawckz.myhcf.database.AutoDBable;

/**
 * Created by Jonah on 6/11/2015.
 */
public abstract class CachePlayer implements AutoDBable {

    public CachePlayer() {
    }

    public abstract String getName();

    public abstract String getUniqueId();
}