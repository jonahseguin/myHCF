package com.shawckz.myhcf;

import com.shawckz.myhcf.database.DatabaseManager;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * myHCF
 * @author Shawckz & Ozcr
 * @version 0.0.0
 */
public class Factions extends JavaPlugin {

    private static Factions instance;

    private DatabaseManager databaseManager;

    @Override
    public void onEnable(){
        instance = this;
        databaseManager = new DatabaseManager(this);
    }

    @Override
    public void onDisable(){
        databaseManager.shutdown();

        databaseManager = null;
        instance = null;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static Factions getInstance() {
        return instance;
    }
}
