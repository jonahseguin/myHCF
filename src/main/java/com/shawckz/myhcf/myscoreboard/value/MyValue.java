/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.myscoreboard.value;

import com.google.common.base.Splitter;
import com.shawckz.myhcf.myscoreboard.MyScoreboard;
import com.shawckz.myhcf.myscoreboard.label.MyLabel;

import java.util.Iterator;
import java.util.UUID;

import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

/**
 * Created by jonahseguin on 2016-07-22.
 */
public class MyValue {

    private final MyScoreboard scoreboard;
    private final MyLabel label;
    private String name;
    private Team team;
    private Score score;

    public MyValue(MyScoreboard scoreboard, MyLabel label) {
        this.scoreboard = scoreboard;
        this.label = label;
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
        return score != null ? (score.getScore()) : label.getScore();
    }

    public void setValue(int value) {
        if (!score.isScoreSet()) {
            score.setScore(-1);
        }

        score.setScore(value);
    }

    public void update(String newName) {
        int value = getValue();
        if (newName.equals(name)) {
            return;
        }

        create(newName);
        setValue(value);
    }

    public void remove() {
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
        }
        else {
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

}
