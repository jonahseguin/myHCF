/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.database;

import com.shawckz.myhcf.faction.FDataMode;
import com.shawckz.myhcf.util.HCFException;

public class AutoDBer {

    private FDataMode dataMode;
    private AutoDB autoDB;

    public AutoDBer(FDataMode dataMode) {
        this.dataMode = dataMode;
        if(dataMode == FDataMode.JSON) {
            throw new HCFException("JSON DataMode is not currently supported.  Please change your DataMode to MONGO");
            //this.autoDB = new AutoJSON();
        }
        else if (dataMode == FDataMode.MONGO) {
            this.autoDB = new AutoMongo();
        }
        else {
            throw new HCFException("Selected DataMode not supported");
        }
    }

    public FDataMode getDataMode() {
        return dataMode;
    }

    public AutoDB getAutoDB() {
        return autoDB;
    }
}
