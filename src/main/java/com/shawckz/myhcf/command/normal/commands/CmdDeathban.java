/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.command.normal.commands;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.command.normal.GCmd;
import com.shawckz.myhcf.command.normal.GCmdArgs;
import com.shawckz.myhcf.command.normal.GCommand;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.player.HCFPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdDeathban implements GCommand {

    @GCmd(name = "revive", permission = "myhcf.deathban.revive", usage = "/revive <player>", minArgs = 1,
            aliases = {"db revive", "undeathban", "deathbanunban", "db unban", "deathban unban", "deathban revive"})
    public void onCmdRevive(final GCmdArgs args) {
        final CommandSender sender = args.getSender().getCommandSender();

        String target = args.getArg(0);
        {
            Player t = Bukkit.getPlayer(target);
            if (t != null) {
                target = t.getName();
            }
        }

        final String finalTarget = target;

        new BukkitRunnable(){
            @Override
            public void run() {
                HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(finalTarget);
                if(player != null) {
                    if(player.getDeathban() > System.currentTimeMillis()) {
                        player.setDeathban(0);
                        Factions.getInstance().getDbHandler().push(player);
                        FLang.send(sender, FactionLang.DEATHBAN_REVIVE, player.getName());
                    }
                    else{
                        FLang.send(sender, FactionLang.DEATHBAN_NOT_BANNED);
                    }
                }
                else{
                    FLang.send(sender, FactionLang.PLAYER_NOT_FOUND, finalTarget);
                }
            }
        }.runTaskAsynchronously(Factions.getInstance());

    }
}
