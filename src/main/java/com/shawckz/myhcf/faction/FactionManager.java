package com.shawckz.myhcf.faction;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.database.search.SearchText;
import com.shawckz.myhcf.event.FactionLoadEvent;
import com.shawckz.myhcf.event.FactionUnloadEvent;
import com.shawckz.myhcf.faction.data.DBFaction;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.util.HCFException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FactionManager {

    private final Map<String, Faction> factions = new HashMap<>();

    public Collection<Faction> getFactions() {
        return factions.values();
    }

    public void addToCache(Faction faction) {
        FactionLoadEvent event = new FactionLoadEvent(faction);
        Bukkit.getServer().getPluginManager().callEvent(event);
        faction = event.getFaction();
        factions.put(faction.getName().toLowerCase(), faction);
    }

    public void removeFromCache(Faction faction) {
        if (factions.containsKey(faction.getName().toLowerCase())) {
            FactionUnloadEvent event = new FactionUnloadEvent(faction);
            Bukkit.getServer().getPluginManager().callEvent(event);
            factions.remove(faction.getName().toLowerCase());
        }
    }

    public boolean isInCacheById(String id) {
        return factions.containsKey(id);
    }

    public Faction createFaction(String name, FactionType type) {
        if (Factions.getDataMode() == FDataMode.MONGO) {
            String id = UUID.randomUUID().toString().toLowerCase(); //Maybe use an md5 of the name for the ID instead?
            return new DBFaction(id, name.toLowerCase(), name, type);
        }
        else if (Factions.getDataMode() == FDataMode.JSON) {
            String id = UUID.randomUUID().toString().toLowerCase();
            return new DBFaction(id, name.toLowerCase(), name, type);
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

    public Faction getFaction(String name) {
        if (isInCache(name)) {
            return getLocalFaction(name);
        }
        else {
            return getFactionFromDatabase(name);
        }
    }

    public Faction getFactionById(String id) {
        if (isInCacheById(id)) {
            return getLocalFactionById(id);
        }
        else {
            return getFactionFromDatabaseById(id);
        }
    }

    public void getFactionLaterById(String id, FactionGetter handler) {
        new FactionFetch(id, FactionFetch.Type.ID, handler).requestFetch();
    }

    public Faction getFactionFromDatabase(String name) {
        DBFaction dbFaction = new DBFaction();
        if(Factions.getInstance().getDbHandler().fetch(dbFaction, new SearchText("name", name))) {
            if (dbFaction.getName().equalsIgnoreCase(name)) {
                return dbFaction;
            }
        }
        return null;
    }

    public Faction getFactionFromDatabaseById(String id) {
        DBFaction dbFaction = new DBFaction();
        if(Factions.getInstance().getDbHandler().fetch(dbFaction, new SearchText("_id", id))) {
            if (dbFaction.getId().equalsIgnoreCase(id)) {
                return dbFaction;
            }
        }
        return null;
    }

    public void getFactionFromArg(CommandSender player, String target, FactionGetter getter) {
        //Priority 1 for search is faction name
        if (Factions.getInstance().getFactionManager().factionExists(target)) {
            Factions.getInstance().getFactionManager().getFaction(target, faction -> getter.fetch(faction));
        }
        else if (Bukkit.getPlayer(target) != null) {
            Player t = Bukkit.getPlayer(target);
            HCFPlayer thcf = Factions.getInstance().getCache().getHCFPlayer(t);
            if (thcf != null) {
                if(thcf.getFaction() != null) {
                    getter.fetch(thcf.getFaction());
                }
                else{
                    FLang.send(player, FactionLang.FACTION_NONE_OTHER, thcf.getName());
                }
            }
            else {
                player.sendMessage(ChatColor.RED + "There is no faction or player by that name.");
            }
        }
        else {
            new BukkitRunnable(){
                @Override
                public void run() {
                    HCFPlayer t = Factions.getInstance().getCache().getHCFPlayer(target);
                    if (t != null) {
                        if (t.getFaction() != null) {
                            getter.fetch(t.getFaction());
                        }
                        else {
                            FLang.send(player, FactionLang.PLAYER_NOT_IN_FACTION, target);
                        }
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "There is no faction or player by that name.");
                    }
                }
            }.runTaskAsynchronously(Factions.getInstance());
        }
    }

}
