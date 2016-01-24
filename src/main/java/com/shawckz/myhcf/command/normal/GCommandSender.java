/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.command.normal;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on 12/13/2015.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class GCommandSender {

    private final CommandSender sender;

    public GCommandSender(CommandSender sender) {
        this.sender = sender;
    }

    public CommandSender getCommandSender() {
        return sender;
    }

    public Player getPlayer() throws GCommandException {
        if (isPlayer()) return (Player) sender;
        throw new GCommandException("CommandSender is not a player");
    }

    public boolean isPlayer() {
        return sender instanceof Player;
    }

}
