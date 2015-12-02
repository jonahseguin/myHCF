package com.shawckz.myhcf.scoreboard.internal.animation;

import com.shawckz.myhcf.scoreboard.internal.XScoreboard;
import com.shawckz.myhcf.scoreboard.internal.timer.TimerPool;
import lombok.Getter;

/**
 * Created by Jonah on 11/7/2015.
 */
@Getter
public class SimpleScoreboardAnimation extends XScoreboardAnimation {

    private final Animation animation;

    public SimpleScoreboardAnimation(XScoreboard scoreboard, String value, int score, TimerPool timerPool, Animation animation) {
        super(scoreboard, value, score, timerPool);
        this.animation = animation;
        getTimerPool().registerTimer(new XAnimation(animation, 5L) {
            @Override
            public void run() {
                setVisible(true);
                setValue(animate(getValue().getFullValue()));
            }

            @Override
            public void onComplete() {
                setVisible(false);
                getValue().removeValue();
            }

            @Override
            public boolean isComplete() {
                return isRunning();
            }

            @Override
            public boolean isFrozen() {
                return getTimerPool().isFrozen();
            }
        });
    }

    public void pauseAnimation() {
        setFrozen(true);
        getTimerPool().setFrozen(true);
        updateLabel();
    }

    public void unpauseAnimation() {
        setFrozen(false);
        getTimerPool().setFrozen(false);
        updateLabel();
    }

    public void hide() {
        setVisible(false);
        pauseAnimation();//might want to remove this, this is assuming that when you hide you also want to pause the animation
    }

    public void show() {
        setVisible(true);
        updateLabel();
    }


}
