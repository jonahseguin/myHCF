/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.myscoreboard.hcf.timer;

import com.shawckz.myhcf.myscoreboard.hcf.HCFLabelID;
import com.shawckz.myhcf.myscoreboard.hcf.HCFScoreboardWrapper;
import com.shawckz.myhcf.myscoreboard.label.LabelProvider;

import java.text.DecimalFormat;

public class EnergyTimer extends HCFTimerLabel {

    public static final double MAX_ENERGY = 50.0D;

    private final TimerUpdater timerUpdater;

    private static final DecimalFormat FORMAT = new DecimalFormat("#.#");

    public EnergyTimer(HCFScoreboardWrapper scoreboard, int score, double timerValue, TimerUpdater timerUpdater) {
        super(scoreboard, score, timerValue);
        this.timerUpdater = timerUpdater;
    }

    @Override
    public TimerUpdater getLabelUpdater() {
        return timerUpdater;
    }

    @Override
    public LabelProvider<HCFTimerLabel> getLabelProvider() {
        return label -> HCFLabelID.SPAWN_TAG.key + FORMAT.format(label.getTimerValue());
    }

    @Override
    public void onFinish() {
        //Still show it
        timerUpdater.removeLabel(this);
    }

    @Override
    public void updateTime() {
        setTimerValue(getTimerValue() + 0.1D);
    }

    @Override
    public boolean isFinished() {
        return getTimerValue() >= MAX_ENERGY;
    }
}
