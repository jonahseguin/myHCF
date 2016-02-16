/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.util;

import com.shawckz.myhcf.configuration.AbstractSerializer;
import com.shawckz.myhcf.faction.FDataMode;

public class DataModeSerializer extends AbstractSerializer<FDataMode> {

    @Override
    public String toString(FDataMode data) {
        return data.toString();
    }

    @Override
    public FDataMode fromString(Object data) {
        return FDataMode.valueOf(((String)data).toUpperCase());
    }
}
