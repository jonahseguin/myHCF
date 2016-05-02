/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.land.claiming;

import java.util.*;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class VisualPillar {

    public static final Random RANDOM = new Random();

    public static final Material[] MATERIALS = new Material[]{
            Material.LOG, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK,
            Material.WOOD, Material.REDSTONE_BLOCK, Material.LAPIS_BLOCK,
            Material.COBBLESTONE, Material.MELON_BLOCK, Material.COAL_BLOCK,
            Material.EMERALD_BLOCK, Material.DIAMOND_ORE, Material.EMERALD_ORE,
            Material.REDSTONE_ORE, Material.GOLD_ORE, Material.COAL_ORE};

    public static final Material ALT_MATERIAL = Material.GLASS;

    private final Location location;
    private Map<String, Set<Location>> players = new HashMap<>();

    public VisualPillar(Location location) {
        this.location = location;
    }

    public static Material randomMaterial() {
        return MATERIALS[RANDOM.nextInt(MATERIALS.length)];
    }

    public void show(Player player){
        show(player, randomMaterial());
    }

    public void show(Player player, Material material) {
        if (!players.containsKey(player.getName())) {
            players.put(player.getName(), new HashSet<>());
        }

        int y = player.getLocation().getBlockY();
        int maxY = y + 50;
        int minY = y - 50;
        if (minY < 0) {
            minY = 0;
        }
        if (maxY > 256) {
            maxY = 256;
        }

        Set<Location> locations = players.get(player.getName());

        boolean glass = false;

        for (int i = minY; i < maxY; i++) {
            Location l = new Location(location.getWorld(), location.getBlockX(), i, location.getBlockZ());
            locations.add(l);
            if (glass) {
                sendBlockAlternate(player, l);
                glass = false;
            }
            else {
                sendBlock(player, l, material);
                glass = true;
            }
        }
    }

    public void hide(Player player) {
        if (players.containsKey(player.getName())) {
            for (Location loc : players.get(player.getName())) {
                player.sendBlockChange(loc, loc.getWorld().getBlockAt(loc).getType(), loc.getWorld().getBlockAt(loc).getData());
            }
        }
    }

    private void sendBlockAlternate(Player player, Location loc) {
        sendBlock(player, loc, ALT_MATERIAL);
    }

    private void sendBlock(Player player, Location loc, Material material) {
        player.sendBlockChange(loc, material, (byte) 0);
    }

}
