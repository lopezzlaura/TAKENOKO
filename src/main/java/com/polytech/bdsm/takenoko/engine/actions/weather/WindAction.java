package com.polytech.bdsm.takenoko.engine.actions.weather;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class WindAction extends WeatherAction {

    /**
     * Normal override constructor
     *
     * @param player Player that do the action
     */
    public WindAction(Player player) {
        super(player);
    }



    /**
     * Getter for the name of Wind Weather
     *
     * @return a String explaining the weather and the action the player did
     */
    @Override
    public String getName() {
        return "Wind weather : " + player + " can execute twice the same action.";
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
     * Meaning it duplicated all possible action types in the player's actionList
     *
     * @return true
     */
    @Override
    public boolean execute() {
        List<ActionType> possibleActionTypes = new ArrayList<>();
        possibleActionTypes.addAll(player.getActionList());
        player.getActionList().addAll(possibleActionTypes);

        return true;
    }

    /**
     * Equals method for the {@code WindAction}
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof WindAction;
    }
}
