/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.koth;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.database.AutoDBable;
import com.shawckz.myhcf.database.annotations.CollectionName;
import com.shawckz.myhcf.database.annotations.DBColumn;
import com.shawckz.myhcf.database.annotations.DatabaseSerializer;
import com.shawckz.myhcf.database.serial.LocationSerializer;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.scoreboard.hcf.FLabelType;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimerFormat;
import com.shawckz.myhcf.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
@AllArgsConstructor
@CollectionName(name = "myhcf_koths")
public class Koth implements AutoDBable {

    @DBColumn(name = "_id", identifier = true)
    private String id = UUID.randomUUID().toString();

    @DBColumn
    private String name;

    @DBColumn
    private int capTime = 1200;

    @DBColumn @DatabaseSerializer(serializer = LocationSerializer.class) private Location pos1 = null;
    @DBColumn @DatabaseSerializer(serializer = LocationSerializer.class) private Location pos2 = null;
    @DBColumn @DatabaseSerializer(serializer = LocationSerializer.class) private Location capPos1 = null;
    @DBColumn @DatabaseSerializer(serializer = LocationSerializer.class) private Location capPos2 = null;

    private FLabel label = new FLabel("&b" + name + "&7: &6", FLabelType.TIMER, HCFTimerFormat.MM_SS);

    private HCFPlayer capper = null;

    private ConcurrentLinkedQueue<String> capQueue = new ConcurrentLinkedQueue<>();

    private boolean active = false;

    private KothTimer timer = null;

    public Koth() {
        this.id = UUID.randomUUID().toString();
        this.label = new FLabel("&b" + name + "&7: &6", FLabelType.TIMER, HCFTimerFormat.MM_SS);
    }

    public Koth(String name, Location pos1, Location pos2) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.label = new FLabel("&b" + name + "&7: &6", FLabelType.TIMER, HCFTimerFormat.MM_SS);
    }

    public void updateLabel() {
        this.label = new FLabel("&b" + name + "&7: &6", FLabelType.TIMER, HCFTimerFormat.MM_SS);
    }

    private boolean within(Location loc, Location p1, Location p2, boolean useY) {
        final int maxX = Math.max(p1.getBlockX(), p2.getBlockX());
        final int maxZ = Math.max(p1.getBlockZ(), p2.getBlockZ());
        final int maxY = Math.max(p1.getBlockY(), p2.getBlockY());

        final int minX = Math.min(p1.getBlockX(), p2.getBlockX());
        final int minZ = Math.min(p1.getBlockZ(), p2.getBlockZ());
        final int minY = Math.min(p1.getBlockY(), p2.getBlockY());

        final int x = loc.getBlockX();
        final int z = loc.getBlockZ();
        final int y = loc.getBlockY();

        return (x <= maxX) && (x >= minX) && (z <= maxZ) && (z >= minZ) && (!useY || (y <= maxY && y >= minY));
    }

    public boolean withinCap(final Location loc) {
        return within(loc, capPos1, capPos2, true);
    }

    public boolean withinKoth(final Location loc) {
        return within(loc, pos1, pos2, false);
    }

    public boolean inCapQueue(Player player) {
        return capQueue.contains(player.getName());
    }

    public void addToCapQueue(Player player) {
        if(!capQueue.contains(player.getName())) {
            capQueue.add(player.getName());
        }
    }

    public void removeFromCapQueue(Player player) {
        if(capQueue.contains(player.getName())) {
            capQueue.remove(player.getName());
        }
    }

    public void updateCapper() {
        if(capper != null) {
            if(capper.getBukkitPlayer() != null && capper.getBukkitPlayer().isOnline()) {
                if(!withinCap(capper.getBukkitPlayer().getLocation())) {
                    updateCapperToNext();
                }
            }
            else{
                updateCapperToNext();
            }
        }
        else{
           updateCapperToNext();
        }
    }

    public void knock() {
        if(capper != null) {
            removeFromCapQueue(capper.getBukkitPlayer());
        }
        capper = null;
        getTimer().setTime(capTime);
        Bukkit.broadcastMessage(FLang.format(FactionLang.KOTH_KNOCK, name));
        updateCapper();
    }

    private void updateCapperToNext() {
        String next = capQueue.peek();
        if(next != null) {
            Player p = Bukkit.getPlayer(next);
            if(p != null && p.isOnline()) {
                if(capper == null || !capper.getName().equalsIgnoreCase(p.getName())) {
                    capper = Factions.getInstance().getCache().getHCFPlayer(p);
                    Bukkit.broadcastMessage(FLang.format(FactionLang.KOTH_CAP_START, name, capper.getName(), TimeUtil.formatTime(timer.getTime() * 1000)));
                }
            }
        }
    }

}
