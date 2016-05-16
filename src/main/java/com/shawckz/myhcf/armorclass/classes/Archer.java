/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.armorclass.classes;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.armorclass.ArmorClass;
import com.shawckz.myhcf.armorclass.ArmorClassType;
import com.shawckz.myhcf.armorclass.MagicItemType;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.configuration.FactionsConfig;
import com.shawckz.myhcf.nametag.HCFNametag;
import com.shawckz.myhcf.player.HCFPlayer;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArcherTag(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
            if (Factions.getInstance().getFactionsConfig().isArcherTag()) {
                Player p = (Player) e.getEntity();
                Arrow arrow = (Arrow) e.getDamager();
                if (arrow.getShooter() != null && arrow.getShooter() instanceof Player) {
                    Player d = (Player) arrow.getShooter();
                    HCFPlayer dp = Factions.getInstance().getCache().getHCFPlayer(d);
                    if (dp.getArmorClassType() != null && dp.getArmorClassType() == ArmorClassType.ARCHER) {
                        HCFPlayer pp = Factions.getInstance().getCache().getHCFPlayer(p);
                        pp.archerTag();
                        for(Entity nearby : p.getNearbyEntities(30, 30, 30)) {
                            if(nearby instanceof Player) {
                                Player n = (Player) nearby;
                                HCFNametag.update(n, p);
                            }
                        }
                        FLang.send(d, FactionLang.ARCHER_TAG, p.getName(), Factions.getInstance().getFactionsConfig().getArcherTagTime() + "");
                        FLang.send(p, FactionLang.ARCHER_TAG_TARGET, d.getName(), Factions.getInstance().getFactionsConfig().getArcherTagTime() + "");
                        e.setDamage(e.getDamage() * Factions.getInstance().getFactionsConfig().getArcherTagMultiplier());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onArcherTagMultiply(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            if (Factions.getInstance().getFactionsConfig().isArcherTag()) {
                Player p = (Player) e.getEntity();
                HCFPlayer pp = Factions.getInstance().getCache().getHCFPlayer(p);
                if(pp.isArcherTagged()) {
                    e.setDamage(e.getDamage() * Factions.getInstance().getFactionsConfig().getArcherTagMultiplier());
                }
            }
        }
    }

}
