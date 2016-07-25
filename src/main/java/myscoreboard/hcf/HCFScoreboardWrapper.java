/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package myscoreboard.hcf;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.player.HCFPlayer;
import lombok.Getter;
import myscoreboard.MyScoreboard;
import myscoreboard.label.MyLabel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.ChatColor;

@Getter
public class HCFScoreboardWrapper {

    {
        HCFScoreboardWrapper board = new HCFScoreboardWrapper(Factions.getInstance().getCache().getHCFPlayer("Shawckz"));

        board.addLabel(HCFLabelID.SPACER_TOP, new HCFSpacerLabel(board.getScoreboard(), 2), false);

        board.addLabel(HCFLabelID.ARMOR_CLASS, new HCFEndLabel(board.getScoreboard(), 1, "Armor Class: ", "None"), false);

        board.getLabel(HCFLabelID.ARMOR_CLASS)
                .getAsEnd()
                .setEndValue("Archer");

        board.updateScores();
    }

    private final HCFPlayer player;
    private final MyScoreboard scoreboard;

    private final ConcurrentMap<HCFLabelID, HCFLabel> labels = new ConcurrentHashMap<>();

    public HCFScoreboardWrapper(HCFPlayer player) {
        this.player = player;
        this.scoreboard = new MyScoreboard(ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getFactionsConfig().getScoreboardTitle()));

        if(player.getBukkitPlayer() != null) {
            scoreboard.sendToPlayer(player.getBukkitPlayer());
        }
    }

    public HCFScoreboardWrapper addLabel(HCFLabelID id, HCFLabel label, boolean update) {
        labels.put(id, label);
        scoreboard.addLabel(label, update);
        if(update) {
            updateScores();
        }
        return this;
    }

    public HCFScoreboardWrapper removeLabel(HCFLabelID id) {
        HCFLabel label = getLabel(id);
        if(label != null) {
            labels.remove(id);
            scoreboard.getScores().remove(label);
            label.remove();
            updateScores();
        }
        return this;
    }

    public HCFScoreboardWrapper updateScores() {
        int i = 1;
        //Do spacer last so it's at the top
        for(MyLabel label : scoreboard.getScores()) {
            if(!(label instanceof HCFSpacerLabel)) {
                label.setScore(i);
                label.getValue().setValue(i);
                label.update();
                i++;
            }
        }
        if(hasLabel(HCFLabelID.SPACER_TOP)) {
            HCFSpacerLabel spacer = getLabel(HCFLabelID.SPACER_TOP).getAsSpacer();
            spacer.setScore(i);
            spacer.getValue().setValue(i);
            spacer.update();
        }

        return this;
    }

    public HCFLabel getLabel(HCFLabelID id) {
        return labels.get(id);
    }

    public boolean hasLabel(HCFLabelID id) {
        return labels.containsKey(id);
    }

}
