/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.koth;

import com.shawckz.myhcf.database.AutoDBable;
import com.shawckz.myhcf.database.annotations.CollectionName;
import com.shawckz.myhcf.database.annotations.DBColumn;
import com.shawckz.myhcf.database.annotations.JSONDirectory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CollectionName(name = "myhcfscheduledkoths")
@JSONDirectory(name = "scheduledkoths")
public class ScheduledKoth implements AutoDBable {

    @DBColumn(identifier = true)
    private String uniqueId;

    @DBColumn
    private String koth;

    @DBColumn
    private long time;

}
