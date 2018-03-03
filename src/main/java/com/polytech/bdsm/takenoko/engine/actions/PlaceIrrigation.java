package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.engine.board.BoardUtils;
import com.polytech.bdsm.takenoko.engine.board.Irrigation;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */

class PlaceIrrigation extends Action {

    private Parcel parcelToTheRight, parcelToTheLeft;

    private Irrigation irrigation;

    /**
     * Normal Constructor
     *
     * @param player     The player that decide to choose this Action
     * @param irrigation The irrigation to place
     */
    public PlaceIrrigation(Player player, Irrigation irrigation) {
        super(player);
        this.irrigation = irrigation;
        this.parcelToTheLeft = irrigation.getParcelToTheLeft();
        this.parcelToTheRight = irrigation.getParcelToTheRight();
    }

    /**
     * Getter for the place irrigation action name
     *
     * @return PlaceIrrigation action name
     */
    @Override
    public String getName() {
        return " placed an irrigation between " + parcelToTheLeft.getCoordinate() + " and " + parcelToTheRight.getCoordinate() + ".";
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
     * Execute the action
     * Meaning place an irrigation to the board
     * and make a bamboo grow if the neighbouring parcels are irrigated for the first time
     *
     * @return true if the Action was executed correctly, false otherwise
     */
    @Override
    public boolean execute() {

        if (player.getBoard().getIrrigations().contains(irrigation)) return false;
        if (!BoardUtils.getPossibleIrrigations(player.getBoard()).doesContainIrrigation(irrigation)) return false;

        if (player.getInventory().getIrrigations() > 0) {

            //if the parcel is irrigated for the first time, a bamboo is grown
            if (!BoardUtils.isParcelIrrigated(player.getBoard(), parcelToTheLeft)) {
                parcelToTheLeft.pushBamboo();
            }
            if (!BoardUtils.isParcelIrrigated(player.getBoard(), parcelToTheRight)) {
                parcelToTheRight.pushBamboo();
            }

            player.getBoard().addIrrigation(irrigation);
            player.getInventory().removeIrrigation();
            return true;
        }

        return false;
    }

    /**
     * Equals method for the {@code Action}
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof PlaceIrrigation && ((PlaceIrrigation)obj).irrigation.equals(this.irrigation);
    }
}
