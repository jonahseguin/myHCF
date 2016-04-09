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
import com.shawckz.myhcf.player.HCFPlayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@FCommand(name = "deposit", desc = "Deposit money into your faction balance", usage = "/f deposit <amount>", minArgs = 1, playerOnly = true)
public class CmdFactionDeposit implements HCFCommand{

    @Override
    public void onCommand(FCmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);

        if(player.getFaction() != null) {
            int amount;
            try {
                amount = Integer.parseInt(cmdArgs.getArg(0));
            }
            catch (NumberFormatException expected) {
                p.sendMessage(ChatColor.RED + "The amount must be a number.");
                return;
            }
            if(player.getBalance() >= amount) {
                player.getFaction().setBalance(player.getFaction().getBalance() + amount);
                player.setBalance(player.getBalance() - amount);
                player.getFaction().sendMessage(FLang.format(FactionLang.FACTION_DEPOSIT, player.getName(), amount+""));
            }
            else{
                FLang.send(p, FactionLang.FACTION_DEPOSIT_INSUFFICIENT_FUNDS);
            }
        }
        else{
            FLang.send(p, FactionLang.FACTION_NONE);
        }

    }
}
