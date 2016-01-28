package com.shawckz.myhcf.configuration;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.annotations.ConfigData;
import com.shawckz.myhcf.configuration.annotations.ConfigSerializer;
import com.shawckz.myhcf.configuration.serial.MaterialSerializer;
import com.shawckz.myhcf.database.mongo.serial.LocationSerializer;
import com.shawckz.myhcf.database.mongo.serial.MapSerializer;
import com.shawckz.myhcf.deathban.DeathbanRank;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import com.shawckz.myhcf.util.HCFException;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
public class FactionsConfig extends Configuration {

    public FactionsConfig(Plugin plugin) {
        super(plugin);
        load();
        save();
        defaultValues();
        setupValues();
    }

    private void defaultValues() {
        for (FLabel label : FLabel.values()) {
            if (!scoreboardKeys.containsKey(label.toString())) {
                scoreboardKeys.put(label.toString(), label.getKey().replaceAll("§","&"));
            }
        }
        if (deathbanTime.isEmpty()) {
            deathbanTime.add("default:10800");
            deathbanTime.add("2hour:7200");
            deathbanTime.add("1hour:3600");
        }

        for (String dbt : deathbanTime) {
            if (!dbt.contains(":")) {
                throw new HCFException("Invalid DeathbanRank '" + dbt + "'");
            }
            String[] split = dbt.split(":");
            String name = split[0];
            int seconds = Integer.parseInt(split[1]);
            Factions.getInstance().getDeathbanRankManager().registerRank(new DeathbanRank(name, seconds));
        }
        save();
    }

    private void setupValues(){
        for(String key : scoreboardKeys.keySet()){
            scoreboardKeys.put(key, ChatColor.translateAlternateColorCodes('&', scoreboardKeys.get(key)));
        }
    }

    //No final variables (if they are in the config)

    @ConfigData("debug")
    private boolean debug = false;

    @ConfigData("factions.name.max-length")
    private int maxFactionNameLength = 14;

    @ConfigData("factions.name.alphanumeric")
    private boolean factionNameAlphanumeric = true;

    @ConfigData("factions.max-claims")
    private int maxFactionClaims = 5;

    @ConfigData("factions.max-players")
    private int maxFactionSize = 15;

    @ConfigData("factions.dtr.base-dtr")
    private double baseDtr = 1.01;

    @ConfigData("factions.dtr.per-player-dtr")
    private double dtrPerPlayer = 0.51;

    @ConfigData("factions.dtr.max-dtr")
    private double maxDtr = 5.51;

    @ConfigData("enderpearl-cooldown")
    private int enderpearlCooldown = 15;

    @ConfigData("scoreboard.title")
    private String scoreboardTitle = "&6HCF";

    @ConfigData("deathban.time")
    private List<String> deathbanTime = new ArrayList<>();

    @ConfigData("scoreboard.keys")
    @ConfigSerializer(serializer = MapSerializer.class)
    private Map<String, String> scoreboardKeys = new HashMap<>();

    @ConfigData("spawn.disable-hunger")
    private boolean disableHungerInSpawn = true;

    @ConfigData("world.disable-fire-spread")
    private boolean disableFireSpread = true;

    @ConfigData("world.disable-block-burn")
    private boolean disableBlockBurn = true;

    @ConfigData("world.cancel-explosion-block-break")
    private boolean cancelExplosionBlockBreak = true;

    @ConfigData("world.border")
    private int worldBorder = 3000;

    @ConfigData("relation.neutral")
    private String relationNeutral = "&f";

    @ConfigData("relation.ally")
    private String relationAlly = "&9";

    @ConfigData("relation.faction")
    private String relationFaction = "&a";

    @ConfigData("scoreboard.override")
    private boolean scoreboardOverride = true;

    @ConfigData("pvptimer.time.firstjoin")
    private int pvpTimerFirstJoin = 7200; //2 hours

    @ConfigData("pvptimer.time.respawn")
    private int pvpTimerRespawn = 3600; //1 hour

    @ConfigData("spawntag.tag-on-damaged")
    private boolean spawnTagOnDamaged = true;

    @ConfigData("spawntag.time.damaged")
    private int spawnTagTimeDamaged = 7;

    @ConfigData("spawntag.time.damager")
    private int spawnTagTimeDamager = 60;

    @ConfigData("magicitem.speed.name")
    private String magicItemSpeedName = "Speed";

    @ConfigData("magicitem.speed.time")
    private int magicItemSpeedTime = 120;

    @ConfigData("magicitem.speed.amplifier")
    private int magicItemSpeedAmplifier = 3;

    @ConfigData("magicitem.speed.material")
    @ConfigSerializer(serializer = MaterialSerializer.class)
    private Material magicItemSpeedMaterial = Material.SUGAR;

    @ConfigData("magicitem.speed.energy")
    private double magicItemSpeedEnergy = 10;

    @ConfigData("player.energy.maxenergy")
    private double maxEnergy = 50D;

    @ConfigData("armorclass.archer.speed.time")
    private int archerSpeedTime = 120;

    @ConfigData("armorclass.archer.speed.amplifier")
    private int archerSpeedAmplifier = 2;

    @ConfigData("armorclass.energy.regen-per-quarter-second")
    private double energyPerQuarterSecond = 0.7;

    @ConfigData("world.disable-enderchests")
    private boolean disableEnderchests = false;

    @ConfigData("world.disable-blaze-spawners")
    private List<String> disableBlazeSpawners = new ArrayList<>();

    @ConfigData("multiplier.exp")
    private int expMultiplier = 2;

    @ConfigData("spawn.spawn")
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location spawn = Bukkit.getWorld("world").getSpawnLocation();

    public String getScoreboardKey(FLabel label) {
        if (!scoreboardKeys.containsKey(label.toString())) {
            scoreboardKeys.put(label.toString(), label.getKey());
        }
        return scoreboardKeys.get(label.toString());
    }

}
