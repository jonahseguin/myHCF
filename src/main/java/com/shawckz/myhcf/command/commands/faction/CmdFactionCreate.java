/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.command.commands.faction;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.command.CmdArgs;
import com.shawckz.myhcf.command.FCommand;
import com.shawckz.myhcf.command.HCFCommand;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.configuration.FactionsConfig;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.faction.FactionType;
import com.shawckz.myhcf.faction.serial.FactionTypeSerializer;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.util.HCFException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.regex.Pattern;

/**
 * Created by Jonah Seguin on 1/23/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */

@FCommand(name = "create", flags = {"special"}, desc = "Create a faction", usage = "/f create <name>", perm = "myhcf.cmd.faction.create", minArgs = 1)
public class CmdFactionCreate implements HCFCommand {

    private static final Pattern ALPHA_NUMERIC = Pattern.compile("[^a-zA-Z0-9]");

    private final FactionsConfig config = Factions.getInstance().getFactionsConfig();

    private final FactionTypeSerializer factionTypeSerializer = new FactionTypeSerializer();

    @Override
    public void onCommand(CmdArgs args) {
        CommandSender sender = args.getSender();
        String name = args.getArg(0);

        if (name.length() > config.getMaxFactionNameLength()) {
            sender.sendMessage(ChatColor.RED + "The faction name cannot be longer than " + config.getMaxFactionNameLength() + " characters.");
            return;
        }
        if (ALPHA_NUMERIC.matcher(name).find()) {
            sender.sendMessage(ChatColor.RED + "The faction name must be alphanumeric.");
            return;
        }

        if (args.hasFlag("special")) {
            if (sender.hasPermission("myhcf.admin.cmd.create.special")) {
                if (args.getArgs().length >= 2) {
                    //They specified the name, and the type of special
                    String specialString = args.getArg(1);
                    FactionType factionType;
                    try {
                        factionType = factionTypeSerializer.fromString(specialString);
                    }
                    catch (HCFException ex) {
                        sender.sendMessage(ChatColor.RED + "Invalid special value (FactionType).");
                        return;
                    }

                    sender.sendMessage(ChatColor.GREEN + "You have selected to make a faction by name '" + name + "' with type '" + factionType.toString() + "'");

                    //TODO: Create special faction

                }
                else {
                    sender.sendMessage(ChatColor.RED + "Please also specify the type of special for flag '-special'");
                }
            }
            else {
                sender.sendMessage(ChatColor.RED + "No permission for flag 'special'.");
            }
        }
        else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can create normal factions.");
                return;
            }
            final Player player = (Player) sender;
            final HCFPlayer hcfPlayer = Factions.getInstance().getCache().getHCFPlayer(player);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!Factions.getInstance().getFactionManager().factionExists(name)) {

                        Faction faction = Factions.getInstance().getFactionManager().createFaction(name, FactionType.NORMAL);

                        faction.setLeader(hcfPlayer);
                        hcfPlayer.setFactionId(faction.getId());

                        Factions.getInstance().getFactionManager().addToCache(faction);

                        Bukkit.broadcastMessage(FLang.format(FactionLang.FACTION_CREATE_BROADCAST, faction.getDisplayName(), sender.getName()));

                        sender.sendMessage(FLang.format(FactionLang.FACTION_CREATE_FINISH));

                        faction.save();
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "A faction with name '" + name + "' already exists.");
                    }

                }
            }.runTaskAsynchronously(Factions.getInstance());
        }
    }
}
