package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.player.HCFPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class DeathbanListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        HCFPlayer hcfPlayer = Factions.getInstance().getCache().getHCFPlayer(p);
        hcfPlayer.setDeathban(System.currentTimeMillis() + (hcfPlayer.getDeathbanRank().getSeconds() * 1000));

        hcfPlayer.getBukkitPlayer().kickPlayer(Factions.getInstance().getLang().getFormattedLang(FactionLang.DEATHBAN_KICK, hcfPlayer.getLives()+"", "TODO: TIME"));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onLogin(PlayerLoginEvent e){
        Player p = e.getPlayer();
        HCFPlayer hcfPlayer = Factions.getInstance().getCache().getHCFPlayer(p);
        
    }

}
