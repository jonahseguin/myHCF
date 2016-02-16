/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.koth;

import com.mongodb.client.MongoCursor;
import com.shawckz.myhcf.Factions;
import org.bson.Document;

import java.util.HashMap;
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
        MongoCursor<Document> it = Factions.getInstance().getDatabaseManager().getDatabase().getCollection("myhcfkoths").find().iterator();
        while(it.hasNext()){
            Document doc = it.next();
            Koth koth = new Koth();
            Factions.getInstance().getDbHandler().fromDocument(koth, doc);
            koths.put(koth.getName(), koth);
        }
    }

    public Koth getKoth(String name) {
        return koths.get(name);
    }

}
