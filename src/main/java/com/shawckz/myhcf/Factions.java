package com.shawckz.myhcf;

import com.shawckz.myhcf.configuration.FactionsConfig;
import com.shawckz.myhcf.configuration.LanguageConfig;
import com.shawckz.myhcf.deathban.DeathbanRank;
import com.shawckz.myhcf.deathban.DeathbanRankManager;
import com.shawckz.myhcf.faction.FactionManager;
import com.shawckz.myhcf.land.LandBoard;
import com.shawckz.myhcf.listener.FEventManager;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.shawckz.myhcf.command.CommandManager;
import com.shawckz.myhcf.database.DatabaseManager;
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
    private FEventManager fEventManager;
    private FactionsConfig factionsConfig;
    private LanguageConfig lang;
    private FactionManager factionManager;
    private DeathbanRankManager deathbanRankManager = new DeathbanRankManager();
    private LandBoard landBoard;

    @Override
    public void onEnable(){
        instance = this;
        lang = new LanguageConfig(this);
        factionsConfig = new FactionsConfig(this);
        databaseManager = new DatabaseManager(this);
        cache = new HCFCache(this);
        commandManager = new CommandManager(this);
        factionManager = new FactionManager();
        landBoard = new LandBoard();
        fEventManager = new FEventManager(this);
        fEventManager.register();
    }

    @Override
    public void onDisable(){
        databaseManager.shutdown();
        HCFCache.clear();
        fEventManager.unregister();
        factionsConfig.save();
        lang.save();

        fEventManager = null;
        cache = null;
        databaseManager = null;
        factionsConfig = null;
        lang = null;
        instance = null;
    }

    public LandBoard getLandBoard() {
        return landBoard;
    }

    public DeathbanRankManager getDeathbanRankManager() {
        return deathbanRankManager;
    }

    public FactionManager getFactionManager() {
        return factionManager;
    }

    public LanguageConfig getLang() {
        return lang;
    }

    public FEventManager getfEventManager() {
        return fEventManager;
    }

    public FactionsConfig getFactionsConfig() {
        return factionsConfig;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public HCFCache getCache() {
        return cache;
    }

    public static Factions getInstance() {
        return instance;
    }
}