package com.shawckz.myhcf.scoreboard.internal.animation;

import lombok.Getter;

import org.bukkit.ChatColor;

/**
 * Created by Jonah on 11/7/2015.
 */
@Getter
public class ColorAnimation implements Animation {

    private String lastValue = "";
    private int index = 0;
    private final String color;

    public ColorAnimation(String color) {
        this.color = color;
    }

    @Override
    public String animate(String text) {
        if (lastValue.equals("")) {
            this.lastValue = text;
            return text;
        }
        if (index >= text.length()) {
            index = 0;
        }
        String newText = ChatColor.stripColor(text);
        String partA = newText.substring(0, index);
        String partB = newText.substring(index, newText.length());
        partA = ChatColor.translateAlternateColorCodes('&', this.color) + partA;//Add color
        partB = ChatColor.translateAlternateColorCodes('&', "&r") + partB;//Reset
        newText = partA + partB;
        index++;
        this.lastValue = newText;
        return newText;
    }
}
