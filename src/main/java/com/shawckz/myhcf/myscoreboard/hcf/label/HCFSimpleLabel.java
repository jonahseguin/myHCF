/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.myscoreboard.hcf.label;

import lombok.Getter;
import com.shawckz.myhcf.myscoreboard.MyScoreboard;
import com.shawckz.myhcf.myscoreboard.label.LabelProvider;
import com.shawckz.myhcf.myscoreboard.label.LabelUpdater;

@Getter
public class HCFSimpleLabel extends HCFLabel {

    private String labelValue;

    public HCFSimpleLabel(MyScoreboard scoreboard, int score, String labelValue) {
        super(scoreboard, score);
        this.labelValue = labelValue;
    }

    @Override
    public LabelProvider<HCFSimpleLabel> getLabelProvider() {
        return HCFSimpleLabel::getLabelValue;
    }

    @Override
    public LabelUpdater<HCFSimpleLabel> getLabelUpdater() {
        return label -> label.getValue().update(label.getLabelProvider().getLabel(label));
    }
}
