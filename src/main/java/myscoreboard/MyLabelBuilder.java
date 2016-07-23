package myscoreboard;

import org.bukkit.ChatColor;

/**
 * Created by jonahseguin on 2016-07-22.
 */
public class MyLabelBuilder {

    private String value;

    public MyLabelBuilder then(String s){
        if(tooLong(s + value)) {
            throw new MyScoreboardException("Value cannot be longer than 48 characters");
        }
        return this;
    }

    public MyLabelBuilder color(ChatColor color) {
        if(tooLong(color.toString() + value)) {
            throw new MyScoreboardException("Value cannot be longer than 48 characters");
        }
        return this;
    }

    public String build() {
        return value;
    }

    public final boolean tooLong(String s) {
        return s.length() > 48;
    }

}
