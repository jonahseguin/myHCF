package com.shawckz.myhcf.spawn;

import com.shawckz.myhcf.util.HCFException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WallRadius {

    private String world;

    private int maxX;
    private int minX;

    private int maxY;
    private int minY;

    private int maxZ;
    private int minZ;

    private final Set<Location> cache = new HashSet<>();
    private final Map<String, Set<Location>> playerCache = new HashMap<>();

    public WallRadius(String world, int maxX, int minX, int maxY, int minY, int maxZ, int minZ) {
        this.world = world;
        this.maxX = maxX;
        this.minX = minX;
        this.maxY = maxY;
        this.minY = minY;
        this.maxZ = maxZ;
        this.minZ = minZ;
    }

    public WallRadius(Location pos1, Location pos2) {
        this.world = pos1.getWorld().getName();
        if (!pos1.getWorld().getName().equals(pos2.getWorld().getName())) {
            throw new HCFException("WallRadius: pos1 & pos2 must be in some world");
        }
        this.maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        this.minX = Math.min(pos1.getBlockX(), pos2.getBlockX());

        this.maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        this.minY = Math.min(pos1.getBlockY(), pos2.getBlockY());

        this.maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        this.minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
    }

    public void updateCache() {
        cache.clear();
        int x = minX;
        int y;
        int z;
        for (z = minZ; z <= maxZ; z++) {
            for (y = minY; y <= maxY; y++) {
                cache.add(new Location(Bukkit.getWorld(world), x, y, z));
            }
        }
        x = maxX;
        for (z = minZ; z <= maxZ; z++) {
            for (y = minY; y <= maxY; y++) {
                cache.add(new Location(Bukkit.getWorld(world), x, y, z));
            }
        }
        z = minZ;
        for (x = minX; x <= maxX; x++) {
            for (y = minY; y <= maxY; y++) {
                cache.add(new Location(Bukkit.getWorld(world), x, y, z));
            }
        }
        z = maxZ;
        for (x = minX; x <= maxX; x++) {
            for (y = minY; y <= maxY; y++) {
                cache.add(new Location(Bukkit.getWorld(world), x, y, z));
            }
        }
    }

    public Set<Location> getCache() {
        return cache;
    }

    public void resetPlayerCache() {
        for (String p : playerCache.keySet()) {
            Player pl = Bukkit.getPlayer(p);
            if (pl != null) {
                Set<Location> locations = playerCache.get(p);
                for (Location loc : locations) {
                    resetBlock(pl, loc);
                }
            }
        }
        playerCache.clear();
    }

    public void send(Player player, ItemStack block, Set<Location> locations) {
        if (!playerCache.containsKey(player.getName())) {
            playerCache.put(player.getName(), new HashSet<>());
        }
        Set<Location> locs = playerCache.get(player.getName());
        if (locations != null) {
            for (Location l : locations) {
                locs.add(l);
                sendBlock(player, l, block);
            }
        }
        playerCache.put(player.getName(), locs);
    }

    public void remove(Player player, Set<Location> locations) {
        if (!playerCache.containsKey(player.getName())) {
            playerCache.put(player.getName(), new HashSet<>());
        }
        Set<Location> locs = playerCache.get(player.getName());
        if (locations != null) {
            for (Location l : locations) {
                if (locs.contains(l)) {
                    locs.remove(l);
                }
                resetBlock(player, l);
            }
        }
        playerCache.put(player.getName(), locs);
    }

    private void sendBlock(Player p, Location loc, ItemStack stack) {
        p.sendBlockChange(loc, stack.getType(), stack.getData().getData());
    }

    private void resetBlock(Player p, Location loc) {
        p.sendBlockChange(loc, loc.getBlock().getType(), loc.getBlock().getData());
    }

    public boolean hasBlock(Player player, Location loc) {
        if (playerCache.containsKey(player.getName())) {
            return playerCache.get(player.getName()).contains(loc);
        }
        return false;
    }

}
