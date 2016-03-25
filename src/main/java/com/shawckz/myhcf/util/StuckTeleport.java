/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.util;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.land.LandBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@AllArgsConstructor
public class StuckTeleport {

    private Player player;

    public void run(){
        final int seconds = Factions.getInstance().getFactionsConfig().getFactionsStuckTeleportTime();

        new BukkitRunnable(){
            @Override
            public void run() {
                Location loc = player.getLocation();
                LandBoard landBoard = Factions.getInstance().getLandBoard();
                boolean found = false;
                while(!found) {
                    loc = loc.clone().add(1, 0, 1);
                    if(!landBoard.isClaimed(loc)) {
                        found = true;
                    }
                }

                new TeleportTimer(player, loc, seconds){
                    @Override
                    public void onTick() {
                        if(player != null) {
                            if(this.getTime() % 30 == 0 || this.getTime() <= 10 && this.getTime() != seconds) {
                                FLang.send(player, FactionLang.FACTION_STUCK_TELEPORT_UPDATE, (secondsString(getTime())));
                            }
                        }
                    }
                }.run();
                FLang.send(player, FactionLang.FACTION_STUCK_TELEPORT_UPDATE, (secondsString(seconds)));

            }
        }.runTaskAsynchronously(Factions.getInstance());
    }

    private String secondsString(int time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss", Locale.getDefault());
        return formatter.format(new Date(time * 1000));
    }

}
