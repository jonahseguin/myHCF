package myscoreboard;

import myscoreboard.label.MyLabel;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashSet;
import java.util.Set;
/**
 * Created by jonahseguin on 2016-07-22.
 */
public class MyScoreboard {

    private Scoreboard scoreboard;
    private final Set<MyLabel> scores = new HashSet<>();
    private Objective o;
    private Objective buffer;
    private Objective t = null;

    public MyScoreboard(String title) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.o = scoreboard.registerNewObjective("test", "dummy");
        this.buffer = scoreboard.registerNewObjective("buffer", "dummy");

        // Setting up the scoreboard display stuff
        this.o.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.o.setDisplayName(title);
    }

    private void swapBuffer() {
        buffer.setDisplaySlot(o.getDisplaySlot());
        buffer.setDisplayName(o.getDisplayName());
        t = o;
        o = buffer;
        buffer = t;
    }

    public void setScore(int score, String value) {
        buffer.getScore(value).setScore(score);
        swapBuffer();
        buffer.getScore(value).setScore(score);
    }

    public MyScoreboard addLabel(MyLabel label, boolean update) {
        scores.add(label);
        if(update) {
            updateLabel(label);
        }
        return this;
    }

    public MyScoreboard updateLabel(MyLabel label) {
        label.getLabelUpdater().callUpdate(label);
        return this;
    }

    public Objective getObjective() {
        return buffer;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
