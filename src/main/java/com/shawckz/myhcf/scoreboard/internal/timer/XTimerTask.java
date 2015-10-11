package com.shawckz.myhcf.scoreboard.internal.timer;

public interface XTimerTask {

    void run();

    long interval();

    boolean isComplete();

    void onComplete();

    boolean isFrozen();

}
