package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

public class FEventManager {

    private final Factions instance;
    private final Set<Listener> listeners = new HashSet<>();

    public FEventManager(Factions instance) {
        this.instance = instance;

        listeners.add(new EnderpearlListener());
        listeners.add(new BorderListener());
        listeners.add(new ChatListener());
        listeners.add(new PreventionListener());
        listeners.add(new PvPTimerListener());
        listeners.add(new FoundDiamondsListener());
        listeners.add(Factions.getInstance().getVisualMap());
    }

    public void register() {
        for (Listener listener : listeners) {
            instance.getServer().getPluginManager().registerEvents(listener, instance);
        }
    }

    public void unregister() {
        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }
        listeners.clear();
    }

}
