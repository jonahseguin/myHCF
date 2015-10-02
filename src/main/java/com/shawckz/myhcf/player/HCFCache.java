package com.shawckz.myhcf.player;

import com.shawckz.myhcf.player.cache.AbstractCache;
import com.shawckz.myhcf.player.cache.CachePlayer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HCFCache extends AbstractCache {

    public HCFCache(Plugin plugin) {
        super(plugin, HCFPlayer.class);
    }

    @Override
    public CachePlayer create(String name, String uuid) {
        return new HCFPlayer(name, uuid);
    }

    public HCFPlayer getHCFPlayer(Player player){
        return getHCFPlayer(player.getName());
    }

    public HCFPlayer getHCFPlayer(String name){
        CachePlayer cachePlayer = getBasePlayer(name);
        if(cachePlayer != null){
            return (HCFPlayer) cachePlayer;
        }
        return null;
    }

    @Override
    public void init(Player player, CachePlayer cachePlayer) {
        if(cachePlayer instanceof HCFPlayer){
            HCFPlayer hcfPlayer = (HCFPlayer) cachePlayer;
            //TODO: Setup.
        }
    }
}