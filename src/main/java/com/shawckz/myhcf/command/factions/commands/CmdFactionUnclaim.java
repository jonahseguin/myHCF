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
import com.shawckz.myhcf.faction.FactionRole;
import com.shawckz.myhcf.player.HCFPlayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@FCommand(name = "unclaim", desc = "Unclaim your faction's land", usage = "/f unclaim <all|claim>", playerOnly = true)
public class CmdFactionUnclaim implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        if(cmdArgs.getArgs().length == 0) {
            p.sendMessage(ChatColor.RED+"Incorrect usage.  Usage: /f unclaim <all|claim>");
            p.sendMessage(ChatColor.YELLOW+"To unclaim a specific claim, use /f unclaim <claim> - where claim is a claim ID");
            p.sendMessage(ChatColor.YELLOW+"To get the ID of a claim, use '/f claim id' while standing within the claim");
        }
        else{
            if(player.getFaction() != null) {
                if(player.getFactionRole() != FactionRole.MEMBER) {
                    if(cmdArgs.getArg(0).equalsIgnoreCase("all")) {
                        Factions.getInstance().getLandBoard().unclaimAll(player.getFaction());
                        player.getFaction().sendMessage(FLang.format(FactionLang.FACTION_UNCLAIM_ALL, p.getName()));
                    }
                    else{
                        String claimID = cmdArgs.getArg(0);
                        int deleted = Factions.getInstance().getLandBoard().unclaim(player.getFaction(), claimID);
                        if(deleted > 0) {
                            FLang.send(p, FactionLang.FACTION_UNCLAIM, p.getName(), claimID);
                        }
                        else{
                            FLang.send(p, FactionLang.FACTION_UNCLAIM_NOT_FOUND);
                        }
                    }
                }
                else{
                    FLang.send(p, FactionLang.FACTION_CMD_MOD_ONLY);
                }
            }
            else{
                FLang.send(p, FactionLang.FACTION_NONE);
            }
        }

    }
}
