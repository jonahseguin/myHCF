/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;

import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

public class LimiterListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent e) {
        if(e.isCancelled()) return;
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(removeIllegalEnchants(p) > 0) {
                FLang.send(p, FactionLang.LIMITER_REMOVED_ENCHANTS);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamageEntity(EntityDamageByEntityEvent e) {
        if(e.isCancelled()) return;
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if(removeIllegalEnchants(p) > 0) {
                FLang.send(p, FactionLang.LIMITER_REMOVED_ENCHANTS);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnchant(EnchantItemEvent e) {
        if(e.isCancelled()) return;
        Iterator<Enchantment> it = e.getEnchantsToAdd().keySet().iterator();
        while(it.hasNext()) {
            Enchantment enchant = it.next();
            if (Factions.getInstance().getFactionsConfig().getEnchantLimit(enchant) < e.getEnchantsToAdd().get(enchant)) {
                e.getEnchantsToAdd().put(enchant, Factions.getInstance().getFactionsConfig().getEnchantLimit(enchant));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player)) return;
        final Player p = (Player) e.getWhoClicked();
        final Inventory inv = e.getInventory();
        if(inv.getType() == InventoryType.ANVIL) {
            if(e.getRawSlot() == e.getView().convertSlot(e.getRawSlot()) && e.getRawSlot() == 2) {
                e.setCurrentItem(getCleanItem(e.getCurrentItem()));
            }
        }
        else if (inv.getType() == InventoryType.MERCHANT) {
            for (final ItemStack item : e.getInventory()) {
                if (item != null) {
                    removeIllegalEnchants(item);
                }
            }
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if(e.getCaught() instanceof Item) {
            removeIllegalEnchants(((Item) e.getCaught()).getItemStack());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Iterator<ItemStack> it = e.getDrops().iterator();
        while(it.hasNext()) {
            removeIllegalEnchants(it.next());
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        removeIllegalEnchants(e.getItem().getItemStack());
    }

    public int removeIllegalEnchants(Player p) {
        int x = 0;
        for(ItemStack itemStack : p.getInventory().getArmorContents()) {
            x += removeIllegalEnchants(itemStack);
        }
        for(ItemStack itemStack : p.getInventory().getContents()) {
            x += removeIllegalEnchants(itemStack);
        }
        return x;
    }

    public ItemStack getCleanItem(ItemStack itemStack) {
        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            if (Factions.getInstance().getFactionsConfig().getEnchantLimit(enchantment) < itemStack.getEnchantmentLevel(enchantment)) {
                itemStack.removeEnchantment(enchantment);
            }
        }
        return itemStack;
    }

    public int removeIllegalEnchants(ItemStack itemStack) {
        int x = 0;
        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            if (Factions.getInstance().getFactionsConfig().getEnchantLimit(enchantment) < itemStack.getEnchantmentLevel(enchantment)) {
                x++;
                itemStack.removeEnchantment(enchantment);
            }
        }
        return x;
    }

    //Potions

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPotionConsume(PlayerItemConsumeEvent e) {
        if(e.isCancelled()) return;
        if(e.getItem().getType() == Material.POTION) {
            Potion potion = Potion.fromItemStack(e.getItem());
            if(potion.getLevel() > Factions.getInstance().getFactionsConfig().getPotionLimit(potion.getType())) {
                e.setCancelled(true);
                potion.setLevel(Factions.getInstance().getFactionsConfig().getPotionLimit(potion.getType()));
                e.getPlayer().setItemInHand(potion.toItemStack(e.getItem().getAmount()));
                FLang.send(e.getPlayer(), FactionLang.LIMITER_POTION);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPotionSplash(PotionSplashEvent e) {
        if(e.isCancelled()) return;
        for(LivingEntity le : e.getAffectedEntities()) {
            if(le instanceof Player && Factions.getInstance().getSpawn().withinSpawn(le.getLocation())) {
                e.setIntensity(le, 0);
            }
        }
        Iterator<PotionEffect> it = e.getPotion().getEffects().iterator();
        while(it.hasNext()) {
            PotionEffect pe = it.next();
            PotionType type = PotionType.getByEffect(pe.getType());
            if(pe.getAmplifier() > Factions.getInstance().getFactionsConfig().getPotionLimit(type)) {
                e.getPotion().getEffects().remove(pe);
            }
        }
    }


}
