package com.shawckz.myhcf;

import com.shawckz.myhcf.armorclass.ArmorClassManager;
import com.shawckz.myhcf.armorclass.classes.Archer;
import com.shawckz.myhcf.combatlog.CombatLogManager;
import com.shawckz.myhcf.command.factions.FCommandManager;
import com.shawckz.myhcf.command.normal.GCommandHandler;
import com.shawckz.myhcf.command.normal.commands.*;
import com.shawckz.myhcf.configuration.FactionsConfig;
import com.shawckz.myhcf.configuration.LanguageConfig;
import com.shawckz.myhcf.database.AutoDB;
import com.shawckz.myhcf.database.AutoDBer;
import com.shawckz.myhcf.database.DatabaseManager;
import com.shawckz.myhcf.deathban.DeathbanRank;
import com.shawckz.myhcf.deathban.DeathbanRankManager;
import com.shawckz.myhcf.dtr.DTRRegenerator;
import com.shawckz.myhcf.faction.FDataMode;
import com.shawckz.myhcf.faction.FactionManager;
import com.shawckz.myhcf.koth.KothManager;
import com.shawckz.myhcf.land.ClaimSelector;
import com.shawckz.myhcf.land.LandBoard;
import com.shawckz.myhcf.land.claiming.VisualMap;
import com.shawckz.myhcf.listener.FEventManager;
import com.shawckz.myhcf.nametag.HCFNametag;
import com.shawckz.myhcf.nametag.NametagManager;
import com.shawckz.myhcf.nametag.NametagPlayer;
import com.shawckz.myhcf.player.HCFCache;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import com.shawckz.myhcf.spawn.Spawn;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * myHCF
 *
 * @author Shawckz
 * @version 0.0.0
 */
public class Factions extends JavaPlugin {

    private static Factions instance;

    private DatabaseManager databaseManager;
    private AutoDBer dbHandler;
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
    private KothManager kothManager;
    private ClaimSelector claimSelector;
    private CombatLogManager combatLogManager;

    @Override
    public void onEnable() {
        instance = this;
        factionsConfig = new FactionsConfig(this);

        FLabel.ARMOR_CLASS.register();
        FLabel.ENDER_PEARL.register();
        FLabel.ENERGY.register();
        FLabel.PVP_TIMER.register();
        FLabel.SPAWN_TAG.register();

        lang = new LanguageConfig(this);
        databaseManager = new DatabaseManager(this);
        dbHandler = new AutoDBer(getDataMode());
        cache = new HCFCache(this);
        commandManager = new FCommandManager(this);
        factionManager = new FactionManager();
        landBoard = new LandBoard();
        landBoard.loadClaims();
        visualMap = new VisualMap(this);
        claimSelector = new ClaimSelector();
        fEventManager = new FEventManager(this);
        fEventManager.register();
        gCommandHandler = new GCommandHandler(this);

        gCommandHandler.registerCommands(new CmdPvPTimer());
        gCommandHandler.registerCommands(new CmdEconomy());
        gCommandHandler.registerCommands(new CmdSetSpawn());
        gCommandHandler.registerCommands(new CmdKoth());
        gCommandHandler.registerCommands(new CmdDeathban());

        kothManager = new KothManager(this);

        armorClassManager = new ArmorClassManager(this);
        armorClassManager.registerArmorClass(new Archer());
        spawn = new Spawn(this);
        combatLogManager = new CombatLogManager();

        HCFNametag.runUpdateTask();
        new DTRRegenerator().run();

        if(deathbanRankManager.getRank("default") == null) {
            deathbanRankManager.registerRank(new DeathbanRank("default", 7200));
        }
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (NametagManager.contains(player)) {
                NametagPlayer nametagPlayer = NametagManager.getPlayer(player);
                nametagPlayer.reset();
                NametagManager.remove(player);
            }
        }
        NametagManager.clear();

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

    public CombatLogManager getCombatLogManager() {
        return combatLogManager;
    }

    public ClaimSelector getClaimSelector() {
        return claimSelector;
    }

    public KothManager getKothManager() {
        return kothManager;
    }

    public AutoDB getDbHandler() {
        return dbHandler.getAutoDB();
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

    public static boolean isDebug() {
        return getInstance().getFactionsConfig().isDebug();
    }

    public static void log(String msg) {
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

    public static FDataMode getDataMode() {
        return getInstance().getFactionsConfig().getDataMode();
    }

}