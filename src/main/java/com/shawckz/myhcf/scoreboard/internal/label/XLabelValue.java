package com.shawckz.myhcf.scoreboard.internal.label;

import com.shawckz.myhcf.scoreboard.internal.XScoreboard;
import com.shawckz.myhcf.scoreboard.internal.util.FakeOfflinePlayer;
import lombok.Getter;

import java.util.UUID;

import org.bukkit.scoreboard.Team;


@Getter
public class XLabelValue {

    private final XScoreboard scoreboard;
    private final XLabel label;
    private Team team;
    private String preValue;
    private String value;
    private String postValue;
    private String fullValue;
    private boolean didSplit = false;
    private FakeOfflinePlayer fakeOfflinePlayer = null;
    private String initValue = null;

    public XLabelValue(XScoreboard scoreboard, XLabel label, String fullValue) {
        this.scoreboard = scoreboard;
        this.label = label;
        this.fullValue = fullValue;
        updateValues();
    }

    public void setValue(String fullValue, boolean update) {
        if (update) {
            tryCreateTeam();
            this.fullValue = fullValue;
            if (label.isVisible()) {
                updateValues();
                addValue();
            } else {
                removeValue();
                updateValues();
            }
        } else {
            this.fullValue = fullValue;
        }
    }

    public void update() {
        tryCreateTeam();
        if (label.isVisible()) {
            updateValues();
            addValue();
        } else {
            removeValue();
            updateValues();
        }
    }

    private Team tryCreateTeam() {
        if (team == null) {
            team = createTeam();
        }
        return team;
    }

    private Team createTeam() {
        return scoreboard.getScoreboard().registerNewTeam(UUID.randomUUID().toString().substring(0, 10));
    }

    private void addValue() {
        if (fakeOfflinePlayer == null) {
            if(!scoreboard.getScoreboard().getTeams().contains(team)) {
                team.unregister();
                team = null;
                tryCreateTeam();
            }
            scoreboard.getScoreboard().resetScores(value);
            fakeOfflinePlayer = new FakeOfflinePlayer(value);
            if(!team.hasPlayer(fakeOfflinePlayer)) {
                team.addPlayer(fakeOfflinePlayer);
            }
            scoreboard.getScoreboard().resetScores(value);
            team.setPrefix(preValue);
            team.setSuffix(postValue);
            scoreboard.getObjective().getScore(value).setScore(label.getScore());
            initValue = value;
        } else {
            fakeOfflinePlayer.setName(value);
            team.setPrefix(preValue);
            team.setSuffix(postValue);
        }
    }

    public void removeValue() {
        if (fakeOfflinePlayer != null) {
            if (team.hasPlayer(fakeOfflinePlayer)) {
                team.removePlayer(fakeOfflinePlayer);
            }
            fakeOfflinePlayer.setName("");
            fakeOfflinePlayer = null;
        }
        team.setPrefix("");
        team.setSuffix("");
        scoreboard.getScoreboard().resetScores(value);
        if (initValue != null) {
            scoreboard.getScoreboard().resetScores(initValue);
        }
    }

    private void updateValues() {
        if (fullValue.length() > 16) {
            String[] splitter = splitter(fullValue, 3);
            this.preValue = splitter[0];
            this.value = splitter[1];
            this.postValue = splitter[2];
        } else {
            this.preValue = "";
            this.value = fullValue;
            this.postValue = "";
        }
    }

    private String[] splitter(String word, int size) {
        String[] val = new String[size];
        int split = (int) Math.ceil(word.length() / size);
        int index = 0;
        int valIndex = 0;
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (index > split) {
                index = 0;
                valIndex++;
            }
            if (val[valIndex] == null) {
                val[valIndex] = "";
            }
            val[valIndex] += chars[i];
            index++;
        }
        for (int i = 0; i < size; i++) {
            if (val[i] == null) {
                val[i] = "";
            }
        }
        return val;
    }

}
