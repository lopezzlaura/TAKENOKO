package com.polytech.bdsm.takenoko.engine.actions.weather;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class SunAction extends WeatherAction {

    /**
     * Normal override constructor
     *
     * @param player Player that do the action
     */
    public SunAction(Player player) {
        super(player);
    }

    /**
     * Getter for the name of Sun Weather
     *
     * @return a String explaining the weather and the action the player did
     */
    @Override
    public String getName() {
        return "Sun weather : " + player + " benefits from an additional action.";
    }

    /**
     * Check if the Action is recorded as an action for the player
     *
     * @return True if the Action counts in the possible actions of the player for a turn,
     *         false otherwise.
     */
    @Override
    public boolean isActionDecrementedFromPlayer() {
        return false;
    }

    /**
     * Method to execute the action
     * meaning here adding an action to the player's possible actions
     *
     * @return true if the action was correctly executed, false
     * otherwise
     */
    @Override
    public boolean execute() {
        player.addNbActions(1);
        player.getActionList().add(ActionType.NONE);
        return true;
    }


    /**
     * Equals method for the {@code SunAction}
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof SunAction;
    }
}
