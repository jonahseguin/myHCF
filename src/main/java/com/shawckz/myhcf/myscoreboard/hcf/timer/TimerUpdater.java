package com.shawckz.myhcf.myscoreboard.hcf.timer;

import com.shawckz.myhcf.myscoreboard.label.LabelUpdater;
import com.shawckz.myhcf.myscoreboard.label.TimerLabel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by jonahseguin on 2016-07-25.
 */
public class TimerUpdater implements LabelUpdater<TimerLabel> {

    private final Set<TimerLabel> labels = new HashSet<>();
    private BukkitTask task = null;
    private boolean running = false;

    public boolean startTimer(Plugin plugin, long delay, boolean async) {
        if (!running) {
            running = true;
            if (async) {
                task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        updateLabels();
                    }
                }.runTaskTimerAsynchronously(plugin, delay, delay);
            } else {
                task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        updateLabels();
                    }
                }.runTaskTimer(plugin, delay, delay);
            }
        }
        return !running;
    }

    public boolean stopTimer() {
        if (running) {
            running = false;
            if (task != null) {
                task.cancel();
                task = null;
            }
        }

        return running;
    }

    public boolean isRunning() {
        return running;
    }

    public void addLabel(TimerLabel label) {
        labels.add(label);
    }

    public void removeLabel(TimerLabel label) {
        labels.remove(label);
    }

    public boolean hasLabel(TimerLabel label) {
        return labels.contains(label);
    }

    private void updateLabels() {
        for(Iterator<TimerLabel> it = labels.iterator(); it.hasNext(); ) {
            TimerLabel label = it.next();
            if(!label.isFinished()) {
                label.updateTime();
                callUpdate(label);
            }
            else{
                label.onFinish();
            }
        }
    }

    @Override
    public void callUpdate(TimerLabel label) {
        label.getValue().update(label.getLabelProvider().getLabel(label));
    }
}
