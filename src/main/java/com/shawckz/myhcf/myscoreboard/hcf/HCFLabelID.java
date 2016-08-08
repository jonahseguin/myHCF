/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.myscoreboard.hcf;

import net.md_5.bungee.api.ChatColor;

public enum HCFLabelID {

    SPACER_TOP(6, "&r ", Type.HEADER), // Done
    ARMOR_CLASS(5, "&6Armor Class&7: ", Type.NORMAL), // Done
    ENDER_PEARL(4, "&ePearl&7: ", Type.NORMAL), // Done
    PVP_TIMER(3, "&9PvP Timer&7: ", Type.NORMAL), // Done
    ENERGY(2, "&bEnergy&7: ", Type.NORMAL), // Done
    SPAWN_TAG(1, "&cSpawn Tag&7: ", Type.NORMAL); // Done

    public final int score;
    public final String key;
    public final Type type;

    HCFLabelID(int score, String key, Type type) {
        this.score = score;
        this.key = ChatColor.translateAlternateColorCodes('&', key);
        this.type = type;
    }

    public enum Type {
        HEADER,
        FOOTER,
        NORMAL
    }

}
