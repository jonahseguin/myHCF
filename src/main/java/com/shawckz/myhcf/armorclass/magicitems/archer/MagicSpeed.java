/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.armorclass.magicitems.archer;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.armorclass.MagicItem;
import com.shawckz.myhcf.armorclass.MagicItemType;
import com.shawckz.myhcf.configuration.FactionsConfig;
import com.shawckz.myhcf.player.HCFPlayer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class MagicSpeed extends MagicItem {

    private static final FactionsConfig config = Factions.getInstance().getFactionsConfig();

    private final PotionEffect potionEffect;

    public MagicSpeed() {
        super(config.getMagicItemSpeedName(), MagicItemType.SPEED, config.getMagicItemSpeedMaterial(), config.getMagicItemSpeedEnergy());
        this.potionEffect = new PotionEffect(PotionEffectType.SPEED,
                config.getMagicItemSpeedTime(),
                config.getMagicItemSpeedAmplifier());
    }

    @Override
    public void apply(HCFPlayer player) {
        potionEffect.apply(player.getBukkitPlayer());
    }

}
