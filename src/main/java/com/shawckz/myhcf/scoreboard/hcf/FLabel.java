package com.shawckz.myhcf.scoreboard.hcf;

import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimerFormat;

import org.bukkit.ChatColor;

public enum FLabel {

    ENDER_PEARL("&cEnderPearl&7: &6", FLabelType.TIMER, HCFTimerFormat.TENTH_OF_SECOND),
    SPAWN_TAG("&aSpawn Tag&7: &6", FLabelType.TIMER, HCFTimerFormat.TENTH_OF_SECOND),
    ARMOR_CLASS("&9Armor Class&7: &6", FLabelType.LABEL, HCFTimerFormat.TENTH_OF_SECOND),
    PVP_TIMER("&9PVP Timer: &6", FLabelType.TIMER, HCFTimerFormat.HH_MM_SS),
    ENERGY("&bEnergy: &6", FLabelType.TIMER, HCFTimerFormat.TENTH_OF_SECOND);

    private final String key;
    private final FLabelType labelType;
    private final HCFTimerFormat format;

    FLabel(String key, FLabelType labelType, HCFTimerFormat format) {
        this.key = key;
        this.labelType = labelType;
        this.format = format;
    }

    public HCFTimerFormat getFormat() {
        return format;
    }

    public String getKey() {
        return ChatColor.translateAlternateColorCodes('&', key);
    }

    public FLabelType getLabelType() {
        return labelType;
    }
}
