/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.event;

import com.shawckz.myhcf.faction.Faction;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class FactionLoadEvent extends Event {

    private final Faction faction;

    public FactionLoadEvent(Faction faction) {
        this.faction = faction;
    }

    public Faction getFaction() {
        return faction;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
