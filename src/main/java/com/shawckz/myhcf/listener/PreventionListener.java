package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.faction.FactionType;
import com.shawckz.myhcf.land.LandBoard;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PreventionListener implements Listener {

    private final LandBoard landBoard = Factions.getInstance().getLandBoard();

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
