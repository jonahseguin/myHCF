/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.faction.serial;

import com.shawckz.myhcf.configuration.AbstractSerializer;
import com.shawckz.myhcf.faction.FactionType;
import com.shawckz.myhcf.util.HCFException;

/**
 * Created by Jonah Seguin on 1/23/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class FactionTypeSerializer extends AbstractSerializer<FactionType> {

    @Override
    public String toString(FactionType data) {
        return data.toString();
    }

    @Override
    public FactionType fromString(Object data) {
        if (data instanceof String) {
            String s = (String) data;
            for (FactionType factionType : FactionType.values()) {
                if (factionType.toString().equalsIgnoreCase(s)) {
                    return factionType;
                }
            }
        }
        throw new HCFException("Unable to deserialize FactionType (data not string, or factiontype null)");
    }
}
