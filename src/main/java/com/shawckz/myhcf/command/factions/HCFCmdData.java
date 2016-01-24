/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.command.factions;

import lombok.Data;

/**
 * Created by 360 on 21/07/2015.
 */
@Data
public class HCFCmdData {

    private final HCFCommand command;

    private final String name;

    private final String[] aliases;

    private final boolean playerOnly;

    private final String permission;

    private final String usage;

    private final String[] flags;

    private final boolean allowFlags;

    private final int minArgs;

    private final String description;

}