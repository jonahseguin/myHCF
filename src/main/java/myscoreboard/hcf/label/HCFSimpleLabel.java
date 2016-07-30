/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package myscoreboard.hcf.label;

import lombok.Getter;
import myscoreboard.MyScoreboard;
import myscoreboard.label.LabelGetter;
import myscoreboard.label.LabelUpdater;

@Getter
public class HCFSimpleLabel extends HCFLabel {

    private String labelValue;

    public HCFSimpleLabel(MyScoreboard scoreboard, int score, String labelValue) {
        super(scoreboard, score);
        this.labelValue = labelValue;
    }

    @Override
    public LabelGetter<HCFSimpleLabel> getLabelGetter() {
        return HCFSimpleLabel::getLabelValue;
    }

    @Override
    public LabelUpdater<HCFSimpleLabel> getLabelUpdater() {
        return label -> label.getValue().update(label.getLabelGetter().getLabel(label));
    }
}
