/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.combatlog;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.util.HCFException;
import lombok.Getter;
import net.techcable.npclib.NPC;
import net.techcable.npclib.NPCLib;
import net.techcable.npclib.NPCRegistry;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class CombatLogger {

    private final Player player;

    private final String name;
    private final String uuid;
    private final List<ItemStack> items = new ArrayList<>();
    private NPC npc = null;
    private boolean spawned = false;
    private final NPCRegistry registry = NPCLib.getNPCRegistry("myHCF", Factions.getInstance());
    private CombatLogTimer timer;

    public CombatLogger(Player player) {
        this.player = player;
        this.name = player.getName();
        this.uuid = player.getUniqueId().toString();
        for(ItemStack i : player.getInventory().getContents()) {
            items.add(i);
        }
        for(ItemStack i : player.getInventory().getArmorContents()) {
            items.add(i);
        }
        this.timer = new CombatLogTimer(30) {
            @Override
            public void complete() {
                if(spawned) {
                    despawn();
                }
            }
        };
        //todo listen for when the NPC dies so we can drop the items
    }

    public void spawn(Location loc){
        if(!spawned) {
            npc = registry.createNPC(EntityType.PLAYER, name);
            npc.spawn(loc);
            npc.setProtected(false);
            Player player = (Player) npc.getEntity();
            player.setNoDamageTicks(0);
            spawned = true;
            timer.setTime(30);
            timer.run();
        }
        else{
            throw new HCFException("Cannot spawn CombatLogger that is already spawned");
        }
    }

    public void despawn() {
        if(spawned) {
            npc.despawn();
            timer.cancel();
            spawned = false;
        }
        else{
            throw new HCFException("Cannot despawn CombatLogger that has not been spawned");
        }
    }

}
