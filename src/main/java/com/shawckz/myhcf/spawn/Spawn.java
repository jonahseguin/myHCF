/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.spawn;

import com.mongodb.BasicDBObject;
import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.database.mongo.AutoMongo;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.faction.FactionType;
import com.shawckz.myhcf.faction.data.MongoFaction;
import com.shawckz.myhcf.land.Claim;
import com.shawckz.myhcf.land.LandBoard;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.scoreboard.hcf.FLabel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.Set;

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
        List<AutoMongo> mongos = MongoFaction.select(new BasicDBObject("factionType", FactionType.SPAWN.toString()), MongoFaction.class);
        for (AutoMongo mongo : mongos) {
            if (mongo instanceof MongoFaction) {
                MongoFaction mongoFaction = (MongoFaction) mongo;
                if (mongoFaction.getFactionType() == FactionType.SPAWN) {
                    this.spawnFaction = mongoFaction;
                    foundFac = true;
                    break;
                }
            }
        }
        if (!foundFac) {
            this.spawnFaction = Factions.getInstance().getFactionManager().createFaction("Spawn", FactionType.SPAWN);
            spawnFaction.save();
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
