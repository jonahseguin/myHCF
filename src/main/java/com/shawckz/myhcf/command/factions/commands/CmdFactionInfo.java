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
import com.shawckz.myhcf.faction.FactionType;
import com.shawckz.myhcf.player.HCFPlayer;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@FCommand(name = "info", desc = "View information about a faction", usage = "/f info [faction]", aliases = {"i", "who", "show"})
public class CmdFactionInfo implements HCFCommand {

    @Override
    public void onCommand(final FCmdArgs args) {
        new BukkitRunnable(){
            @Override
            public void run() {
                CommandSender sender = args.getSender();
                if (args.getArgs().length == 0) {
                    //View own faction
                    if (sender instanceof Player) {
                        Player pl = (Player) sender;
                        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(pl);
                        Faction fac = player.getFaction();
                        if (fac != null) {
                            sendInfo(sender, fac);
                        }
                        else {
                            sender.sendMessage(FLang.format(FactionLang.FACTION_NONE));
                        }
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "Only players can view info on their own faction.");
                    }
                }
                else {
                    Faction faction = Factions.getInstance().getFactionManager().getFaction(args.getArg(0));
                    if(faction != null){
                        sendInfo(sender, faction);
                    }
                    else{
                        //View another players' faction
                        Player target = Bukkit.getPlayer(args.getArg(0));
                        if (target != null) {
                            HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(target);
                            Faction fac = player.getFaction();
                            if (fac != null) {
                                sendInfo(sender, fac);
                            }
                            else {
                                sender.sendMessage(FLang.format(FactionLang.FACTION_NONE_OTHER));
                            }
                        }
                        else {
                            HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(args.getArg(0));
                            if(player != null){
                                Faction fac = player.getFaction();
                                if (fac != null) {
                                    sendInfo(sender, fac);
                                }
                                else {
                                    sender.sendMessage(FLang.format(FactionLang.FACTION_NONE_OTHER));
                                }
                            }
                            else{
                                sender.sendMessage(FLang.format(FactionLang.PLAYER_FACTION_NOT_FOUND, args.getArg(0)));
                            }
                        }
                    }
                }
            }
        }.runTaskAsynchronously(Factions.getInstance());
    }

    /*
    -----------------------
    Faction - $50 - "We're cool"
    DTR: -0.01 [Raidable & Frozen] - [30 minutes]
    Home: -300, 30, 304
    Members: Shawckz, some1, lol
    Allies: ally1, ally5
    -----------------------
     */

    public void sendInfo(final CommandSender p, final Faction f) {
        FLang.send(p, FactionLang.FACTION_INFO_HEADER_FOOTER);
        if (f.isNormal()) {
            FLang.send(p, FactionLang.FACTION_INFO_DESCRIPTION, f.getDisplayName(), f.getBalance() + "", f.getDescription());
            String dtr = (f.getDeathsUntilRaidable() >= 0 ? ChatColor.YELLOW : ChatColor.RED) + "" + f.getDeathsUntilRaidable();
            String dtrStatus = "";
            boolean hasOne = false;
            {
                if (f.isRaidable()) {
                    dtrStatus += ChatColor.RED + "Raidable";
                    hasOne = true;
                }
                if (f.isDtrFrozen()) {
                    if (hasOne) {
                        dtrStatus += ChatColor.GRAY + " & ";
                    }
                    dtrStatus += ChatColor.AQUA + "Frozen";
                    hasOne = true;
                }
                if (!f.isDtrFrozen() && !f.isRaidable()) {
                    if (hasOne) {
                        dtrStatus += ChatColor.GRAY + " & ";
                    }
                    dtrStatus += ChatColor.GREEN + "Normal";
                    hasOne = true;
                }
                if (!f.isDtrFrozen() && f.getDeathsUntilRaidable() < f.getMaxDTR()) {
                    if (hasOne) {
                        dtrStatus += ChatColor.GRAY + " & ";
                    }
                    dtrStatus += ChatColor.LIGHT_PURPLE + "Regenerating";
                    hasOne = true;
                }
            }

            String dtrFreeze = "";

            if (f.getDtrFreezeFinish() > System.currentTimeMillis()) {
                long millis = f.getDtrFreezeFinish() - System.currentTimeMillis();
                dtrFreeze = String.format("%02d:%02d:%02d",
                        TimeUnit.SECONDS.toHours(millis),
                        TimeUnit.SECONDS.toMinutes(millis) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(millis)),
                        TimeUnit.SECONDS.toSeconds(millis) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(millis)));
            }

            FLang.send(p, FactionLang.FACTION_INFO_DTR, dtr, dtrStatus, dtrFreeze);
            if (f.getHome() != null) {
                FLang.send(p, FactionLang.FACTION_INFO_HOME, convertLocation(f.getHome()));
            }
            else {
                FLang.send(p, FactionLang.FACTION_INFO_HOME, "Not set");
            }

            FLang.send(p, FactionLang.FACTION_INFO_MEMBERS, convertMembers(f));
            if(f.getAllies().size() > 0) {
                FLang.send(p, FactionLang.FACTION_INFO_ALLIES, convertAllies(f));
            }
        }
        else {
            if (f.getFactionType() == FactionType.KOTH) {
                FLang.send(p, FactionLang.FACTION_INFO_KOTH_NAME, f.getDisplayName());
                if (f.getHome() != null) {
                    FLang.send(p, FactionLang.FACTION_INFO_KOTH_LOCATION, convertLocation(f.getHome()));
                }
                else {
                    FLang.send(p, FactionLang.FACTION_INFO_KOTH_LOCATION, "Not set");
                }
            }
            else if (f.getFactionType() == FactionType.ROAD) {
                FLang.send(p, FactionLang.FACTION_INFO_ROAD_NAME, f.getDisplayName());
            }
            else if (f.getFactionType() == FactionType.SPECIAL) {
                FLang.send(p, FactionLang.FACTION_INFO_SPECIAL_NAME, f.getDisplayName());
                if (f.getHome() != null) {
                    FLang.send(p, FactionLang.FACTION_INFO_SPECIAL_LOCATION, convertLocation(f.getHome()));
                }
                else {
                    FLang.send(p, FactionLang.FACTION_INFO_SPECIAL_LOCATION, "Not set");
                }
            }
            else if (f.getFactionType() == FactionType.SPAWN) {
                FLang.send(p, FactionLang.FACTION_INFO_SPAWN_NAME, f.getDisplayName());
                if (f.getHome() != null) {
                    FLang.send(p, FactionLang.FACTION_INFO_SPAWN_LOCATION, convertLocation(f.getHome()));
                }
                else {
                    FLang.send(p, FactionLang.FACTION_INFO_SPAWN_LOCATION, "Not set");
                }
            }
        }
        FLang.send(p, FactionLang.FACTION_INFO_HEADER_FOOTER);
    }

    public String convertLocation(Location loc) {
        return loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
    }

    public String convertMembers(Faction fac) {
        String s = "";
        Set<HCFPlayer> members = fac.getMembers();
        for (HCFPlayer player : members) {
            if (Bukkit.getPlayer(player.getName()) != null) {
                if (player.getFactionRole() == FactionRole.LEADER) {
                    s += ChatColor.DARK_GRAY + "**";
                }
                else if (player.getFactionRole() == FactionRole.MODERATOR) {
                    s += ChatColor.DARK_GRAY + "*";
                }
                s += ChatColor.GOLD + player.getName();
                s += ChatColor.GRAY + ", ";
            }
            else {
                if (player.getFactionRole() == FactionRole.LEADER) {
                    s += ChatColor.DARK_GRAY + "**";
                }
                else if (player.getFactionRole() == FactionRole.MODERATOR) {
                    s += ChatColor.DARK_GRAY + "*";
                }
                s += ChatColor.GRAY + player.getName();
                s += ChatColor.GRAY + ", ";
            }
        }
        if (s.length() > 4) {
            s = s.substring(0, s.length() - 4);//Remove trailing '&7, '
        }

        return s;
    }

    public String convertAllies(Faction fac) {
        String s = "";
        String color = ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getFactionsConfig().getRelationAlly());
        for (String id : fac.getAllies()) {
            Faction target = Factions.getInstance().getFactionManager().getFactionById(id);
            if (target != null) {
                s += color + target.getName() + ChatColor.GRAY + ", ";
            }
        }
        if (s.length() >= 4) {
            s = s.substring(0, s.length() - 4);
        }
        return s;
    }

}
