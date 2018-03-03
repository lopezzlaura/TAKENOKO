package com.polytech.bdsm.takenoko.ai.strategies;

import com.polytech.bdsm.takenoko.engine.goals.PandaGoal;
import com.polytech.bdsm.takenoko.engine.parcels.Bamboo;
import com.polytech.bdsm.takenoko.engine.player.Inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

public final class PandaStrategyHelpers {

    /**
     * Private Constructor to block instantiation
     */
    private PandaStrategyHelpers() {}

    /**
     * Getter for the easiest PandaGoal to achieve
     *
     * @param possibleGoals The different PandaGoal from which we will try to find the easiest one
     * @param inventory The inventory to get the Bamboo from
     * @return The PandaGoal that have the less remaining Bamboo to get
     */
    public static PandaGoal getEasiestGoalToComplete(List<PandaGoal> possibleGoals, Inventory inventory) {
        int minDelta = Integer.MAX_VALUE;
        int nbContain = 0;
        int delta;
        PandaGoal bestResult = null;
        for (PandaGoal pandaGoal : possibleGoals) {
            for (Bamboo bamboo : pandaGoal.getBamboosToCollect()) {
                if (inventory.containBamboo(bamboo)) nbContain++;
            }
            delta = pandaGoal.getBamboosToCollect().size() - nbContain;
            if (minDelta > delta) {
                minDelta = delta;
                bestResult = pandaGoal;
            }
            nbContain = 0;
        }
        return bestResult;
    }

    /**
     * Return a List containing only differents colors of bamboos
     *
     * @param bambooList the list to sort
     * @return the list that was sorted
     */
    public static List<Bamboo> getDifferentBamboos(List<Bamboo> bambooList) {
        List<Bamboo> bamboosResult = new ArrayList<>();
        for (Bamboo bamboo : bambooList) {
            if (!bamboosResult.contains(bamboo)) {
                bamboosResult.add(bamboo);
            }
        }

        return bamboosResult;
    }
}
