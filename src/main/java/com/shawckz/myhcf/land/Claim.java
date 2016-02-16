package com.shawckz.myhcf.land;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.database.AutoDBable;
import com.shawckz.myhcf.database.annotations.CollectionName;
import com.shawckz.myhcf.database.annotations.DBColumn;
import com.shawckz.myhcf.util.HCFException;
import lombok.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by 360 on 21/07/2015.
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@CollectionName(name = "myhcfclaims")
public class Claim implements AutoDBable, Iterable<Coordinate> {

    @NonNull
    @DBColumn
    private String factionID;

    @NonNull
    @DBColumn
    private String world;
    @NonNull
    @DBColumn
    private int minX;
    @NonNull
    @DBColumn
    private int minY;
    @NonNull
    @DBColumn
    private int minZ;
    @NonNull
    @DBColumn
    private int maxX;
    @NonNull
    @DBColumn
    private int maxY;
    @NonNull
    @DBColumn
    private int maxZ;

    private ClaimType claimType = ClaimType.NORMAL;

    public Claim(final Location p1, final Location p2) {
        if (!p1.getWorld().getName().equals(p2.getWorld().getName())) {
            throw new HCFException("Claim location 1 must be in the same world as location 2");
        }
        this.world = p1.getWorld().getName();
        this.minX = Math.min(p1.getBlockX(), p2.getBlockX());
        this.maxX = Math.max(p1.getBlockX(), p2.getBlockX());
        this.minY = Math.min(p1.getBlockY(), p2.getBlockY());
        this.maxY = Math.max(p1.getBlockY(), p2.getBlockY());
        this.minZ = Math.min(p1.getBlockZ(), p2.getBlockZ());
        this.maxZ = Math.max(p1.getBlockZ(), p2.getBlockZ());
    }

    public boolean within(final Location loc) {
        final int x = loc.getBlockX();
        final int z = loc.getBlockZ();

        return (x <= maxX) && (x >= minX) && (z <= maxZ) && (z >= minZ);
    }

    public Set<Claim> touchingClaims() {
        Set<Claim> s = new HashSet<>();
        for (Coordinate c : expand(CuboidDirection.Horizontal, 1)) {
            Location loc = new Location(Bukkit.getWorld(world), c.getX(), 0, c.getZ());
            final Claim cl = Factions.getInstance().getLandBoard().getClaim(loc);
            if (cl != null) {
                s.add(cl);
            }
        }
        return s;
    }

    public Claim expand(final CuboidDirection dir, final int amount) {
        switch (dir) {
            case North: {
                return new Claim(factionID, world, minX - amount, minY, minZ, maxX, maxY, maxZ);
            }
            case South: {
                return new Claim(factionID, world, minX, minY, minZ, maxX + amount, maxY, maxZ);
            }
            case East: {
                return new Claim(factionID, world, minX, minY, minZ - amount, maxX, maxY, maxZ);
            }
            case West: {
                return new Claim(factionID, world, minX, minY, minZ, maxX, maxY, maxZ + amount);
            }
            case Down: {
                return new Claim(factionID, world, minX, minY - amount, minZ, maxX, maxY, maxZ);
            }
            case Up: {
                return new Claim(factionID, world, minX, minY, minZ, maxX, maxY + amount, maxZ);
            }
            default: {
                throw new IllegalArgumentException("Invalid direction " + dir);
            }
        }
    }

    public Claim outset(final CuboidDirection dir, final int amount) {
        Claim claim = null;
        switch (dir) {
            case Horizontal: {
                claim = this.expand(CuboidDirection.North, amount).expand(CuboidDirection.South, amount).expand(CuboidDirection.East, amount).expand(CuboidDirection.West, amount);
                break;
            }
            case Vertical: {
                claim = this.expand(CuboidDirection.Down, amount).expand(CuboidDirection.Up, amount);
                break;
            }
            case Both: {
                claim = this.outset(CuboidDirection.Horizontal, amount).outset(CuboidDirection.Vertical, amount);
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid direction " + dir);
            }
        }
        return claim;
    }

    public Location getMinimumPoint() {
        return new Location(Bukkit.getWorld(world), minX, minY, minZ);
    }

    public Location getMaximumPoint() {
        return new Location(Bukkit.getWorld(world), maxX, maxY, maxZ);
    }

    @Override
    public Iterator<Coordinate> iterator() {
        return new BorderIterator(this, minX, minZ, maxX, maxZ);
    }

    @Override
    public String toString() {
        return world + "," + minX + "," + minY + "," + minZ
                + "," + maxX + "," + maxY + "," + maxZ + "," + claimType.toString() + "," + factionID;
    }

    public static Claim fromString(String s) {
        String[] args = s.split(",");
        String world = args[0];
        int minX = Integer.parseInt(args[1]);
        int minY = Integer.parseInt(args[2]);
        int minZ = Integer.parseInt(args[3]);
        int maxX = Integer.parseInt(args[4]);
        int maxY = Integer.parseInt(args[5]);
        int maxZ = Integer.parseInt(args[6]);
        ClaimType claimType = ClaimType.valueOf(args[7].toUpperCase());
        String factionID = args[8];
        return new Claim(factionID, world, minX, minY, minZ, maxX, maxY, maxZ, claimType);
    }


    public enum CuboidDirection {
        North,
        East,
        South,
        West,
        Up,
        Down,
        Horizontal,
        Vertical,
        Both,
        Unknown;
    }

}
