package com.shawckz.myhcf.configuration;

import com.shawckz.myhcf.Factions;
import org.bukkit.command.CommandSender;

public class FLang {

    public static String format(FactionLang factionLang, String... args) {
        return Factions.getInstance().getLang().getFormattedLang(factionLang, args);
    }

    public static void send(CommandSender player, FactionLang lang, String... args) {
        String s = format(lang, args);
        if (!s.equals("")) {
            player.sendMessage(s);
        }
    }

}
