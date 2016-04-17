/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.command.factions.commands;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.command.factions.FCmdArgs;
import com.shawckz.myhcf.command.factions.FCommand;
import com.shawckz.myhcf.command.factions.HCFCommand;
import com.shawckz.myhcf.faction.Faction;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Jonah Seguin on 1/25/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@FCommand(name = "save", desc = "Save", usage = "/f save", playerOnly = false, perm = "myhcf.cmd.admin.save", minArgs = 0)
public class CmdFactionSave implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs args) {
        Player p = (Player) args.getSender();
        //HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        new BukkitRunnable(){
            @Override
            public void run() {
                p.sendMessage(ChatColor.GRAY + "Saving factions...");
                int i = 0;
                int fail = 0;
                for(Faction faction : Factions.getInstance().getFactionManager().getFactions()) {
                    if(!Factions.getInstance().getDbHandler().push(faction)) {
                        fail++;
                    }
                    else{
                        i++;
                    }
                }
                p.sendMessage(ChatColor.GREEN + "Faction save completed with " + i + " saved factions and " + fail + " failures.");
            }
        }.runTaskAsynchronously(Factions.getInstance());
    }
}
