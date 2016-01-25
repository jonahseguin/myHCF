/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.armorclass.classes;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.armorclass.ArmorClass;
import com.shawckz.myhcf.armorclass.ArmorClassType;
import com.shawckz.myhcf.armorclass.MagicItemType;
import com.shawckz.myhcf.configuration.FactionsConfig;
import com.shawckz.myhcf.player.HCFPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class Archer extends ArmorClass implements Listener {

    private static final FactionsConfig config = Factions.getInstance().getFactionsConfig();

    public Archer() {
        super("Archer", ArmorClassType.ARCHER);
        registerEffect(new PotionEffect(PotionEffectType.SPEED, config.getArcherSpeedTime(), config.getArcherSpeedAmplifier()));
        registerMagicItem(Factions.getInstance().getArmorClassManager().getMagicItem(MagicItemType.SPEED));
    }

    @Override
    public boolean isApplicable(HCFPlayer player) {
        Player p = player.getBukkitPlayer();
        if (p != null) {
            if (p.getInventory().getHelmet() != null &&
                    p.getInventory().getChestplate() != null &&
                    p.getInventory().getLeggings() != null &&
                    p.getInventory().getBoots() != null) {
                if (p.getInventory().getHelmet().getType() == Material.LEATHER_HELMET
                        && p.getInventory().getChestplate().getType() == Material.LEATHER_CHESTPLATE
                        && p.getInventory().getLeggings().getType() == Material.LEATHER_LEGGINGS
                        && p.getInventory().getBoots().getType() == Material.LEATHER_BOOTS) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void equip(HCFPlayer player) {

    }
}
