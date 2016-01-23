package com.shawckz.myhcf.configuration;

import com.shawckz.myhcf.Factions;

public class FLang {

    public static String format(FactionLang factionLang, String... args) {
        return Factions.getInstance().getLang().getFormattedLang(factionLang, args);
    }

}
