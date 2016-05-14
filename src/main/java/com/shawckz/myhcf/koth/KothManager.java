/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.koth;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.database.AutoDBable;
import com.shawckz.myhcf.player.HCFPlayer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class KothManager {

    //Key = koth name #toLowerCase
    private Map<String, Koth> koths = new ConcurrentHashMap<>();

    private KothSchedule schedule = new KothSchedule();

    public KothManager(Factions instance) {
        schedule.loadSchedule();
        loadKoths();
        new BukkitRunnable(){
            @Override
            public void run() {
                ScheduledKoth next = schedule.getNextKoth();
                if(next != null) {
                    Koth koth = getKoth(next.getName());
                    if(koth != null) {
                        activateKoth(koth);
                    }
                }
            }
        }.runTaskTimerAsynchronously(Factions.getInstance(), 240L, 240L);
    }

    public void activateKoth(Koth koth) {
        koth.updateLabel();
        koth.setTimer(new KothTimer(koth, this, koth.getCapTime()) {
            @Override
            public void onComplete() {
                Bukkit.broadcastMessage(FLang.format(FactionLang.KOTH_CAP_FINISH, koth.getName(), koth.getCapper().getName()));
                koth.setActive(false);
                //TODO: Reward capper
                HCFPlayer capper = koth.getCapper();

                capper.getBukkitPlayer().sendMessage(ChatColor.GOLD + "Congrats!  You capped the Koth '" + ChatColor.BLUE + koth.getName() + ChatColor.GOLD + "'.");
            }
        });
        koth.getTimer().start();
        koth.setActive(true);
        Bukkit.broadcastMessage(FLang.format(FactionLang.KOTH_ACTIVE, koth.getName()));
    }

    public void cancelKoth(Koth koth) {
        koth.setActive(false);
        if(koth.getTimer() != null) {
            koth.getTimer().cancel();
        }
        Bukkit.broadcastMessage(FLang.format(FactionLang.KOTH_CANCEL, koth.getName()));
    }

    public Koth getKoth(String name) {
        return koths.get(name.toLowerCase());
    }

    public Map<String, Koth> getKoths() {
        return koths;
    }

    public KothSchedule getSchedule() {
        return schedule;
    }

    public boolean inKothCap(Location loc) {
        for(Koth koth : koths.values()) {
            if(koth.isActive()) {
                if(koth.withinCap(loc)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Koth getKothCap(Location loc) {
        for(Koth koth : koths.values()) {
            if(koth.isActive()) {
                if(koth.withinCap(loc)) {
                    return koth;
                }
            }
        }
        return null;
    }

    public void registerKoth(Koth koth) {
        koths.put(koth.getName().toLowerCase(), koth);
    }

    public void unregisterKoth(Koth koth) {
        if(koth.isActive()) {
            cancelKoth(koth);
        }
        koths.remove(koth.getName().toLowerCase());
    }

    public void loadKoths() {
        Set<AutoDBable> result = Factions.getInstance().getDbHandler().fetchAll(new Koth());

        result.stream().filter(autoDBable -> autoDBable instanceof Koth).forEach(ret -> {
            Koth koth = (Koth) ret;
            registerKoth(koth);
            koth.updateLabel();
        });
    }



}
