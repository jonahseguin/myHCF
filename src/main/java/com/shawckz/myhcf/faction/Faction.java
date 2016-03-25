package com.shawckz.myhcf.faction;

import com.shawckz.myhcf.database.AutoDBable;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.util.Relation;

import java.util.Set;

import org.bukkit.Location;

public interface Faction extends AutoDBable {

    void setId(String id);

    String getId();

    String getName();

    void setName(String name);

    String getDisplayName();

    void setDisplayName(String displayName);

    HCFPlayer getLeader();

    void setLeader(HCFPlayer member);

    Set<HCFPlayer> getMembers();//Includes moderators, leader, etc.

    Set<HCFPlayer> getOnlineMembers();

    Location getHome();

    void addPlayer(HCFPlayer player);

    void leavePlayer(HCFPlayer player);

    void kickPlayer(HCFPlayer player, HCFPlayer kickedBy);

    void setHome(Location home);

    void setDescription(String description);

    double getDeathsUntilRaidable();

    void setDeathsUntilRaidable(double dtr);

    double getBalance();

    void setBalance(double balance);

    FactionType getFactionType();

    void setFactionType(FactionType type);

    long getDtrFreezeFinish();

    void setDtrFreezeFinish(long dtrFreezeFinish);

    void setAllies(Faction target, boolean ally);

    Set<String> getInvitations();

    void invitePlayer(HCFPlayer invitedBy, HCFPlayer player);

    boolean isNormal();

    boolean isCached(HCFPlayer player);

    boolean isDtrFrozen();

    boolean hasMember(HCFPlayer player);

    void setRole(HCFPlayer member, FactionRole role);

    FactionRole getRole(HCFPlayer member);

    boolean isRaidable();

    void sendMessage(String message);

    Relation getRelationTo(Faction faction);

    Set<String> getAllies();

    String getDescription();

    double getMaxDTR();

    Location getRally();

    void setRally(Location rally);

}