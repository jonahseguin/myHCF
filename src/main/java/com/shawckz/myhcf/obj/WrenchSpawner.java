/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.obj;

import lombok.Getter;
import org.apache.commons.lang.WordUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class WrenchSpawner {

    private final EntityType type;
    private final ItemStack item;
    private final ItemMeta itemMeta;

    public WrenchSpawner(EntityType type) {
        this.type = type;
        this.item = new ItemStack(Material.MOB_SPAWNER, 1);
        this.itemMeta = item.getItemMeta();

        this.itemMeta.setDisplayName(ChatColor.GOLD + WordUtils.capitalizeFully(type.toString().toLowerCase().replaceAll("_"," ")));
    }

    public static EntityType fromItem(ItemStack item) {
        if(item.hasItemMeta()){
            String s = item.getItemMeta().getDisplayName();
            if(s != null) {
                s = ChatColor.stripColor(s);
                s = s.toUpperCase().replaceAll(" ", "_");
                return EntityType.valueOf(s.toUpperCase());
            }
        }
        return null;
    }

}
