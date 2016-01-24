/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.faction;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.util.HCFException;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jonah Seguin on 1/23/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class FactionFetch extends BukkitRunnable {

    public enum Type {
        NAME, ID
    }

    private final String id;
    private final Set<FactionGetter> handlers = new HashSet<>();
    private final Type type;

    public FactionFetch(String id) {
        this.id = id;
        this.type = Type.NAME;
    }

    public FactionFetch(String id, FactionGetter getter) {
        this.id = id;
        this.type = Type.NAME;
        registerHandler(getter);
    }

    public FactionFetch(String id, Type type) {
        this.id = id;
        this.type = type;
    }

    public FactionFetch(String id, Type type, FactionGetter getter) {
        this.id = id;
        this.type = type;
        registerHandler(getter);
    }

    public void registerHandler(FactionGetter handler) {
        handlers.add(handler);
    }

    //dont call
    public void run() {
        final Faction faction = getFaction(id);
        for (FactionGetter handler : handlers) {
            handler.fetch(faction);
        }
        handlers.clear();
    }

    public void requestFetch() {
        runTaskAsynchronously(Factions.getInstance());
    }

    public Faction getFaction(String id) {
        if (type == Type.ID) {
            return Factions.getInstance().getFactionManager().getFactionFromDatabaseById(id);
        }
        else if (type == Type.NAME) {
            return Factions.getInstance().getFactionManager().getFactionFromDatabase(id);
        }
        else {
            throw new HCFException("Could not FactionFetch query: invalid Type '" + type.toString() + "'");
        }
    }

}
