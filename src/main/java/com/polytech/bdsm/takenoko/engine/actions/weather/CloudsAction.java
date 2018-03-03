package com.polytech.bdsm.takenoko.engine.actions.weather;

import com.polytech.bdsm.takenoko.engine.actions.params.ActionParams;
import com.polytech.bdsm.takenoko.engine.actions.params.ActionResult;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.RainActionParams;
import com.polytech.bdsm.takenoko.engine.actions.params.weather.StormActionParams;
import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.parcels.Color;
import com.polytech.bdsm.takenoko.engine.player.Player;

import javax.annotation.Nullable;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class CloudsAction extends WeatherAction {

    private Facility facility;

    @Nullable
    private ActionParams otherWeather;

    /**
     * Normal override constructor
     *
     * @param player Player that do the action
     */
    public CloudsAction(Player player, Facility facility, @Nullable ActionParams otherWeather) {
        super(player);
        this.facility = facility;
        this.otherWeather = otherWeather;
    }

    /**
     * Check if the Action is recorded as an action for the player
     *
     * @return True if the Action counts in the possible actions of the player for a turn,
     * false otherwise.
     */
    @Override
    public boolean isActionDecrementedFromPlayer() {
        return false;
    }

    /**
     * Getter for the name of Clouds Weather
     *
     * @return a String explaining the weather and the action the player did
     */
    @Override
    public String getName() {
        String result = "";
        if (otherWeather != null) {
            switch (otherWeather.getActionType()) {
                case RAIN_ACTION:
                    RainActionParams rap = (RainActionParams) otherWeather;
                    result = "Rain weather : " + player + " can add a bamboo on an irrigated parcel."
                            + "\n\t\t\t  " + player + " made a bamboo grow on " + rap.getParcel().getCoordinate().toString() + ".";
                    break;
                case SUN_ACTION:
                    result = "Sun weather : " + player + " benefits from an additional action.";
                    break;
                case WIND_ACTION:
                    result = "Wind weather : " + player + " can execute twice the same action.";
                    break;
                case STORM_ACTION:
                    StormActionParams sap = (StormActionParams) otherWeather;
                    result = "Storm weather : " + player + " can move the Panda to a parcel, on which the Panda will eat a bamboo." +
                            "\n\t\t\t  " + player + " moved the Panda to " + sap.getParcel().getCoordinate().toString() + " and has " + player.getInventory().getBamboosCount(Color.GREEN) + " green bamboo(s), " +
                            player.getInventory().getBamboosCount(Color.PINK) + " pink bamboo(s), " + player.getInventory().getBamboosCount(Color.YELLOW) + " yellow bamboo(s).";
                    break;
                case NONE:
                    result = player + " did nothing.";
                    break;
            }
        }
        return "Clouds weather : " + player + " has to draw a facility. "
                + (facility != null ? "\n\t\t\t" + player + " drew a " + this.facility.name() + "." : "")
                + (otherWeather != null ? "\n\t\t\t  There is no more facilities, so the player choose : " + otherWeather.getActionType().toString() + "\n\t\t\t" + result : "");
    }

    /**
     * Method to execute the action
     * If the facility deck is empty, the player already chose a new weather
     * and the method to determine the params for the weather is called again with the new weather
     * If the deck isn't empty, the player will draw the facility he has chosen earlier
     *
     * @return true if the action was correctly executed, false
     * otherwise
     */
    @Override
    public boolean execute() {
        if (player.getBoard().getFacilityDeck().isEmpty()) {
            WeatherDice dice = null;
            switch (otherWeather.getActionType()) {
                case RAIN_ACTION:
                    dice = WeatherDice.RAIN;
                    break;
                case CLOUDS_ACTION:
                    dice = WeatherDice.CLOUDS;
                    break;
                case SUN_ACTION:
                    dice = WeatherDice.SUN;
                    break;
                case WIND_ACTION:
                    dice = WeatherDice.WIND;
                    break;
                case STORM_ACTION:
                    dice = WeatherDice.STORM;
                    break;
                case NONE:
                    return player.getActionFactory().doAction(otherWeather, player, new ActionResult());
            }

            return player.getActionFactory().doWeatherAction(dice, otherWeather, player, new ActionResult());
        }

        if (player.getBoard().getFacilityDeck().drawFacility(facility).isPresent()) {
            player.getInventory().addFacility(facility);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Equals method for the {@code CloudsAction}
     *
     * @param obj the object to be compared
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof CloudsAction;
    }
}
