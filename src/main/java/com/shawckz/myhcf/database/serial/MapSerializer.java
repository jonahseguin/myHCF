package com.shawckz.myhcf.database.serial;

import java.util.HashMap;

import com.mongodb.util.JSON;
import com.shawckz.myhcf.configuration.AbstractSerializer;

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