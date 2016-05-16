package com.shawckz.myhcf.player;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.nametag.HCFNametag;
import com.shawckz.myhcf.nametag.NametagManager;
import com.shawckz.myhcf.player.cache.AbstractCache;
import com.shawckz.myhcf.player.cache.CachePlayer;
import com.shawckz.myhcf.scoreboard.hcf.HCFScoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HCFCache extends AbstractCache {

    public HCFCache(Plugin plugin) {
        super(plugin, HCFPlayer.class);
    }

    @Override
    public CachePlayer create(String name, String uuid) {
        return new HCFPlayer(name, uuid);
    }

    public HCFPlayer getHCFPlayer(Player player) {
        return getHCFPlayer(player.getName());
    }

    public HCFPlayer getHCFPlayer(String name) {
        CachePlayer cachePlayer = getBasePlayer(name);
        if (cachePlayer != null) {
            return (HCFPlayer) cachePlayer;
        }
        return null;
    }

    public HCFPlayer getHCFPlayerByUUID(String uuid) {
        CachePlayer cachePlayer = getBasePlayerByUUID(uuid);
        if (cachePlayer != null) {
            return (HCFPlayer) cachePlayer;
        }
        return null;
    }

    public void saveHCFPlayer(HCFPlayer player) {
        new BukkitRunnable(){
            @Override
            public void run() {
                Factions.getInstance().getDbHandler().push(player);
            }
        }.runTaskAsynchronously(Factions.getInstance());
    }

    @Override
    public void init(Player player, CachePlayer cachePlayer) {
        if (cachePlayer instanceof HCFPlayer) {
            HCFPlayer hcfPlayer = (HCFPlayer) cachePlayer;
            hcfPlayer.setBukkitPlayer(player);
            hcfPlayer.setScoreboard(new HCFScoreboard(player));
            hcfPlayer.getScoreboard().sendToPlayer(player);

            NametagManager.setup(player);
            HCFNametag.refresh(player);

            player.sendMessage(ChatColor.GRAY + "Loading your faction from the database...");
            if (hcfPlayer.getFactionId() != null) {
                if (!Factions.getInstance().getFactionManager().isInCacheById(hcfPlayer.getFactionId())) {
                    //Load from database
                    Factions.getInstance().getFactionManager().getFactionById(hcfPlayer.getFactionId(), faction -> {
                        if (faction != null) {
                            Factions.getInstance().getFactionManager().addToCache(faction);
                            player.sendMessage(ChatColor.GRAY + "Your faction '"+faction.getDisplayName()+"' was loaded.");
                        }
                        else {
                            player.sendMessage(ChatColor.GRAY + "Your faction was deleted.  Updated.");
                            //Their faction no longer exists
                            hcfPlayer.setFactionId(null);
                        }
                        player.sendMessage(ChatColor.GREEN + "Done.");
                    });
                }
                else{
                    Faction faction = Factions.getInstance().getFactionManager().getLocalFactionById(hcfPlayer.getFactionId());
                    player.sendMessage(ChatColor.GRAY + "Your faction '"+faction.getDisplayName()+"' was loaded.");
                }
            }
            else{
                player.sendMessage(ChatColor.GRAY + "You are not in a faction.");
                player.sendMessage(ChatColor.GREEN + "Done.");
            }
        }
    }
}