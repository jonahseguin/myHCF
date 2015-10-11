package com.shawckz.myhcf.scoreboard.hcf.timer;

import com.shawckz.myhcf.scoreboard.internal.timer.XScoreboardTimer;
import com.shawckz.myhcf.scoreboard.internal.timer.XTimerTask;

public abstract class HCFTimerTask implements XTimerTask {

    private final XScoreboardTimer timer;
    private final long interval;

    public HCFTimerTask(XScoreboardTimer timer, long interval) {
        this.timer = timer;
        this.interval = interval;
    }

    @Override
    public abstract void run();

    @Override
    public long interval() {
        return interval;
    }

    @Override
    public boolean isComplete() {
        return interval <= 0.1;
    }

    @Override
    public void onComplete() {
        timer.setVisible(false);
        timer.setFrozen(true);
        timer.updateLabel();
    }

    @Override
    public boolean isFrozen() {
        return timer.isFrozen();
    }
}
