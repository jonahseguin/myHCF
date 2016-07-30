package com.shawckz.myhcf.listener;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.player.HCFPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.concurrent.TimeUnit;

/**
 * Created by jonahseguin on 2016-07-28.
 */
public class DTRListener implements Listener {

    private double dtrDeath = Factions.getInstance().getFactionsConfig().getDtrDeathPenalty();
    private double minDTR = Factions.getInstance().getFactionsConfig().getDtrMinimum();
    private int dtrFreezeMinutes = Factions.getInstance().getFactionsConfig().getDtrFreezeMinutes();

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        HCFPlayer fp = Factions.getInstance().getCache().getHCFPlayer(p);

        if(fp.getFaction() != null) {
            double newDTR = Math.max(minDTR, (fp.getFaction().getDeathsUntilRaidable() - dtrDeath));
            fp.getFaction().setDeathsUntilRaidable(newDTR);
            fp.getFaction().setDtrFreezeFinish(System.currentTimeMillis() + (60 * 1000 * dtrFreezeMinutes));
            sendDTRDeath(p, fp.getFaction());
        }
    }

    private void sendDTRDeath(Player p, Faction f) {
        String dtr = (f.getDeathsUntilRaidable() > 0 ? ChatColor.YELLOW : ChatColor.RED) + "" + f.getDeathsUntilRaidable();
        String dtrStatus = "";
        boolean hasOne = false;
        {
            if (f.isRaidable()) {
                dtrStatus += ChatColor.RED + "Raidable";
                hasOne = true;
            }
            if (f.isDtrFrozen()) {
                if (hasOne) {
                    dtrStatus += ChatColor.GRAY + " & ";
                }
                dtrStatus += ChatColor.AQUA + "Frozen";
                hasOne = true;
            }
            if (!f.isDtrFrozen() && !f.isRaidable()) {
                if (hasOne) {
                    dtrStatus += ChatColor.GRAY + " & ";
                }
                dtrStatus += ChatColor.GREEN + "Normal";
                hasOne = true;
            }
            if (!f.isDtrFrozen() && f.getDeathsUntilRaidable() < f.getMaxDTR()) {
                if (hasOne) {
                    dtrStatus += ChatColor.GRAY + " & ";
                }
                dtrStatus += ChatColor.LIGHT_PURPLE + "Regenerating";
                hasOne = true;
            }
        }

        String dtrFreeze = "";

        if (f.getDtrFreezeFinish() > System.currentTimeMillis()) {
            long millis = f.getDtrFreezeFinish() - System.currentTimeMillis();
            dtrFreeze = String.format("%02d:%02d:%02d",
                    TimeUnit.SECONDS.toHours(millis),
                    TimeUnit.SECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(millis)),
                    TimeUnit.SECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(millis)));
        }

        f.sendMessage(FLang.format(FactionLang.FACTION_MEMBER_DEATH, p.getName(), dtr, dtrStatus, dtrFreeze));
    }

}
