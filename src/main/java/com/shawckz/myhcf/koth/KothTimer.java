/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.koth;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class KothTimer extends BukkitRunnable {

    private final Koth koth;
    private final KothManager manager;

    private final int defaultTime;
    private int time;

    private boolean frozen = true;

    public KothTimer(Koth koth, KothManager manager, int time) {
        this.koth = koth;
        this.manager = manager;
        this.defaultTime = time;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void start() {
        this.time = defaultTime;
        setTime(defaultTime);
        runTaskTimerAsynchronously(Factions.getInstance(), 20L, 20L);
    }



    public void setTime(int time) {
        this.time = time;
        updateScoreboards();
    }

    private void updateScoreboards() {
        for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
            HCFPlayer pp = Factions.getInstance().getCache().getHCFPlayer(pl);
            if(pp != null) {
                HCFTimer timer = pp.getScoreboard().getTimer(koth.getLabel());
                if(timer.getTime() != time) {
                    timer.setTime(time);
                }
                if(timer.isPaused() != frozen) {
                    if(frozen) {
                        timer.pauseTimer();
                    }
                    else{
                        timer.unpauseTimer();
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        if(koth.getCapper() == null) {
            if(!frozen) {
                updateScoreboards();
            }
            frozen = true;
            return;
        }
        else{
            if(frozen) {
                frozen = false;
                updateScoreboards();
            }
        }
        if(time > 0) {
            koth.updateCapper();
            time--;
        }
        else{
            //Finish
            onComplete();
            cancel();
            for(Player pl : Bukkit.getOnlinePlayers()) {
                HCFPlayer pp = Factions.getInstance().getCache().getHCFPlayer(pl);
                if(pp != null) {
                    pp.getScoreboard().getTimer(koth.getLabel()).hide();
                }
            }
        }
    }

    public abstract void onComplete();

}
