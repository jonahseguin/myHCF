package com.shawckz.myhcf.configuration;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.annotations.ConfigData;
import com.shawckz.myhcf.configuration.annotations.ConfigSerializer;
import com.shawckz.myhcf.database.mongo.serial.MapSerializer;
import com.shawckz.myhcf.deathban.DeathbanRank;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import com.shawckz.myhcf.util.HCFException;
import lombok.Getter;
import lombok.Setter;
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
    }

    private void defaultValues(){
        for(FLabel label : FLabel.values()){
            if(!scoreboardKeys.containsKey(label.toString())){
                scoreboardKeys.put(label.toString(), label.getKey());
            }
        }
        if(deathbanTime.isEmpty()){
            deathbanTime.add("default:10800");
            deathbanTime.add("2hour:7200");
            deathbanTime.add("1hour:3600");
        }

        for(String dbt : deathbanTime){
            if(!dbt.contains(":")){
                throw new HCFException("Invalid DeathbanRank '"+dbt+"'");
            }
            String[] split = dbt.split(":");
            String name = split[0];
            int seconds = Integer.parseInt(split[1]);
            Factions.getInstance().getDeathbanRankManager().registerRank(new DeathbanRank(name, seconds));
        }
    }

    //No final variables (if they are in the config)

    @ConfigData("factions.max-claims")
    private int maxFactionClaims = 5;

    @ConfigData("factions.dtr.base-dtr")
    private double baseDtr = 1.01;

    @ConfigData("factions.dtr.per-player-dtr")
    private double dtrPerPlayer = 0.51;

    @ConfigData("enderpearl-cooldown")
    private int enderpearlCooldown = 15;

    @ConfigData("scoreboard.title")
    private String scoreboardTitle = "&6HCF";

    @ConfigData("deathban.time")
    private List<String> deathbanTime = new ArrayList<>();

    @ConfigData("scoreboard.keys")
    @ConfigSerializer(serializer = MapSerializer.class)
    private Map<String, String> scoreboardKeys = new HashMap<>();

    public String getScoreboardKey(FLabel label){
        if(!scoreboardKeys.containsKey(label.toString())){
            scoreboardKeys.put(label.toString(),label.getKey());
        }
        return scoreboardKeys.get(label.toString());
    }

}
