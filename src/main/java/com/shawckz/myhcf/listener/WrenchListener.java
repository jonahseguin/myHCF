/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.obj.Wrench;
import com.shawckz.myhcf.obj.WrenchSpawner;
import com.shawckz.myhcf.util.HCFException;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class WrenchListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e){
        if(!e.isCancelled()) {
            if(e.getPlayer().getItemInHand() != null && Wrench.isWrench(e.getPlayer().getItemInHand()) ) {
                if(e.getBlock().getType() == Material.MOB_SPAWNER) {
                    e.setCancelled(true);
                    Wrench wrench = Wrench.fromItem(e.getPlayer().getItemInHand());
                    if(wrench != null) {
                        if(wrench.getUses() > 1) {
                            wrench.updateUses(wrench.getUses() - 1);
                            e.getPlayer().setItemInHand(wrench.getItemStack());
                        }
                        else{
                            e.getPlayer().setItemInHand(null);
                            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ITEM_BREAK, 50L, 50L);
                        }

                        CreatureSpawner spawner = (CreatureSpawner) e.getBlock().getState();
                        WrenchSpawner spawn = new WrenchSpawner(spawner.getSpawnedType());
                        e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(), spawn.getItem());
                    }
                    else{
                        throw new HCFException("Could not get wrench correctly");
                    }
                }
            }
        }
    }


}
