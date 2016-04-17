package com.shawckz.myhcf.scoreboard.hcf.timer;

import com.shawckz.myhcf.scoreboard.internal.XScoreboard;
import com.shawckz.myhcf.scoreboard.internal.timer.TimerPool;
import com.shawckz.myhcf.scoreboard.internal.timer.XScoreboardTimer;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class HCFTimer extends XScoreboardTimer {

    private final String key;
    private final HCFTimerFormat format;
    private double time = 0.0D;
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.#");

    public HCFTimer(XScoreboard scoreboard, String key, int score, TimerPool timerPool, HCFTimerFormat format) {
        super(scoreboard, key + "0.0", score, timerPool);
        this.key = key;
        this.format = format;
        getTimerPool().registerTimer(new HCFTimerTask(this, timerPool.getInterval()) {
            @Override
            public void run() {
                if (time - 0.1D > 0) {
                    setTime(time - 0.1D);
                }
                else {
                    onComplete();
                }
            }
        });
    }

    public HCFTimer(XScoreboard scoreboard, String key, int score, TimerPool timerPool) {
        this(scoreboard, key + "0.0", score, timerPool, HCFTimerFormat.TENTH_OF_SECOND);
    }

    public HCFTimer setTime(double time) {
        this.time = time;
        if (time > 0) {
            if (isFrozen()) {
                setFrozen(false);
            }
            if (!isVisible()) {
                setVisible(true);
            }
            updateTime();
        }
        return this;
    }

    public HCFTimer pauseTimer() {
        setFrozen(true);
        updateLabel();
        return this;
    }

    public HCFTimer unpauseTimer() {
        setFrozen(false);
        updateLabel();
        return this;
    }

    public boolean isPaused() {
        return isFrozen();
    }

    public HCFTimer stopTimer() {
        setFrozen(true);
        setVisible(false);
        setTime(0.0D);
        updateLabel();
        return this;
    }

    public String getKey() {
        return key;
    }

    public double getTime() {
        return time;
    }

    private void updateTime() {
        if (format == HCFTimerFormat.TENTH_OF_SECOND) {
            setValue(key + Float.parseFloat(DECIMAL_FORMAT.format(time)));
        }
        else if (format == HCFTimerFormat.HH_MM_SS) {
            int millis = (int) Math.round(time);
            setValue(key + String.format("%02d:%02d:%02d",
                            TimeUnit.SECONDS.toHours(millis),
                            TimeUnit.SECONDS.toMinutes(millis) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(millis)),
                            TimeUnit.SECONDS.toSeconds(millis) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(millis))));
        }
        else if (format == HCFTimerFormat.MM_SS) {
            int millis = (int) Math.round(time);
            setValue(key + String.format("%02d:%02d",
                            TimeUnit.SECONDS.toMinutes(millis) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(millis)),
                            TimeUnit.SECONDS.toSeconds(millis) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(millis))));
        }
    }

    public HCFTimer hide() {
        setVisible(false);
        setFrozen(true);
        updateLabel();
        return this;
    }

    public HCFTimer show() {
        boolean update = false;
        if (!isVisible()) {
            update = true;
            setVisible(true);
        }
        if (isFrozen()) {
            update = true;
            setFrozen(false);
        }
        if (update) {
            updateLabel();
        }
        return this;
    }
}
