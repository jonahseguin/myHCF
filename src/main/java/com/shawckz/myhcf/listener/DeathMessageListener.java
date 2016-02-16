/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathMessageListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() != null) {
            e.setDeathMessage(FLang.format(FactionLang.DEATH_MESSAGE_PLAYER, e.getEntity().getName(), e.getEntity().getKiller().getName()));
        }
        else{
            e.setDeathMessage(FLang.format(FactionLang.DEATH_MESSAGE, e.getEntity().getName()));
        }
    }

}
