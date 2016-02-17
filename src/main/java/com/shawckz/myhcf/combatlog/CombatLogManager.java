/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.combatlog;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class CombatLogManager {

    private final Map<String, CombatLogger> loggers = new HashMap<>();

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
        return loggers.get(name);
    }

}
