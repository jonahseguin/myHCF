/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.nametag;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.util.Relation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Jonah on 6/20/2015.
 */
public class HCFNametag {

    public static final Nametag NAMETAG_NEUTRAL = new Nametag("neutral", ChatColor.YELLOW + "", "");
    public static final Nametag NAMETAG_ALLY = new Nametag("ally", ChatColor.BLUE + "", "");
    public static final Nametag NAMETAG_TEAM = new Nametag("team", ChatColor.GREEN + "", "", true, true);
    public static final Nametag NAMETAG_ARCHER_TAG = new Nametag("tag", ChatColor.DARK_RED + "", "");

    public static void runUpdateTask() {
        new BukkitRunnable(){
            @Override
            public void run() {
                refresh();
            }
        }.runTaskTimer(Factions.getInstance(), 300L, 300L);
    }

    public static void refresh() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            refresh(p);
        }
    }

    public static void refresh(Player p) {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            update(p, pl);
            update(pl, p);
        }
    }

    public static void update(Player p, Player f) {
        HCFPlayer pp = Factions.getInstance().getCache().getHCFPlayer(p);
        HCFPlayer ff = Factions.getInstance().getCache().getHCFPlayer(f);
        Relation relation = pp.getRelationTo(ff);
        Nametag tag;

        if(ff.isArcherTagged()) {
            tag = NAMETAG_ARCHER_TAG;
        }
        else {

            if (relation == Relation.ALLY) {
                tag = NAMETAG_ALLY;
            }
            else if (relation == Relation.FACTION) {
                tag = NAMETAG_TEAM;
            }
            else {
                tag = NAMETAG_NEUTRAL;
            }
        }

        setNametag(p, f, tag);
    }

    private static void setNametag(Player p, Player f, Nametag tag) {
        if (NametagManager.contains(p)) {
            NametagPlayer np = NametagManager.getPlayer(p);
            if (NametagManager.contains(f)) {
                NametagPlayer nf = NametagManager.getPlayer(f);
                np.setPlayerNametag(nf, tag);
            }
        }
    }

}
