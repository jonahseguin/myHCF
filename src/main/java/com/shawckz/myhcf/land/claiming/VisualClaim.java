/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.land.claiming;

import lombok.Getter;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@Getter
public class VisualClaim {

    private final String id;
    private Location pos1;
    private Location pos2;

    private VisualPillar pillar1 = null;
    private VisualPillar pillar2 = null;

    private Material material;

    public VisualClaim(String id,Location pos1, Location pos2) {
        this.id = id;
        this.pos1 = pos1;
        this.pos2 = pos2;

        this.material = VisualPillar.randomMaterial();

        if(pos1 != null) {
            this.pillar1 = new VisualPillar(pos1);
        }
        if(pos2 != null) {
            this.pillar2 = new VisualPillar(pos2);
        }
    }

    public VisualClaim(Location pos1, Location pos2) {
        this.id = UUID.randomUUID().toString();
        this.pos1 = pos1;
        this.pos2 = pos2;

        this.material = VisualPillar.randomMaterial();

        if(pos1 != null) {
            this.pillar1 = new VisualPillar(pos1);
        }
        if(pos2 != null) {
            this.pillar2 = new VisualPillar(pos2);
        }
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
        this.pillar1 = new VisualPillar(pos1);
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
        this.pillar2 = new VisualPillar(pos2);
    }

    public void show(Player player) {
        if(pillar1 != null) {
            pillar1.show(player, material);
        }
        if(pillar2 != null) {
            pillar2.show(player, material);
        }
    }

    public void hide(Player player) {
        if(pillar1 != null) {
            pillar1.hide(player);
        }
        if(pillar2 != null) {
            pillar2.hide(player);
        }
    }

}
