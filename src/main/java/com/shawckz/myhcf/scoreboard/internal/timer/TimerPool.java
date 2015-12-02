package com.shawckz.myhcf.scoreboard.internal.timer;

import com.shawckz.myhcf.scoreboard.internal.util.XScoreboardException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.util.io.netty.util.internal.ConcurrentSet;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class TimerPool {

    private final long interval;

    private BukkitTask bukkitTask = null;
    private boolean running = false;
    private boolean frozen = false;
    private Set<XTimerTask> timers = new ConcurrentSet<>();

    public final void startTimerPool(Plugin plugin) {
        startTimerPool(plugin, false);
    }

    public final void startTimerPool(Plugin plugin, boolean async) {
        if (!running) {
            if (async) {
                bukkitTask = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
                    @Override
                    public void run() {
                        updateTimerPool();
                    }
                }, 0L, interval);
            } else {
                bukkitTask = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                    @Override
                    public void run() {
                        updateTimerPool();
                    }
                }, 0L, interval);
            }
            running = true;
        } else {
            throw new XScoreboardException("ScoreboardTimer is already running");
        }
    }

    public final void stopTimerPool() {
        if (running) {
            if (bukkitTask != null) {
                bukkitTask.cancel();
                bukkitTask = null;
            }
            running = false;
        } else {
            throw new XScoreboardException("ScoreboardTimer is not running");
        }
    }

    private void updateTimerPool() {
        if (frozen) {
            return;
        }
        Iterator<XTimerTask> it = timers.iterator();
        while (it.hasNext()) {
            XTimerTask timer = it.next();
            if (!timer.isComplete()) {
                if (!timer.isFrozen()) {
                    timer.run();
                }
            } else {
                unregisterTimer(timer);
                timer.onComplete();
            }
        }
    }

    public final void registerTimer(XTimerTask timer) {
        timers.add(timer);
    }

    public final void unregisterTimer(XTimerTask timer) {
        timers.remove(timer);
    }

}
