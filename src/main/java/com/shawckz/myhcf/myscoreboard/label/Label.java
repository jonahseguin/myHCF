/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.myscoreboard.label;

import com.shawckz.myhcf.myscoreboard.MyScoreboard;
import com.shawckz.myhcf.myscoreboard.value.MyValue;

public interface Label<T extends Label> {

    MyValue getValue();

    int getScore();

    void update();

    LabelProvider<T> getLabelProvider();

    LabelUpdater<T> getLabelUpdater();

    MyScoreboard getScoreboard();

}
