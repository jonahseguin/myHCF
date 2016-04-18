/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.command.factions.commands;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.command.factions.FCmdArgs;
import com.shawckz.myhcf.command.factions.FCommand;
import com.shawckz.myhcf.command.factions.HCFCommand;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.util.ChatMode;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@FCommand(name = "chat", desc = "Toggle chat mode", usage = "/f chat <(A)LLY|(P)UBLIC|(F)ACTION>", minArgs = 1, playerOnly = true, aliases = {"ch", "c"})
public class CmdFactionChat implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        String mode = cmdArgs.getArg(0).toLowerCase();

        if(mode.startsWith("a")) {
            player.setChatMode(ChatMode.ALLY);
            FLang.send(p, FactionLang.FACTION_CHAT_MODE, ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getFactionsConfig().getRelationAlly() + "ALLY"));
        }
        else if (mode.startsWith("p")) {
            player.setChatMode(ChatMode.PUBLIC);
            FLang.send(p, FactionLang.FACTION_CHAT_MODE, ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getFactionsConfig().getRelationNeutral() + "PUBLIC"));
        }
        else if (mode.startsWith("f")) {
            player.setChatMode(ChatMode.FACTION);
            FLang.send(p, FactionLang.FACTION_CHAT_MODE, ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getFactionsConfig().getRelationFaction() + "FACTION"));
        }
        else{
            p.sendMessage(ChatColor.RED + "Unknown chat mode.  Try 'ally', 'public', or 'faction'.");
        }

    }
}
