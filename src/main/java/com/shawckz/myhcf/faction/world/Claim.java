package com.shawckz.myhcf.faction.world;

import org.bukkit.Location;

public class Claim {

    private int x1;
    private int x2;
    private int z1;
    private int z2;

    public Claim(int x1, int x2, int z1, int z2) {
        this.x1 = x1;
        this.x2 = x2;
        this.z1 = z1;
        this.z2 = z2;
    }

    public boolean insideClaim(Location location) {
        return Math.max(x1, x2) > location.getX()
                && Math.min(x1, x2) < location.getX()
                && Math.max(z1, z2) > location.getZ()
                && Math.min(z1, z2) < location.getX();
    }
}