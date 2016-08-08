/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.armorclass;

import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.myscoreboard.hcf.HCFLabelID;
import com.shawckz.myhcf.player.HCFPlayer;
import lombok.Getter;

import org.bukkit.Material;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@Getter
public abstract class MagicItem {

    private final String name;
    private final MagicItemType type;
    private final Material material;
    private final double energy;

    public MagicItem(String name, MagicItemType type, Material material, double energy) {
        this.name = name;
        this.type = type;
        this.material = material;
        this.energy = energy;
    }

    public final boolean applyMagicItem(HCFPlayer player) {
        if (applicable(player)) {
            apply(player);
            double time = player.getScoreboard().getLabel(HCFLabelID.ENERGY).getAsTimer().getTimerValue();
            player.getScoreboard().addLabel(HCFLabelID.ENERGY, true).getAsTimer().setTimerValue(Math.max(0, time - energy)).update();
            return true;
        }
        return false;
    }

    public abstract void apply(HCFPlayer player);

    public boolean applicable(HCFPlayer player) {
        boolean applicable = player.getScoreboard().getLabel(HCFLabelID.ENERGY).getAsTimer().getTimerValue() >= energy;
        if(!applicable) {
            FLang.send(player.getBukkitPlayer(), FactionLang.MAGIC_ITEM_NOT_ENOUGH_ENERGY, name, energy+"");
        }
        return applicable;
    }

}
