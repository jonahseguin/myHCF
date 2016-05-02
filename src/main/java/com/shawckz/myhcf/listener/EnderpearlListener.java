package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimer;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimerFormat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EnderpearlListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Player player = e.getPlayer();
            if(player.getItemInHand().getType() == Material.ENDER_PEARL){
                if(!checkIfCanThrow(player)){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEnderpearl(ProjectileLaunchEvent e){
        if(e.isCancelled()) return;
        if(e.getEntity() instanceof EnderPearl){
            if(e.getEntity().getShooter() instanceof Player){
                Player player = (Player) e.getEntity().getShooter();
                if(!checkIfCanThrow(player)){
                    e.setCancelled(true);
                }
                else{
                    updateTime(player);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPearl(PlayerTeleportEvent e){
        Player p = e.getPlayer();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        if(e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            if(Factions.getInstance().getSpawn().withinSpawn(e.getTo())) {
                e.setCancelled(true);
                player.getScoreboard().getTimer(FLabel.ENDER_PEARL).setTime(0).hide();//reset time because the pearl was cancelled
                p.sendMessage(ChatColor.RED + "You cannot enderpearl into spawn.");
            }
        }

    }

    public void updateTime(Player player){
        HCFPlayer hcfPlayer = Factions.getInstance().getCache().getHCFPlayer(player);
        hcfPlayer.getScoreboard().getTimer(FLabel.ENDER_PEARL).setTime(Factions.getInstance().getFactionsConfig().getEnderpearlCooldown());
    }

    public boolean checkIfCanThrow(Player player){
        HCFPlayer hcfPlayer = Factions.getInstance().getCache().getHCFPlayer(player);
        if(hcfPlayer.getScoreboard().getTimer(FLabel.PVP_TIMER, HCFTimerFormat.HH_MM_SS).getTime() > 0.1) {
            FLang.send(player, FactionLang.PVP_TIMER_NOT_ALLOWED);
            return false;
        }
        if(canThrowPearl(hcfPlayer)){
            return true;
        }
        else{
            player.sendMessage(FLang.format(FactionLang.ENDERPEARL_COOLDOWN, getFormattedEnderpearlCooldown(hcfPlayer)));
            player.updateInventory();
            return false;
        }
    }

    public String getFormattedEnderpearlCooldown(HCFPlayer player){
        return ""+Float.parseFloat(HCFTimer.DECIMAL_FORMAT.format(player.getScoreboard().getTimer(FLabel.ENDER_PEARL).getTime()));
    }

    public boolean canThrowPearl(HCFPlayer player) {
        return player.getEnderpearlCooldown() <= 0.1;
    }

}