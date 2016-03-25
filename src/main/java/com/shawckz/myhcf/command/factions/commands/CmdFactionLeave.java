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
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.faction.FactionRole;
import com.shawckz.myhcf.player.HCFPlayer;

import org.bukkit.entity.Player;

@FCommand(name = "leave", desc = "Leave your faction", usage = "/f leave", aliases = "quit", playerOnly = true)
public class CmdFactionLeave implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        if(player.getFaction() != null) {
            Faction faction = player.getFaction();
            if(player.getFactionRole() != FactionRole.LEADER) {
                faction.leavePlayer(player);
                player.setFactionId(null);
            }
            else{
                FLang.send(p, FactionLang.FACTION_LEADER_LEAVE);
            }
        }
        else{
            FLang.send(p, FactionLang.FACTION_NONE);
        }
    }
}
