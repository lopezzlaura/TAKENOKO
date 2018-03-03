package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau de Sébastien Mosser
 * @version 8.0
 */

class PlaceParcel extends Action {

    private Parcel toPlace, from;
    private int indexToPlace;

    /**
     * Normal override constructor
     *
     * @param player  Player that do the action
     * @param toPlace Parcel to place on the board
     * @param from    Parcel from which the new parcel will be placed
     * @param index   Index to place in the from parcel
     */
    public PlaceParcel(Player player, Parcel toPlace, Parcel from, int index) {
        super(player);
        this.toPlace = toPlace;
        this.from = from;
        this.indexToPlace = index;
    }

    /**
     * Getter for the parcel that will be placed
     *
     * @return The toPlace parcel
     */
    public Parcel getToPlace() {
        return toPlace;
    }

    /**
     * Getter for move action name
     *
     * @return Move action name
     */
    @Override
    public String getName() {
        if(toPlace.getFacility() == Facility.NO_FACILITY) {
            return " placed a " + toPlace.getColor().toString() + " parcel at " + toPlace.getCoordinate() + ".";
        } else {
            return " placed a " + toPlace.getColor().toString() + " parcel " + "with " + toPlace.getFacility() + " at " + toPlace.getCoordinate() + ".";
        }

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
     * Meaning place the parcel on the board
     * If the Parcel is placed next to the PondParcel a new bamboo is grown
     * The same thing goes is the Parcel contains a PondFacility
     *
     * @return true if the action was executed correctly, false otherwise
     */
    @Override
    public boolean execute() {
        try {
            from.addNode(indexToPlace, toPlace);

            if (toPlace.isIrrigated()) {
                toPlace.pushBamboo();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Equals method for the {@code PlaceParcel} action
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof PlaceParcel && ((PlaceParcel)obj).toPlace.equals(this.toPlace) && ((PlaceParcel)obj).from.equals(this.from) && ((PlaceParcel)obj).indexToPlace == this.indexToPlace;
    }
}
