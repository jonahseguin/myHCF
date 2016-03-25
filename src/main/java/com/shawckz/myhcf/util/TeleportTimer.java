/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.util;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportTimer implements Listener  {

    private Player player;
    private Location location;
    private int time;
    private boolean moved = false;

    public TeleportTimer(Player player, Location location, int time) {
        this.player = player;
        this.location = location;
        this.time = time;
    }

    public void run() {
        Bukkit.getPluginManager().registerEvents(this, Factions.getInstance());
        msgPlayer(FLang.format(FactionLang.PLAYER_TELEPORT, time+""));
        new BukkitRunnable(){
            @Override
            public void run() {
                if(moved) {
                    cancel();
                    unregister();
                    return;
                }
                if(time > 0) {
                    time--;
                }
                else{
                    if(player != null) {
                        player.teleport(location);
                    }
                    unregister();
                    cancel();
                }
            }
        }.runTaskTimer(Factions.getInstance(), 20L, 20L);
    }

    //Overridable
    public void onTick(){

    }

    private void unregister() {
        HandlerList.unregisterAll(this);
    }

    private void msgPlayer(String msg){
        if(player != null) {
            player.sendMessage(msg);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent e) {
        if(e.isCancelled()) return;
        if(e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) return;
        if(e.getPlayer().getName().equals(player.getName())) {
            if(!moved) {
                moved = true;
                msgPlayer(FLang.format(FactionLang.PLAYER_TELEPORT_MOVED));
                unregister();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent e) {
        if(e.isCancelled()) return;
        if(e.getEntity() instanceof Player) {
            Player en = (Player) e.getEntity();
            if(en.getName().equals(player.getName())) {
                if(!moved) {
                    moved = true;
                    msgPlayer(FLang.format(FactionLang.PLAYER_TELEPORT_MOVED));
                    unregister();
                }
            }
        }
    }
    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public int getTime() {
        return time;
    }

    public boolean isMoved() {
        return moved;
    }
}
