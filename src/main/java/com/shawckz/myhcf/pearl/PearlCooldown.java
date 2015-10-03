package com.shawckz.myhcf.pearl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.player.HCFPlayer;

public class PearlCooldown implements Listener {

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer((Player) e.getEntity());
        if (!canThrowPearl(player)) {
            e.setCancelled(true);
            return;
        }
        player.setTimeCanThrow(System.currentTimeMillis() + 10000); // Make the time configurable via config.
    }

    public boolean canThrowPearl(HCFPlayer player) {
        return player.getTimeCanThrow() <= System.currentTimeMillis();
    }

    public void updateScoreboardVariable() {
        // Awaiting scoreboard API.
    }
}