/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.command.factions;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.command.factions.commands.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by 360 on 21/07/2015.
 */
public class FCommandManager implements CommandExecutor {

    @Getter
    private static FCommandManager instance;
    @Getter
    private Map<String, HCFCmdData> cmds = new HashMap<>();

    public FCommandManager(Factions plugin) {
        instance = this;

        plugin.getCommand("factions").setExecutor(this);

        //Register commands
        registerCommand(new CmdFactionCreate());
        registerCommand(new CmdFactionDisband());
        registerCommand(new CmdFactionInfo());
        registerCommand(new CmdFactionMap());
        registerCommand(new CmdFactionJoin());
        registerCommand(new CmdFactionAdminClaim());
        registerCommand(new CmdFactionLeave());
        registerCommand(new CmdFactionKick());
        registerCommand(new CmdFactionTag());
        registerCommand(new CmdFactionDescription());
        registerCommand(new CmdFactionHome());
        registerCommand(new CmdFactionSetHome());
        if(Factions.getInstance().getFactionsConfig().isFactionsUseRally()) {
            //Only register rally commands if rally is enabled
            registerCommand(new CmdFactionRally());
            registerCommand(new CmdFactionSetRally());
        }
        registerCommand(new CmdFactionInvite());
        registerCommand(new CmdFactionPromote());
        registerCommand(new CmdFactionDemote());
        registerCommand(new CmdFactionLeader());
        registerCommand(new CmdFactionStuck());
        registerCommand(new CmdFactionAlly());
        registerCommand(new CmdFactionEnemy());
        registerCommand(new CmdFactionHelp());
        registerCommand(new CmdFactionSetDtr());
        registerCommand(new CmdFactionUnfreeze());
        registerCommand(new CmdFactionClaim());
        registerCommand(new CmdFactionDeposit());
        registerCommand(new CmdFactionWithdraw());
        registerCommand(new CmdFactionUnclaim());
        registerCommand(new CmdFactionSave());
        registerCommand(new CmdFactionList());
        registerCommand(new CmdFactionChat());
    }

    public void registerCommand(HCFCommand cmd) {
        if (cmd.getClass().isAnnotationPresent(FCommand.class)) {
            FCommand command = cmd.getClass().getAnnotation(FCommand.class);
            HCFCmdData data = new HCFCmdData(cmd, command.name(), command.aliases(), command.playerOnly(), command.perm(),
                    command.usage(), command.flags(), command.allowFlags(), command.minArgs(), command.desc());
            cmds.put(data.getName().toLowerCase(), data);
        } else {
            throw new IllegalStateException("Class must have a @FCommand annotation");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("factions")) {

            String command = "help";

            if (args.length > 0) {
                command = args[0].toLowerCase();
            }

            HCFCmdData c = null;

            if (cmds.containsKey(command.toLowerCase())) {
                c = cmds.get(command.toLowerCase());
            } else {
                for (HCFCmdData data : cmds.values()) {
                    if (c != null) break;
                    if (data.getName().equalsIgnoreCase(command)) {
                        c = data;
                        break;
                    } else {
                        for (String alias : data.getAliases()) {
                            if (c != null) break;
                            if (alias.equalsIgnoreCase(command)) {
                                c = data;
                                break;
                            }
                        }
                    }
                }
            }

            if (c != null) {
                handle(sender, c, args);
            } else {
                sender.sendMessage(ChatColor.RED + "Unknown factions command '" + command + "'.  Type /factions help for a list of commands.");
            }

        }
        return true;
    }

    private void handle(CommandSender sender, HCFCmdData cmd, String[] args) {
        if (cmd.isPlayerOnly() && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This is a player only command.");
            return;
        }
        if (!cmd.getPermission().equals("")) {
            if (!sender.hasPermission(cmd.getPermission())) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return;
            }
        }

        String[] newArgs = new String[(args.length > 0 ? args.length - 1 : 0)];
        for (int i = 1; i < args.length; i++) {
            newArgs[i - 1] = args[i];
        }
        args = newArgs;
        if (args.length < cmd.getMinArgs()) {
            if (cmd.getUsage().equalsIgnoreCase("")) {
                sender.sendMessage(ChatColor.RED + "Incorrect usage.");
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
            }
            return;
        }

        FCmdArgs cmdArgs = new FCmdArgs(sender, args);
        cmd.getCommand().onCommand(cmdArgs);
    }
}