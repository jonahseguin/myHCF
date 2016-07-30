package myscoreboard.hcf.timer;

import myscoreboard.hcf.label.HCFLabel;
import myscoreboard.label.LabelGetter;
import myscoreboard.label.LabelUpdater;
import myscoreboard.label.MyLabel;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jonahseguin on 2016-07-25.
 */
public class TimerUpdater implements LabelUpdater<HCFTimerLabel> {

    private final Set<HCFTimerLabel> labels = new HashSet<>();
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

    public void addLabel(HCFTimerLabel label) {
        labels.add(label);
    }

    public void removeLabel(HCFTimerLabel label) {
        labels.remove(label);
    }

    public boolean hasLabel(HCFTimerLabel label) {
        return labels.contains(label);
    }

    private void updateLabels() {
        labels.stream()
                .filter(label -> !label.isFinished())
                .forEach(label -> {
                    label.updateTime();
                    callUpdate(label);
                });
    }

    @Override
    public void callUpdate(HCFTimerLabel label) {
        label.getValue().update(label.getNextValue().getLabel(label));
    }
}
