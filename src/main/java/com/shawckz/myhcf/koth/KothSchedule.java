/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.koth;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.database.AutoDBable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KothSchedule {

    private Map<String, ScheduledKoth> schedule = new HashMap<>();

    public void scheduleKoth(ScheduledKoth scheduledKoth) {
        schedule.put(scheduledKoth.getUniqueId(), scheduledKoth);
    }

    public void cancelKoth(ScheduledKoth scheduledKoth) {
        schedule.remove(scheduledKoth.getUniqueId());
    }

    public Map<String, ScheduledKoth> getSchedule() {
        return schedule;
    }

    public ScheduledKoth getScheduledKoth(String uniqueId) {
        return schedule.get(uniqueId);
    }

    public boolean hasScheduledKoth(String uniqueId) {
        return schedule.containsKey(uniqueId);
    }

    public void loadSchedule() {
        Set<AutoDBable> results = Factions.getInstance().getDbHandler().fetchAll(new ScheduledKoth());
        for(AutoDBable result : results){
            if(result instanceof ScheduledKoth) {
                ScheduledKoth scheduledKoth = (ScheduledKoth) result;
                schedule.put(scheduledKoth.getUniqueId(), scheduledKoth);
            }
        }
    }


}
