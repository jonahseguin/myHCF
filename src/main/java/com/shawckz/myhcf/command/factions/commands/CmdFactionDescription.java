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

import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on 1/25/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@FCommand(name = "description", desc = "Change the description of your faction", usage = "/f desc <description>", playerOnly = true, minArgs = 1, aliases = {"desc", "setdesc"})
public class CmdFactionDescription implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs args) {
        Player p = (Player) args.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);
        String description = args.getArg(0);

        if(player.getFaction() != null) {
            if(player.getFactionRole() != FactionRole.MEMBER) {
                if(description.length() <= 32) {
                    player.getFaction().setDescription(description);
                    player.getFaction().sendMessage(FLang.format(FactionLang.FACTION_DESC_LOCAL, player.getName(), description));
                }
                else{
                    FLang.send(p, FactionLang.FACTION_DESC_LENGTH);
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
