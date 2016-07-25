/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package myscoreboard.label;

import myscoreboard.util.MyScoreboardException;

import org.bukkit.ChatColor;

/**
 * Created by jonahseguin on 2016-07-22.
 */
public class MyLabelBuilder {

    private String value = "";

    public MyLabelBuilder(String value) {
        then(value);
    }

    public MyLabelBuilder() {
    }

    public MyLabelBuilder then(String s){
        if(tooLong(s + value)) {
            throw new MyScoreboardException("Value cannot be longer than 48 characters");
        }
        value += s;
        return this;
    }

    public MyLabelBuilder color(ChatColor color) {
        if(tooLong(color.toString() + value)) {
            throw new MyScoreboardException("Value cannot be longer than 48 characters");
        }
        value += color.toString();
        return this;
    }

    public String build() {
        return value;
    }

    public final boolean tooLong(String s) {
        return s.length() > 48;
    }

}
