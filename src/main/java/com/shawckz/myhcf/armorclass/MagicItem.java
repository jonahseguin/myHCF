/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.armorclass;

import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
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
            player.getScoreboard().getTimer(FLabel.ENERGY).setTime(player.getScoreboard().getTimer(FLabel.ENERGY).getTime() - energy);
            return true;
        }
        return false;
    }

    public abstract void apply(HCFPlayer player);

    public boolean applicable(HCFPlayer player) {
        return player.getScoreboard().getTimer(FLabel.ENERGY).getTime() >= energy;
    }

}
