package com.shawckz.myhcf.scoreboard.internal.animation;

import com.shawckz.myhcf.scoreboard.internal.timer.XTimerTask;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Created by Jonah on 11/7/2015.
 */
@RequiredArgsConstructor
@Setter
public abstract class XAnimation implements XTimerTask {

    private final Animation animation;
    private final long interval;

    public String animate(String text) {
        return animation.animate(text);
    }

    @Override
    public abstract void run();

    @Override
    public long interval() {
        return interval;
    }

    @Override
    public abstract boolean isComplete();

    @Override
    public abstract void onComplete();

    @Override
    public abstract boolean isFrozen();
}
