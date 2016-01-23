package com.shawckz.myhcf.scoreboard.hcf;

import org.bukkit.ChatColor;

public enum FLabel {

    ENDER_PEARL("&cEnderPearl&7: &6", FLabelType.TIMER),
    SPAWN_TAG("&aSpawn Tag&7: &6", FLabelType.TIMER),
    ARMOR_CLASS("&9Armor Class&7: &6", FLabelType.LABEL),
    PVP_TIMER("&9PVP Timer: &6", FLabelType.TIMER);

    private final String key;
    private final FLabelType labelType;

    FLabel(String key, FLabelType labelType) {
        this.key = key;
        this.labelType = labelType;
    }

    public String getKey() {
        return ChatColor.translateAlternateColorCodes('&', key);
    }

    public FLabelType getLabelType() {
        return labelType;
    }
}
