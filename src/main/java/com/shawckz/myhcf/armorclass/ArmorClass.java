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

import java.util.HashSet;
import java.util.Set;

import org.bukkit.potion.PotionEffect;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
@Getter
public abstract class ArmorClass {

    private final String name;
    private final ArmorClassType type;
    private final Set<PotionEffect> effects = new HashSet<>();
    private final Set<MagicItem> magicItems = new HashSet<>();

    public ArmorClass(String name, ArmorClassType type) {
        this.name = name;
        this.type = type;
    }

    public final void equipArmorClass(HCFPlayer player) {
        equip(player);
        player.setArmorClassType(type);
        player.getScoreboard().addLabel(HCFLabelID.ARMOR_CLASS, true).getAsEnd().setEndValue(name).update();

        FLang.send(player.getBukkitPlayer(), FactionLang.ARMOR_CLASS_EQUIP, name);

        player.getScoreboard().addLabel(HCFLabelID.ENERGY, true).getAsTimer().setTimerValue(10).update();
    }

    public final void unequipArmorClass(HCFPlayer player) {
        player.setArmorClassType(null);
        player.getScoreboard().removeLabel(HCFLabelID.ARMOR_CLASS).getAsEnd().setEndValue("None");
        for (PotionEffect effect : effects) {
            for (PotionEffect pe : player.getBukkitPlayer().getActivePotionEffects()) {
                if (pe.getDuration() <= effect.getDuration()) {
                    player.getBukkitPlayer().removePotionEffect(pe.getType());
                }
            }
        }
        FLang.send(player.getBukkitPlayer(), FactionLang.ARMOR_CLASS_REMOVE, name);

        player.getScoreboard().removeLabel(HCFLabelID.ENERGY).getAsTimer().setTimerValue(0);
    }

    public final void updateEffects(HCFPlayer player) {
        for (PotionEffect effect : effects) {
            if(player.getBukkitPlayer().hasPotionEffect(effect.getType())) {
                player.getBukkitPlayer().removePotionEffect(effect.getType());
            }
            effect.apply(player.getBukkitPlayer());
        }
    }

    public void registerEffect(PotionEffect effect) {
        effects.add(effect);
    }

    public void registerMagicItem(MagicItem magicItem) {
        magicItems.add(magicItem);
    }

    public abstract boolean isApplicable(HCFPlayer player);

    public abstract void equip(HCFPlayer player);

}
