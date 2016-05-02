/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.command.normal.commands;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.command.normal.GCmd;
import com.shawckz.myhcf.command.normal.GCmdArgs;
import com.shawckz.myhcf.command.normal.GCommand;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.player.HCFPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdEconomy implements GCommand {

    @GCmd(name = "economy", aliases = {"eco"}, permission = "myhcf.economy.admin")
    public void onCommand(GCmdArgs args) {
        CommandSender sender = args.getSender().getCommandSender();

        sender.sendMessage(ChatColor.GOLD + "myHCF Economy Commands");
        sender.sendMessage(ChatColor.GRAY + "/balance [player]");
        sender.sendMessage(ChatColor.GRAY + "/eco give <player> <amount>");
        sender.sendMessage(ChatColor.GRAY + "/eco set <player> <amount>");
        sender.sendMessage(ChatColor.GRAY + "/eco take <player> <amount>");
    }

    @GCmd(name = "economy give", aliases = {"eco give"}, permission = "myhcf.economy.admin.give", usage = "/eco give <player> <amount>", minArgs = 2)
    public void onCommandGive(GCmdArgs args) {
        new BukkitRunnable(){
            @Override
            public void run() {
                CommandSender sender = args.getSender().getCommandSender();

                String s = args.getArg(0);
                Player t = Bukkit.getPlayer(s);
                if(t != null) {
                    s = t.getName();
                }

                HCFPlayer target = Factions.getInstance().getCache().getHCFPlayer(s);

                if(target != null) {
                    int i;
                    try{
                        i = Integer.parseInt(args.getArg(1));
                    }
                    catch (NumberFormatException ex) {
                        sender.sendMessage(ChatColor.RED + "The amount must be a number.");
                        return;
                    }
                    target.setBalance(target.getBalance() + i);

                    if(t == null) {
                        Factions.getInstance().getDbHandler().push(target);
                    }

                    FLang.send(sender, FactionLang.CMD_ECONOMY_UPDATE, s, target.getBalance()+"");

                    if(t != null) {
                        FLang.send(t, FactionLang.CMD_ECONOMY_UPDATE_TARGET, target.getBalance()+"");
                    }

                }
                else{
                    FLang.send(sender, FactionLang.PLAYER_NOT_FOUND, s);
                }
            }
        }.runTaskAsynchronously(Factions.getInstance());
    }

    @GCmd(name = "economy take", aliases = {"eco take"}, permission = "myhcf.economy.admin.give", usage = "/eco take <player> <amount>", minArgs = 2)
    public void onCommandTake(GCmdArgs args) {
        new BukkitRunnable(){
            @Override
            public void run() {
                CommandSender sender = args.getSender().getCommandSender();

                String s = args.getArg(0);
                Player t = Bukkit.getPlayer(s);
                if(t != null) {
                    s = t.getName();
                }

                HCFPlayer target = Factions.getInstance().getCache().getHCFPlayer(s);

                if(target != null) {
                    int i;
                    try{
                        i = Integer.parseInt(args.getArg(1));
                    }
                    catch (NumberFormatException ex) {
                        sender.sendMessage(ChatColor.RED + "The amount must be a number.");
                        return;
                    }
                    target.setBalance(target.getBalance() - i);

                    if(target.getBalance() < 0) {
                        target.setBalance(0);
                    }

                    if(t == null) {
                        Factions.getInstance().getDbHandler().push(target);
                    }

                    FLang.send(sender, FactionLang.CMD_ECONOMY_UPDATE, s, target.getBalance()+"");

                    if(t != null) {
                        FLang.send(t, FactionLang.CMD_ECONOMY_UPDATE_TARGET, target.getBalance()+"");
                    }
                }
                else{
                    FLang.send(sender, FactionLang.PLAYER_NOT_FOUND, s);
                }
            }
        }.runTaskAsynchronously(Factions.getInstance());
    }

    @GCmd(name = "economy set", aliases = {"eco set"}, permission = "myhcf.economy.admin.set", usage = "/eco set <player> <amount>", minArgs = 2)
    public void onCommandSet(GCmdArgs args) {
        new BukkitRunnable(){
            @Override
            public void run() {
                CommandSender sender = args.getSender().getCommandSender();

                String s = args.getArg(0);
                Player t = Bukkit.getPlayer(s);
                if(t != null) {
                    s = t.getName();
                }

                HCFPlayer target = Factions.getInstance().getCache().getHCFPlayer(s);

                if(target != null) {
                    int i;
                    try{
                        i = Integer.parseInt(args.getArg(1));
                    }
                    catch (NumberFormatException ex) {
                        sender.sendMessage(ChatColor.RED + "The amount must be a number.");
                        return;
                    }
                    target.setBalance(i);

                    if(t == null) {
                        Factions.getInstance().getDbHandler().push(target);
                    }

                    FLang.send(sender, FactionLang.CMD_ECONOMY_UPDATE, s, target.getBalance()+"");

                    if(t != null) {
                        FLang.send(t, FactionLang.CMD_ECONOMY_UPDATE_TARGET, target.getBalance()+"");
                    }

                }
                else{
                    FLang.send(sender, FactionLang.PLAYER_NOT_FOUND, s);
                }
            }
        }.runTaskAsynchronously(Factions.getInstance());
    }

    @GCmd(name = "balance", aliases = {"bal", "money", "gold", "dollars"}, playerOnly = true)
    public void onCommandBalance(GCmdArgs args) {
        Player p = args.getSender().getPlayer();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        if(args.getArgs().length == 0 || !p.hasPermission("myhcf.economy.balance.others")) {
            FLang.send(p, FactionLang.ECO_BALANCE, player.getBalance() + "");
        }
        else{
            String target = args.getArg(0);
            Player tt = Bukkit.getPlayer(target);
            if(tt != null) {
                target = tt.getName();
            }
            HCFPlayer t = Factions.getInstance().getCache().getHCFPlayer(target);
            if(t != null) {
                FLang.send(p, FactionLang.ECO_BALANCE_OTHER, t.getName(), t.getBalance()+"");
            }
            else{
                FLang.send(p, FactionLang.PLAYER_NOT_FOUND, target);
            }
        }
    }


}
