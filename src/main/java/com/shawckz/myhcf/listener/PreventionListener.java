package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.faction.FactionType;
import com.shawckz.myhcf.land.LandBoard;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.util.Relation;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PreventionListener implements Listener {

    private final LandBoard landBoard = Factions.getInstance().getLandBoard();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player d = (Player) e.getDamager();
            HCFPlayer player = Factions.getInstance().getCache().getHCFPlayer(p);
            HCFPlayer damager = Factions.getInstance().getCache().getHCFPlayer(d);

            if(player.getFaction() != null && damager.getFaction() != null) {
                if(player.getFaction().getId().equalsIgnoreCase(damager.getFaction().getId())) {
                    e.setCancelled(true);
                    FLang.send(d, FactionLang.FACTION_DAMAGE_MEMBER, p.getName());
                }
                else if (player.getFaction().getRelationTo(damager.getFaction()) == Relation.ALLY) {
                    e.setCancelled(true);
                    FLang.send(d, FactionLang.FACTION_DAMAGE_ALLY, p.getName(), player.getFaction().getDisplayName());
                }
            }

        }
    }

    @EventHandler
    public void onMineBlazeSpawner(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.MOB_SPAWNER) {
            CreatureSpawner spawner = (CreatureSpawner) e.getBlock().getState();
            if (spawner.getSpawnedType() == EntityType.BLAZE) {
                String world = e.getBlock().getLocation().getWorld().getName();
                List<String> disabled = Factions.getInstance().getFactionsConfig().getDisableBlazeSpawners();
                for (String s : disabled) {
                    if (world.equalsIgnoreCase(s)) {
                        FLang.send(e.getPlayer(), FactionLang.BLAZE_SPAWNER_DISABLED);
                        e.setCancelled(true);
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!Factions.getInstance().getFactionsConfig().isDisableEnderchests()) return;
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.ENDER_CHEST) {
                e.setCancelled(true);
                FLang.send(player, FactionLang.ENDER_CHEST_DISABLED);
            }
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent e) {
        if (landBoard.isProtected(e.getBlock().getLocation())) {
            Faction f = landBoard.getFactionAt(e.getBlock().getLocation());
            if (f.isNormal()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (!Factions.getInstance().getFactionsConfig().isDisableHungerInSpawn()) return;
        Location loc = e.getEntity().getLocation();
        if (landBoard.isClaimed(loc)) {
            Faction f = landBoard.getFactionAt(loc);
            if (f.getFactionType() == FactionType.SPAWN) {
                e.setFoodLevel(20);
            }
        }
    }

    @EventHandler
    public void onIgnite(BlockIgniteEvent e) {
        if (!Factions.getInstance().getFactionsConfig().isDisableFireSpread()) return;
        if (e.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        if (!Factions.getInstance().getFactionsConfig().isDisableBlockBurn()) return;
        Faction f = landBoard.getFactionAt(e.getBlock().getLocation());
        if (f == null) return;
        if (!f.isRaidable()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (Factions.getInstance().getFactionsConfig().isCancelExplosionBlockBreak()) {
            e.blockList().clear();
        }
    }

}
