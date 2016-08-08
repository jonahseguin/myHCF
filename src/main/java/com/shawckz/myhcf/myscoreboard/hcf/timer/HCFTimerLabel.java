/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.myscoreboard.hcf.timer;

import com.shawckz.myhcf.myscoreboard.hcf.HCFScoreboardWrapper;
import com.shawckz.myhcf.myscoreboard.hcf.label.HCFLabel;
import com.shawckz.myhcf.myscoreboard.label.TimerLabel;

public abstract class HCFTimerLabel extends HCFLabel implements TimerLabel {

    private double timerValue = 0;

    private final HCFScoreboardWrapper scoreboardWrapper;

    public HCFTimerLabel(HCFScoreboardWrapper scoreboard, int score, double timerValue) {
        super(scoreboard.getScoreboard(), score);
        this.timerValue = timerValue;
        this.scoreboardWrapper = scoreboard;
    }

    @Override
    public abstract TimerUpdater getLabelUpdater();

    @Override
    public abstract void updateTime();

    @Override
    public abstract boolean isFinished();

    @Override
    public abstract void onFinish();

    public double getTimerValue() {
        return timerValue;
    }

    public HCFTimerLabel setTimerValue(double timerValue) {
        this.timerValue = timerValue;
        return this;
    }

    public HCFScoreboardWrapper getScoreboardWrapper() {
        return scoreboardWrapper;
    }

}
