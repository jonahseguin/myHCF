/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package myscoreboard.hcf;

import lombok.Getter;
import myscoreboard.MyScoreboard;
import myscoreboard.label.LabelGetter;
import myscoreboard.label.LabelUpdater;

import org.bukkit.ChatColor;

@Getter
public class HCFSpacerLabel extends HCFLabel {

    private String labelValue;

    public HCFSpacerLabel(MyScoreboard scoreboard, int score) {
        super(scoreboard, score);
        this.labelValue = ChatColor.RESET + " ";
    }

    @Override
    public LabelGetter<HCFSpacerLabel> getLabelGetter() {
        return HCFSpacerLabel::getLabelValue;
    }

    @Override
    public LabelUpdater<HCFSpacerLabel> getLabelUpdater() {
        return label -> label.getValue().update(label.getLabelGetter().getLabel(label));
    }
}
