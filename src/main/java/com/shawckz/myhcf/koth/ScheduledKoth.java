/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.koth;

import com.shawckz.myhcf.database.AutoDBable;
import com.shawckz.myhcf.database.annotations.CollectionName;
import com.shawckz.myhcf.database.annotations.DBColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@CollectionName(name = "myhcf_scheduledkoths")
public class ScheduledKoth implements AutoDBable {

    @DBColumn(identifier = true)
    private String name;
    @DBColumn
    private long date;

    public ScheduledKoth() {
    }



}
