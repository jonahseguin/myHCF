/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.command.factions.commands;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.command.factions.FCmdArgs;
import com.shawckz.myhcf.command.factions.FCommand;
import com.shawckz.myhcf.command.factions.HCFCommand;
import com.shawckz.myhcf.land.claiming.VisualMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on 1/25/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@FCommand(name = "map", desc = "View nearby claims", usage = "/f map [on/off]", playerOnly = true, aliases = {"m"})
public class CmdFactionMap implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs args) {
        Player player = (Player) args.getSender();
        VisualMap visualMap = Factions.getInstance().getVisualMap();
        if (args.getArgs().length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Your visual map is currently " + ChatColor.BLUE + (visualMap.isMapEnabled(player) ? "on" : "off") + ChatColor.YELLOW + ".");
        }
        else {
            if (args.getArg(0).equalsIgnoreCase("on")) {
                if (!visualMap.isMapEnabled(player)) {
                    visualMap.enableMap(player);
                    player.sendMessage(ChatColor.YELLOW + "Your faction map has been " + ChatColor.GREEN + "enabled" + ChatColor.YELLOW + ".");
                }
                else {
                    player.sendMessage(ChatColor.RED + "Your faction map is already enabled.");
                }
            }
            else if (args.getArg(0).equalsIgnoreCase("off")) {
                if (visualMap.isMapEnabled(player)) {
                    visualMap.disableMap(player);
                    player.sendMessage(ChatColor.YELLOW + "Your faction map has been " + ChatColor.RED + "disabled" + ChatColor.YELLOW + ".");
                }
                else {
                    player.sendMessage(ChatColor.RED + "Your faction map is not enabled.");
                }
            }
            else {
                player.sendMessage(ChatColor.RED + "Invalid usage.  Usage: /f map [on/off]");
            }
        }
    }
}
