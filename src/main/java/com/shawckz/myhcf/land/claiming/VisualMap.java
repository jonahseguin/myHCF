/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.land.claiming;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.land.Claim;
import com.shawckz.myhcf.land.LandBoard;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class VisualMap implements Listener {

    private final Map<Claim, VisualClaim> visualClaims = new HashMap<>();
    private final Map<String, Set<String>> playerClaims = new HashMap<>();
    private final Set<String> players = new HashSet<>();

    private final ConcurrentLinkedQueue<ClaimQueue> queue = new ConcurrentLinkedQueue<>();

    public VisualMap(Factions instance) {
        runQueue();
    }

    private void runQueue() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(queue.isEmpty()) return;
                for (int i = 0; i < 5; i++) {
                    ClaimQueue q = queue.peek();
                    if(q != null) {
                        if (queue.contains(q)) {
                            queue.remove(q);
                        }
                        if (q.getPlayer() != null && q.getPlayer().isOnline()) {
                            drawNearClaims(q.getPlayer(), q.getLocation());
                        }
                    }
                    else{
                        break;
                    }
                }
            }
        }.runTaskTimerAsynchronously(Factions.getInstance(), 3L, 3L);
    }

    public boolean inQueue(Player player) {
        for (ClaimQueue q : queue) {
            if (q.getPlayer().getName().equals(player.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isMapEnabled(Player player) {
        return players.contains(player.getName());
    }

    public void enableMap(Player player) {
        if (!players.contains(player.getName())) {
            players.add(player.getName());
        }
    }

    public void disableMap(Player player) {
        if (players.contains(player.getName())) {
            players.remove(player.getName());
        }

        if (playerClaims.containsKey(player.getName())) {
            playerClaims.get(player.getName()).stream().forEach(id -> {
                visualClaims.values().stream()
                        .filter(visualClaim -> id.equalsIgnoreCase(visualClaim.getId()))
                        .forEach(visualClaim -> visualClaim.hide(player));
            });
        }
    }


    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.isCancelled()) return;
        if (!players.contains(e.getPlayer().getName())) return;
        if (e.getTo().getBlockX() != e.getFrom().getBlockX() || e.getTo().getBlockZ() != e.getFrom().getBlockZ()) {
            //They actually moved
            if (!inQueue(e.getPlayer())) {
                queue.add(new ClaimQueue(e.getPlayer(), e.getTo()));
            }
        }
    }

    public VisualClaim convertClaim(Claim claim) {
        return new VisualClaim(claim.getMaximumPoint(), claim.getMinimumPoint());
    }

    public void drawNearClaims(Player player, Location location) {
        for (Claim claim : getNearClaims(location)) {
            drawClaim(player, claim);
        }
    }

    public Set<Claim> getNearClaims(Location location) {
        Set<Claim> near = new HashSet<>();
        LandBoard landBoard = Factions.getInstance().getLandBoard();
        for (Location loc : getNearBlocks(location, 50)) {
            Claim claim = landBoard.getClaim(loc);
            if (claim != null) {
                if (!near.contains(claim)) {
                    near.add(claim);
                }
            }
        }
        return near;
    }

    public static Set<Location> getNearBlocks(Location loc, int r) {
        Set<Location> locs = new HashSet<>();
        int X = loc.getBlockX();
        int Z = loc.getBlockZ();

        int x = (X - r);
        int z = (Z - r);

        int bx = x;

        for (int j = 0; j < r * 2 + 1; j++) {
            for (int k = 0; k < r * 2 + 1; k++) {
                locs.add(new Location(loc.getWorld(), x, loc.getBlockY(), z));
                x++;
            }
            z++;
            x = bx;
        }
        return locs;
    }

    public void drawClaim(Player player, Claim claim) {
        VisualClaim visualClaim = getOrConvertVisualClaim(claim);
        visualClaim.show(player);
        if (!playerClaims.containsKey(player.getName())) {
            playerClaims.put(player.getName(), new HashSet<>());
        }
        playerClaims.get(player.getName()).add(visualClaim.getId());
    }

    public VisualClaim getOrConvertVisualClaim(Claim claim) {
        if (visualClaims.containsKey(claim)) {
            return visualClaims.get(claim);
        }
        VisualClaim c = convertClaim(claim);
        visualClaims.put(claim, c);
        return c;
    }

}
