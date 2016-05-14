/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.scoreboard.hcf;

import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimerFormat;

import java.util.ArrayList;
import java.util.List;

public class FLabel {

    public static FLabel ENDER_PEARL = new FLabel("&cEnderPearl&7: &6",FLabelType.TIMER, HCFTimerFormat.TENTH_OF_SECOND);
    public static FLabel SPAWN_TAG = new FLabel("&aSpawn Tag&7: &6", FLabelType.TIMER, HCFTimerFormat.TENTH_OF_SECOND);
    public static FLabel ARMOR_CLASS = new FLabel("&9Armor Class&7: &6", FLabelType.LABEL, HCFTimerFormat.TENTH_OF_SECOND);
    public static FLabel PVP_TIMER = new FLabel("&9PVP Timer: &6", FLabelType.TIMER, HCFTimerFormat.HH_MM_SS);
    public static FLabel ENERGY = new FLabel("&bEnergy: &6", FLabelType.TIMER, HCFTimerFormat.TENTH_OF_SECOND);

    private final String key;
    private final FLabelType labelType;
    private final HCFTimerFormat format;
    private static final List<FLabel> values = new ArrayList<>();

    public FLabel(String key, FLabelType labelType, HCFTimerFormat format) {
        this.key = key;
        this.labelType = labelType;
        this.format = format;
    }

    public FLabel register() {
        values.add(this);
        return this;
    }

    public static List<FLabel> values() {
        return values;
    }

    public HCFTimerFormat getFormat() {
        return format;
    }

    public FLabelType getLabelType() {
        return labelType;
    }

    public String getKey() {
        return key;
    }
}
