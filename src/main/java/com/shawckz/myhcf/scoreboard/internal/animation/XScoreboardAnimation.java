package com.shawckz.myhcf.scoreboard.internal.animation;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.scoreboard.internal.XScoreboard;
import com.shawckz.myhcf.scoreboard.internal.label.XLabel;
import com.shawckz.myhcf.scoreboard.internal.timer.TimerPool;
import com.shawckz.myhcf.scoreboard.internal.timer.XTimerTask;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.scheduler.BukkitTask;

/**
 * Created by Jonah on 11/7/2015.
 */
@Getter
@Setter
public class XScoreboardAnimation extends XLabel {

    private boolean frozen = false;
    private boolean running = false;
    private BukkitTask bukkitTask = null;
    private final TimerPool timerPool;

    public XScoreboardAnimation(XScoreboard scoreboard, String value, int score, long interval) {
        super(scoreboard, value, score);
        this.timerPool = new TimerPool(interval);
    }

    public XScoreboardAnimation(XScoreboard scoreboard, String value, int score, TimerPool timerPool) {
        super(scoreboard, value, score);
        this.timerPool = timerPool;
    }

    public final void startTimer(XTimerTask task) {
        timerPool.registerTimer(task);
        if (!timerPool.isRunning()) {
            timerPool.startTimerPool(Factions.getInstance());
        }
    }


}
