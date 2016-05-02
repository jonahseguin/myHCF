/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.player.HCFPlayer;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by Jonah Seguin on 1/23/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class SpawnTagListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTag(EntityDamageByEntityEvent e) {
        if(e.isCancelled()) return;
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);
            if(e.getDamager() instanceof Projectile) {
                Projectile proj = (Projectile) e.getDamager();
                if(proj.getShooter() != null) {
                    if(proj.getShooter() instanceof Player) {
                        Player d = (Player) proj.getShooter();
                        HCFPlayer damager = Factions.getInstance().getCache().getHCFPlayer(d);
                        tag(player, damager);
                    }
                }
            }
            else if (e.getDamager() instanceof Player) {
                Player d = (Player) e.getDamager();
                HCFPlayer damager = Factions.getInstance().getCache().getHCFPlayer(d);
                tag(player, damager);
            }
        }
    }

    public void tag(HCFPlayer p, HCFPlayer d) {
        if(p == null || d == null) return;
        if(Factions.getInstance().getFactionsConfig().isSpawnTagOnDamaged()) {
            if(p.getSpawnTag() < Factions.getInstance().getFactionsConfig().getSpawnTagTimeDamaged()) {
                p.setSpawnTag(Factions.getInstance().getFactionsConfig().getSpawnTagTimeDamaged());
            }
            if(p.getSpawnTag() <= 0.1) {
                //Send spawn tag message
                FLang.send(p.getBukkitPlayer(), FactionLang.SPAWN_TAG);
            }
        }
        d.setSpawnTag(Factions.getInstance().getFactionsConfig().getSpawnTagTimeDamager());
        if(d.getSpawnTag() <= 0.1) {
            //Send spawn tag message
            FLang.send(d.getBukkitPlayer(), FactionLang.SPAWN_TAG);
        }
    }

}
