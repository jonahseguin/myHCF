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

import org.bukkit.command.CommandSender;

@FCommand(name = "unfreeze", desc = "Unfreeze a faction's DTR", usage = "/f unfreeze <faction|player>", minArgs = 1, perm = "myhcf.cmd.admin.unfreeze")
public class CmdFactionUnfreeze implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs cmdArgs) {
        CommandSender sender = cmdArgs.getSender();
        Factions.getInstance().getFactionManager().getFactionFromArg(sender, cmdArgs.getArg(0), faction -> {
            faction.setDtrFreezeFinish(System.currentTimeMillis());
            FLang.send(sender, FactionLang.FACTION_UNFREEZE_SENDER, faction.getDisplayName());
            faction.sendMessage(FLang.format(FactionLang.FACTION_UNFREEZE, sender.getName()));
        });
    }

}
