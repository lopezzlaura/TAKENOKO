package com.polytech.bdsm.takenoko.engine.player;

import com.polytech.bdsm.takenoko.engine.actions.ActionType;
import com.polytech.bdsm.takenoko.engine.actions.params.ActionParams;
import com.polytech.bdsm.takenoko.engine.actions.params.ActionResult;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.CloudsActionParams;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.UnknownActionParams;
import com.polytech.bdsm.takenoko.engine.actions.weather.WeatherDice;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */


public class Turn extends Observable implements Observer {

    private int turnNumber;
    private Player player;
    private boolean playerFinishTurn = false;
    private ActionResult result;

    /**
     * Turn Constructor
     *
     * @param player        The player for this Turn
     * @param turnNumber    The turn number
     */
    public Turn(Player player, int turnNumber) {
        this.turnNumber = turnNumber;
        this.player = player;
        this.player.addNbActions(2);

        List<ActionType> actionTypes = new ArrayList<>();
        actionTypes.add(ActionType.MOVE_PANDA);
        actionTypes.add(ActionType.MOVE_GARDENER);
        actionTypes.add(ActionType.PLACE_PARCEL);
        actionTypes.add(ActionType.DRAW_IRRIGATION);
        actionTypes.add(ActionType.PLACE_IRRIGATION);
        actionTypes.add(ActionType.PLACE_FACILITY);
        actionTypes.add(ActionType.VALIDATE_GOAL);
        actionTypes.add(ActionType.DRAW_GOAL);

        this.player.setActionList(actionTypes);
    }

    /**
     * Getter for the Player of this Turn
     *
     * @return The Player of this Turn
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Getter for the result of the latest action
     *
     * @return The latest Action Result
     */
    public ActionResult getResult() {
        return result;
    }

    /**
     * Getter for this turn number
     *
     * @return The current turn number
     */
    public int getTurnNumber() {
        return turnNumber;
    }

    /**
     * Method that simulate a turn :
     *  - Start by roll the weather
     *  - Player execute the weather action
     *  - Player play his remaining action
     *
     * @return If every action were correctly executed
     */
    public boolean playTurn() {

        ActionParams a;

        // Roll the weather dice only since the second turn
        if (this.turnNumber >= 2) {

            // Roll the weather dice to have this turn's weather
            WeatherDice weatherDice = WeatherDice.rollTheDice(player.getBoard().getGlobalRandom());
            ActionParams weatherParams = weatherDice.getWeatherParams();

            // Tell the Game for the new Weather
            setChanged();
            notifyObservers(weatherDice);

            // Request the player action if the weather needs it
            if (weatherDice.isPlayerRequested()) {
                weatherParams = player.getWeatherMove(weatherParams);
            }

            // Add a None action for the optional weather actions
            if (weatherParams.getActionType() != ActionType.CLOUDS_ACTION) {
                player.getActionList().add(ActionType.NONE);
            }

            // If the weather is unknown, create a UnknownActionParams
            ActionParams finalWeatherParams;
            if (weatherDice == WeatherDice.UNKNOWN) {
                finalWeatherParams = new UnknownActionParams();
                ((UnknownActionParams) finalWeatherParams).setActionParams(weatherParams);
            } else if (weatherDice == WeatherDice.CLOUDS && weatherParams.getActionType() != ActionType.CLOUDS_ACTION) {
                finalWeatherParams = new CloudsActionParams();
                ((CloudsActionParams) finalWeatherParams).setOtherParams(weatherParams);
            } else {
                finalWeatherParams = weatherParams;
            }

            // Execute the weather action
            this.result = new ActionResult();
            if (!player.getActionFactory().doWeatherAction(weatherDice, finalWeatherParams, player, result)) {
                return false;
            }

            setChanged();
            notifyObservers(result);

            // Remove the None action if it was not used
            if (player.getActionList().contains(ActionType.NONE) && weatherParams.getActionType() != ActionType.SUN_ACTION) {
                player.removeActionType(ActionType.NONE);
            }
        }

        while (!playerFinishTurn) {
            // If the action went wrong, do another action
            a = player.getNextMove();

            this.result = new ActionResult();
            while (!player.getActionFactory().doAction(a, player, result)) {
                a = player.requestOtherMove(a);
            }

            setChanged();
            notifyObservers(result);
        }

        return true;
    }

    /**
     * Update method when the {@code Observable} notify if the player has end his turn
     *
     * @param o   The {@code Observable} that notify the Game
     * @param arg The argument given
     */
    @Override
    public void update(Observable o, Object arg) {
        this.playerFinishTurn = true;
    }


}
