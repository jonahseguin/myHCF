package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class EnderpearlListener implements Listener {

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer((Player) e.getEntity());
        if (!canThrowPearl(player)) {
            e.setCancelled(true);
            return;
        }

        player.getScoreboard().getTimer(FLabel.ENDER_PEARL).setTime(Factions.getInstance().getFactionsConfig().getEnderpearlCooldown());
    }

    public boolean canThrowPearl(HCFPlayer player) {
        return player.getEnderpearlCooldown() <= 0.01;
    }
}