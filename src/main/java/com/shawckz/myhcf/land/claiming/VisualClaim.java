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
public class VisualClaim {

    private final Location pos1;
    private final Location pos2;

    private final VisualPillar pillar1;
    private final VisualPillar pillar2;

    public VisualClaim(Location pos1, Location pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;

        this.pillar1 = new VisualPillar(pos1);
        this.pillar2 = new VisualPillar(pos2);
    }

    public void show(Player player) {
        pillar1.show(player);
        pillar2.show(player);
    }

    public void hide(Player player) {
        pillar1.hide(player);
        pillar2.hide(player);
    }

}
