package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimer;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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

    public void updateTime(Player player){
        HCFPlayer hcfPlayer = Factions.getInstance().getCache().getHCFPlayer(player);
        hcfPlayer.getScoreboard().getTimer(FLabel.ENDER_PEARL).setTime(Factions.getInstance().getFactionsConfig().getEnderpearlCooldown());
    }

    public boolean checkIfCanThrow(Player player){
        HCFPlayer hcfPlayer = Factions.getInstance().getCache().getHCFPlayer(player);
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