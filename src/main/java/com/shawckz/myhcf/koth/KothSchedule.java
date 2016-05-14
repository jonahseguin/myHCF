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

    public KothSchedule() {
    }

    public void loadSchedule() {
        Set<AutoDBable> result = Factions.getInstance().getDbHandler().fetchAll(new ScheduledKoth());
        result.stream().filter(ret -> ret instanceof ScheduledKoth).forEach(ret -> {
            ScheduledKoth sk = (ScheduledKoth) ret;
            schedule.put(sk.getDate(), sk);
        });
    }

    private Map<Long, ScheduledKoth> schedule = new HashMap<>();

    public void scheduleKoth(long time, Koth koth) {
        ScheduledKoth scheduledKoth = new ScheduledKoth(koth.getName(), time);
        schedule.put(time, scheduledKoth);
        Factions.getInstance().getDbHandler().push(scheduledKoth);
    }

    public Map<Long, ScheduledKoth> getSchedule() {
        return schedule;
    }

    public ScheduledKoth getNextKoth() {
        ScheduledKoth current = null;
        for(ScheduledKoth k : schedule.values()) {
            if(current == null || k.getDate() < current.getDate()) {
                current = k;
            }
        }
        return current;
    }

}
