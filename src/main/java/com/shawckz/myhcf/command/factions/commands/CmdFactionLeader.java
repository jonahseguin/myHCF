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

/**
 * Created by Jonah Seguin on 1/25/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@FCommand(name = "leader", desc = "Set the faction leader", usage = "/f leader <player>", playerOnly = true, minArgs = 1, aliases = {"setleader", "admin", "setadmin"})
public class CmdFactionLeader implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs args) {
        Player p = (Player) args.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        String t = args.matchPlayer(0);

        if(player.getFaction() != null) {
            if(player.getFactionRole() == FactionRole.LEADER) {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        HCFPlayer target = Factions.getInstance().getCache().getHCFPlayer(t);
                        if(target.getFaction() != null) {
                            if(target.getFaction().getId().equals(player.getFaction().getId())) {
                                player.getFaction().setLeader(target);
                                target.setFactionRole(FactionRole.LEADER);
                                player.setFactionRole(FactionRole.MEMBER);
                                Factions.getInstance().getDbHandler().push(player);
                                player.getFaction().sendMessage(FLang.format(FactionLang.FACTION_LEADER_LOCAL, target.getName(), player.getName()));
                            }
                            else{
                                FLang.send(p, FactionLang.FACTION_NOT_SAME, target.getName());
                            }
                        }
                        else{
                            FLang.send(p, FactionLang.FACTION_NONE_OTHER);
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
