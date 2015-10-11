package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.util.ChatMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private String getFormat(String... args){
        return FLang.getFormattedLang(FactionLang.CHAT_FORMAT, args);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        HCFPlayer hcfPlayer = Factions.getInstance().getCache().getHCFPlayer(p);
        if(hcfPlayer.getChatMode() == ChatMode.PUBLIC){

        }
        else if (hcfPlayer.getChatMode() == ChatMode.ALLY){

        }
        else if (hcfPlayer.getChatMode() == ChatMode.FACTION){

        }
    }

}
