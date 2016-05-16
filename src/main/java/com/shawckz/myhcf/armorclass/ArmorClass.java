/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.armorclass;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import com.shawckz.myhcf.scoreboard.hcf.HCFScoreboard;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimer;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimerFormat;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
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
        player.getScoreboard().getHCFLabel(FLabel.ARMOR_CLASS).setEndValue(name).show();
        FLang.send(player.getBukkitPlayer(), FactionLang.ARMOR_CLASS_EQUIP, name);
        if (!player.getScoreboard().hasTimer(FLabel.ENERGY)) {
            final double regen = Factions.getInstance().getFactionsConfig().getEnergyPerQuarterSecond();
            final double maxEnergy = Factions.getInstance().getFactionsConfig().getMaxEnergy();
            final HCFTimer hcfTimer = new HCFTimer(player.getScoreboard(),
                    player.getScoreboard().getKey(FLabel.ENERGY),
                    player.getScoreboard().scoreIndex++,
                    HCFScoreboard.getTimerPool(),
                    HCFTimerFormat.TENTH_OF_SECOND,
                    true);

            Bukkit.getLogger().info("Max Energy:" + maxEnergy);

           // player.getScoreboard().registerTimer(FLabel.ENERGY, hcfTimer);

           /* HCFScoreboard.getTimerPool().registerTimer(new HCFTimerTask(hcfTimer, HCFScoreboard.getTimerPool().getInterval()) {
                @Override
                public void run() {
                    hcfTimer.setTime(hcfTimer.getTime() + 0.1D);
                }

                @Override
                public boolean isFrozen() {
                    return super.isFrozen();
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    Bukkit.getLogger().info("TIMER CALL ONCOMPLETE");
                }

                @Override
                public boolean isComplete() {
                    return super.isComplete();
                }
            });*/

            player.getScoreboard().getTimer(FLabel.ENERGY).setTime(10);

        }
        else {
            player.getScoreboard().getTimer(FLabel.ENERGY).setTime(10);
        }
    }

    public final void unequipArmorClass(HCFPlayer player) {
        player.setArmorClassType(null);
        player.getScoreboard().getHCFLabel(FLabel.ARMOR_CLASS).setEndValue("None").hide();
        for (PotionEffect effect : effects) {
            for (PotionEffect pe : player.getBukkitPlayer().getActivePotionEffects()) {
                if (pe.getDuration() <= effect.getDuration()) {
                    player.getBukkitPlayer().removePotionEffect(pe.getType());
                }
            }
        }
        FLang.send(player.getBukkitPlayer(), FactionLang.ARMOR_CLASS_REMOVE, name);

        if (player.getScoreboard().hasTimer(FLabel.ENERGY)) {
            player.getScoreboard().getTimer(FLabel.ENERGY).setTime(0).pauseTimer().hide();
        }

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
