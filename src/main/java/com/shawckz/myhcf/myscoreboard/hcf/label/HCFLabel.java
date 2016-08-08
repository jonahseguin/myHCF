/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.myscoreboard.hcf.label;

import com.shawckz.myhcf.myscoreboard.MyScoreboard;
import com.shawckz.myhcf.myscoreboard.hcf.timer.HCFTimerLabel;
import com.shawckz.myhcf.myscoreboard.hcf.type.HCFSpacerLabel;
import com.shawckz.myhcf.myscoreboard.label.MyLabel;
import com.shawckz.myhcf.util.HCFException;

public abstract class HCFLabel extends MyLabel {

    public HCFLabel(MyScoreboard scoreboard, int score) {
        super(scoreboard, score);
    }

    @Override
    public final void update() {
        getLabelUpdater().callUpdate(this);
    }

    public void remove() {
        getValue().remove();
    }

    public HCFEndLabel getAsEnd() {
        if(this instanceof HCFEndLabel) {
            return (HCFEndLabel) this;
        }
        throw new HCFException("Label must be an endLabel to do this");
    }

    public HCFSimpleLabel getAsSimple() {
        if(this instanceof HCFSimpleLabel) {
            return (HCFSimpleLabel) this;
        }
        throw new HCFException("Label must be a simpleLabel to do this");
    }

    public HCFSpacerLabel getAsSpacer() {
        if(this instanceof HCFSpacerLabel) {
            return (HCFSpacerLabel) this;
        }
        throw new HCFException("Label must be a spacerLabel to do this");
    }

    public HCFTimerLabel getAsTimer() {
        if(this instanceof HCFTimerLabel) {
            return (HCFTimerLabel) this;
        }
        throw new HCFException("Label must be a timer to do this");
    }
}
