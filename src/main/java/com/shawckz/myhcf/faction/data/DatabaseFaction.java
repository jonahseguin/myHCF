package com.shawckz.myhcf.faction.data;

import com.shawckz.myhcf.database.mongo.AutoMongo;
import com.shawckz.myhcf.database.mongo.annotations.CollectionName;

@CollectionName(name = "myhcffactions")
public class DatabaseFaction extends AutoMongo{

    public DatabaseFaction() { } //Leave empty constructor so that AutoMongo can instantiate

}
