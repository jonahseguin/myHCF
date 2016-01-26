/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.util;

import org.bukkit.Location;

/**
 * Created by Jonah Seguin on 1/25/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class FSelection {

    private final Location min;
    private final Location max;

    public FSelection(Location min, Location max) {
        this.min = min;
        this.max = max;
    }

    public Location getMin() {
        return min;
    }

    public Location getMax() {
        return max;
    }
}
