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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@FCommand(name = "list", desc = "List online factions", usage = "/f list [page]", aliases = "online")
public class CmdFactionList implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs cmdArgs) {
        CommandSender sender = cmdArgs.getSender();
        List<Faction> list = new ArrayList<>();

        for(Faction f : Factions.getInstance().getFactionManager().getFactions()) {
            if(f.getOnlineMembers().size() > 0) {
                list.add(f);
            }
        }

        list.sort(new FactionComparator());

        int page = 1;

        if(cmdArgs.getArgs().length > 0) {
            try{
                page = Integer.parseInt(cmdArgs.getArg(0));
                if(page < 1) {
                    page = 1;
                }
            }
            catch (NumberFormatException expected) {
                sender.sendMessage(ChatColor.RED + "Page must be a number");
            }
        }

        FLang.send(sender, FactionLang.FACTION_INFO_HEADER_FOOTER);

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7*** &9Factions List &7- &ePage " + page + " &7***"));

        while (list.size() < page * 10) {
            page -= 1;
            if(page < 1) {
                page = 1;
                break;
            }
        }

        List<Faction> pgList = list.subList(((page * 10) - 10), page * 10);

        for(int i = 0; i < pgList.size(); i++) {
            Faction entry = pgList.get(i);
            FLang.send(sender, FactionLang.FACTION_LIST,
                    (i+1)+"",
                    entry.getDisplayName(),
                    entry.getOnlineMembers().size()+"",
                    entry.getMembers().size()+"",
                    (entry.getDeathsUntilRaidable() + "&7/&e" + entry.getMaxDTR()));
        }

        FLang.send(sender, FactionLang.FACTION_INFO_HEADER_FOOTER);


    }

    class FactionComparator implements Comparator<Faction> {

        @Override
        public int compare(Faction o1, Faction o2) {
            return o1.getOnlineMembers().size() - o2.getOnlineMembers().size();
        }
    }

}
