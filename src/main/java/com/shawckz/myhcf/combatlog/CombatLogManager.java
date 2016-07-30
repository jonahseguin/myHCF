/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.combatlog;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class CombatLogManager {

    private final Map<String, CombatLogger> loggers = new HashMap<>();

    public CombatLogManager() {
        List<Entity> toRemove = new ArrayList<>();
        for(World world : Bukkit.getWorlds()) {
            for(Entity e : world.getEntities()) {
                if(e instanceof HumanEntity) {
                   HumanEntity he = (HumanEntity) e;
                    if(he.getName().startsWith("Logger-")) {
                        toRemove.add(he);
                    }
                }
            }
        }
        toRemove.forEach(Entity::remove);
    }

    public CombatLogger spawnLogger(Player player) {
        CombatLogger combatLogger = new CombatLogger(player);
        combatLogger.spawn(player.getLocation());
        loggers.put(player.getName(), combatLogger);
        return combatLogger;
    }

    public void despawnLogger(CombatLogger logger) {
        if(logger.isSpawned()){
            logger.despawn();
        }
        if(loggers.containsKey(logger.getName())){
            loggers.remove(logger.getName());
        }
    }

    public CombatLogger getLogger(String name) {
        if(loggers.containsKey(name)) {
            return loggers.get(name);
        }
        return null;
    }

}
