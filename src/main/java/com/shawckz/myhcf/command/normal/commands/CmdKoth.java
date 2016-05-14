/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.command.normal.commands;


import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.command.normal.GCmd;
import com.shawckz.myhcf.command.normal.GCmdArgs;
import com.shawckz.myhcf.command.normal.GCommand;
import com.shawckz.myhcf.koth.Koth;
import com.shawckz.myhcf.koth.KothSchedule;
import com.shawckz.myhcf.koth.ScheduledKoth;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.util.FSelection;

import java.text.SimpleDateFormat;
import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CmdKoth implements GCommand {

    Comparator<Long> descendingComp = new Comparator<Long>() {
        @Override
        public int compare(Long o1, Long o2) {
            return Math.round(o1 - o2);
        }
    };

    @GCmd(name = "koth help", playerOnly = false)
    public void onCmdHelp(GCmdArgs args) {
        CommandSender sender = args.getSender().getCommandSender();

        msg(sender, "&7*** &6Koth Commands &7***");
        msg(sender, "&7- &e/koth&9 ");
        if(sender.hasPermission("myhcf.koth.admin")) {
            msg(sender, "&7- &e/koth&9 create");
            msg(sender, "&7- &e/koth&9 delete");
            msg(sender, "&7- &e/koth&9 schedule");
            msg(sender, "&7- &e/koth&9 start");
            msg(sender, "&7- &e/koth&9 stop");
            msg(sender, "&7- &e/koth&9 setcap");
            msg(sender, "&7- &e/koth&9 rename");
        }
    }

    @GCmd(name = "koth", aliases = {"koth time", "koth list", "koth times"})
    public void onCommand(GCmdArgs args) {
        CommandSender sender = args.getSender().getCommandSender();

        msg(sender, "&7*** &6Koth Schedule &7***");

        KothSchedule schedule = Factions.getInstance().getKothManager().getSchedule();

        TreeMap<Long, ScheduledKoth> koths = new TreeMap<>(descendingComp);
        koths.putAll(schedule.getSchedule());

        for(long key : koths.descendingKeySet()) {
            ScheduledKoth koth = koths.get(key);

            long date = koth.getDate();
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("est"));
            cal.setTimeInMillis(date);

            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int hour = cal.get(Calendar.HOUR);
            int minute = cal.get(Calendar.MINUTE);
            boolean am = cal.get(Calendar.AM_PM) == Calendar.AM;

            // '4/29 @ 8:05PM EST'
            msg(sender, "&7- &a" + koth.getName() + " &7- &e" + month+"/"+day+" @ " + hour+":"+minute+ " " + (am ? "AM" : "PM") +" EST");
        }
    }

    @GCmd(name = "koth create", description = "Create a koth with your current selection", usage = "/koth create <name>", minArgs = 1, permission = "myhcf.koth.create", playerOnly = true)
    public void onCmdCreate(GCmdArgs args) {
        Player p = args.getSender().getPlayer();
        HCFPlayer hcfPlayer = Factions.getInstance().getCache().getHCFPlayer(p);
        FSelection selection = hcfPlayer.getSelection();
        String name = args.getArg(0);

        if(selection.getMin() != null && selection.getMax() != null) {
            if(Factions.getInstance().getKothManager().getKoth(name) == null) {
                p.sendMessage(ChatColor.GRAY + "Creating koth...");
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        Koth koth = new Koth(name, selection.getMin(), selection.getMax());
                        Factions.getInstance().getDbHandler().push(koth);
                        Factions.getInstance().getKothManager().registerKoth(koth);

                        p.sendMessage(ChatColor.GREEN + "Koth '" + koth.getName() + "' created.");

                    }
                }.runTaskAsynchronously(Factions.getInstance());
            }
            else{
                p.sendMessage(ChatColor.RED + "A koth with that name already exists.");
            }
        }
        else{
            p.sendMessage(ChatColor.RED + "You must make a selection using a golden axe first. (Right & left click)");
        }
    }

    @GCmd(name = "koth setcap", description = "Set the cap zone for a koth with your current selection", usage = "/koth setkcap <koth>", minArgs = 1, permission = "myhcf.koth.setcap", playerOnly = true)
    public void onCmdSetCap(GCmdArgs args) {
        Player p = args.getSender().getPlayer();
        HCFPlayer hcfPlayer = Factions.getInstance().getCache().getHCFPlayer(p);
        FSelection selection = hcfPlayer.getSelection();
        String name = args.getArg(0);

        if(selection.getMin() != null && selection.getMax() != null) {
            final Koth koth = Factions.getInstance().getKothManager().getKoth(name);
            if(koth != null) {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        koth.setCapPos1(selection.getMin());
                        koth.setCapPos2(selection.getMax());
                        Factions.getInstance().getDbHandler().push(koth);

                        p.sendMessage(ChatColor.GREEN + "Updated the cap zone for the koth '" + koth.getName() + "'.");

                    }
                }.runTaskAsynchronously(Factions.getInstance());
            }
            else{
                p.sendMessage(ChatColor.RED + "A koth with that name does not exist.");
            }
        }
        else{
            p.sendMessage(ChatColor.RED + "You must make a selection using a golden axe first. (Right & left click)");
        }
    }

    @GCmd(name = "koth delete", description = "Delete a koth", usage = "/koth delete <name>", minArgs = 1, permission = "myhcf.koth.delete", playerOnly = false)
    public void onCmdDelete(GCmdArgs args) {
        CommandSender p = args.getSender().getCommandSender();
        String name = args.getArg(0);
        final Koth koth = Factions.getInstance().getKothManager().getKoth(name);
        if(koth != null) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    Factions.getInstance().getDbHandler().delete(koth);
                    Factions.getInstance().getKothManager().unregisterKoth(koth);

                    p.sendMessage(ChatColor.GREEN + "Koth '" + koth.getName() + "' deleted.");
                }
            }.runTaskAsynchronously(Factions.getInstance());
        }
        else{
            p.sendMessage(ChatColor.RED + "A koth with that name does not exist.");
        }
    }

    @GCmd(name = "koth rename", description = "Rename a koth", usage = "/koth rename <name> <newname>", minArgs = 2, permission = "myhcf.koth.rename", playerOnly = false)
    public void onCmdRename(GCmdArgs args) {
        CommandSender p = args.getSender().getCommandSender();
        String name = args.getArg(0);
        final String newName = args.getArg(1);
        final Koth koth = Factions.getInstance().getKothManager().getKoth(name);
        if(koth != null) {
            new BukkitRunnable(){
                @Override
                public void run() {
                    koth.setName(newName);

                    //Re-register with new name
                    Factions.getInstance().getKothManager().unregisterKoth(koth);
                    Factions.getInstance().getKothManager().registerKoth(koth);

                    Factions.getInstance().getDbHandler().push(koth);

                    p.sendMessage(ChatColor.GREEN + "Koth '" + name + "' renamed to '"+newName+"'.");
                }
            }.runTaskAsynchronously(Factions.getInstance());
        }
        else{
            p.sendMessage(ChatColor.RED + "A koth with that name does not exist.");
        }
    }

    @GCmd(name = "koth start", description = "Start a koth", usage = "/koth start <name>", minArgs = 1, permission = "myhcf.koth.start", playerOnly = false)
    public void onCmdStart(GCmdArgs args) {
        CommandSender p = args.getSender().getCommandSender();
        String name = args.getArg(0);
        final Koth koth = Factions.getInstance().getKothManager().getKoth(name);
        if(koth != null) {
            if(!koth.isActive()) {
                Factions.getInstance().getKothManager().activateKoth(koth);

                p.sendMessage(ChatColor.GREEN + "Koth '" + koth.getName() + "' force-started.");
            }
            else{
                p.sendMessage(ChatColor.RED + "That koth is already active.");
            }
        }
        else{
            p.sendMessage(ChatColor.RED + "A koth with that name does not exist.");
        }
    }

    @GCmd(name = "koth stop", aliases = {"koth cancel"}, description = "Stop a koth", usage = "/koth stop <name>", minArgs = 1, permission = "myhcf.koth.stop", playerOnly = false)
    public void onCmdStop(GCmdArgs args) {
        CommandSender p = args.getSender().getCommandSender();
        String name = args.getArg(0);
        final Koth koth = Factions.getInstance().getKothManager().getKoth(name);
        if(koth != null) {
            if(koth.isActive()) {
                Factions.getInstance().getKothManager().cancelKoth(koth);
                p.sendMessage(ChatColor.GREEN + "Koth '" + koth.getName() + "' cancelled.");
            }
            else{
                p.sendMessage(ChatColor.RED + "That koth is not active.");
            }
        }
        else{
            p.sendMessage(ChatColor.RED + "A koth with that name does not exist.");
        }
    }

    @GCmd(name = "koth schedule", description = "Schedule a koth", usage = "/koth schedule <name> <timestamp: MM-dd-hh-mm>", minArgs = 2, permission = "myhcf.koth.schedule", playerOnly = false)
    public void onCmdSchedule(GCmdArgs args) {
        CommandSender p = args.getSender().getCommandSender();
        String name = args.getArg(0);
        final String inputTime = args.getArg(1);
        final Koth koth = Factions.getInstance().getKothManager().getKoth(name);
        if(koth != null) {
           new BukkitRunnable(){
               @Override
               public void run() {
                   String[] split = inputTime.split("-");
                   if(split.length >= 5) {
                       String formattedInputTime = split[0] + "-" + split[1] + " " +split[2] + ":" +split[3];
                       SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm");
                       Date date;
                       try {
                           date = dateFormat.parse(formattedInputTime);
                       }
                       catch (java.text.ParseException expected) {
                           p.sendMessage(ChatColor.RED + "Time format must match [MM-dd-hh-mm]");
                           return;
                       }

                       Factions.getInstance().getKothManager().getSchedule().scheduleKoth(date.getTime(), koth);
                       p.sendMessage(ChatColor.GREEN + "Scheduled koth '" + koth.getName()+"' for " + date.toString());
                   }
                   else{
                       p.sendMessage(ChatColor.RED + "Time format must match [MM-dd-hh-mm]");
                   }
               }
           }.runTaskAsynchronously(Factions.getInstance());
        }
        else{
            p.sendMessage(ChatColor.RED + "A koth with that name does not exist.");
        }
    }


    private void msg(CommandSender sender, String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
}
