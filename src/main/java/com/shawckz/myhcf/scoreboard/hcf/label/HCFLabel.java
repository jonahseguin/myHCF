package com.shawckz.myhcf.scoreboard.hcf.label;

import com.shawckz.myhcf.scoreboard.internal.XScoreboard;
import com.shawckz.myhcf.scoreboard.internal.label.XScoreboardLabel;

public class HCFLabel extends XScoreboardLabel {

    public HCFLabel(XScoreboard scoreboard, String value, int score) {
        super(scoreboard, value, score);
    }

    public void show(){
        setVisible(true);
        updateLabel();
    }

    public void hide(){
        setVisible(false);
        updateLabel();
    }

}
