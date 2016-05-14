/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.koth.Koth;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class KothCapListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCap(PlayerMoveEvent e) {
        if(e.isCancelled()) return;
        Player p = e.getPlayer();
        if(e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockZ() == e.getFrom().getBlockZ() && e.getTo().getBlockY() == e.getFrom().getBlockY()) return;

        if(Factions.getInstance().getKothManager().inKothCap(e.getTo())) {
            if (Factions.getInstance().getKothManager().inKothCap(e.getFrom())) {
                return;//already was in cap, ignore
            }
            Koth koth = Factions.getInstance().getKothManager().getKothCap(e.getTo());

            if (!koth.inCapQueue(p)) {
                koth.addToCapQueue(p);
            }
            if(koth.getCapper() == null) {
                koth.updateCapper();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKnock(PlayerMoveEvent e) {
        if(e.isCancelled()) return;
        Player p = e.getPlayer();
        if(e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockZ() == e.getFrom().getBlockZ() && e.getTo().getBlockY() == e.getFrom().getBlockY()) return;

        for(Koth koth : Factions.getInstance().getKothManager().getKoths().values()) {
            if(koth.isActive() && koth.getCapper() != null && koth.getCapper().getName().equalsIgnoreCase(p.getName())) {
                if(koth.withinCap(e.getFrom()) && !koth.withinCap(e.getTo())) {
                    //Knocked
                    koth.knock();
                }
            }
        }

    }

}
