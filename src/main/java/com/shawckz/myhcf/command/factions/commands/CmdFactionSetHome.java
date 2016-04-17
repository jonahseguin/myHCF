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

@FCommand(name = "sethome", desc = "Set your faction's home", usage = "/f sethome", playerOnly = true, aliases = {"seth", "sethq"})
public class CmdFactionSetHome implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        if(player.getFaction() != null) {
            Faction land = Factions.getInstance().getLandBoard().getFactionAt(p.getLocation());
            if(land != null && land.getId().equals(player.getFaction().getId())) {
                if(player.getFactionRole() != FactionRole.MEMBER) {
                    player.getFaction().setHome(p.getLocation());
                    player.getFaction().sendMessage(FLang.format(FactionLang.FACTION_SETHOME_LOCAL, player.getName()));
                }
                else{
                    FLang.send(p, FactionLang.FACTION_CMD_MOD_ONLY);
                }
            }
            else{
                FLang.send(p, FactionLang.FACTION_SETHOME_CLAIM);
            }
        }
        else{
            FLang.send(p, FactionLang.FACTION_NONE);
        }
    }
}
