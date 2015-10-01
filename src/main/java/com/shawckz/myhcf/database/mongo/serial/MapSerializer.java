package com.shawckz.myhcf.database.mongo.serial;

import com.mongodb.util.JSON;
import com.shawckz.myhcf.configuration.AbstractSerializer;

import java.util.HashMap;

public class MapSerializer extends AbstractSerializer<HashMap> {

    @Override
    public String toString(HashMap data) {
        return JSON.serialize(data);
    }

    @Override
    public HashMap fromString(Object data) {
        HashMap map = (HashMap) JSON.parse(((String) data));
        return map;
    }
}
