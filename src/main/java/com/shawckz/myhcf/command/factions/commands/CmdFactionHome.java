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
import com.shawckz.myhcf.util.TeleportTimer;

import org.bukkit.entity.Player;

@FCommand(name = "home", desc = "Go to your faction's home", usage = "/f home", playerOnly = true, aliases = {"h"})
public class CmdFactionHome implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        if(player.getFaction() != null) {
            if(player.getFaction().getHome() != null) {
                new TeleportTimer(p, player.getFaction().getHome(), Factions.getInstance().getFactionsConfig().getFactionsHomeTeleportTime())
                        .run();
            }
            else {
                FLang.send(p, FactionLang.FACTION_HOME_NONE);
            }
        }
        else{
            FLang.send(p, FactionLang.FACTION_NONE);
        }

    }
}
