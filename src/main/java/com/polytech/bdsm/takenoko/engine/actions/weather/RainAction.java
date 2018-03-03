package com.polytech.bdsm.takenoko.engine.actions.weather;

import com.polytech.bdsm.takenoko.engine.board.BoardUtils;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */
public class RainAction extends WeatherAction {

    private Parcel parcel;

    /**
     * Normal override constructor
     *
     * @param player Player that do the action
     */
    public RainAction(Player player, Parcel parcel) {
        super(player);
        this.parcel = parcel;
    }

    /**
     * Getter for the name of Rain Weather
     *
     * @return a String explaining the weather and the action the player did
     */
    @Override
    public String getName() {
        return "Rain weather : " + player + " can add a bamboo on an irrigated parcel."
                + "\n\t\t\t" + player + " made a bamboo grow on " + parcel.getCoordinate().toString() + ".";
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
     * Method to grow a <code>Bamboo</code> on the <code>Parcel</code>
     * if it is irrigated
     *
     * @return true if the grow could be done, false otherwise
     */
    public boolean execute() {
        int size = parcel.getBambooStack().size();

        if (BoardUtils.isParcelIrrigated(player.getBoard(), parcel)) {
            if (size < 4) {
                parcel.pushBamboo();
            }
        }

        return parcel.getBambooStack().size() > size;
    }

    /**
     * Equals method for the {@code RainAction}
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof RainAction;
    }
}
