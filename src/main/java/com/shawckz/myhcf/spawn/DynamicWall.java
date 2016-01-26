/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.spawn;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jonah Seguin on 1/25/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class DynamicWall {

    private final WallRadius wallRadius;

    public DynamicWall(WallRadius wallRadius) {
        this.wallRadius = wallRadius;

    }

    public Set<Location> getNear(Player player, int radius, int max) {
        final Location centre = player.getLocation();
        int count = 0;
        Set<Location> near = new HashSet<>();
        for (Location loc : wallRadius.getCache()) {
            if (max > 0 && count >= max) {
                break;
            }
            if (!wallRadius.hasBlock(player, loc)) {
                if (loc.distanceSquared(centre) <= radius) {
                    near.add(loc);
                    count++;
                }
            }
        }
        return near;
    }

}
