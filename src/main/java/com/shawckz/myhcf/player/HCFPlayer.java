package com.shawckz.myhcf.player;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.armorclass.ArmorClassType;
import com.shawckz.myhcf.database.annotations.CollectionName;
import com.shawckz.myhcf.database.annotations.DBColumn;
import com.shawckz.myhcf.database.annotations.JSONDirectory;
import com.shawckz.myhcf.deathban.DeathbanRank;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.faction.FactionRole;
import com.shawckz.myhcf.player.cache.CachePlayer;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import com.shawckz.myhcf.scoreboard.hcf.HCFScoreboard;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimer;
import com.shawckz.myhcf.util.ChatMode;
import com.shawckz.myhcf.util.FSelection;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

@CollectionName(name = "myhcfplayers")
@JSONDirectory(name = "players")
@Getter
@Setter
@RequiredArgsConstructor
public class HCFPlayer extends CachePlayer {

    public HCFPlayer() {
        //Leave empty constructor so that AutoMongo can instantiate
    }

    @DBColumn(name = "name")//Don't make name an identifier as it could change and mess up the database query.
    @NonNull
    private String name;

    @DBColumn(name = "uniqueId", identifier = true)
    @NonNull
    private String uniqueId;

    @DBColumn(name = "factionId")
    private String factionId = null;

    @DBColumn
    private FactionRole factionRole = FactionRole.MEMBER;

    @DBColumn
    private int lives = 0;

    @DBColumn
    private long deathban = 0L;

    @DBColumn
    private String deathbanRank = "default";

    @DBColumn
    private double balance = 0.0D;

    @DBColumn
    private boolean combatLogged = false;

    private Player bukkitPlayer;
    private HCFScoreboard scoreboard;
    private ChatMode chatMode = ChatMode.PUBLIC;
    private ArmorClassType armorClassType = null;

    private FSelection selection = null;

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
        if(factionId != null) {
            Faction f = Factions.getInstance().getFactionManager().getFactionById(factionId);
            if(f != null) {
                if(!Factions.getInstance().getFactionManager().isInCacheById(factionId)) {
                    Factions.getInstance().getFactionManager().addToCache(f);
                }
                return f;
            }
        }
        return null;
    }

    public void setSpawnTag(double tag) {
        scoreboard.getTimer(FLabel.SPAWN_TAG).setTime(tag);
    }

    public double getSpawnTag() {
        return scoreboard.getTimer(FLabel.SPAWN_TAG).getTime();
    }

    public float getCooldown(long finish) {
        return Float.parseFloat(HCFTimer.DECIMAL_FORMAT.format((finish - System.currentTimeMillis()) / 1000.0));
    }

    public double getEnderpearlCooldown() {
        return getScoreboard().getTimer(FLabel.ENDER_PEARL).getTime();
    }

    public boolean inCombat() {
        return scoreboard.getTimer(FLabel.SPAWN_TAG).getTime() > 0.1;
    }

}