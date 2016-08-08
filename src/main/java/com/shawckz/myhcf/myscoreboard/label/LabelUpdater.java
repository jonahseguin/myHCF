/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.myscoreboard.label;

/**
 * Created by jonahseguin on 2016-07-22.
 */
public interface LabelUpdater<T extends Label> {

    void callUpdate(T label);

}
