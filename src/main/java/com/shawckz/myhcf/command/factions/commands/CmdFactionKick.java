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
import org.bukkit.scheduler.BukkitRunnable;

@FCommand(name = "kick", desc = "Kick a player from the faction", usage = "/f kick <player>", minArgs = 1, playerOnly = true)
public class CmdFactionKick implements HCFCommand {

    @Override
    public void onCommand(final FCmdArgs cmdArgs) {
        final Player p = (Player) cmdArgs.getSender();
        final HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        final Player t = cmdArgs.getPlayer(0);

        if(player.getFaction() != null) {
            if (t != null) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        HCFPlayer target = Factions.getInstance().getCache().getHCFPlayer(t);
                        if (target != null) {
                            if (player.getFactionRole() != FactionRole.MEMBER) {
                                if(target.getFactionId().equals(player.getFactionId())) {
                                    if(target.getFactionRole() != FactionRole.LEADER) {
                                        if(!target.getName().equals(p.getName())) {
                                            if(target.getFactionRole() == FactionRole.MODERATOR && player.getFactionRole() == FactionRole.MODERATOR) {
                                                // mod can't kick moderator
                                                FLang.send(p, FactionLang.FACTION_NO_KICK_MOD);
                                            }
                                            else{
                                                target.getFaction().kickPlayer(target, player);
                                            }
                                        }
                                        else{
                                            FLang.send(p, FactionLang.FACTION_NO_KICK_SELF);
                                        }
                                    }
                                    else{
                                        FLang.send(p, FactionLang.FACTION_NO_KICK_LEADER);
                                    }
                                }
                                else{
                                    FLang.send(p, FactionLang.FACTION_NOT_SAME, target.getName());
                                }
                            }
                            else {
                                FLang.send(p, FactionLang.FACTION_CMD_MOD_ONLY);
                            }
                        }
                        else {
                            FLang.send(p, FactionLang.PLAYER_NOT_FOUND, cmdArgs.getArg(0));
                        }
                    }
                }.runTaskAsynchronously(Factions.getInstance());
            }
            else {
                FLang.send(p, FactionLang.PLAYER_NOT_FOUND, cmdArgs.getArg(0));
            }
        }
        else{
            FLang.send(p, FactionLang.FACTION_NONE);
        }

    }
}
