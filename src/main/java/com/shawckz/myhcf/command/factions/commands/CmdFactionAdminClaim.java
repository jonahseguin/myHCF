/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.command.factions.commands;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.command.factions.FCmdArgs;
import com.shawckz.myhcf.command.factions.FCommand;
import com.shawckz.myhcf.command.factions.HCFCommand;
import com.shawckz.myhcf.faction.FactionType;
import com.shawckz.myhcf.land.Claim;
import com.shawckz.myhcf.player.HCFPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Jonah Seguin on 1/25/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@FCommand(name = "adminclaim", desc = "Admin Claim", usage = "/f adminclaim <faction>", playerOnly = true, perm = "myhcf.cmd.admin.adminclaim", minArgs = 1)
public class CmdFactionAdminClaim implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs args) {
        Player p = (Player) args.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        Factions.getInstance().getFactionManager().getFaction(args.getArg(0), faction -> {
            if(faction != null){
                if(player.getSelection() != null
                        && player.getSelection().getMax() != null && player.getSelection().getMin() != null){
                    final Claim claim = new Claim(player.getSelection().getMin(), player.getSelection().getMax(), faction);
                    if(faction.getFactionType() != FactionType.NORMAL) {
                        if(faction.getFactionType() == FactionType.SPAWN) {
                            Factions.getInstance().getLandBoard().unclaimAll(faction);
                            Factions.getInstance().getSpawn().setClaim(claim);
                        }
                        Factions.getInstance().getLandBoard().claim(claim, faction);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Factions.getInstance().getDbHandler().push(claim);
                            }
                        }.runTaskAsynchronously(Factions.getInstance());
                        p.sendMessage(ChatColor.YELLOW + "Successfully admin-claimed for faction '" + faction.getDisplayName() + "'.");
                    }
                    else{
                        p.sendMessage(ChatColor.RED + "You cannot admin-claim for normal factions.");
                    }
                }
                else{
                    p.sendMessage(ChatColor.RED + "You must make a selection using a gold axe first.");
                }
            }
            else{
                p.sendMessage(ChatColor.RED + "Faction not found.");
            }
        });

    }
}
