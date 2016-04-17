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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on 1/25/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@FCommand(name = "tag", desc = "Change the name of your faction", usage = "/f tag <name>", playerOnly = true, minArgs = 1)
public class CmdFactionTag implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs args) {
        Player p = (Player) args.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);
        String name = args.getArg(0);

        if(player.getFaction() != null) {
            if(player.getFactionRole() != FactionRole.MEMBER) {
                if(CmdFactionCreate.validName(name, p)) {
                    if(player.getFaction().getDisplayName().equals(name)) {
                        p.sendMessage(ChatColor.RED + "The new faction name must be different than your current name.");
                        return;
                    }
                    Faction f = player.getFaction();
                    Bukkit.broadcastMessage(FLang.format(FactionLang.FACTION_TAG_BROADCAST, f.getDisplayName(), name));
                    f.sendMessage(FLang.format(FactionLang.FACTION_TAG_LOCAL, p.getName(), name));
                    f.setDisplayName(name);
                    Factions.getInstance().getDbHandler().push(f);
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
}
