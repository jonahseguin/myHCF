package com.shawckz.myhcf.land;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.database.AutoDBable;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.spawn.DynamicWall;
import com.shawckz.myhcf.spawn.WallRadius;

import java.util.*;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by 360 on 14/07/2015.
 */
public class LandBoard {

    private final Map<Claim, String> land = new HashMap<>();


    public void loadClaims() {
        Set<AutoDBable> ret = Factions.getInstance().getDbHandler().fetchAll(new Claim());
        for(AutoDBable r : ret) {
            if(r instanceof Claim) {
                Claim c = (Claim) r;
                land.put(c, c.getFactionID().toLowerCase());
                c.setDynamicWall(new DynamicWall(new WallRadius(c.getWorld(), c.getMaxX(), c.getMinX(), c.getMaxY(), c.getMinY(), c.getMaxZ(), c.getMinZ())));
            }
        }
    }

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

    public Set<Claim> getClaimsInRadius(Location loc, int radius) {
        Set<Claim> claims = new HashSet<>();
        for (int x = loc.getBlockX() - radius; x < loc.getBlockX() + radius; x++) {
            for (int y = loc.getBlockY() - radius; y < loc.getBlockY() + radius; y++) {
                for (int z = loc.getBlockZ() - radius; z < loc.getBlockZ() + radius; z++) {
                    Location target = new Location(loc.getWorld(), x, y, z);
                    Claim c = getClaim(target);
                    if(c != null) {
                        claims.add(c);
                    }
                }
            }
        }
        return claims;
    }

    public void claim(Claim claim, Faction fac) {
        land.put(claim, fac.getId().toLowerCase());
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
                    Factions.getInstance().getDbHandler().delete(claim);
                }
            }
        }.runTaskAsynchronously(Factions.getInstance());

    }

    public int unclaim(Faction fac, String claimID) {
        int deleted = 0;
        Iterator<Claim> it = land.keySet().iterator();
        final Set<Claim> delete = new HashSet<>();
        while (it.hasNext()) {
            Claim claim = it.next();
            if (claim.getFactionID().equalsIgnoreCase(fac.getId())) {
                if (claim.getId().equalsIgnoreCase(claimID)) {
                    delete.add(claim);
                }
            }
        }
        for (Claim claim : delete) {
            land.remove(claim);
            deleted++;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Claim claim : delete) {
                    Factions.getInstance().getDbHandler().delete(claim);
                }
            }
        }.runTaskAsynchronously(Factions.getInstance());

        return deleted;
    }

}
