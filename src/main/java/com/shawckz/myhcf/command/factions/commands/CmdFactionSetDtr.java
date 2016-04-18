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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@FCommand(name = "setdtr", desc = "Set a faction's DTR", usage = "/f setdtr <faction|player> <dtr>", minArgs = 2, perm = "myhcf.cmd.admin.setdtr")
public class CmdFactionSetDtr implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs cmdArgs) {
        CommandSender sender = cmdArgs.getSender();
        Factions.getInstance().getFactionManager().getFactionFromArg(sender, cmdArgs.getArg(0), faction -> {
            double dtr;
            try{
                dtr = Double.parseDouble(cmdArgs.getArg(1));
            }
            catch (NumberFormatException expected) {
                sender.sendMessage(ChatColor.RED + "DTR must be a number.");
                return;
            }
            faction.setDeathsUntilRaidable(dtr);
            faction.sendMessage(FLang.format(FactionLang.FACTION_UPDATE_DTR, dtr+"", sender.getName()));
            FLang.send(sender, FactionLang.FACTION_UPDATE_DTR_SENDER, faction.getDisplayName(), dtr+"");
            faction.sendMessage("DTR: " + faction.getDeathsUntilRaidable());

            Faction f = Factions.getInstance().getFactionManager().getFactionById(faction.getId());
            faction.sendMessage("(2)DTR: " + f.getDeathsUntilRaidable());

        });
    }

}
