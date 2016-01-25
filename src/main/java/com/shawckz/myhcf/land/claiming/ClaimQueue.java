/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  
 * Thank you.
 */

package com.shawckz.myhcf.land.claiming;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@Getter
public class ClaimQueue {

    private final Player player;
    private final Location location;

    public ClaimQueue(Player player, Location location) {
        this.player = player;
        this.location = location;
    }
}
