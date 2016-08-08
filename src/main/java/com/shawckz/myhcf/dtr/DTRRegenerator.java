package com.shawckz.myhcf.dtr;

import com.shawckz.myhcf.Factions;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by jonahseguin on 2016-07-28.
 */
public class DTRRegenerator {

    private double dtrRegen = Factions.getInstance().getFactionsConfig().getDtrRegen45s();

    private static boolean running = false;

    public void run() {
        if(!running) {
            running = true;

            new BukkitRunnable(){
                @Override
                public void run() {
                    Factions.getInstance().getFactionManager().getFactions()
                            .stream()
                            .filter(faction -> !faction.isDtrFrozen()
                                    && faction.getDeathsUntilRaidable() < faction.getMaxDTR())
                            .forEach(faction -> faction.setDeathsUntilRaidable(
                                        Math.min(faction.getDeathsUntilRaidable() + dtrRegen, faction.getMaxDTR())
                                ));
                }
            }.runTaskTimerAsynchronously(Factions.getInstance(), (45  * 20), (45 * 20));
        }
    }

}
