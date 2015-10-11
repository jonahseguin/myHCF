package com.shawckz.myhcf.faction;

import java.util.HashMap;
import java.util.Map;

public class FactionManager {

    private final Map<String, Faction> factions = new HashMap<>();

    public Map<String, Faction> getFactionsMap() {
        return factions;
    }

    public Faction getFaction(String id){
        if(factions.containsKey(id)){
            return factions.get(id);
        }
        else{
            return getFactionFromDatabase(id);
        }
    }

    public Faction getFactionFromDatabase(String id){
        //TODO
        return null;
    }

}
