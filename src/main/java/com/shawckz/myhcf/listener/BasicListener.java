/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BasicListener implements Listener {

    //Remove claim pillars
    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        Factions.getInstance().getClaimSelector().cancelSelection(p);

        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        player.getScoreboard().getTimer(FLabel.PVP_TIMER).setTime(0).hide();


    }

}
