package com.shawckz.myhcf.player;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.armorclass.ArmorClassType;
import com.shawckz.myhcf.database.annotations.CollectionName;
import com.shawckz.myhcf.database.annotations.DBColumn;
import com.shawckz.myhcf.database.annotations.JSONDirectory;
import com.shawckz.myhcf.deathban.DeathbanRank;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.faction.FactionRole;
import com.shawckz.myhcf.myscoreboard.hcf.HCFLabelID;
import com.shawckz.myhcf.myscoreboard.hcf.HCFScoreboardWrapper;
import com.shawckz.myhcf.player.cache.CachePlayer;
import com.shawckz.myhcf.util.ChatMode;
import com.shawckz.myhcf.util.FSelection;
import com.shawckz.myhcf.util.Relation;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

@CollectionName(name = "myhcfplayers")
@JSONDirectory(name = "players")
@Getter
@Setter
@RequiredArgsConstructor
public class HCFPlayer extends CachePlayer {

    private static final DecimalFormat COOLDOWN_FORMAT = new DecimalFormat("#.#");

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
    private HCFScoreboardWrapper scoreboard;
    private ChatMode chatMode = ChatMode.PUBLIC;
    private ArmorClassType armorClassType = null;

    private boolean logKilled = false;

    private FSelection selection = null;

    private long archerTag = 0;

    public boolean isArcherTagged() {
        return archerTag > System.currentTimeMillis();
    }

    public void archerTag() {
        archerTag = (System.currentTimeMillis() + (Factions.getInstance().getFactionsConfig().getArcherTagTime() * 1000));
    }

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
        scoreboard.addLabel(HCFLabelID.SPAWN_TAG, true).getAsTimer().setTimerValue(tag).update();
    }

    public double getSpawnTag() {
        return scoreboard.getLabel(HCFLabelID.SPAWN_TAG).getAsTimer().getTimerValue();
    }

    public float getCooldown(long finish) {
        return Float.parseFloat(COOLDOWN_FORMAT.format((finish - System.currentTimeMillis()) / 1000.0));
    }

    public double getEnderpearlCooldown() {
        return scoreboard.getLabel(HCFLabelID.ENDER_PEARL).getAsTimer().getTimerValue();
    }

    public boolean inCombat() {
        return scoreboard.getLabel(HCFLabelID.SPAWN_TAG).getAsTimer().getTimerValue() > 0.0D;
    }

    public Relation getRelationTo(HCFPlayer player) {
        if(getFaction() != null && player.getFaction() != null) {
            return getFaction().getRelationTo(player.getFaction());
        }
        else{
            return Relation.NEUTRAL;
        }
    }

}