/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package myscoreboard.hcf.label;

import lombok.Getter;
import lombok.Setter;
import myscoreboard.MyScoreboard;
import myscoreboard.label.LabelGetter;
import myscoreboard.label.LabelUpdater;

@Getter
@Setter
public class HCFEndLabel extends HCFLabel {

    private String key;
    private String endValue;

    public HCFEndLabel(MyScoreboard scoreboard, int score, String key, String endValue) {
        super(scoreboard, score);
        this.key = key;
        this.endValue = endValue;
    }

    public HCFEndLabel setEndValue(String endValue) {
        this.endValue = endValue;
        return this;
    }

    @Override
    public LabelUpdater<HCFEndLabel> getLabelUpdater() {
        return label -> label.getValue().update(label.getLabelGetter().getLabel(label));
    }

    @Override
    public LabelGetter<HCFEndLabel> getLabelGetter() {
        return label -> label.getKey() + label.getEndValue();
    }
}
