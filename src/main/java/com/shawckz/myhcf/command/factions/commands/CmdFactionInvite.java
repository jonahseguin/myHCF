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

@FCommand(name = "invite", desc = "Invite a player to the faction", usage = "/f invite <player>", minArgs = 1, playerOnly = true, aliases = {"inv"})
public class CmdFactionInvite implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        if(player.getFaction() != null) {
            if(player.getFactionRole() != FactionRole.MEMBER) {
                String t = cmdArgs.matchPlayer(0);

                new BukkitRunnable(){
                    @Override
                    public void run() {
                        HCFPlayer target = Factions.getInstance().getCache().getHCFPlayer(t);
                        if(target != null) {
                            if(target.getFactionId() == null) {
                                if(!player.getFaction().getInvitations().contains(target.getUniqueId())) {
                                    player.getFaction().invitePlayer(player, target);
                                }
                                else{
                                    FLang.send(p, FactionLang.FACTION_INVITED_ALREADY, target.getName());
                                }
                            }
                            else{
                                FLang.send(p, FactionLang.FACTION_ALREADY_IN_OTHER, target.getName());
                            }
                        }
                        else{
                            FLang.send(p, FactionLang.PLAYER_NOT_FOUND, cmdArgs.getArg(0));
                        }
                    }
                }.runTaskAsynchronously(Factions.getInstance());
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
