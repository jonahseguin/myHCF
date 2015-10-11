package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;

public class BorderListener implements Listener {

    private int getBorder() {
        return Factions.getInstance().getFactionsConfig().getWorldBorder();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        int x = Math.abs(e.getBlock().getX());
        int z = Math.abs(e.getBlock().getZ());
        int border = getBorder();
        if (x > border || z > border) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(FLang.getFormattedLang(FactionLang.BORDER_BLOCK_DENY));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        int x = Math.abs(e.getBlock().getX());
        int z = Math.abs(e.getBlock().getZ());
        int border = getBorder();
        if (x > border || z > border) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(FLang.getFormattedLang(FactionLang.BORDER_BLOCK_DENY));
        }
    }

    @EventHandler
    public void onPlayerPortal(final PlayerPortalEvent event) {
        int border = getBorder();
        if (Math.abs(event.getTo().getBlockX()) > border
                || Math.abs(event.getTo().getBlockZ()) > border) {
            final Location newLocation = event.getTo().clone();
            while (Math.abs(newLocation.getX()) > border) {
                newLocation.setX(newLocation.getX() - ((newLocation.getX() > 0.0) ? 1 : -1));
            }
            while (Math.abs(newLocation.getZ()) > border) {
                newLocation.setZ(newLocation.getZ() - ((newLocation.getZ() > 0.0) ? 1 : -1));
            }
            event.setTo(newLocation);
            event.getPlayer().sendMessage(FLang.getFormattedLang(FactionLang.BORDER_PORTAL));
        }
    }

    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (!event.getTo().getWorld().equals(event.getFrom().getWorld())) {
            return;
        }
        if (event.getTo().distance(event.getFrom()) < 0.0
                || event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
            return;
        }
        int border = getBorder();
        if (Math.abs(event.getTo().getBlockX()) > border ||
                Math.abs(event.getTo().getBlockZ()) > border) {
            final Location newLocation = event.getTo().clone();
            while (Math.abs(newLocation.getX()) > border) {
                newLocation.setX(newLocation.getX() - ((newLocation.getX() > 0.0) ? 1 : -1));
            }
            while (Math.abs(newLocation.getZ()) > border) {
                newLocation.setZ(newLocation.getZ() - ((newLocation.getZ() > 0.0) ? 1 : -1));
            }
            event.setTo(newLocation);
            event.getPlayer().sendMessage(FLang.getFormattedLang(FactionLang.BORDER_TELEPORT));
        }
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Location from = event.getFrom();
        final Location to = event.getTo();
        int border = getBorder();
        if ((from.getX() != to.getX() || from.getZ() != to.getZ() || from.getY() != to.getY()) &&
                (Math.abs(event.getTo().getBlockX()) > border ||
                        Math.abs(event.getTo().getBlockZ()) > border)) {
            if (event.getPlayer().getVehicle() != null) {
                event.getPlayer().getVehicle().eject();
            }
            final Location newLocation = event.getTo().clone();
            while (Math.abs(newLocation.getX()) > border) {
                newLocation.setX(newLocation.getX() - ((newLocation.getX() > 0.0) ? 1 : -1));
            }
            while (Math.abs(newLocation.getZ()) > border) {
                newLocation.setZ(newLocation.getZ() - ((newLocation.getZ() > 0.0) ? 1 : -1));
            }
            event.setTo(newLocation);
            event.getPlayer().sendMessage(ChatColor.RED + "You have reached the world border.");
        }
    }

}
