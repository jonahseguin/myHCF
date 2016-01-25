package com.shawckz.myhcf.player;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.armorclass.ArmorClassType;
import com.shawckz.myhcf.database.mongo.annotations.CollectionName;
import com.shawckz.myhcf.database.mongo.annotations.MongoColumn;
import com.shawckz.myhcf.deathban.DeathbanRank;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.faction.FactionRole;
import com.shawckz.myhcf.player.cache.CachePlayer;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import com.shawckz.myhcf.scoreboard.hcf.HCFScoreboard;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimer;
import com.shawckz.myhcf.util.ChatMode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

@CollectionName(name = "myhcfplayers")
@Getter
@Setter
@RequiredArgsConstructor
public class HCFPlayer extends CachePlayer {

    public HCFPlayer() {
        //Leave empty constructor so that AutoMongo can instantiate
    }

    @MongoColumn(name = "name")//Don't make name an identifier as it could change and mess up the database query.
    @NonNull
    private String name;

    @MongoColumn(name = "uniqueId", identifier = true)
    @NonNull
    private String uniqueId;

    @MongoColumn(name = "factionId")
    private String factionId = null;

    @MongoColumn
    private FactionRole factionRole = FactionRole.MEMBER;

    @MongoColumn
    private int lives = 0;

    @MongoColumn
    private long deathban = 0L;

    @MongoColumn
    private String deathbanRank = "default";

    @MongoColumn
    private double balance = 0.0D;

    private Player bukkitPlayer;
    private HCFScoreboard scoreboard;
    private ChatMode chatMode = ChatMode.PUBLIC;
    private ArmorClassType armorClassType = null;

    public DeathbanRank getDeathbanRank() {
        DeathbanRank deathbanRank = Factions.getInstance().getDeathbanRankManager().getRank(this.deathbanRank);
        if (deathbanRank != null) {
            return deathbanRank;
        } else {
            return Factions.getInstance().getDeathbanRankManager().getRank("default");
        }
    }

    public boolean inFaction() {
        return getFaction() != null;
    }

    public Faction getFaction() {
        if (Factions.getInstance().getFactionManager().isInCacheById(factionId)) {
            return Factions.getInstance().getFactionManager().getLocalFactionById(factionId);
        }
        return null;
    }

    public float getCooldown(long finish) {
        return Float.parseFloat(HCFTimer.DECIMAL_FORMAT.format((finish - System.currentTimeMillis()) / 1000.0));
    }

    public double getEnderpearlCooldown() {
        return getScoreboard().getTimer(FLabel.ENDER_PEARL).getTime();
    }

}