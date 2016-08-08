/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.myscoreboard.label;

public interface TimerLabel<K extends TimerLabel> extends Label<K> {

    boolean isFinished();

    void updateTime();

    void onFinish();

}
