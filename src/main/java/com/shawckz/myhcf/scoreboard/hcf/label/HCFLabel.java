package com.shawckz.myhcf.scoreboard.hcf.label;

import com.shawckz.myhcf.scoreboard.internal.XScoreboard;
import com.shawckz.myhcf.scoreboard.internal.label.XScoreboardLabel;
import net.md_5.bungee.api.ChatColor;

public class HCFLabel extends XScoreboardLabel {

    private String endValue = "";

    public HCFLabel(XScoreboard scoreboard, String value, int score) {
        super(scoreboard, ChatColor.translateAlternateColorCodes('&', value), score);
    }

    public String getEndValue() {
        return endValue;
    }

    public HCFLabel setEndValue(String endValue) {
        endValue = ChatColor.translateAlternateColorCodes('&', endValue);
        if (!this.endValue.equals("")) {
            setValue(getValue().getFullValue().replaceAll(this.endValue, endValue));
            this.endValue = endValue;
        }
        else{
            setValue(getValue().getFullValue() + endValue);
            this.endValue = endValue;
            updateLabel();
        }
        return this;
    }

    public HCFLabel show() {
        setVisible(true);
        updateLabel();
        return this;
    }

    public HCFLabel hide() {
        setVisible(false);
        updateLabel();
        return this;
    }

}
