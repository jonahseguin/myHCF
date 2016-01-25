package com.shawckz.myhcf.land;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.faction.Faction;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Created by 360 on 14/07/2015.
 */
public class LandBoard {

    private final Map<Claim, String> land = new HashMap<>();

    public boolean isProtected(Location loc) {
        Faction f = getFactionAt(loc);
        if (f != null) {
            if (!f.isRaidable() && f.isNormal()) {
                return true;
            }
            else if (!f.isNormal()) {
                return true;
            }
        }
        return false;
    }

    public Set<Claim> getClaims() {
        return land.keySet();
    }

    public Claim getClaim(Location loc) {
        for (Claim claim : land.keySet()) {
            if (claim.within(loc)) return claim;
        }
        return null;
    }

    public Set<Claim> getClaims(Faction faction) {
        Set<Claim> c = new HashSet<>();
        for (Claim claim : land.keySet()) {
            if (claim.getFactionID().equalsIgnoreCase(faction.getId())) {
                c.add(claim);
            }
        }
        return c;
    }

    /**
     * Warning: Non-async database query
     *
     * @param loc
     * @return
     */
    public Faction getFactionAt(Location loc) {
        for (Claim claim : land.keySet()) {
            if (claim.within(loc)) {
                Faction faction = Factions.getInstance().getFactionManager().getFactionById(claim.getFactionID());
                if (faction != null) {
                    return faction;
                }
            }
        }
        return null;
    }

    public void claim(Claim claim, Faction fac) {
        land.put(claim, fac.getId());
    }

    public boolean isClaimed(Location loc) {
        for (Claim claim : land.keySet()) {
            if (claim.within(loc)) {
                return true;
            }
        }
        return false;
    }

    public void unclaimAll(Faction fac) {
        Iterator<Claim> it = land.keySet().iterator();
        final Set<Claim> delete = new HashSet<>();
        while (it.hasNext()) {
            Claim claim = it.next();
            if (claim.getFactionID().equalsIgnoreCase(fac.getId())) {
                delete.add(claim);
            }
        }
        for (Claim claim : delete) {
            land.remove(claim);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Claim claim : delete) {
                    claim.delete();
                }
            }
        }.runTaskAsynchronously(Factions.getInstance());

    }

}
