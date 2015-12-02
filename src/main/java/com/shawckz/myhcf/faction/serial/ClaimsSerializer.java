package com.shawckz.myhcf.faction.serial;

import com.mongodb.BasicDBList;
import com.mongodb.util.JSON;
import com.shawckz.myhcf.configuration.AbstractSerializer;
import com.shawckz.myhcf.land.Claim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 360 on 22/07/2015.
 */
public class ClaimsSerializer extends AbstractSerializer<List<Claim>> {

    @Override
    public String toString(List<Claim> data) {
        Set<String> s = new HashSet<>();
        for (Claim c : data) {
            s.add(c.toString());
        }
        return JSON.serialize(s);
    }

    @Override
    public List<Claim> fromString(Object data) {
        String d = (String) data;
        BasicDBList bdl = (BasicDBList) JSON.parse(d);
        List<String> s = new ArrayList<>();
        for (Object o : bdl) {
            if (o instanceof String) {
                s.add(((String) o));
            }
        }
        List<Claim> claims = new ArrayList<>();
        for (String c : s) {
            claims.add(Claim.fromString(c));
        }
        return claims;
    }
}
