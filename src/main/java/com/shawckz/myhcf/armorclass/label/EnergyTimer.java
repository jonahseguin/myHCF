/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.
 * Thank you.
 */

package com.shawckz.myhcf.armorclass.label;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimer;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimerFormat;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimerTask;
import com.shawckz.myhcf.scoreboard.internal.XScoreboard;
import com.shawckz.myhcf.scoreboard.internal.timer.TimerPool;

/**
 * Created by Jonah Seguin on 1/24/2016.
 *
 * @author Shawckz
 *         Shawckz.com
 */
public class EnergyTimer extends HCFTimer {

    private static final double maxEnergy = Factions.getInstance().getFactionsConfig().getMaxEnergy();

    private static final TimerPool energyTimerPool = new TimerPool(5L);

    public EnergyTimer(XScoreboard scoreboard, String key, int score, TimerPool timerPool) {
        super(scoreboard, key, score, timerPool, HCFTimerFormat.TENTH_OF_SECOND);
        final double regen = Factions.getInstance().getFactionsConfig().getEnergyPerQuarterSecond();
        energyTimerPool.registerTimer(new HCFTimerTask(this, timerPool.getInterval()) {
            @Override
            public void run() {
                if (getTime() + regen <= maxEnergy) {
                    setTime(getTime() + regen);
                }
                else {
                    onComplete();
                }
            }

            @Override
            public void onComplete() {
                timer.setVisible(true);
                timer.setFrozen(true);
                timer.updateLabel();
            }

        });

        if (!energyTimerPool.isRunning()) {
            energyTimerPool.startTimerPool(Factions.getInstance(), true);
        }

    }

    public static TimerPool getEnergyTimerPool() {
        return energyTimerPool;
    }
}
