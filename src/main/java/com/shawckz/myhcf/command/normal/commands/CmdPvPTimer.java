/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.command.normal.commands;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.command.normal.GCmd;
import com.shawckz.myhcf.command.normal.GCmdArgs;
import com.shawckz.myhcf.command.normal.GCommand;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.myscoreboard.hcf.HCFLabelID;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimerFormat;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class CmdPvPTimer implements GCommand {

    @GCmd(name = "pvptimer", aliases = {"pvp"}, playerOnly = false)
    public void onCommand(GCmdArgs args) {
        args.getSender().getCommandSender().sendMessage(ChatColor.GRAY + "*** PvPTimer Commands ***");
        args.getSender().getCommandSender().sendMessage(ChatColor.AQUA + "/pvp enable - Enable pvp");
        args.getSender().getCommandSender().sendMessage(ChatColor.AQUA + "/pvp time - Check your PvPTimer time");
        if (args.getSender().getCommandSender().hasPermission("myhcf.cmd.pvptimer.admin")) {
            args.getSender().getCommandSender().sendMessage(ChatColor.GRAY + "*** PvPTimer Admin Commands ***");
            args.getSender().getCommandSender().sendMessage(ChatColor.AQUA + "/pvp settime <player> <time in minutes> - Set a player's PvPTimer");
            args.getSender().getCommandSender().sendMessage(ChatColor.AQUA + "/pvp checktime <player> - Check a player's PvPTimer time");
            args.getSender().getCommandSender().sendMessage(ChatColor.AQUA + "/pvp forceenable <player> - Force a player to enable pvp");
        }
    }

    @GCmd(name = "pvptimer enable", aliases = "pvp enable", playerOnly = true)
    public void onCommandEnable(GCmdArgs args) {
        Player pl = args.getSender().getPlayer();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(pl);
        if (player.getScoreboard().getLabel(HCFLabelID.PVP_TIMER).getAsTimer().getTimerValue() > 0.0D) {
            player.getScoreboard().removeLabel(HCFLabelID.PVP_TIMER).getAsTimer().setTimerValue(0);
            player.getBukkitPlayer().sendMessage(FLang.format(FactionLang.PVP_TIMER_REMOVE));
        }
        else {
            pl.sendMessage(FLang.format(FactionLang.PVP_TIMER_NOT_ACTIVE));
        }
    }

    @GCmd(name = "pvptimer time", aliases = "pvp time", playerOnly = true)
    public void onCommandTime(GCmdArgs args) {
        Player pl = args.getSender().getPlayer();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(pl);
        if (player.getScoreboard().getLabel(HCFLabelID.PVP_TIMER).getAsTimer().getTimerValue() > 0.0D) {
            int seconds = (int) Math.round(player.getScoreboard().getLabel(HCFLabelID.PVP_TIMER).getAsTimer().getTimerValue());
            long millis = seconds * 1000;
            String time = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

            pl.sendMessage(FLang.format(FactionLang.PVP_TIMER_TIME_REMAINING, time));
        }
        else {
            pl.sendMessage(FLang.format(FactionLang.PVP_TIMER_NOT_ACTIVE));
        }
    }

    @GCmd(name = "pvptimer settime", aliases = "pvp settime", permission = "myhcf.cmd.pvptimer.settime", minArgs = 2)
    public void onCommandSetTime(GCmdArgs args) {
        CommandSender sender = args.getSender().getCommandSender();
        Player target = Bukkit.getPlayer(args.getArg(0));
        if (target != null) {
            int minutes;
            try {
                minutes = Integer.parseInt(args.getArg(1));
            }
            catch (NumberFormatException expected) {
                sender.sendMessage(ChatColor.RED + "The second argument ('time in minutes') must be a number.");
                return;
            }

            int seconds = minutes * 60;
            HCFPlayer tHCF = Factions.getInstance().getCache().getHCFPlayer(target);
            tHCF.getScoreboard().getTimer(FLabel.PVP_TIMER, HCFTimerFormat.HH_MM_SS).setTime(seconds).show();
            tHCF.getScoreboard().addLabel(HCFLabelID.PVP_TIMER, true).getAsTimer().setTimerValue(seconds).update();

            long millis = seconds * 1000;
            String time = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

            target.sendMessage(ChatColor.YELLOW + "Your PVPTimer was set to " + ChatColor.BLUE + time + ChatColor.YELLOW + ".");
            sender.sendMessage(ChatColor.YELLOW + "You set " + ChatColor.BLUE + target.getName() +
                    ChatColor.YELLOW + "'s PvPTimer to " + ChatColor.BLUE + time + ChatColor.YELLOW + ".");
        }
        else {
            sender.sendMessage(FLang.format(FactionLang.PLAYER_NOT_FOUND, args.getArg(0)));
        }
    }

    @GCmd(name = "pvptimer checktime", aliases = "pvp checktime", permission = "myhcf.cmd.pvptimer.checktime", minArgs = 1)
    public void onCommandCheckTime(GCmdArgs args) {
        CommandSender sender = args.getSender().getCommandSender();
        Player target = Bukkit.getPlayer(args.getArg(0));
        if (target != null) {
            HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(target);
            if (player.getScoreboard().getTimer(FLabel.PVP_TIMER, HCFTimerFormat.HH_MM_SS).getTime() > 0.1) {
                int seconds = (int) Math.round(player.getScoreboard().getTimer(FLabel.PVP_TIMER, HCFTimerFormat.HH_MM_SS).getTime());
                long millis = seconds * 1000;
                String time = String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                sender.sendMessage(ChatColor.BLUE + target.getName() + ChatColor.YELLOW + "'s PvPTimer has " +
                        ChatColor.BLUE + time + ChatColor.YELLOW + " remaining.");
            }
            else {
                sender.sendMessage(FLang.format(FactionLang.PVP_TIMER_NOT_ACTIVE_OTHER, target.getName()));
            }
        }
        else {
            sender.sendMessage(FLang.format(FactionLang.PLAYER_NOT_FOUND, args.getArg(0)));
        }
    }

    @GCmd(name = "pvptimer forceenable", aliases = "pvp forceenable", permission = "myhcf.cmd.pvptimer.forceenable", minArgs = 1)
    public void onCommandForceEnable(GCmdArgs args) {
        CommandSender sender = args.getSender().getCommandSender();
        Player target = Bukkit.getPlayer(args.getArg(0));
        if (target != null) {
            HCFPlayer tHCF = Factions.getInstance().getCache().getHCFPlayer(target);
            if (tHCF.getScoreboard().getTimer(FLabel.PVP_TIMER, HCFTimerFormat.HH_MM_SS).getTime() > 0.1) {
                tHCF.getScoreboard().getTimer(FLabel.PVP_TIMER, HCFTimerFormat.HH_MM_SS).setTime(0).pauseTimer().hide();
                sender.sendMessage(FLang.format(FactionLang.PVP_TIMER_FORCE_ENABLE, target.getName()));
                target.sendMessage(FLang.format(FactionLang.PVP_TIMER_REMOVE));
            }
            else {
                target.sendMessage(FLang.format(FactionLang.PVP_TIMER_NOT_ACTIVE_OTHER, target.getName()));
            }
        }
        else {
            sender.sendMessage(FLang.format(FactionLang.PLAYER_NOT_FOUND, args.getArg(0)));
        }
    }

}
