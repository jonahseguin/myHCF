/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Created by Jonah Seguin on 1/23/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class FoundDiamondsListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (!event.isCancelled() && event.getBlock().getType() == Material.DIAMOND_ORE) {
            event.getBlock().setMetadata("DiamondPlaced", new FixedMetadataValue(Factions.getInstance(), true));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(final BlockBreakEvent event) {
        if (!event.isCancelled() && event.getBlock().getType() == Material.DIAMOND_ORE && !event.getBlock().hasMetadata("DiamondPlaced")) {
            int diamonds = 0;
            for (int x = -5; x < 5; ++x) {
                for (int y = -5; y < 5; ++y) {
                    for (int z = -5; z < 5; ++z) {
                        final Block block = event.getBlock().getLocation().add(x, y, z).getBlock();
                        if (block.getType() == Material.DIAMOND_ORE && !block.hasMetadata("DiamondPlaced")) {
                            ++diamonds;
                            block.setMetadata("DiamondPlaced", new FixedMetadataValue(Factions.getInstance(), true));
                        }
                    }
                }
            }
            Bukkit.broadcastMessage(FLang.format(FactionLang.FOUND_DIAMONDS_BROADCAST,
                    event.getPlayer().getName(), "" + diamonds + " diamond" + (diamonds > 1 ? "s" : "")));
        }
    }

}
