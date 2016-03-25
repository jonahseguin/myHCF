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

@FCommand(name = "demote", desc = "Demote a member from faction moderator", usage = "/f demote <player>", minArgs = 1, playerOnly = true)
public class CmdFactionDemote implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        String t = cmdArgs.matchPlayer(0);

        if(player.getFaction() != null) {
            if(player.getFactionRole() == FactionRole.LEADER) {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        HCFPlayer target = Factions.getInstance().getCache().getHCFPlayer(t);
                        if(target.getFaction() != null && target.getFaction().getId().equals(player.getFaction().getId())) {
                            if(target.getFactionRole() == FactionRole.MODERATOR) {
                                target.setFactionRole(FactionRole.MEMBER);
                                player.getFaction().setRole(target, FactionRole.MEMBER);
                                player.getFaction().sendMessage(FLang.format(FactionLang.FACTION_DEMOTE_LOCAL, target.getName(), player.getName()));
                            }
                            else{
                                FLang.send(p, FactionLang.FACTION_DEMOTE_NOT_MOD, t);
                            }
                        }
                        else{
                            FLang.send(p, FactionLang.FACTION_NOT_SAME, t);
                        }
                    }
                }.runTaskAsynchronously(Factions.getInstance());
            }
            else{
                FLang.send(p, FactionLang.FACTION_LEADER_ONLY);
            }
        }
        else{
            FLang.send(p, FactionLang.FACTION_NONE);
        }

    }
}
