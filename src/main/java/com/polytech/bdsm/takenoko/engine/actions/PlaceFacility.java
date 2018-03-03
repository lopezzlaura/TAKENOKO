package com.polytech.bdsm.takenoko.engine.actions;

import com.polytech.bdsm.takenoko.engine.facilities.Facility;
import com.polytech.bdsm.takenoko.engine.facilities.FertilizerLeaf;
import com.polytech.bdsm.takenoko.engine.facilities.PaddockLeaf;
import com.polytech.bdsm.takenoko.engine.facilities.PondLeaf;
import com.polytech.bdsm.takenoko.engine.parcels.Parcel;
import com.polytech.bdsm.takenoko.engine.parcels.PondParcel;
import com.polytech.bdsm.takenoko.engine.player.Player;

/**
 * @author Bureau De Sébastien Mosser
 * @version 8.0
 */


public class PlaceFacility extends Action {

    private Facility facility;
    private Parcel parcel;

    /**
     * Constructor for a PlaceFacility Action
     * @param player the player doing the action
     * @param parcel the parcel on which the parcel is to be placed
     * @param facility the facility to place
     */
    public PlaceFacility(Player player, Parcel parcel, Facility facility) {
        super(player);
        this.parcel = parcel;
        this.facility = facility;
    }

    /**
     * Getter for the action name
     *
     * @return Action name
     */
    @Override
    public String getName() {
        return " placed a " + facility.name() + " facility on : " + parcel.getColor() + " parcel " + parcel.getCoordinate() + ".";
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
     * Meaning check if the parcel does not contains already a facility and if there are no bamboos on it and
     * if it's not the Pond Parcel, then change the parcel into the good facility and remove the facility from the player inventory
     *
     * @return true if the execution went good, false otherwise
     */
    @Override
    public boolean execute() {
        if (parcel instanceof PondParcel || (parcel.getFacility() != Facility.NO_FACILITY) || !(parcel.getBambooStack().isEmpty())) return false;

        Parcel parcelTemp;

        switch (facility) {
            case POND_FACILITY:
                parcelTemp = new PondLeaf(parcel);
                parcel = parcelTemp;
                break;
            case FERTILIZER_FACILITY:
                parcelTemp = new FertilizerLeaf(parcel);
                parcel = parcelTemp;
                break;
            case PADDOCK_FACILITY:
                parcelTemp = new PaddockLeaf(parcel);
                parcel = parcelTemp;
                break;
        }

        player.getInventory().removeFacility(facility);

        return true;
    }

    /**
     * Equals method for the {@code PlaceFacility}
     *
     * @param obj the object to be compared
     *
     * @return true if the objects are equals, false either
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof PlaceFacility && (((PlaceFacility)obj).parcel.equals(this.parcel) && ((PlaceFacility)obj).facility == this.facility);
    }

    /**
     * toString method
     *
     * @return a representation of the Object into a String format
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
