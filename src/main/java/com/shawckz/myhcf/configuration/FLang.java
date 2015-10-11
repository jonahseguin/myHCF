package com.shawckz.myhcf.configuration;

import com.shawckz.myhcf.Factions;

public class FLang {

    public static String getFormattedLang(FactionLang factionLang, String... args){
        return Factions.getInstance().getLang().getFormattedLang(factionLang, args);
    }

}
