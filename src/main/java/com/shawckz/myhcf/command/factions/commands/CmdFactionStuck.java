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
import com.shawckz.myhcf.util.Relation;
import com.shawckz.myhcf.util.StuckTeleport;

import org.bukkit.entity.Player;

@FCommand(name = "stuck", desc = "Teleport out of enemy land if stuck", usage = "/f stuck", aliases = "st", playerOnly = true)
public class CmdFactionStuck implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        if(!player.inCombat()){
            if(Factions.getInstance().getLandBoard().isClaimed(p.getLocation())) {
                if(player.getFaction() != null && player.getFaction().getRelationTo(Factions.getInstance().getLandBoard().getFactionAt(p.getLocation())) != Relation.FACTION) {
                    StuckTeleport stuck = new StuckTeleport(p);
                    stuck.run();
                }
                else{
                    FLang.send(p, FactionLang.FACTION_STUCK_NOT_STUCK);
                }
            }
            else{
                FLang.send(p, FactionLang.FACTION_STUCK_NOT_STUCK);
            }
        }
        else{
            FLang.send(p, FactionLang.FACTION_STUCK_COMBAT);
        }

    }
}
