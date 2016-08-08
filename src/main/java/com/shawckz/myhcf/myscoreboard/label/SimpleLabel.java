/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.myscoreboard.label;

import com.shawckz.myhcf.myscoreboard.MyScoreboard;

public class SimpleLabel extends MyLabel {

    private String baseValue = null;

    private LabelProvider provider;
    private LabelUpdater updater;

    public SimpleLabel(MyScoreboard scoreboard, int score, final String baseValue) {
        super(scoreboard, score);

        this.baseValue = baseValue;
        this.provider = new LabelProvider<SimpleLabel>() {
            @Override
            public String getLabel(SimpleLabel label) {
                return label.getBaseValue();
            }
        };
        this.updater = new LabelUpdater<SimpleLabel>() {
            @Override
            public void callUpdate(SimpleLabel label) {
                label.getValue().update(label.getLabelProvider().getLabel(label));
            }
        };
    }

    public SimpleLabel(MyScoreboard scoreboard, int score, LabelProvider<SimpleLabel> provider) {
        super(scoreboard, score);
        this.baseValue = provider.getLabel(this);
        this.provider = provider;
        this.updater = new LabelUpdater<SimpleLabel>() {
            @Override
            public void callUpdate(SimpleLabel label) {
                label.getValue().update(label.getLabelProvider().getLabel(label));
            }
        };
    }

    public SimpleLabel(MyScoreboard scoreboard, int score, LabelProvider<SimpleLabel> provider, LabelUpdater<SimpleLabel> updater) {
        super(scoreboard, score);
        this.provider = provider;
        this.updater = updater;
    }

    public String getBaseValue() {
        return baseValue;
    }

    @Override
    public void update() {
        getLabelUpdater().callUpdate(this);
    }

    public SimpleLabel setProvider(LabelProvider provider) {
        this.provider = provider;
        return this;
    }

    public SimpleLabel setUpdater(LabelUpdater updater) {
        this.updater = updater;
        return this;
    }

    @Override
    public LabelProvider getLabelProvider() {
        return null;
    }

    @Override
    public LabelUpdater getLabelUpdater() {
        return null;
    }
}
