/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.armorclass.MagicItem;
import com.shawckz.myhcf.player.HCFPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class MagicItemListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);
            if(player.getArmorClassType() != null) {
                for(MagicItem magicItem : Factions.getInstance().getArmorClassManager().getArmorClass(player.getArmorClassType()).getMagicItems()) {
                    if(magicItem.getMaterial() == p.getItemInHand().getType()) {
                        if(magicItem.applicable(player)) {
                            magicItem.applyMagicItem(player);
                        }
                    }
                }
            }
        }
    }

}
