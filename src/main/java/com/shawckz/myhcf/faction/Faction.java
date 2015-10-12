package com.shawckz.myhcf.faction;

import java.util.List;
import java.util.Set;

import com.shawckz.myhcf.land.Claim;
import com.shawckz.myhcf.util.Relation;
import org.bukkit.Location;

import com.shawckz.myhcf.player.HCFPlayer;

public interface Faction {

    void save();

    String getId();

    String getName();

    void setName(String name);

    HCFPlayer getLeader();

    void setLeader(HCFPlayer member);

    Set<HCFPlayer> getMembers();//Includes moderators, leader, etc.

    Set<HCFPlayer> getOnlineMembers();

    Location getHome();

    void setHome(Location home);

    double getDeathsUntilRaidable();

    void setDeathsUntilRaidable(double dtr);

    double getBalance();

    void setBalance(double balance);

    FactionType getFactionType();

    void setFactionType(FactionType type);

    long getDtrFreezeFinish();

    void setDtrFreezeFinish(long dtrFreezeFinish);

    Set<Faction> getAllies();

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

    List<Claim> getClaims();

    Relation getRelationTo(Faction faction);

}