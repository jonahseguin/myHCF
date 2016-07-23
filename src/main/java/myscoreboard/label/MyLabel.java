package myscoreboard.label;

import myscoreboard.LabelGetter;
import myscoreboard.LabelUpdater;
import myscoreboard.MyScoreboard;
import myscoreboard.MyValue;

/**
 * Created by jonahseguin on 2016-07-22.
 */
public abstract class MyLabel {

    private final MyScoreboard scoreboard;
    private int score;
    private boolean visible;
    private MyValue value;

    private LabelUpdater labelUpdater = (label -> {
         // label#getValue#update...
    });

    public MyLabel(MyScoreboard scoreboard, int score, String value) {
        this.scoreboard = scoreboard;
        this.value = new MyValue(scoreboard, this, value);
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void update() {
        value.update(getLabelGetter().getLabel(this));
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public LabelUpdater getLabelUpdater() {
        return labelUpdater;
    }

    public void setLabelUpdater(LabelUpdater labelUpdater) {
        this.labelUpdater = labelUpdater;
    }

    public MyValue getValue() {
        return value;
    }

    public MyScoreboard getScoreboard() {
        return scoreboard;
    }

    public abstract LabelGetter getLabelGetter();

}
