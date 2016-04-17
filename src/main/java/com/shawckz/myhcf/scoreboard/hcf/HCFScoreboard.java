package com.shawckz.myhcf.scoreboard.hcf;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.scoreboard.hcf.label.HCFLabel;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimer;
import com.shawckz.myhcf.scoreboard.hcf.timer.HCFTimerFormat;
import com.shawckz.myhcf.scoreboard.internal.XScoreboard;
import com.shawckz.myhcf.scoreboard.internal.timer.TimerPool;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Getter
public class HCFScoreboard extends XScoreboard {

    private final Player player;
    private static final TimerPool timerPool = new TimerPool(2L);
    private int scoreIndex = 1;

    private Map<String, HCFTimer> timers = new HashMap<>();
    private Map<String, HCFLabel> labels = new HashMap<>();

    public HCFScoreboard(Player player) {
        super(ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getFactionsConfig().getScoreboardTitle()), player);
        this.player = player;

        if (!timerPool.isRunning()) {
            timerPool.startTimerPool(Factions.getInstance(), true);
        }
    }

    public void stopTimerPool() {
        timerPool.stopTimerPool();
    }

    public void registerTimer(FLabel key, HCFTimer timer) {
        timers.put(key.toString(), timer);
    }

    public HCFTimer getTimer(FLabel key) {
        return getTimer(key, key.getFormat());
    }

    public boolean hasTimer(FLabel key) {
        return timers.containsKey(key.toString());
    }

    public boolean hasHCFLabel(FLabel key) {
        return labels.containsKey(key.toString());
    }

    public HCFTimer getTimer(FLabel key, boolean visible) {
        HCFTimer timer = getTimer(key);
        timer.setVisible(visible);
        return timer;
    }

    public HCFTimer getTimer(FLabel key, HCFTimerFormat format) {
        if (!timers.containsKey(key.toString())) {
            registerTimer(key, new HCFTimer(this, getKey(key), scoreIndex++, timerPool, format));
        }
        return timers.get(key.toString());
    }

    public HCFLabel getHCFLabel(FLabel key) {
        if (!labels.containsKey(key.toString())) {
            labels.put(key.toString(), new HCFLabel(this, getKey(key), scoreIndex++));
        }
        return labels.get(key.toString());
    }

    public String getKey(FLabel key) {
        return Factions.getInstance().getFactionsConfig().getScoreboardKey(key);
    }

    public static TimerPool getTimerPool() {
        return timerPool;
    }

    public int getNewScoreIndex() {
        return scoreIndex++;
    }

}
