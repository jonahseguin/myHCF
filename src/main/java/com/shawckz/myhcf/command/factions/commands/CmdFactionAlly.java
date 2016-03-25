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
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.faction.FactionRole;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.util.HCFException;
import com.shawckz.myhcf.util.Relation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@FCommand(name = "ally", desc = "Ally a faction", usage = "/f ally <faction|player>", aliases = "truce", playerOnly = true, minArgs = 1)
public class CmdFactionAlly implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);
        String target = cmdArgs.getArg(0);

        if(player.getFaction() != null) {
            if(player.getFactionRole() != FactionRole.MEMBER) {
                if (Factions.getInstance().getFactionManager().factionExists(target)) {
                    Factions.getInstance().getFactionManager().getFaction(target, faction -> {
                        ally(player, faction);
                    });
                }
                else if (Bukkit.getPlayer(target) != null) {
                    Player t = Bukkit.getPlayer(target);
                    HCFPlayer thcf = Factions.getInstance().getCache().getHCFPlayer(t);
                    if (thcf != null) {
                        if(thcf.getFaction() != null) {
                            ally(player, thcf.getFaction());
                        }
                        else{
                            FLang.send(p, FactionLang.PLAYER_NOT_IN_FACTION, target);
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
                            ally(player, t.getFaction());
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
            else{
                FLang.send(p, FactionLang.FACTION_CMD_MOD_ONLY);
            }
        }
        else{
            FLang.send(p, FactionLang.FACTION_NONE);
        }

    }

    public void ally(HCFPlayer player, Faction target) {
        if(target != null) {
            if(player.getFaction() != null) {
                if(target.getRelationTo(player.getFaction()) == Relation.NEUTRAL) {
                    if(target.getAllies().contains(player.getFaction().getId())) {
                        target.setAllies(player.getFaction(), true);
                        target.sendMessage(FLang.format(FactionLang.FACTION_ALLY_ACCEPT, player.getFaction().getDisplayName()));
                        player.getFaction().sendMessage(FLang.format(FactionLang.FACTION_ALLY_ACCEPT, target.getDisplayName()));
                    }
                    else{
                        player.getFaction().getAllies().add(target.getId());
                        target.sendMessage(FLang.format(FactionLang.FACTION_ALLY_REQUEST_TARGET, player.getFaction().getDisplayName()));
                        player.getFaction().sendMessage(FLang.format(FactionLang.FACTION_ALLY_REQUEST_LOCAL, target.getDisplayName()));
                    }
                }
                else{
                    FLang.send(player.getBukkitPlayer(), FactionLang.FACTION_ALLY_ALREADY, target.getDisplayName());
                }
            }
            else{
                throw new HCFException("Cannot ally when allying player is not in a faction");
            }
        }
        else{
            throw new HCFException("Cannot ally null faction target");
        }
    }

}
