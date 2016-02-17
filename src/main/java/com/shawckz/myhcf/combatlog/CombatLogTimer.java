/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.combatlog;

import com.shawckz.myhcf.Factions;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class CombatLogTimer {

    private int time;
    private BukkitTask task = null;

    public CombatLogTimer(int time) {
        this.time = time;
    }

    public void run() {
        cancel();
        task = new BukkitRunnable(){
            @Override
            public void run() {
                if(time > 0) {
                    time--;
                }
                else{
                    task = null;
                    cancel();
                    complete();
                }
            }
        }.runTaskTimer(Factions.getInstance(), 20L, 20L);
    }

    public abstract void complete();

    public void cancel() {
        if(task != null) {
            task.cancel();
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
