/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.combatlog;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.util.HCFException;
import com.shawckz.myhcf.util.PlayerUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CombatLogListener implements Listener {

    @EventHandler
    public void onCombatLog(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);
        Factions.getInstance().getCombatLogManager().spawnLogger(p);//We want to always spawn it
    }

    @EventHandler
    public void onNPCDeath(EntityDeathEvent e) {
        if(e.getEntity() instanceof HumanEntity) {
            HumanEntity p = (HumanEntity) e.getEntity();
            if(p.getName().startsWith("Logger-")) {
                CombatLogger logger = Factions.getInstance().getCombatLogManager().getLogger(p.getName().split("-")[1]);
                if(logger != null) {
                    logger.setDead();
                    logger.getItems().stream().filter(itemStack1 -> itemStack1 != null && itemStack1.getType() != Material.AIR).forEach(itemStack -> e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), itemStack));
                    HCFPlayer player = Factions.getInstance().getCache().getHCFPlayerByUUID(logger.getUuid());
                    if(player != null) {
                        if(!player.isCombatLogged()) {
                            PlayerUtils.killOffline(logger.getPlayer());
                            player.setCombatLogged(true);
                            Factions.getInstance().getCache().saveHCFPlayer(player);
                        }
                    }
                    else{
                        throw new HCFException("Could not find HCFPLayer for CombatLogger NPC");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        //Despawn NPC if its still there
        //Also kill the player if their NPC was killed - add to database list when their npc is killed or something
        CombatLogger logger = Factions.getInstance().getCombatLogManager().getLogger("Logger-" + e.getPlayer().getName());
        if(logger != null) {
            if(logger.isDead()) {
                killPlayer(e.getPlayer());
            }
            else{
                Factions.getInstance().getCombatLogManager().despawnLogger(logger);
            }
        }
        else{
            HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(e.getPlayer());
            if(player.isCombatLogged()) {
                killPlayer(e.getPlayer());
                player.setCombatLogged(false);
                e.getPlayer().spigot().respawn();
                Factions.getInstance().getCache().saveHCFPlayer(player);
            }
        }
    }

    private void killPlayer(Player p) {
        p.sendMessage(ChatColor.RED + "Your Combat-Logger was killed.");
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);
        player.setLogKilled(true);
    }


}
