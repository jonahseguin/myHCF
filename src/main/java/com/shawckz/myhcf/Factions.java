package com.shawckz.myhcf;

import com.shawckz.myhcf.armorclass.ArmorClassManager;
import com.shawckz.myhcf.armorclass.classes.Archer;
import com.shawckz.myhcf.command.factions.FCommandManager;
import com.shawckz.myhcf.command.normal.GCommandHandler;
import com.shawckz.myhcf.command.normal.commands.CmdPvPTimer;
import com.shawckz.myhcf.configuration.FactionsConfig;
import com.shawckz.myhcf.configuration.LanguageConfig;
import com.shawckz.myhcf.database.DatabaseManager;
import com.shawckz.myhcf.deathban.DeathbanRankManager;
import com.shawckz.myhcf.faction.FDataMode;
import com.shawckz.myhcf.faction.FactionManager;
import com.shawckz.myhcf.land.LandBoard;
import com.shawckz.myhcf.land.claiming.VisualMap;
import com.shawckz.myhcf.listener.FEventManager;
import com.shawckz.myhcf.player.HCFCache;
import com.shawckz.myhcf.spawn.Spawn;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * myHCF
 *
 * @author Shawckz
 * @version 0.0.0
 */
public class Factions extends JavaPlugin {

    public static final FDataMode DATA_MODE = FDataMode.MONGO;// TODO: Change to a getter and make configurable

    private static Factions instance;

    private DatabaseManager databaseManager;
    private HCFCache cache;
    private FCommandManager commandManager;
    private FEventManager fEventManager;
    private FactionsConfig factionsConfig;
    private LanguageConfig lang;
    private FactionManager factionManager;
    private DeathbanRankManager deathbanRankManager = new DeathbanRankManager();
    private LandBoard landBoard;
    private GCommandHandler gCommandHandler;
    private ArmorClassManager armorClassManager;
    private VisualMap visualMap;
    private Spawn spawn;

    @Override
    public void onEnable() {
        instance = this;
        factionsConfig = new FactionsConfig(this);
        lang = new LanguageConfig(this);
        databaseManager = new DatabaseManager(this);
        cache = new HCFCache(this);
        commandManager = new FCommandManager(this);
        factionManager = new FactionManager();
        landBoard = new LandBoard();
        visualMap = new VisualMap(this);
        fEventManager = new FEventManager(this);
        fEventManager.register();
        gCommandHandler = new GCommandHandler(this);

        gCommandHandler.registerCommands(new CmdPvPTimer());

        armorClassManager = new ArmorClassManager(this);
        armorClassManager.registerArmorClass(new Archer());
        spawn = new Spawn(this);
    }

    @Override
    public void onDisable() {
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

    public GCommandHandler getGCommandHandler() {
        return gCommandHandler;
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

    public FCommandManager getCommandManager() {
        return commandManager;
    }

    public HCFCache getCache() {
        return cache;
    }

    public static Factions getInstance() {
        return instance;
    }

    public static boolean isDebug(){
        return getInstance().getFactionsConfig().isDebug();
    }

    public static void log(String msg){
        getInstance().getLogger().info(msg);
    }

    public ArmorClassManager getArmorClassManager() {
        return armorClassManager;
    }

    public VisualMap getVisualMap() {
        return visualMap;
    }

    public Spawn getSpawn() {
        return spawn;
    }
}