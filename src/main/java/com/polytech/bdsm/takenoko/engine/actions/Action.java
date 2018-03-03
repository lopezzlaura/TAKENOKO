package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public abstract class Action {

    protected Player player;

    /**
     * Normal Constructor
     *
     * @param player the {@code Player} doing the {@code Action}
     */
    protected Action(Player player) {
        this.player = player;
    }

    /**
     * Getter for the action name
     *
     * @return Action name
     */
    public abstract String getName();

    /**
     * Getter for the player that executes the action
     *
     * @return Player instance
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Check if the Action is recorded as an action for the player
     *
     * @return True if the Action counts in the possible actions of the player for a turn,
     *         false otherwise.
     */
    public abstract boolean isActionDecrementedFromPlayer();

    /**
     * Execute this action
     *
     * @return True if the action was correctly accomplished, false otherwise
     */
    public abstract boolean execute();


    /**
     * Equals method for the {@code Action}
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * ToString method
     *
     * @return Representation of the Object in a String format
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
