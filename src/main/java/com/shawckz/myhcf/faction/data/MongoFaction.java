package com.shawckz.myhcf.faction.data;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.configuration.FLang;
import com.shawckz.myhcf.configuration.FactionLang;
import com.shawckz.myhcf.database.mongo.AutoMongo;
import com.shawckz.myhcf.database.mongo.annotations.CollectionName;
import com.shawckz.myhcf.database.mongo.annotations.DatabaseSerializer;
import com.shawckz.myhcf.database.mongo.annotations.MongoColumn;
import com.shawckz.myhcf.database.mongo.serial.LocationSerializer;
import com.shawckz.myhcf.faction.Faction;
import com.shawckz.myhcf.faction.FactionRole;
import com.shawckz.myhcf.faction.FactionType;
import com.shawckz.myhcf.faction.serial.FactionTypeSerializer;
import com.shawckz.myhcf.faction.serial.HCFPlayerIdSerializer;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.util.Relation;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@CollectionName(name = "myhcffactions")
@Getter
@Setter
@RequiredArgsConstructor
public class MongoFaction extends AutoMongo implements Faction {

    @NonNull
    @MongoColumn(name = "_id", identifier = true)
    private String id;

    @NonNull
    @MongoColumn
    private String name;

    @NonNull
    @MongoColumn
    private String displayName;

    @NonNull
    @MongoColumn
    @DatabaseSerializer(serializer = FactionTypeSerializer.class)
    private FactionType factionType;

    @MongoColumn
    private String description = "Default faction description";

    @MongoColumn
    @DatabaseSerializer(serializer = LocationSerializer.class)
    private Location home = null;
    @MongoColumn
    private double deathsUntilRaidable = Factions.getInstance().getFactionsConfig().getBaseDtr();

    @MongoColumn
    @DatabaseSerializer(serializer = HCFPlayerIdSerializer.class)
    private HCFPlayer leader;

    @MongoColumn
    private double balance = 0.0D;

    @MongoColumn
    private long dtrFreezeFinish = 0L;

    @MongoColumn
    private Set<String> allies = new HashSet<>();//(Set<Faction ID>)

    @MongoColumn
    private Set<String> members = new HashSet<>();//(Set<Player UUID>)

    @MongoColumn
    private Set<String> invitations = new HashSet<>();//(Set<Player UUID>)

    public MongoFaction() {
    } //Leave empty constructor so that AutoMongo can instantiate.

    @Override
    public Set<HCFPlayer> getMembers() {
        Set<HCFPlayer> m = new HashSet<>();
        for (String s : members) {
            m.add(Factions.getInstance().getCache().getHCFPlayerByUUID(s));
        }
        m.add(leader);
        return m;
    }

    @Override
    public void setAllies(Faction target, boolean ally) {
        if (ally) {
            if (!allies.contains(target.getId())) {
                allies.add(target.getId());
            }
        } else {
            if (allies.contains(target.getId())) {
                allies.remove(target.getId());
            }
        }
    }

    @Override
    public void addPlayer(HCFPlayer player) {
        if (player == null) {
            throw new IllegalArgumentException("Cannot add null player to faction");
        }
        if (invitations.contains(player.getUniqueId())) {
            invitations.remove(player.getUniqueId());
        }
        members.add(player.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                updateMemberCache();
            }
        }.runTaskAsynchronously(Factions.getInstance());
        sendMessage(FLang.format(FactionLang.FACTION_JOIN_LOCAL, player.getName()));
        if (player.getBukkitPlayer() != null) {
            player.getBukkitPlayer().sendMessage(FLang.format(FactionLang.FACTION_JOIN_PLAYER, this.getName()));
        }
    }

    @Override
    public void invitePlayer(HCFPlayer invitedBy, HCFPlayer player) {
        if (!invitations.contains(player.getUniqueId())) {
            invitations.add(player.getUniqueId());
        }
        sendMessage(FLang.format(FactionLang.FACTION_INVITE_LOCAL, invitedBy.getName(), player.getName()));
        if (player.getBukkitPlayer() != null) {
            player.getBukkitPlayer().sendMessage(FLang.format(FactionLang.FACTION_INVITE_PLAYER, invitedBy.getFaction().getName(), invitedBy.getName()));
        }
    }

    @Override
    public boolean isNormal() {
        return getFactionType() == FactionType.NORMAL;
    }

    @Override
    public boolean isCached(HCFPlayer player) {
        return Factions.getInstance().getCache().getPlayersMap().containsKey(player.getName());
    }

    @Override
    public boolean isDtrFrozen() {
        return dtrFreezeFinish > System.currentTimeMillis();
    }

    @Override
    public boolean hasMember(HCFPlayer player) {
        return members.contains(player.getUniqueId());
    }

    @Override
    public Set<HCFPlayer> getOnlineMembers() {
        Set<HCFPlayer> onlineMembers = new HashSet<>();
        for (String memberUUID : this.members) {
            Player p = Bukkit.getPlayer(UUID.fromString(memberUUID));
            if (p != null) {
                onlineMembers.add(Factions.getInstance().getCache().getHCFPlayer(p));
            }
        }
        return onlineMembers;
    }

    @Override
    public void setRole(HCFPlayer member, FactionRole role) {
        member.setFactionRole(role);
    }

    @Override
    public FactionRole getRole(HCFPlayer member) {
        return member.getFactionRole();
    }

    @Override
    public boolean isRaidable() {
        return deathsUntilRaidable < 0.0D;
    }

    @Override
    public void sendMessage(String message) {
        for (HCFPlayer player : getOnlineMembers()) {
            if (player.getBukkitPlayer() != null) {
                player.getBukkitPlayer().sendMessage(message);
            }
        }
    }

    public void updateMemberCache() {
        for (String s : members) {
            Factions.getInstance().getCache().getHCFPlayerByUUID(s);
        }
    }

    @Override
    public void save() {
        update();
    }

    @Override
    public Relation getRelationTo(Faction faction) {
        if (faction.getId().equals(this.getId())) {
            return Relation.FACTION;
        }
        else if (faction.getAllies().contains(this.getId()) && getAllies().contains(faction.getId())) {
            return Relation.ALLY;
        }
        return Relation.NEUTRAL;
    }

    @Override
    public void setLeader(HCFPlayer player) {
        this.leader = player;
        player.setFactionRole(FactionRole.LEADER);
    }

    @Override
    public double getMaxDTR() {
        double dtrPerPlayer = Factions.getInstance().getFactionsConfig().getDtrPerPlayer();
        double max = Factions.getInstance().getFactionsConfig().getMaxDtr();
        double maxDTR = dtrPerPlayer * getMembers().size();
        if (maxDTR > max) {
            maxDTR = max;
        }
        return maxDTR;
    }

}