package com.shawckz.myhcf.scoreboard.internal.label;

import com.shawckz.myhcf.scoreboard.internal.XScoreboard;

public abstract class XLabel {

    protected final XScoreboard scoreboard;
    protected final XLabelValue value;
    protected int score;
    protected boolean visible = true;

    public XLabel(XScoreboard scoreboard, String value, int score) {
        this.scoreboard = scoreboard;
        this.score = score;
        this.value = new XLabelValue(scoreboard, this, value);
    }

    public final void setValue(String value) {
        setValue(value, true);
    }

    public final void setValue(String value, boolean update) {
        this.value.setValue(value, update);
    }

    public final void updateLabel() {
        value.update();
    }

    public final void setScore(int score) {
        this.score = score;
    }

    public final void setVisible(boolean visible) {
        this.visible = visible;
    }

    public final XLabelValue getValue() {
        return value;
    }

    public final XScoreboard getScoreboard() {
        return scoreboard;
    }

    public final int getScore() {
        return score;
    }

    public final boolean isVisible() {
        return visible;
    }

}
