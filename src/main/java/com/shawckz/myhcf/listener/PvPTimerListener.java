/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.faction.FactionType;
import com.shawckz.myhcf.land.Claim;
import com.shawckz.myhcf.land.LandBoard;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimerFormat;
import com.shawckz.myhcf.scoreboard.internal.label.XLabel;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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
                    .show();
            player.getBukkitPlayer().sendMessage(FLang.format(FactionLang.PVP_TIMER_START,
                    player.getScoreboard().getTimer(FLabel.PVP_TIMER).getValue().getFullValue()
                            .split(player.getScoreboard().getTimer(FLabel.PVP_TIMER).getKey())[1]));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRespawnFixScoreboard(PlayerRespawnEvent e) {
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(e.getPlayer());
        for(XLabel label : player.getScoreboard().getScores().values()) {
            player.getScoreboard().removeLabel(label);
        }
        player.getScoreboard().getTimers().clear();
        player.getScoreboard().getLabels().clear();
        player.getScoreboard().sendToPlayer(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(final PlayerRespawnEvent e) {
        new BukkitRunnable(){
            @Override
            public void run() {
                HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(e.getPlayer());
                player.getScoreboard().getTimer(FLabel.PVP_TIMER).setTime(Factions.getInstance().getFactionsConfig().getPvpTimerRespawn());
                player.getBukkitPlayer().sendMessage(FLang.format(FactionLang.PVP_TIMER_START,
                        player.getScoreboard().getTimer(FLabel.PVP_TIMER).getValue().getFullValue()
                                .split(player.getScoreboard().getTimer(FLabel.PVP_TIMER).getKey())[1]));
            }
        }.runTaskLater(Factions.getInstance(), 20L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(e.getPlayer());
        player.getScoreboard().getTimer(FLabel.PVP_TIMER).pauseTimer();
    }

    public boolean hasPvPTimer(Player pl) {
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(pl);
        if(player != null) {
            if(player.getScoreboard().getTimer(FLabel.PVP_TIMER) != null) {
                return player.getScoreboard().getTimer(FLabel.PVP_TIMER).getTime() > 0.1;
            }
        }
        return false;
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent e) {
        if (e.isCancelled()) return;
        if (e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) {
            return;
        }
        Player player = e.getPlayer();
        if(!hasPvPTimer(player)) return;
        HCFPlayer hcfPlayer = Factions.getInstance().getCache().getHCFPlayer(player);
        LandBoard landBoard = Factions.getInstance().getLandBoard();
        boolean spawnTagged = hcfPlayer.getScoreboard().getTimer(FLabel.SPAWN_TAG).getTime() > 0.1;
        if (landBoard.isClaimed(e.getTo())) {
            Faction claim = landBoard.getFactionAt(e.getTo());
            if (claim.getFactionType() == FactionType.NORMAL || claim.getFactionType() == FactionType.KOTH) {
                if (!spawnTagged) {
                    player.setFoodLevel(20);
                }
                else {
                    e.setTo(e.getFrom());
                }
            }
        }

        for(Claim c : Factions.getInstance().getLandBoard().getClaimsInRadius(e.getTo(), 25)) {
            Set<Location> toSend = c.getDynamicWall().getNear(e.getPlayer(), 25, 10);
            if(toSend != null && !toSend.isEmpty()) {
                c.getDynamicWall().getWallRadius().send(player, new ItemStack(Factions.getInstance().getFactionsConfig().getSpawnTagWallMaterial(), 1, (byte)Factions.getInstance().getFactionsConfig().getSpawnTagWallMaterialData()), toSend);
            }
        }
    }

}
