/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package myscoreboard.hcf.label;

import myscoreboard.MyScoreboard;
import myscoreboard.hcf.type.HCFSpacerLabel;
import myscoreboard.label.MyLabel;

public abstract class HCFLabel extends MyLabel {

    public HCFLabel(MyScoreboard scoreboard, int score) {
        super(scoreboard, score);
    }

    @Override
    public void update() {
        getLabelUpdater().callUpdate(this);
    }

    public void remove() {
        getValue().remove();
    }

    public HCFEndLabel getAsEnd() {
        return (HCFEndLabel) this;
    }

    public HCFSimpleLabel getAsSimple() {
        return (HCFSimpleLabel) this;
    }

    public HCFSpacerLabel getAsSpacer() {
        return (HCFSpacerLabel) this;
    }

}
