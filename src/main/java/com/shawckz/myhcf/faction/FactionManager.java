package com.shawckz.myhcf.faction;

import com.mongodb.BasicDBObject;
import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.database.mongo.AutoMongo;
import com.shawckz.myhcf.faction.data.MongoFaction;
import com.shawckz.myhcf.util.HCFException;

import java.util.*;

public class FactionManager {

    private final Map<String, Faction> factions = new HashMap<>();

    public Collection<Faction> getFactions() {
        return factions.values();
    }

    public void addToCache(Faction faction) {
        factions.put(faction.getName().toLowerCase(), faction);
    }

    public boolean isInCacheById(String id) {
        return factions.containsKey(id);
    }

    public Faction createFaction(String name, FactionType type) {
        if (Factions.DATA_MODE == FDataMode.MONGO) {
            String id = UUID.randomUUID().toString().toLowerCase(); //Maybe use an md5 of the name for the ID instead?
            return new MongoFaction(id, name.toLowerCase(), name, type);
        }
        else {
            throw new HCFException("Could not create a faction (unknown data mode)");
        }
    }

    public boolean factionExists(String name) {
        name = name.toLowerCase();
        if (factions.containsKey(name)) {
            return true;
        }
        else {
            if (getFactionFromDatabase(name) != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCache(String name) {
        for (Faction faction : factions.values()) {
            if (faction.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public Faction getLocalFaction(String name) {
        for (Faction faction : factions.values()) {
            if (faction.getName().equalsIgnoreCase(name)) {
                return faction;
            }
        }
        throw new HCFException("Could not find faction in cache by name '" + name + "'");
    }

    public Faction getLocalFactionById(String id) {//gets only from cache
        if (factions.containsKey(id)) {
            return factions.get(id);
        }
        throw new HCFException("Could not find faction in cache by id '" + id + "'");
    }

    public void getFactionById(String id, FactionGetter getter) {
        if (factions.containsKey(id)) {
            getter.fetch(factions.get(id));
        }
        else {
            new FactionFetch(id, FactionFetch.Type.ID, getter).requestFetch();
        }
    }

    public void getFaction(String name, FactionGetter getter) {
        if (isInCache(name)) {
            getter.fetch(getLocalFaction(name));
        }
        else {
            new FactionFetch(name, getter);
        }
    }

    public void getFactionLaterById(String id, FactionGetter handler) {
        new FactionFetch(id, FactionFetch.Type.ID, handler).requestFetch();
    }

    public Faction getFactionFromDatabase(String name) {
        if (Factions.DATA_MODE == FDataMode.MONGO) {
            List<AutoMongo> mongos = MongoFaction.select(new BasicDBObject("name", name), MongoFaction.class);
            for (AutoMongo mongo : mongos) {
                if (mongo instanceof MongoFaction) {
                    MongoFaction faction = (MongoFaction) mongo;
                    if (faction.getName().equalsIgnoreCase(name)) {
                        return faction;
                    }
                }
            }
        }
        else {
            throw new HCFException("Could not get faction from database by name '" + name + "' (unregistered FDataMode)");
        }
        return null;
    }

    public Faction getFactionFromDatabaseById(String id) {
        if (Factions.DATA_MODE == FDataMode.MONGO) {
            List<AutoMongo> mongos = MongoFaction.select(new BasicDBObject("_id", id), MongoFaction.class);
            for (AutoMongo mongo : mongos) {
                if (mongo instanceof MongoFaction) {
                    MongoFaction faction = (MongoFaction) mongo;
                    if (faction.getId().equalsIgnoreCase(id)) {
                        return faction;
                    }
                }
            }
        }
        else {
            throw new HCFException("Could not get faction from database by id '" + id + "' (unregistered FDataMode)");
        }
        return null;
    }

}
