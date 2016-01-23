package com.shawckz.myhcf.command;

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