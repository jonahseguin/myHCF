/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.scoreboard.internal.label;

import com.shawckz.myhcf.scoreboard.internal.XScoreboard;
import com.shawckz.myhcf.scoreboard.internal.util.FakeOfflinePlayer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
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
        List<String> split = splitEqually(fullValue, 16);
        if(split.isEmpty()) {
            this.preValue = "";
            this.value = "";
            this.postValue = "";
        }
        else if(split.size() == 1) {
            this.preValue = "";
            this.value = split.get(0);
            this.postValue = "";
        }
        else if (split.size() == 2) {
            this.preValue = "";
            this.value = split.get(0);
            this.postValue = split.get(1);
        }
        else if (split.size() == 3) {
            this.preValue = split.get(0);
            this.value = split.get(1);
            this.postValue = split.get(2);
        }
        else{
            this.preValue = "";
            this.value = "";
            this.postValue = "";
            throw new RuntimeException("Value length is too long");
        }
    }

    public List<String> splitEqually(String text, int size) {
        // Give the list the right capacity to start with. You could use an array
        // instead if you wanted.
        List<String> ret = new ArrayList<>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }

}
