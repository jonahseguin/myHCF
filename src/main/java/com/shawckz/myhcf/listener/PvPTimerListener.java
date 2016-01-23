/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimerFormat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Created by Jonah Seguin on 1/23/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class PvPTimerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFirstJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPlayedBefore()) {
            HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(e.getPlayer());
            player.getScoreboard().getTimer(FLabel.PVP_TIMER, HCFTimerFormat.HH_MM_SS)
                    .setTime(Factions.getInstance().getFactionsConfig().getPvpTimerFirstJoin())
                    .show()
                    .unpauseTimer();
            player.getBukkitPlayer().sendMessage(FLang.format(FactionLang.PVP_TIMER_START,
                    player.getScoreboard().getTimer(FLabel.PVP_TIMER).getValue().getFullValue()
                            .split(player.getScoreboard().getTimer(FLabel.PVP_TIMER).getKey())[1]));
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(e.getPlayer());
        player.getScoreboard().getTimer(FLabel.PVP_TIMER, HCFTimerFormat.HH_MM_SS)
                .unpauseTimer()
                .show()
                .setTime(Factions.getInstance().getFactionsConfig().getPvpTimerRespawn());
        player.getBukkitPlayer().sendMessage(FLang.format(FactionLang.PVP_TIMER_START,
                player.getScoreboard().getTimer(FLabel.PVP_TIMER).getValue().getFullValue()
                        .split(player.getScoreboard().getTimer(FLabel.PVP_TIMER).getKey())[1]));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(e.getPlayer());
        player.getScoreboard().getTimer(FLabel.PVP_TIMER).hide().pauseTimer();
    }

    public boolean hasPvPTimer(Player pl) {
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(pl);
        return player.getScoreboard().getTimer(FLabel.PVP_TIMER).getTime() > 0.1;

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getDamager() instanceof Player) {
                Player damager = (Player) e.getDamager();
                if (hasPvPTimer(p)) {
                    damager.sendMessage(FLang.format(FactionLang.PVP_TIMER_NO_DAMAGE, p.getName()));
                    e.setCancelled(true);
                }
                else if (hasPvPTimer(damager)) {
                    damager.sendMessage(FLang.format(FactionLang.PVP_TIMER_NO_ATTACK));
                    e.setCancelled(true);
                }
            }
            else if (e.getDamager() instanceof Projectile) {
                Projectile proj = (Projectile) e.getDamager();
                if (proj.getShooter() != null) {
                    if (proj.getShooter() instanceof Player) {
                        Player damager = (Player) proj.getShooter();
                        if (hasPvPTimer(p)) {
                            damager.sendMessage(FLang.format(FactionLang.PVP_TIMER_NO_DAMAGE, p.getName()));
                            e.setCancelled(true);
                        }
                        else if (hasPvPTimer(damager)) {
                            damager.sendMessage(FLang.format(FactionLang.PVP_TIMER_NO_ATTACK));
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }

    }

}
