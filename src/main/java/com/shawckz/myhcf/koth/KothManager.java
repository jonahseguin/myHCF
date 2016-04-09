/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.koth;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.database.AutoDBable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class KothManager {

    private final Map<String, Koth> koths = new HashMap<>();

    private final KothSchedule kothSchedule = new KothSchedule();

    public void loadKoths() {
        koths.clear();
        Set<AutoDBable> ret = Factions.getInstance().getDbHandler().fetchAll(new Koth());
        for(AutoDBable r : ret) {
            if(r instanceof Koth) {
                Koth koth = (Koth) r;
                koths.put(koth.getName(), koth);
            }
        }
        kothSchedule.loadSchedule();
    }

    public Koth getKoth(String name) {
        return koths.get(name);
    }

    public KothSchedule getSchedule() {
        return kothSchedule;
    }
}
