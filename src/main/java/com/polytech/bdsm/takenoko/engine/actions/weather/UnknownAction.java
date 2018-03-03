package com.polytech.bdsm.takenoko.engine.actions.weather;

import com.polytech.bdsm.takenoko.engine.actions.params.ActionParams;
import com.polytech.bdsm.takenoko.engine.actions.params.ActionResult;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.CloudsActionParams;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.RainActionParams;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.StormActionParams;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class UnknownAction extends WeatherAction {

    ActionParams params;

    /**
     * Normal override constructor
     *
     * @param player Player that do the action
     */
    public UnknownAction(Player player, ActionParams params) {
        super(player);
        this.params = params;
    }

    /**
     * Getter for the params of the {@code UnknownAction}
     *
     * @return the params of {@code UnknownAction}
     */
    public ActionParams getParams(){
        return this.params;
    }

    /**
     * Getter for the name of Unknown Weather
     *
     * @return a String explaining the weather and the action the player did
     */
    @Override
    public String getName() {
        String result = "";
        switch (params.getActionType()) {
            case RAIN_ACTION:
                RainActionParams rap = (RainActionParams) params;
                result = "Rain weather : " + player + " can add a bamboo on an irrigated parcel."
                        + "\n\t\t\t  " + player + " made a bamboo grow on " + rap.getParcel().getCoordinate().toString() + ".";
                break;
            case CLOUDS_ACTION:
                CloudsActionParams cap = (CloudsActionParams) params;
                result = "Clouds weather : " + player + " has to draw a facility. "
                        + (cap.getFacility() != null ? "\n\t\t\t" + player + " drew a " + cap.getFacility().name() + "." : "")
                        + (cap.getOtherParams() != null ? "\n\t\t\t  The player choose : " + cap.getOtherParams().getActionType().toString() : "");
                break;
            case SUN_ACTION:
                result = "Sun weather : " + player + " benefits from an additional action.";
                break;
            case WIND_ACTION:
                result = "Wind weather : " + player + " can execute twice the same action.";
                break;
            case STORM_ACTION:
                StormActionParams sap = (StormActionParams) params;
                result = "Storm weather : " + player + " can move the Panda to a parcel, on which the Panda will eat a bamboo." +
                    "\n\t\t\t  " + player + " moved the Panda to " + sap.getParcel().getCoordinate().toString() + " and has " + player.getInventory().getBamboosCount(Color.GREEN) + " green bamboo(s), " +
                    player.getInventory().getBamboosCount(Color.PINK) + " pink bamboo(s), " + player.getInventory().getBamboosCount(Color.YELLOW) + " yellow bamboo(s).";
                break;
            case NONE:
                result = player + " did nothing.";
                break;
        }

        return "? weather : " + player + " has to choose a weather action."
                + "\n\t\t\t  The player choose : " + this.params.getActionType().toString()
                + "\n\t\t\t " + result;
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
     * Execute the weather action depending on the params of UnknownAction
     *
     * @return true if the action was correctly executed, false
     * otherwise
     */
    @Override
    public boolean execute() {
        boolean result = false;

        switch (params.getActionType()) {
            case RAIN_ACTION:
                result = player.getActionFactory().doWeatherAction(WeatherDice.RAIN, params, player, new ActionResult());
                break;
            case CLOUDS_ACTION:
                result = player.getActionFactory().doWeatherAction(WeatherDice.CLOUDS, params, player, new ActionResult());
                break;
            case SUN_ACTION:
                result = player.getActionFactory().doWeatherAction(WeatherDice.SUN, params, player, new ActionResult());
                break;
            case WIND_ACTION:
                result = player.getActionFactory().doWeatherAction(WeatherDice.WIND, params, player, new ActionResult());
                break;
            case STORM_ACTION:
                result = player.getActionFactory().doWeatherAction(WeatherDice.STORM, params, player, new ActionResult());
                break;
            case NONE:
                result = player.getActionFactory().doAction(params, player, new ActionResult());
                break;
        }

        return result;
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
        return obj instanceof UnknownAction;
    }


}
