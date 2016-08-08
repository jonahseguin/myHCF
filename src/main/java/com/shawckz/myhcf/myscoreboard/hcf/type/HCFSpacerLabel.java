/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.myscoreboard.hcf.type;

import lombok.Getter;
import com.shawckz.myhcf.myscoreboard.MyScoreboard;
import com.shawckz.myhcf.myscoreboard.hcf.label.HCFLabel;
import com.shawckz.myhcf.myscoreboard.label.LabelProvider;
import com.shawckz.myhcf.myscoreboard.label.LabelUpdater;

import org.bukkit.ChatColor;

@Getter
public class HCFSpacerLabel extends HCFLabel {

    private String labelValue;

    public HCFSpacerLabel(MyScoreboard scoreboard, int score) {
        super(scoreboard, score);
        this.labelValue = ChatColor.RESET + " ";
    }

    @Override
    public LabelProvider<HCFSpacerLabel> getLabelProvider() {
        return HCFSpacerLabel::getLabelValue;
    }

    @Override
    public LabelUpdater<HCFSpacerLabel> getLabelUpdater() {
        return label -> label.getValue().update(label.getLabelProvider().getLabel(label));
    }
}
