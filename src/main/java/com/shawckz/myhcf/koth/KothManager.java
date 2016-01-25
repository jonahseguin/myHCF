/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.koth;

import com.mongodb.BasicDBObject;
import com.shawckz.myhcf.database.mongo.AutoMongo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class KothManager {

    private final Map<String, Koth> koths = new HashMap<>();

    public void loadKoths() {
        koths.clear();
        List<AutoMongo> m = Koth.select(new BasicDBObject(), Koth.class);
        for (AutoMongo mongo : m) {
            if (mongo instanceof Koth) {
                Koth koth = (Koth) mongo;
                koths.put(koth.getName(), koth);
            }
        }
    }

    public Koth getKoth(String name) {
        return koths.get(name);
    }

}
