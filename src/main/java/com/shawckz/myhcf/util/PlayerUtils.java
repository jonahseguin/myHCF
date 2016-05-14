/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.util;

import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by zack6849 on 7/27/2015.
 */
public class PlayerUtils {
    public static void killOffline(OfflinePlayer offlinePlayer) {
        Player player = getPlayer(offlinePlayer);
        EntityPlayer entityPlayer = toEntityPlayer(player);
        entityPlayer.setHealth(0.0F);
        entityPlayer.inventory = new PlayerInventory(entityPlayer);
        player.saveData();
    }

    public static Player getPlayer(OfflinePlayer offlinePlayer) {
        Player player = Bukkit.getPlayer(offlinePlayer.getUniqueId());
        if (player == null) { // Player probably isn't online ;(
            MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
            EntityPlayer entityPlayer = new EntityPlayer(
                    minecraftServer,
                    minecraftServer.getWorldServer(0),
                    constructProfile(offlinePlayer.getUniqueId(), offlinePlayer.getName()),
                    new PlayerInteractManager((World) minecraftServer.getWorldServer(0))
            );
            player = (Player) entityPlayer.getBukkitEntity();
        }

        if (player != null) {
            player.loadData();
        }
        return player;
    }

    public static EntityPlayer toEntityPlayer(Player player) {
        return (EntityPlayer) ((CraftPlayer) player).getHandle();
    }

    public static GameProfile constructProfile(UUID uuid, String name) {
        return new GameProfile(uuid, name);
    }
}
