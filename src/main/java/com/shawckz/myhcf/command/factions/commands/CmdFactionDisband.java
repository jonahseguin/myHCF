/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.command.factions.commands;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.command.factions.FCmdArgs;
import com.shawckz.myhcf.command.factions.FCommand;
import com.shawckz.myhcf.command.factions.HCFCommand;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.faction.FactionRole;
import com.shawckz.myhcf.player.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@FCommand(name = "disband", desc = "Disband your faction", usage = "/f disband", aliases = "delete")
public class CmdFactionDisband implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs args) {
        CommandSender sender = args.getSender();
        if (args.getArgs().length > 0 && sender.hasPermission("myhcf.cmd.faction.disband.other")) {
            //Disband another fac
            Factions.getInstance().getFactionManager().getFaction(args.getArg(0), faction -> {
                if (faction != null) {
                    disband(faction, sender);
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Faction '" + args.getArg(0) + "' not found.");
                }
            });
        }
        else {
            if (sender instanceof Player) {
                Player pl = (Player) sender;
                HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(pl);
                if (player.getFaction() != null) {
                    if (player.getFaction().getLeader().getName().equals(player.getName()) && player.getFactionRole() == FactionRole.LEADER) {
                        Faction faction = player.getFaction();
                        if (faction.isNormal()) {
                            if (faction.getDtrFreezeFinish() <= System.currentTimeMillis()) {
                                disband(faction, sender);
                                sender.sendMessage(ChatColor.YELLOW + "You disbanded your faction.");
                            }
                            else {
                                sender.sendMessage(ChatColor.RED + "You cannot disband while your DTR is frozen.");
                            }
                        }
                        else {
                            disband(faction, sender);
                        }
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Only the faction leader can disband the faction.");
                    }
                }
                else {
                    sender.sendMessage(ChatColor.RED + "You are not in a faction.");
                }
            }
            else {
                sender.sendMessage(ChatColor.RED + "Only players can disband their own factions.");
            }
        }
    }

    public void disband(Faction faction, CommandSender disbander) {
        faction.delete();
        if (faction.isNormal()) {
            //Only broadcast if normal
            Bukkit.broadcastMessage(FLang.format(FactionLang.FACTION_DISBAND_BROADCAST, faction.getDisplayName(), disbander.getName()));
        }
        Factions.getInstance().getFactionManager().removeFromCache(faction);
        for (HCFPlayer player : faction.getOnlineMembers()) {
            player.setFactionId(null);
        }
        Factions.getInstance().getLandBoard().unclaimAll(faction);
        FLang.send(disbander, FactionLang.FACTION_DISBAND_FINISH, faction.getDisplayName());
    }

}
