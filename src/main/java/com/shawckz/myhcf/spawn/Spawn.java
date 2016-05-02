/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.spawn;

import com.mongodb.client.MongoCursor;
import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.database.search.SearchText;
import com.shawckz.myhcf.faction.FDataMode;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.faction.FactionType;
import com.shawckz.myhcf.faction.data.DBFaction;
import com.shawckz.myhcf.land.Claim;
import com.shawckz.myhcf.land.LandBoard;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import com.shawckz.myhcf.util.Relation;
import net.md_5.bungee.api.ChatColor;
import org.bson.Document;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Jonah Seguin on 1/25/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class Spawn implements Listener {

    private final WallRadius wallRadius;
    private final DynamicWall wall;
    private Location spawn;
    private Faction spawnFaction;
    private Claim claim = null;

    public Spawn(Factions instance) {
        boolean foundFac = false;

        if(Factions.getDataMode() == FDataMode.MONGO) {
            MongoCursor<Document> it = Factions.getInstance().getDatabaseManager().getDatabase().getCollection("myhcffactions").find(new Document("factionType", FactionType.SPAWN.toString())).iterator();

            while (it.hasNext()) {
                Document doc = it.next();
                DBFaction fac = new DBFaction();
                Factions.getInstance().getDbHandler().fromDocument(fac, doc);
                if (fac.getFactionType() == FactionType.SPAWN) {
                    this.spawnFaction = fac;
                    foundFac = true;
                    break;
                }
            }
        }
        else if (Factions.getDataMode() == FDataMode.JSON) {
            Factions.getInstance().getLogger().info("Trying to load Spawn Faction [JSON]");
            DBFaction fac = new DBFaction();
            if(Factions.getInstance().getDbHandler().fetch(fac, new SearchText("name", "Spawn"))) {
                foundFac = true;
                this.spawnFaction = fac;
                Factions.getInstance().getLogger().info("Loaded Spawn Faction");
            }
            else{
                Factions.getInstance().getLogger().info("Failed to fetch Spawn Faction [JSON]");
            }
        }

        if (!foundFac) {
            this.spawnFaction = Factions.getInstance().getFactionManager().createFaction("Spawn", FactionType.SPAWN);
            Factions.getInstance().getDbHandler().push(spawnFaction);
            Factions.getInstance().getLogger().info("Created Spawn Faction");
        }

        instance.getFactionManager().addToCache(spawnFaction);

        this.spawn = instance.getFactionsConfig().getSpawn();

        Location pos1 = spawn;
        Location pos2 = spawn;

        Claim claim;

        Set<Claim> claims = instance.getLandBoard().getClaims(spawnFaction);
        if (!claims.isEmpty()) {
            claim = claims.stream().findFirst().get();
            pos1 = claim.getMinimumPoint();
            pos2 = claim.getMaximumPoint();
            this.claim = claim;
        }

        this.wallRadius = new WallRadius(pos1, pos2);
        this.wall = new DynamicWall(wallRadius);
        instance.getfEventManager().add(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent e) {
        if (e.isCancelled()) return;
        if (e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) {
            return;
        }
        Player player = e.getPlayer();
        HCFPlayer hcfPlayer = Factions.getInstance().getCache().getHCFPlayer(player);
        LandBoard landBoard = Factions.getInstance().getLandBoard();
        boolean spawnTagged = hcfPlayer.getScoreboard().getTimer(FLabel.SPAWN_TAG).getTime() > 0.1;
        if (landBoard.isClaimed(e.getTo())) {
            Faction claim = landBoard.getFactionAt(e.getTo());
            if(claim != null) {
                if (claim.getFactionType() == FactionType.SPAWN) {
                    if (!spawnTagged) {
                        player.setFoodLevel(20);
                    }
                    else {
                        e.setTo(e.getFrom());
                    }
                }
            }
        }
        if (spawnTagged) {
            if (spawn != null && player.getLocation().distanceSquared(spawn) < 150) {
                wallRadius.send(
                        player,
                        new ItemStack(Factions.getInstance().getFactionsConfig().getSpawnTagWallMaterial(), 1, (byte)Factions.getInstance().getFactionsConfig().getSpawnTagWallMaterialData()),
                        wall.getNear(player, 30, 10)
                );
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        if(e.isCancelled()) return;
        Player p = e.getPlayer();
        Location loc = e.getBlock().getLocation();
        if(withinSpawn(loc)) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "You cannot build in spawn.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e) {
        if(e.isCancelled()) return;
        Player p = e.getPlayer();
        Location loc = e.getBlock().getLocation();
        if(withinSpawn(loc)) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "You cannot build in spawn.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent e){
        if(e.isCancelled()) return;
        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            Location loc = player.getLocation();
            if(withinSpawn(loc)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMoveMessages(PlayerMoveEvent e) {
        if (e.isCancelled()) return;
        if (e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) {
            return;
        }
        Player player = e.getPlayer();
        HCFPlayer hcfPlayer = Factions.getInstance().getCache().getHCFPlayer(player);
        LandBoard landBoard = Factions.getInstance().getLandBoard();

        if(landBoard.isClaimed(e.getTo()) && landBoard.isClaimed(e.getFrom())) {
            //Entering to, leaving from
            Faction to = landBoard.getFactionAt(e.getTo());
            Faction from = landBoard.getFactionAt(e.getFrom());
            if(to != null && from != null) {
                if(!to.getId().equalsIgnoreCase(from.getId())) {
                    FLang.send(player, FactionLang.LAND_MOVE_MESSAGE, coloredFactionName(hcfPlayer, to), coloredFactionName(hcfPlayer, from));
                }
            }

        }
        else if (landBoard.isClaimed(e.getTo()) && !landBoard.isClaimed(e.getFrom())) {
            //Entering to, leaving wild
            Faction to = landBoard.getFactionAt(e.getTo());
            FLang.send(player, FactionLang.LAND_MOVE_MESSAGE, coloredFactionName(hcfPlayer, to), ChatColor.DARK_GREEN  + "Wilderness");
        }
        else if (!landBoard.isClaimed(e.getTo()) && landBoard.isClaimed(e.getFrom())) {
            //Entering wild, leaving from
            Faction from = landBoard.getFactionAt(e.getFrom());
            FLang.send(player, FactionLang.LAND_MOVE_MESSAGE, ChatColor.DARK_GREEN  + "Wilderness", coloredFactionName(hcfPlayer, from));
        }

    }

    private String coloredFactionName(HCFPlayer p, Faction f) {
        if(f.isNormal()) {
            Relation relation = f.getRelationTo(p.getFaction());
            if (relation == Relation.ALLY) {
                return ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getFactionsConfig().getRelationAlly() + f.getDisplayName());
            }
            else if (relation == Relation.FACTION) {
                return ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getFactionsConfig().getRelationFaction() + f.getDisplayName());
            }
            else {
                return ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getFactionsConfig().getRelationNeutral() + f.getDisplayName());
            }
        }
        else{
            if(f.getFactionType() == FactionType.SPAWN) {
                return ChatColor.GREEN + f.getDisplayName();
            }
            else if (f.getFactionType() == FactionType.ROAD) {
                return ChatColor.AQUA + f.getDisplayName();
            }
            else if (f.getFactionType() == FactionType.SPECIAL) {
                return ChatColor.DARK_PURPLE + f.getDisplayName();
            }
            else if (f.getFactionType() == FactionType.KOTH) {
                return ChatColor.GOLD + f.getDisplayName();
            }
        }
        return ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getFactionsConfig().getRelationNeutral() + f.getDisplayName());
    }

    public boolean withinSpawn(Location loc) {
        if(claim != null) {
            return claim.within(loc);
        }
        return false;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public WallRadius getWallRadius() {
        return wallRadius;
    }

    public DynamicWall getWall() {
        return wall;
    }

    public Location getSpawn() {
        return spawn;
    }

    public Faction getSpawnFaction() {
        return spawnFaction;
    }
}
