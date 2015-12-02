package com.shawckz.myhcf.scoreboard.internal;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.scoreboard.internal.label.XLabel;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class XScoreboard {

    public static final String OBJECTIVE_TYPE = "dummy";
    public static final DisplaySlot DISPLAY_SLOT = DisplaySlot.SIDEBAR;

    protected final Scoreboard scoreboard;
    protected final Objective objective;
    protected final ConcurrentMap<Integer, XLabel> scores = new ConcurrentHashMap<>();

    public XScoreboard(String scoreboardTitle, Player player) {
        if (player.getScoreboard() != null && !Factions.getInstance().getFactionsConfig().isScoreboardOverride()) {
            this.scoreboard = player.getScoreboard();
        } else {
            this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }
        this.objective = this.scoreboard.registerNewObjective(getFilteredTitle(scoreboardTitle), OBJECTIVE_TYPE);
        this.objective.setDisplaySlot(DISPLAY_SLOT);
    }

    public final void addLabel(XLabel label) {
        scores.put(label.getScore(), label);
        label.updateLabel();
    }

    public final boolean hasLabel(XLabel label) {
        for (XLabel l : scores.values()) {
            if (l.equals(label)) {
                return true;
            }
        }
        return false;
    }

    public final void removeLabel(XLabel label) {
        label.getValue().removeValue();
        Iterator<Integer> it = scores.keySet().iterator();
        while (it.hasNext()) {
            int key = it.next();
            if (scores.get(key).getValue().equals(label.getValue())) {
                scores.remove(key);
            }
        }
    }

    public final XLabel getLabel(String value) {
        for (XLabel label : scores.values()) {
            if (label.getValue().getFullValue().equals(value)) {
                return label;
            }
        }
        return null;
    }

    public final XLabel getLabel(int score) {
        if (scores.containsKey(score)) {
            return scores.get(score);
        }
        return null;
    }

    public final Scoreboard getScoreboard() {
        return scoreboard;
    }

    public final Objective getObjective() {
        return objective;
    }

    private String getFilteredTitle(String title) {
        return (title.length() > 24 ? title.substring(0, 24) : title);
    }

    public final ConcurrentMap<Integer, XLabel> getScores() {
        return scores;
    }

    public final void sendToPlayer(Player player) {
        player.setScoreboard(scoreboard);
    }

}
