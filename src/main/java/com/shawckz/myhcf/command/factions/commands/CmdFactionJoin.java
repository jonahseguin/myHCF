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
import com.shawckz.myhcf.player.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on 1/25/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@FCommand(name = "join", desc = "Join a faction", usage = "/f join <faction>", aliases = "j", playerOnly = true, minArgs = 1)
public class CmdFactionJoin implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs args) {
        Player p = (Player) args.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);
        if(player.getFaction() != null) {
            FLang.send(p, FactionLang.FACTION_ALREADY_IN);
            return;
        }
        String target = args.getArg(0);
        //Priority 1 for search is faction name
        if (Factions.getInstance().getFactionManager().factionExists(target)) {
            Factions.getInstance().getFactionManager().getFaction(target, faction -> {
                join(player, faction);
            });
        }
        else if (Bukkit.getPlayer(target) != null) {
            Player t = Bukkit.getPlayer(target);
            HCFPlayer thcf = Factions.getInstance().getCache().getHCFPlayer(t);
            if (thcf != null) {
                if(thcf.getFaction() != null) {
                    join(player, thcf.getFaction());
                }
                else{
                    FLang.send(p, FactionLang.FACTION_NONE_OTHER, thcf.getName());
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "There is no faction or player by that name.");
            }
        }
        else {
            HCFPlayer t = Factions.getInstance().getCache().getHCFPlayer(target);
            if (t != null) {
                if (t.getFaction() != null) {
                    join(player, t.getFaction());
                }
                else {
                    FLang.send(p, FactionLang.PLAYER_NOT_IN_FACTION, target);
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "There is no faction or player by that name.");
            }
        }
    }

    public void join(HCFPlayer player, Faction faction) {
        if (faction.getInvitations().contains(player.getUniqueId())) {
            if ((faction.getMembers().size() + 1) <= Factions.getInstance().getFactionsConfig().getMaxFactionSize()) {
                faction.addPlayer(player);
            }
            else {
                FLang.send(player.getBukkitPlayer(), FactionLang.FACTION_JOIN_FULL, faction.getDisplayName());
            }
        }
        else {
            FLang.send(player.getBukkitPlayer(), FactionLang.FACTION_JOIN_NOT_INVITED, faction.getDisplayName());
        }
    }

}
