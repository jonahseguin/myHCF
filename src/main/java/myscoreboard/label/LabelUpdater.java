/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package myscoreboard.label;

/**
 * Created by jonahseguin on 2016-07-22.
 */
public interface LabelUpdater<T extends MyLabel> {

    void callUpdate(T label);

}
