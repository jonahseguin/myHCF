package com.shawckz.myhcf.faction;

import com.shawckz.myhcf.player.HCFPlayer;

import java.util.Set;

import org.bukkit.Location;

public interface Faction {

    String getId();

    String getName();

    void setName(String name);

    FactionMember getLeader();

    void setLeader(FactionMember member);

    Set<FactionMember> getMembers();//Includes moderators, leader, etc.

    FactionMember getMember(String name);

    FactionMember getMember(HCFPlayer player);

    Location getHome();

    void setHome(Location home);

    double getDeathsUntilRaidable();

    void setDeathsUntilRaidable(int dtr);

    double getBalance();

    void setBalance(double balance);

    FactionType getFactionType();

    void setFactionType(FactionType type);

    long getDtrFreezeFinish();

    void setDtrFreezeFinish(long dtrFreezeFinish);

    Set<Faction> getAllies();

    void setAllies(Faction target, boolean ally);

    Set<String> getInvitations();

    void invitePlayer(String name);

    boolean isNormal();

    boolean isCached(HCFPlayer player);

    boolean isDtrFrozen();

    boolean hasMember(HCFPlayer player);

    boolean hasMember(String name);

    void setRole(FactionMember member, FactionRole role);

    FactionRole getRole(FactionMember member);

    boolean isRaidable();

    void sendMessage(String message);

}
