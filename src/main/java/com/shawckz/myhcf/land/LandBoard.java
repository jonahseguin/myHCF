package com.shawckz.myhcf.land;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.util.HCFException;

import org.bukkit.Location;

import java.util.*;

/**
 * Created by 360 on 14/07/2015.
 */
public class LandBoard {

    private final Map<Claim, String> land = new HashMap<>();

    public void loadFromFaction(Faction f) {
        for (Claim c : f.getClaims()) {
            land.put(c, f.getId());
        }
    }

    public boolean isProtected(Location loc) {
        Faction f = getFactionAt(loc);
        if (f != null) {
            if (!f.isRaidable() && f.isNormal()) {
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

    public Faction getFactionAt(Location loc) {
        for (Claim claim : land.keySet()) {
            if (claim.within(loc)) {
                for (Faction fac : Factions.getInstance().getFactionManager().getFactionsMap().values()) {
                    if (fac.getClaims().contains(claim)) {
                        return fac;
                    }
                }
            }
        }
        return null;
    }

    public void claim(Claim claim, Faction fac) {
        if (fac.getClaims().toArray().length >= Factions.getInstance().getFactionsConfig().getMaxFactionClaims()) {
            throw new HCFException("Faction cannot have more than " + Factions.getInstance().getFactionsConfig().getMaxFactionClaims() + " claims");
        }
        land.put(claim, fac.getId());
        fac.getClaims().add(claim);
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
        while (it.hasNext()) {
            Claim claim = it.next();
            if (fac.getClaims().contains(claim)) {
                land.remove(claim);
            }
        }
        fac.getClaims().clear();
    }

}
