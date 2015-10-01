package com.shawckz.myhcf;

import com.shawckz.myhcf.command.CommandManager;
import com.shawckz.myhcf.database.DatabaseManager;
import com.shawckz.myhcf.player.HCFCache;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * myHCF
 * @author Shawckz & Ozcr
 * @version 0.0.0
 */
public class Factions extends JavaPlugin {

    private static Factions instance;

    private DatabaseManager databaseManager;
    private HCFCache cache;
    private CommandManager commandManager;

    @Override
    public void onEnable(){
        instance = this;
        databaseManager = new DatabaseManager(this);
        cache = new HCFCache(this);
        commandManager = new CommandManager(this);
    }

    @Override
    public void onDisable(){
        databaseManager.shutdown();
        HCFCache.clear();

        cache = null;
        databaseManager = null;
        instance = null;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public HCFCache getCache() {
        return cache;
    }

    public static Factions getInstance() {
        return instance;
    }
}
