/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.command.normal.commands;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.command.normal.GCmd;
import com.shawckz.myhcf.command.normal.GCmdArgs;
import com.shawckz.myhcf.command.normal.GCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CmdSetSpawn implements GCommand {

    @GCmd(name = "setspawn", permission = "myhcf.cmd.setspawn", playerOnly = false)
    public void onCommand(GCmdArgs args) {
        Player p = args.getSender().getPlayer();
        Factions.getInstance().getFactionsConfig().setSpawn(p.getLocation());
        Factions.getInstance().getSpawn().setSpawn(p.getLocation());
        p.getWorld().setSpawnLocation(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
        p.sendMessage(ChatColor.GRAY + "Spawn set to your current location.");
    }

}
