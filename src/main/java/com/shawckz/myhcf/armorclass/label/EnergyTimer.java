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

    public EnergyTimer(XScoreboard scoreboard, String key, int score) {
        super(scoreboard, key, score, energyTimerPool, HCFTimerFormat.TENTH_OF_SECOND, false);
        final double regen = Factions.getInstance().getFactionsConfig().getEnergyPerQuarterSecond();
        getTimerPool().registerTimer(new HCFTimerTask(this, energyTimerPool.getInterval()) {
            @Override
            public void run() {
                if(getTime() < 0 || getTime() > maxEnergy) {
                    setTime(0);
                   // Factions.log("RESET: " + getTime());
                }
                if (getTime() + regen <= maxEnergy) {
                    setTime(getTime() + regen);
                  //  Factions.log("Time: " + getTime());
                }
                else {
                 //   Factions.log("COMPLETE");
                    if(getTime() != maxEnergy) {
                        setTime(maxEnergy);
                    }
                    onComplete();
                }
            //    getValue().update();
            //    updateLabel();
              //  Factions.log("Value: " + getValue().getFullValue());
            }

            @Override
            public void onComplete() {
                timer.setVisible(true);
                timer.setFrozen(true);
                timer.updateLabel();
            }

            @Override
            public boolean isComplete() {
                return false;
            }

        });

        if (!energyTimerPool.isRunning()) {
            Factions.log("Started energyTimerPool");
            energyTimerPool.startTimerPool(Factions.getInstance(), true);
        }

    }

    public static TimerPool getEnergyTimerPool() {
        return energyTimerPool;
    }
}
