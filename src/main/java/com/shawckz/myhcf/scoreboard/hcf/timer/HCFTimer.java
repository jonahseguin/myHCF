package com.shawckz.myhcf.scoreboard.hcf.timer;

import com.shawckz.myhcf.scoreboard.internal.XScoreboard;
import com.shawckz.myhcf.scoreboard.internal.timer.TimerPool;
import com.shawckz.myhcf.scoreboard.internal.timer.XScoreboardTimer;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class HCFTimer extends XScoreboardTimer {

    private final String key;
    private final HCFTimerFormat format;
    private double time = 0.0D;
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.#");

    public HCFTimer(XScoreboard scoreboard, String key, int score, TimerPool timerPool, HCFTimerFormat format) {
        super(scoreboard, key + ChatColor.GRAY+" - "+ ChatColor.GOLD+"0.0", score, timerPool);
        this.key = key;
        this.format = format;
        getTimerPool().registerTimer(new HCFTimerTask(this, timerPool.getInterval()) {
            @Override
            public void run() {
                if(time - 0.1D > 0){
                    setTime(time - 0.1D);
                }
                else{
                    onComplete();
                }
            }
        });
    }

    public HCFTimer(XScoreboard scoreboard, String key, int score, TimerPool timerPool) {
        this(scoreboard, key + ChatColor.GRAY+" - "+ ChatColor.GOLD+"0.0", score, timerPool, HCFTimerFormat.TENTH_OF_SECOND);
    }

    public void setTime(double time) {
        this.time = time;
        if(time > 0){
            if(isFrozen()){
                setFrozen(false);
            }
            if(!isVisible()){
                setVisible(true);
            }
            updateTime();
        }
    }

    public void pauseTimer(){
        setFrozen(true);
        updateLabel();
    }

    public void unpauseTimer(){
        setFrozen(false);
        updateLabel();
    }

    public void stopTimer(){
        setFrozen(true);
        setVisible(false);
        setTime(0.0D);
        updateLabel();
    }

    public String getKey() {
        return key;
    }

    public double getTime() {
        return time;
    }

    private void updateTime(){
        if(format == HCFTimerFormat.TENTH_OF_SECOND){
            setValue(key + ChatColor.GRAY+" - "+ ChatColor.GOLD + Float.parseFloat(DECIMAL_FORMAT.format(time)));
        }
        else if (format == HCFTimerFormat.HH_MM_SS){
            int millis = (int) Math.round(time);
            setValue(key + ChatColor.GRAY + " - " +
                    ChatColor.GOLD +
                    String.format("%02d:%02d:%02d",
                            TimeUnit.SECONDS.toHours(millis),
                            TimeUnit.SECONDS.toMinutes(millis) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(millis)),
                            TimeUnit.SECONDS.toSeconds(millis) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(millis))));
        }
        else if (format == HCFTimerFormat.MM_SS){
            int millis = (int) Math.round(time);
            setValue(key + ChatColor.GRAY + " - "+
                    ChatColor.GOLD +
                    String.format("%02d:%02d",
                            TimeUnit.SECONDS.toMinutes(millis) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(millis)),
                            TimeUnit.SECONDS.toSeconds(millis) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(millis))));
        }
    }

    public void hide() {
        setVisible(false);
        setFrozen(true);
        updateLabel();
    }
}
