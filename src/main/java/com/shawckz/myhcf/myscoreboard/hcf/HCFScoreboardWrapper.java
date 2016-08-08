/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.myscoreboard.hcf;

import com.shawckz.myhcf.Factions;
import com.shawckz.myhcf.myscoreboard.MyScoreboard;
import com.shawckz.myhcf.myscoreboard.hcf.label.HCFEndLabel;
import com.shawckz.myhcf.myscoreboard.hcf.label.HCFLabel;
import com.shawckz.myhcf.myscoreboard.hcf.label.HCFSimpleLabel;
import com.shawckz.myhcf.myscoreboard.hcf.timer.*;
import com.shawckz.myhcf.myscoreboard.hcf.type.HCFSpacerLabel;
import com.shawckz.myhcf.myscoreboard.label.MyLabel;
import com.shawckz.myhcf.player.HCFPlayer;
import com.shawckz.myhcf.util.HCFException;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.ChatColor;

@Getter
public class HCFScoreboardWrapper {

    private static final TimerUpdater timerUpdater = new TimerUpdater();

    private final HCFPlayer player;
    private final MyScoreboard scoreboard;

    private final ConcurrentMap<HCFLabelID, HCFLabel> labels = new ConcurrentHashMap<>();

    /**
     * Create an HCFScoreboardWrapper for a player
     * Will also immediately send the scoreboard to the player, and start the TimerUpdater if it is not running
     * All labels in the local {@link HCFScoreboardWrapper#labels} ConcurrentHashMap should not be removed, if
     * you must manually remove a label, do it via the label's internal {@link HCFLabel#remove()} method,
     * also remove it from the {@link MyScoreboard#scores} index.
     * @param player The player to create this scoreboard for, and to send it to
     */
    public HCFScoreboardWrapper(HCFPlayer player) {
        this.player = player;
        this.scoreboard = new MyScoreboard(ChatColor.translateAlternateColorCodes('&', Factions.getInstance().getFactionsConfig().getScoreboardTitle()));

        /* ====================
        - Normal Labels
        ==================== */

        // Top spacer / blank line, comment out to remove
        labels.put(HCFLabelID.SPACER_TOP, new HCFSimpleLabel(scoreboard, HCFLabelID.SPACER_TOP.score, "§r "));

        // Armor Class
        labels.put(HCFLabelID.ARMOR_CLASS, new HCFEndLabel(scoreboard, HCFLabelID.ARMOR_CLASS.score, HCFLabelID.ARMOR_CLASS.key, "None"));

        /* ====================
        - Timer Labels
        ==================== */

        // Ender pearl
        labels.put(HCFLabelID.ENDER_PEARL, new EnderpearlTimer(this, HCFLabelID.ENDER_PEARL.score, 0.0D, timerUpdater));

        // PvP timer
        labels.put(HCFLabelID.PVP_TIMER, new PvPTimer(this, HCFLabelID.PVP_TIMER.score, 0.0D, timerUpdater));

        // Energy Timer
        labels.put(HCFLabelID.ENERGY, new EnergyTimer(this, HCFLabelID.ENERGY.score, 0.0D, timerUpdater));

        // Spawn Tag Timer
        labels.put(HCFLabelID.SPAWN_TAG, new SpawnTagTimer(this, HCFLabelID.SPAWN_TAG.score, 0.0D, timerUpdater));

        if(player.getBukkitPlayer() != null) {
            scoreboard.sendToPlayer(player.getBukkitPlayer());
        }

        if(!timerUpdater.isRunning()) {
            timerUpdater.startTimer(Factions.getInstance(), 2L, true);
        }
    }

    /**
     * Add the label to the actual scoreboard, and update if specified
     * Also calls {@link #updateScores()} if update
     * If the label is an HCFTimerLabel, it will also add the label to the TimerUpdater if it isn't there already
     *
     * @param id Label to add
     * @param update Whether to update the label, which will actually show it on the scoreboard.
     *               Better idea to manually call updateScores if you are adding several labels at once
     * @return The label added, by ID (created in constructor)
     */
    public HCFLabel addLabel(HCFLabelID id, boolean update) {
        if(labels.containsKey(id)) {
            HCFLabel label = getLabel(id);
            HCFLabel spacer = getLabel(HCFLabelID.SPACER_TOP);

            boolean updatingSpacer = false;

            if (spacer != null && !scoreboard.hasLabel(spacer)) {
                scoreboard.addLabel(spacer, false);
                updatingSpacer = true;
            }
            if(!scoreboard.hasLabel(label)) {
                scoreboard.addLabel(label, (updatingSpacer && update));
            }

            if (label instanceof HCFTimerLabel) {
                HCFTimerLabel timer = (HCFTimerLabel) label;
                if (!timerUpdater.hasLabel(timer)) {
                    timerUpdater.addLabel(timer);
                }
            }
            return label;
        }
        throw new HCFException("HCFScoreboard does not have label indexed by ID: " + id.toString());
    }

    /**
     * Remove a label by ID, from the MyScoreboard; keeping it in the local {@link #labels} HashMap
     * Will also call updateScores, and call {@link HCFLabel#update()}
     * Will also remove from the {@link #timerUpdater} if it is an instance of {@link HCFTimerLabel},
     * and is in the {@link #timerUpdater}
     *
     * @param id The HCFLabelID
     * @return The removed label
     */
    public HCFLabel removeLabel(HCFLabelID id) {
        HCFLabel label = getLabel(id);
        if(label != null) {
            scoreboard.getScores().remove(label);
            label.remove();
            updateScores();

            if(label instanceof HCFTimerLabel) {
                HCFTimerLabel timer = (HCFTimerLabel) label;
                if(timerUpdater.hasLabel(timer)) {
                    timerUpdater.removeLabel(timer);
                }
            }
        }
        throw new HCFException("HCFScoreboard does not have label indexed by ID: " + id.toString());
    }

    /**
     * Re-orders the scores to be in a descending order and ensures that the spacer is at the top
     * Will also update all labels in the {@link MyScoreboard} scoreboard's {@link MyScoreboard#scores} scores
     * @return this
     */
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
