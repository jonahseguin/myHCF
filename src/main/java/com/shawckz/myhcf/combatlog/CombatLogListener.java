/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.combatlog;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CombatLogListener implements Listener {

    @EventHandler
    public void onCombatLog(PlayerQuitEvent e) {
        //TODO
    }

    @EventHandler
    public void onNPCDeath(EntityDeathEvent e) {
        //TODO
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        //Despawn NPC if its still there
        //Also kill the player if their NPC was killed - add to database list when their npc is killed or something
    }

}
