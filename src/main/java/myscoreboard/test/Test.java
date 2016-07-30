package myscoreboard.test;

import com.shawckz.myhcf.Factions;
import myscoreboard.MyScoreboard;
import myscoreboard.hcf.HCFLabelID;
import myscoreboard.hcf.HCFScoreboardWrapper;
import myscoreboard.hcf.timer.HCFTimerLabel;
import myscoreboard.hcf.timer.TimerUpdater;
import myscoreboard.label.LabelGetter;
import org.bukkit.Bukkit;

import java.text.DecimalFormat;

/**
 * Created by jonahseguin on 2016-07-25.
 */
public class Test {

    {
        HCFScoreboardWrapper scoreboard = new HCFScoreboardWrapper(Factions.getInstance().getCache().getHCFPlayer("Shawckz"));

        final TimerUpdater timerUpdater = new TimerUpdater();

        HCFTimerLabel label = new HCFTimerLabel(scoreboard.getScoreboard(), 1, 0.0D) {
            final DecimalFormat df = new DecimalFormat("#.#");
            @Override
            public TimerUpdater getLabelUpdater() {
                return timerUpdater;
            }

            @Override
            public LabelGetter<HCFTimerLabel> getNextValue() {
                return label1 -> "Something: " + df.format(label1.getTimerValue());
            }

            @Override
            public void updateTime() {
                setTimerValue(getTimerValue() + 0.1);
            }

            @Override
            public boolean isFinished() {
                return getTimerValue() >= 60;
            }
        };

        timerUpdater.addLabel(label);

        timerUpdater.startTimer(null, 2L, true); //plugin, delay, async

        scoreboard.addLabel(HCFLabelID.ARMOR_CLASS, label, false);

    }

}
