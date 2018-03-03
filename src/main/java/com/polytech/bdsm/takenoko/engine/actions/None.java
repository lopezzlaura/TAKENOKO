package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

class None extends Action {

    /**
     * Normal override constructor
     *
     * @param player Player that do the action
     */
    public None(Player player) {
        super(player);
    }

    /**
     * Getter for move action name
     *
     * @return Move action name
     */
    @Override
    public String getName() {
        return " did nothing.";
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
     * Execute this action
     * Meaning do nothing here
     *
     * @return true, always because nothing is done
     */
    @Override
    public boolean execute() {
        return true;
    }

    /**
     * Equals method for the {@code None} action
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof None;
    }
}
