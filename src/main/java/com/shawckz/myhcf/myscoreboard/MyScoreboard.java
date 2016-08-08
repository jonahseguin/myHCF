package com.shawckz.myhcf.myscoreboard;

import com.shawckz.myhcf.myscoreboard.label.MyLabel;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
/**
 * Created by jonahseguin on 2016-07-22.
 */
public class MyScoreboard {

    private Scoreboard scoreboard;
    private final Set<MyLabel> scores = new HashSet<>();
    private Objective o;

    public MyScoreboard(String title) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.o = scoreboard.registerNewObjective((title.length() > 16 ? title.substring(0, 16) : title), "dummy");
        this.o.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.o.setDisplayName(title);
    }

    public Set<MyLabel> getScores() {
        return scores;
    }

    public MyScoreboard addLabel(MyLabel label, boolean update) {
        scores.add(label);
        if(update) {
            label.update();
        }
        return this;
    }

    public MyLabel getLabel(int score) {
        for(MyLabel label : scores) {
            if(label.getScore() == score) {
                return label;
            }
        }
        return null;
    }

    public boolean hasLabel(MyLabel label) {
        return scores.contains(label);
    }

    public Objective getObjective() {
        return o;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void sendToPlayer(Player player) {
        player.setScoreboard(scoreboard);
    }

}
