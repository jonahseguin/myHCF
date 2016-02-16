/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.obj;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.util.HCFException;
import lombok.Getter;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class Wrench {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;
    private int uses;

    public Wrench(ItemStack itemStack, int uses) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
        this.uses = uses;
    }

    public static boolean isWrench(ItemStack item) {
        if(item.getType() == Factions.getInstance().getFactionsConfig().getWrenchMaterial()) {
            if(item.hasItemMeta() && item.getItemMeta().getDisplayName() != null) {
                return item.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getFactionsConfig().getWrenchName()));
            }
        }
        return false;
    }

    private ItemStack getWrench() {
        ItemStack is = new ItemStack(Factions.getInstance().getFactionsConfig().getWrenchMaterial(), 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getFactionsConfig().getWrenchName()));
        int uses = Factions.getInstance().getFactionsConfig().getWrenchUses();
        im.setLore(Arrays.asList(ChatColor.AQUA + "Uses: " + uses + " / " + uses));
        is.setItemMeta(im);
        return is;
    }

    public static Wrench fromItem(ItemStack item){
        if(isWrench(item)) {
            String lore = item.getItemMeta().getLore().stream().findFirst().get();
            if(lore != null) {
                lore = ChatColor.stripColor(lore);
                lore = lore.split("Uses: ")[1];
                lore = lore.split(" / ")[0];
                return new Wrench(item, Integer.parseInt(lore));
            }
        }
        return null;
    }

    public void updateUses(int uses){
        if(uses < 1) {
            throw new HCFException("Wrench uses must be set to 1 or more");
        }
        this.uses = uses;
        itemMeta.setLore(Arrays.asList(ChatColor.AQUA + "Uses: " + uses + " / " + uses));
        itemStack.setItemMeta(itemMeta);
    }

}
