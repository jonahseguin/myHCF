package myscoreboard;

import myscoreboard.label.MyLabel;

/**
 * Created by jonahseguin on 2016-07-22.
 */
public interface LabelUpdater<T extends MyLabel> {

    void callUpdate(T label);

}
