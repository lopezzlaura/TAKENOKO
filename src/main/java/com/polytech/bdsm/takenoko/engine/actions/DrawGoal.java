package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.engine.goals.GoalType;
import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 * @see ActionFactory
 * @see Action
 * @see ActionType
 */

public class DrawGoal extends Action {
    private GoalType type;

    /**
     * Normal Constructor
     *
     * @param player the player who will draw a goal
     * @param type the wished type of goal
     */
    DrawGoal(Player player, GoalType type) {
        super(player);
        this.type = type;
    }

    /**
     * Getter for the name of the {@code DrawGoal} action
     *
     * @return The name of the type of the goal drawn
     */
    @Override
    public String getName() {
        return " drew a goal of type : " + this.type + ".";
    }

    /**
     * Check if the {@code Action} is recorded as an action for the {@code Player}
     *
     * @return True if the Action counts in the possible actions of the player for a turn,
     *         false otherwise.
     */
    @Override
    public boolean isActionDecrementedFromPlayer() {
        return true;
    }

    /**
     * Execute the action
     * Meaning check if the player has less than 5 goals in his hand
     * and draw a goal if it is the case
     *
     * @return true if the execution went good, false either
     */
    @Override
    public boolean execute() {
        if (player.getGoals().size() < 5) {
            return player.drawGoal(type);
        }
        return false;
    }

    /**
     * Equals method for the {@code DrawGoal} action
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof DrawGoal && this.type == ((DrawGoal)obj).type;
    }
}
