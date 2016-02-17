/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.spawn;

import com.mongodb.client.MongoCursor;
import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.database.search.SearchText;
import com.shawckz.myhcf.faction.FDataMode;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.faction.FactionType;
import com.shawckz.myhcf.faction.data.DBFaction;
import com.shawckz.myhcf.land.Claim;
import com.shawckz.myhcf.land.LandBoard;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import org.bson.Document;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Jonah Seguin on 1/25/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class Spawn implements Listener {

    private final WallRadius wallRadius;
    private final DynamicWall wall;
    private final Location spawn;
    private Faction spawnFaction;

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
            if (claim.getFactionType() == FactionType.SPAWN) {
                if (!spawnTagged) {
                    player.setFoodLevel(20);
                }
                else {
                    e.setTo(e.getFrom());
                }
            }
        }
        if (spawnTagged) {
            if (player.getLocation().distanceSquared(spawn) < 150) {
                wallRadius.send(player, Material.STAINED_GLASS, wall.getNear(player, 10, 10));
            }
        }


    }


}
