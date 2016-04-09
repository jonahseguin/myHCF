/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.land;

import com.shawckz.myhcf.land.claiming.VisualClaim;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClaimSelector implements Listener {

    private Map<String, VisualClaim> selections = new HashMap<>();

    public VisualClaim getSelection(Player player) {
        if(selections.containsKey(player.getName())) {
            return selections.get(player.getName());
        }
        else{
            VisualClaim visualClaim = new VisualClaim(null, null);
            selections.put(player.getName(), visualClaim);
            return visualClaim;
        }
    }

    public void cancelSelection(Player player) {
        if(selections.containsKey(player.getName())) {
            selections.get(player.getName()).hide(player);
            selections.remove(player.getName());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(p.getItemInHand().getType() == Material.STICK) {
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Location target = e.getClickedBlock().getLocation();
                getSelection(p).setPos1(target);
                getSelection(p).show(p);
            }
            else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                Location target = e.getClickedBlock().getLocation();
                getSelection(p).setPos2(target);
                getSelection(p).show(p);
            }
        }
    }

}
