package myscoreboard;

import com.google.common.base.Splitter;
import myscoreboard.label.MyLabel;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.Iterator;
import java.util.UUID;

/**
 * Created by jonahseguin on 2016-07-22.
 */
public class MyValue {

    private final MyScoreboard scoreboard;
    private final MyLabel label;
    private String name;
    private Team team;
    private Score score;
    private int value;
    private int count = 0;
    private String origName;


    public MyValue(MyScoreboard scoreboard, MyLabel label, String value) {
        this.scoreboard = scoreboard;
        this.label = label;
        this.origName = value;
    }

    public String getName() {
        return name;
    }

    public Team getTeam() {
        return team;
    }

    public Score getScore() {
        return score;
    }

    public int getValue() {
        return score != null ? (value = score.getScore()) : value;
    }

    public void setValue(int value) {
        if (!score.isScoreSet()) {
            score.setScore(-1);
        }

        score.setScore(value);
    }

    public void update(String newName) {
        int value = getValue();
        if (origName != null && newName.equals(origName)) {
            for (int i = 0; i < count; i++) {
                newName = ChatColor.RESET + newName;
            }
        } else if (newName.equals(name)) {
            return;
        }

        create(newName);
        setValue(value);
    }

    void remove() {
        if (score != null) {
            score.getScoreboard().resetScores(score.getEntry());
        }

        if (team != null) {
            team.unregister();
        }
    }

    private void create(String name) {
        this.name = name;
        remove();

        if (name.length() <= 16) {
            int value = getValue();
            score = scoreboard.getObjective().getScore(name);
            score.setScore(value);
            return;
        }

        team = scoreboard.getScoreboard().registerNewTeam(UUID.randomUUID().toString().substring(0, 16));
        Iterator<String> iterator = Splitter.fixedLength(16).split(name).iterator();
        if (name.length() > 16)
            team.setPrefix(iterator.next());
        String entry = iterator.next();
        score = scoreboard.getObjective().getScore(entry);
        if (name.length() > 32)
            team.setSuffix(iterator.next());

        team.addEntry(entry);
    }

}
