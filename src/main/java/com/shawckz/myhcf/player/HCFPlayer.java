package com.shawckz.myhcf.player;

import com.shawckz.myhcf.database.mongo.annotations.CollectionName;
import com.shawckz.myhcf.database.mongo.annotations.MongoColumn;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.player.cache.CachePlayer;
import lombok.*;

@CollectionName(name = "myhcfplayers")
@Getter
@Setter
@RequiredArgsConstructor
public class HCFPlayer extends CachePlayer {

    public HCFPlayer() { } //Leave empty constructor so that AutoMongo can instantiate

    @MongoColumn(name = "name")//Don't make name an identifier as it could change and mess up the database query.
    @NonNull private String name;

    @MongoColumn(name = "uniqueId", identifier = true)
    @NonNull private String uniqueId;

    @MongoColumn(name = "factionId")
    private String factionId = null;

    @MongoColumn(name = "pearlCooldown")
    private long timeCanThrow = 0;

    public void setTimeCanThrow(long time) {
        timeCanThrow = time;
    }

    public long getTimeCanThrow() {
        return timeCanThrow;
    }

    public boolean inFaction(){
        return getFaction() != null;
    }

    public Faction getFaction(){
        //TODO: Check if a faction with id this#factionId != null, return if not null, else return null
        return null;
    }
}