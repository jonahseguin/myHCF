package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.util.ChatMode;
import com.shawckz.myhcf.util.Relation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private String getFormat(String... args){
        return FLang.getFormattedLang(FactionLang.CHAT_FORMAT, args);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();

        HCFPlayer hcfPlayer = Factions.getInstance().getCache().getHCFPlayer(p);
        String pFaction = "-";
        if(hcfPlayer.getFaction() != null){
            pFaction = hcfPlayer.getFaction().getName();
        }
        e.setCancelled(true);
        if(hcfPlayer.getChatMode() == ChatMode.PUBLIC){
            for(Player pl : Bukkit.getOnlinePlayers()){
                Relation relation = Relation.NEUTRAL;
                HCFPlayer thcfPlayer = Factions.getInstance().getCache().getHCFPlayer(pl);
                if(thcfPlayer.getFaction() != null && hcfPlayer.getFaction() != null){
                    relation = thcfPlayer.getFaction().getRelationTo(hcfPlayer.getFaction());
                }
                String color = Factions.getInstance().getFactionsConfig().getRelationNeutral();
                if(relation == Relation.FACTION){
                    color = Factions.getInstance().getFactionsConfig().getRelationFaction();
                }
                else if(relation == Relation.NEUTRAL){
                    color = Factions.getInstance().getFactionsConfig().getRelationNeutral();
                }
                else if(relation == Relation.ALLY){
                    color = Factions.getInstance().getFactionsConfig().getRelationAlly();
                }
                pl.sendMessage(getFormat(color + pFaction, p.getDisplayName(), e.getMessage()));
            }
        }
        else if (hcfPlayer.getChatMode() == ChatMode.ALLY){
            if(hcfPlayer.getFaction() == null){
                hcfPlayer.getBukkitPlayer().sendMessage(ChatColor.RED + "You are not in a faction.  Switching you to public chat mode.");
                hcfPlayer.setChatMode(ChatMode.PUBLIC);
                return;
            }
            for(Player pl : Bukkit.getOnlinePlayers()){
                HCFPlayer thcfPlayer = Factions.getInstance().getCache().getHCFPlayer(pl);
                if(thcfPlayer.getFaction() != null && hcfPlayer.getFaction() != null){
                    if(thcfPlayer.getFaction().getRelationTo(hcfPlayer.getFaction()) == Relation.ALLY ||
                            thcfPlayer.getFaction().getRelationTo(hcfPlayer.getFaction()) == Relation.FACTION){
                        //They are allies or in the same faction
                        pl.sendMessage(FLang.getFormattedLang(FactionLang.CHAT_FORMAT_ALLY, hcfPlayer.getFaction().getName(), hcfPlayer.getName(), e.getMessage()));
                    }
                }
            }
        }
        else if (hcfPlayer.getChatMode() == ChatMode.FACTION){
            if(hcfPlayer.getFaction() == null){
                hcfPlayer.getBukkitPlayer().sendMessage(ChatColor.RED + "You are not in a faction.  Switching you to public chat mode.");
                hcfPlayer.setChatMode(ChatMode.PUBLIC);
                return;
            }
            for(HCFPlayer pl : hcfPlayer.getFaction().getOnlineMembers()){
                pl.getBukkitPlayer().sendMessage(FLang.getFormattedLang(FactionLang.CHAT_FORMAT_FACTION, hcfPlayer.getName(), e.getMessage()));
            }
        }
    }

}
