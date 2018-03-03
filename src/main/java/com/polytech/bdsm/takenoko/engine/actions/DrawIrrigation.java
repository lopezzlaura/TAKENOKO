package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 * @see Action
 */

public class DrawIrrigation extends Action {

    /**
     * Normal Constructor
     *
     * @param player The player that decide to choose this Action
     */
    public DrawIrrigation(Player player) {
        super(player);
    }

    /**
     * Getter for the place irrigation action name
     *
     * @return {@code ValidateGoal} action name
     */
    @Override
    public String getName() {
        return " picked an irrigation in the irrigations deck.";
    }


    /**
     * Check if the Action is recorded as an action for the player
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
     * Meaning check there are irrigations left to draw
     * and draw one and add it to the player inventory
     *
     * @return true if the action was executed correctly, false otherwise
     */
    @Override
    public boolean execute() {
        if (player.getBoard().drawIrrigation() != 0) {
            player.getInventory().addIrrigation();
            return true;
        }

        return false;
    }

    /**
     * Equals method for the {@code DrawIrrigation} action
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof DrawIrrigation;
    }
}
