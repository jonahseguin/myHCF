/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * Created by Jonah Seguin on 1/25/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class ExpMultiplierListener implements Listener {

    int multiplier = Factions.getInstance().getFactionsConfig().getExpMultiplier();

    @EventHandler
    public void onKillMob(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            e.setDroppedExp(e.getDroppedExp() * multiplier);
        }
    }

}
