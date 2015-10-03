package com.shawckz.myhcf;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.shawckz.myhcf.command.CommandManager;
import com.shawckz.myhcf.database.DatabaseManager;
import com.shawckz.myhcf.pearl.PearlCooldown;
import com.shawckz.myhcf.player.HCFCache;

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
        Bukkit.getPluginManager().registerEvents(new PearlCooldown(), this); // Unsure how you want event registering done, this might need changed.
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