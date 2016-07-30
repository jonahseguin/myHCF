/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package myscoreboard.hcf.timer;

import lombok.Getter;
import lombok.Setter;
import myscoreboard.MyScoreboard;
import myscoreboard.hcf.label.HCFLabel;
import myscoreboard.label.LabelGetter;
import myscoreboard.label.LabelUpdater;
import myscoreboard.label.MyLabel;

@Getter
@Setter
public abstract class HCFTimerLabel extends HCFLabel {

    private double timerValue = 0;

    public HCFTimerLabel(MyScoreboard scoreboard, int score, double timerValue) {
        super(scoreboard, score);
        this.timerValue = timerValue;
    }

    @Override
    public abstract TimerUpdater getLabelUpdater();

    public abstract LabelGetter<HCFTimerLabel> getNextValue();

    public abstract void updateTime();

    public abstract boolean isFinished();

    @Override
    public LabelGetter<HCFTimerLabel> getLabelGetter() {
        return label -> label.getNextValue().getLabel(label);
    }
}
