/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.myscoreboard.hcf.label;

import lombok.Getter;
import lombok.Setter;
import com.shawckz.myhcf.myscoreboard.MyScoreboard;
import com.shawckz.myhcf.myscoreboard.label.LabelProvider;
import com.shawckz.myhcf.myscoreboard.label.LabelUpdater;

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
        return label -> label.getValue().update(label.getLabelProvider().getLabel(label));
    }

    @Override
    public LabelProvider<HCFEndLabel> getLabelProvider() {
        return label -> label.getKey() + label.getEndValue();
    }
}
