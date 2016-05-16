/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.armorclass;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.armorclass.magicitems.archer.MagicSpeed;
import com.shawckz.myhcf.player.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class ArmorClassManager {

    private final Map<ArmorClassType, ArmorClass> armorClasses = new HashMap<>();
    private final Map<MagicItemType, MagicItem> magicItems = new HashMap<>();

    public ArmorClassManager(Factions instance) {
        registerMagicItem(new MagicSpeed());
        runTimer();
    }

    private void runTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(pl);
                    if (player.getArmorClassType() != null) {
                        ArmorClass armorClass = getArmorClass(player.getArmorClassType());
                        if (armorClass.isApplicable(player)) {
                            armorClass.updateEffects(player);
                        }
                        else {
                            armorClass.unequipArmorClass(player);
                        }
                    }
                    else {
                        for (ArmorClass armorClass : armorClasses.values()) {
                            if (armorClass.isApplicable(player)) {
                                armorClass.equipArmorClass(player);
                                break;
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Factions.getInstance(), 60L, 60L);
    }

    public ArmorClass getArmorClass(ArmorClassType type) {
        return armorClasses.get(type);
    }

    public MagicItem getMagicItem(MagicItemType type) {
        return magicItems.get(type);
    }

    public void registerArmorClass(ArmorClass armorClass) {
        armorClasses.put(armorClass.getType(), armorClass);
        if(armorClass instanceof Listener) {
            Bukkit.getPluginManager().registerEvents(((Listener)armorClass), Factions.getInstance());
        }
    }

    public void registerMagicItem(MagicItem magicItem) {
        magicItems.put(magicItem.getType(), magicItem);
    }


}
