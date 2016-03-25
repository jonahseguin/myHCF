/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.koth;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.database.AutoDBable;
import com.shawckz.myhcf.database.annotations.CollectionName;
import com.shawckz.myhcf.database.annotations.DBColumn;
import com.shawckz.myhcf.database.annotations.DatabaseSerializer;
import com.shawckz.myhcf.database.annotations.JSONDirectory;
import com.shawckz.myhcf.database.serial.LocationSerializer;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.faction.FactionType;
import com.shawckz.myhcf.spawn.WallRadius;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@Getter
@Setter
@CollectionName(name = "myhcfkoths")
@JSONDirectory(name = "koths")
public class Koth implements AutoDBable {

    @DBColumn
    private String name;

    @DBColumn
    @DatabaseSerializer(serializer = LocationSerializer.class)
    private Location pos1;

    @DBColumn
    @DatabaseSerializer(serializer = LocationSerializer.class)
    private Location pos2;

    @DBColumn
    private String factionID;

    private boolean active = false;

    private WallRadius wallRadius;

    public Koth() {
        //for automongo
    }

    public Koth(String name, Location pos1, Location pos2) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.wallRadius = new WallRadius(pos1, pos2);
        final Faction faction = Factions.getInstance().getFactionManager().createFaction(name, FactionType.KOTH);
        this.factionID = faction.getId();
        Factions.getInstance().getFactionManager().addToCache(faction);
        new BukkitRunnable() {
            @Override
            public void run() {
                Factions.getInstance().getDbHandler().push(faction);
            }
        }.runTaskAsynchronously(Factions.getInstance());
        wallRadius.updateCache();
    }
}
