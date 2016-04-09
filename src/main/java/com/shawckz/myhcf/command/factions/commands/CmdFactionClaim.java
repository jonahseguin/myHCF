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
import com.shawckz.myhcf.land.Claim;
import com.shawckz.myhcf.land.claiming.VisualClaim;
import com.shawckz.myhcf.player.HCFPlayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@FCommand(name = "claim", desc = "Claim land for your faction", usage = "/f claim", aliases = {"cl"}, playerOnly = true)
public class CmdFactionClaim implements HCFCommand {

    @Override
    public void onCommand(FCmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        if(cmdArgs.getArgs().length == 0) {
            p.sendMessage(ChatColor.GOLD + "To claim land for your faction, you must be faction moderator and use a stick.");
            p.sendMessage(ChatColor.YELLOW + "To begin claiming, select two points:");
            p.sendMessage(ChatColor.YELLOW + "For the first point, right-click with your stick on a block.");
            p.sendMessage(ChatColor.YELLOW + "For the second point, left-click with your stick on a block.");
            p.sendMessage(ChatColor.YELLOW + "To cancel claiming, type /f claim cancel.");
            p.sendMessage(ChatColor.YELLOW + "To purchase your claim, type /f claim purchase.");
        }
        else {
            if(cmdArgs.getArg(0).equalsIgnoreCase("cancel")) {
                Factions.getInstance().getClaimSelector().cancelSelection(p);
                FLang.send(p, FactionLang.FACTION_CLAIM_CANCEL);
            }
            else if (cmdArgs.getArg(0).equalsIgnoreCase("purchase")) {
                if(player.getFaction() != null){
                    VisualClaim claim = Factions.getInstance().getClaimSelector().getSelection(p);
                    if(claim != null && claim.getPos1() != null && claim.getPos2() != null) {
                        double price = Claim.getPrice(new Claim(claim.getPos1(), claim.getPos2()), player.getFaction(), true);
                        if(player.getFaction().getBalance() >= price) {
                            Claim buyClaim = new Claim(claim.getPos1(), claim.getPos2(), player.getFaction());
                            player.getFaction().setBalance(player.getFaction().getBalance() - price);
                            Factions.getInstance().getLandBoard().claim(buyClaim, player.getFaction());
                            player.getFaction().sendMessage(FLang.format(FactionLang.FACTION_CLAIM_PURCHASE, player.getName(), price+""));
                        }
                        else{
                            FLang.send(p, FactionLang.FACTION_CLAIM_NOT_ENOUGH_MONEY, price+"");
                        }
                    }
                    else{
                        p.sendMessage(ChatColor.RED + "You must make a complete claim selection before purchasing.");
                    }
                }
                else{
                    FLang.send(p, FactionLang.FACTION_NONE);
                }
            }
            else if (cmdArgs.getArg(0).equalsIgnoreCase("id")) {
                Claim claim = Factions.getInstance().getLandBoard().getClaim(p.getLocation());
                if(claim != null) {
                    if(player.getFaction() != null && claim.getFactionID().equalsIgnoreCase(player.getFaction().getId())) {
                        FLang.send(p, FactionLang.FACTION_CLAIM_ID, claim.getId());
                    }
                    else{
                        p.sendMessage(ChatColor.RED + "Your faction does not own this land.");
                    }
                }
                else{
                    p.sendMessage(ChatColor.RED+"You are not standing within a claim.");
                }
            }
        }
    }
}
