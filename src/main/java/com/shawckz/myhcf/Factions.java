package com.shawckz.myhcf;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * myHCF
 * @author Shawckz & Ozcr
 * @version 0.0.0
 */
public class Factions extends JavaPlugin {

    private static Factions instance;

    @Override
    public void onEnable(){
        instance = this;
    }

    @Override
    public void onDisable(){
        instance = null;
    }

    public static Factions getInstance() {
        return instance;
    }
}
