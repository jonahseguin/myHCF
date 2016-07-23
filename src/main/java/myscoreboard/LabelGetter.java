package myscoreboard;

import myscoreboard.label.MyLabel;

/**
 * Created by jonahseguin on 2016-07-22.
 */
public interface LabelGetter<T extends MyLabel> {

    String getLabel(T label);

}
