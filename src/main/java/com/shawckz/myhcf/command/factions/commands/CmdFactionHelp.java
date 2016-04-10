/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.command.factions.commands;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.command.factions.FCmdArgs;
import com.shawckz.myhcf.command.factions.FCommand;
import com.shawckz.myhcf.command.factions.HCFCmdData;
import com.shawckz.myhcf.command.factions.HCFCommand;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@FCommand(name = "help", desc = "Factions help & commands", usage = "/f help", aliases = "commands")
public class CmdFactionHelp implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs cmdArgs) {
        CommandSender sender = cmdArgs.getSender();

        String version = Factions.getInstance().getDescription().getVersion();

        FLang.send(sender, FactionLang.FACTION_INFO_HEADER_FOOTER);

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7*** &6myHCF &7v"+version+" &7***"));
        sender.sendMessage(ChatColor.GRAY+"Developed by Shawckz");

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7*** &9Commands &7***"));
        for(HCFCmdData cmd : Factions.getInstance().getCommandManager().getCmds().values()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/f " + cmd.getName() + " &8- " + cmd.getDescription()));
        }

        FLang.send(sender, FactionLang.FACTION_INFO_HEADER_FOOTER);

    }
}
