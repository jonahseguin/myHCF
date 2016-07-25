package myscoreboard.label;

import myscoreboard.MyScoreboard;
import myscoreboard.value.MyValue;

/**
 * Created by jonahseguin on 2016-07-22.
 */
public abstract class MyLabel {

    private final MyScoreboard scoreboard;
    private int score;
    private MyValue value;

    public MyLabel(MyScoreboard scoreboard, int score) {
        this.scoreboard = scoreboard;
        this.value = new MyValue(scoreboard, this);
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public MyValue getValue() {
        return value;
    }

    public MyScoreboard getScoreboard() {
        return scoreboard;
    }

    public abstract void update();

    public abstract LabelGetter getLabelGetter();

    public abstract LabelUpdater getLabelUpdater();

}
